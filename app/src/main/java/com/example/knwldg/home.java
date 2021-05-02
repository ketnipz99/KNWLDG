package com.example.knwldg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Random;

public class home extends AppCompatActivity {

    private TextView factsOTD;
    private Button startGame, howToPlay;
    private BottomNavigationView bottomNav;
    private CountDownTimer countDownTimer;
    private long timeRemainingMillis;
    private static final long COUNTDOWN_IN_MIlLIS = 360000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

       factsOTD = findViewById(R.id.tvFacts);
       startGame = findViewById(R.id.btnStartGame);
       howToPlay = findViewById(R.id.btnRules);

       //Bottom navigation
       bottomNav = findViewById(R.id.bottom_navigation);
       bottomNav.setOnNavigationItemSelectedListener(navListener);
       Menu menu = bottomNav.getMenu();
       MenuItem menuItem = menu.getItem(0);
       menuItem.setChecked(true);

       //Display facts of the day and start the countdown to display next fact
        displayFactsOTD();
        timeRemainingMillis = COUNTDOWN_IN_MIlLIS;
        startCountdownTimer();

        SharedPreferences preferences = getSharedPreferences("difficulty", MODE_PRIVATE);

       startGame.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent (home.this, gameCategory.class);
               startActivity(intent);
           }
       });

       howToPlay.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               displayRules();
           }
       });
    }

    private void displayRules() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.rules);

        alertDialogBuilder.setPositiveButton("GOTCHA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void displayFactsOTD () {
        Random random = new Random();
        int factsIdentifier = random.nextInt(20);
        String[] facts = this.getResources().getStringArray(R.array.malaysia_facts);

        String displayedFacts = facts[factsIdentifier];

        factsOTD.setText(displayedFacts);
    }


    private void startCountdownTimer() {
        countDownTimer = new CountDownTimer(timeRemainingMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemainingMillis = millisUntilFinished;
            }
            @Override
            public void onFinish() {
                displayFactsOTD();
            }
        }.start();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch(item.getItemId()) {

                        case R.id.nav_home:
                           Intent intentHome = new Intent (getApplicationContext(), home.class);
                           startActivity(intentHome);
                           overridePendingTransition(0,0);
                           finish();
                           break;

                        case R.id.nav_achievement:
                            Intent intentAchievement = new Intent (getApplicationContext(), achievement.class);
                            startActivity(intentAchievement);
                            overridePendingTransition(0,0);
                            finish();
                            break;

                        case R.id.nav_eShop:
                            Intent intenteShop = new Intent (getApplicationContext(), eShop.class);
                            startActivity(intenteShop);
                            overridePendingTransition(0,0);
                            finish();
                            break;

                        case R.id.nav_settings:
                            Intent intentSettings = new Intent (getApplicationContext(), settings.class);
                            startActivity(intentSettings);
                            overridePendingTransition(0,0);
                            finish();
                            break;
                    }
                    return false;
                }
            };

    @Override
    protected void onStop() {
        super.onStop();
    }

}