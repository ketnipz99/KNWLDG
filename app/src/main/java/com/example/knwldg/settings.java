package com.example.knwldg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import static android.provider.ContactsContract.Intents.Insert.EMAIL;

public class settings extends Activity {

    private TextView profileName;
    private TextView profileEmail;
    private Button editProfile;
    private Button help, terms;
    private Button logout;
    private BottomNavigationView bottomNav;
    private UserDatabaseHelper dbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        profileName = findViewById(R.id.tvName);
        profileEmail = findViewById(R.id.tvEmail);
        editProfile = findViewById(R.id.btnEditProfile);
        help = findViewById(R.id.btnHelp);
        terms = findViewById(R.id.btnTerms);
        logout = findViewById(R.id.btnLogout);
        bottomNav = findViewById(R.id.bottom_navigation);

        //Bottom Navigation
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        dbUser = new UserDatabaseHelper(this);

        //Get the value of logged in email
        SharedPreferences preferencesEmail = this.getSharedPreferences(EMAIL, MODE_PRIVATE);
        String email = preferencesEmail.getString("email", "");
        Cursor cursor = dbUser.fetchName(email);

        if( cursor.getCount() >= 1) {
           while(cursor.moveToNext() ) {
                //Setting the displayed name and email in setting page
                profileName.setText(cursor.getString(0));
                profileEmail.setText(email);
            }
            cursor.close();
        }

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (settings.this, editProfile.class);
                startActivity(intent);
            }
        });


        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (settings.this, helpFeedback.class);
                startActivity(intent);
            }
        });

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (settings.this, termsPrivacyPolicy.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Resetting the shared value of "remember" after logging out
                SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("remember", "false");
                editor.putString("email", "");
                editor.apply();

                Intent intent = new Intent (settings.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
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



}