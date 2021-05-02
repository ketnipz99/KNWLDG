package com.example.knwldg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import static android.provider.ContactsContract.Intents.Insert.EMAIL;

public class achievement extends AppCompatActivity {

    private ProgressBar progressBar1;
    private ProgressBar progressBar2;
    private ProgressBar progressBar3;
    private UserDatabaseHelper dbUser;

    //Achievement counter
    private int passProgress = 0;
    private int fullmarkProgress = 0;
    private int distinctionProgress = 0;

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);

        progressBar1 = findViewById(R.id.pbProgress_1);
        progressBar2 = findViewById(R.id.pbProgress_2);
        progressBar3 = findViewById(R.id.pbProgress_3);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        dbUser = new UserDatabaseHelper(this);
        SharedPreferences preferencesEmail = this.getSharedPreferences(EMAIL, MODE_PRIVATE);
        String email = preferencesEmail.getString("email", "");
        //Retrieve user's data from db
        Cursor cursor = dbUser.fetchStatistics(email);


        if( cursor != null) {
            if (cursor.moveToFirst()) {
                passProgress = Integer.parseInt(cursor.getString(0));
                distinctionProgress = Integer.parseInt(cursor.getString(1));
                fullmarkProgress = Integer.parseInt(cursor.getString(2));
            }
            cursor.close();
        }
        //Update the achievement page
        updateProgress(passProgress, distinctionProgress, fullmarkProgress);

    }

    private void updateProgress(int pass, int distinction, int fullmark) {

        //Set the progress bar based on 100% metric
        progressBar1.setProgress(pass * 10);
        progressBar2.setProgress(distinction * 10);
        progressBar3.setProgress(fullmark * 10 / (3/2));

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