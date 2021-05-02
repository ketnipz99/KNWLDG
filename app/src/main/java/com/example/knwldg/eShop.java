package com.example.knwldg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import static android.provider.ContactsContract.Intents.Insert.EMAIL;

//extend View.OnClickListener
public class eShop extends AppCompatActivity implements View.OnClickListener {

    private BottomNavigationView bottomNav;
    private TextView eCoins;
    private Button bstTime_5, bstTime_10, bstTime_15;
    private Button bstHint_1, bstHint_5, bstHint_10;
    private UserDatabaseHelper dbUser;

    //Initialized user balance first
    public int userBalance = 0;
    public int addTime_5_left = 0;
    public int addTime_10_left = 0;
    public int addTime_15_left = 0;
    public int hintBoost_left = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_shop);

        SharedPreferences preferencesEmail = this.getSharedPreferences(EMAIL, MODE_PRIVATE);
        String email = preferencesEmail.getString("email", "");

        eCoins = findViewById(R.id.tvCoins);

        bstTime_5 = findViewById(R.id.btnBoost_1);
        bstTime_5.setOnClickListener(this);

        bstTime_10 = findViewById(R.id.btnBoost_2);
        bstTime_10.setOnClickListener(this);

        bstTime_15 = findViewById(R.id.btnBoost_3);
        bstTime_15.setOnClickListener(this);

        bstHint_1 = findViewById(R.id.btnBoost_4);
        bstHint_1.setOnClickListener(this);

        bstHint_5 = findViewById(R.id.btnBoost_5);
        bstHint_5.setOnClickListener(this);

        bstHint_10 = findViewById(R.id.btnBoost_6);
        bstHint_10.setOnClickListener(this);


        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        Menu menu = bottomNav.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        dbUser = new UserDatabaseHelper(this);

        //Retrieve user's balance from db
        Cursor cursor = dbUser.fetch_eCoins(email);
        if( cursor.getCount() >= 1) {
            while(cursor.moveToNext() ) {
                eCoins.setText(cursor.getString(0));
                userBalance = Integer.parseInt(cursor.getString(0));
            }
            cursor.close();
        }
    }

    @Override
    public void onClick(View v) {

        SharedPreferences preferencesEmail = this.getSharedPreferences(EMAIL, MODE_PRIVATE);
        String email = preferencesEmail.getString("email", "");

        int bstUnit = 0; //Boost unit
        int price = 0; //Boost price
        boolean checkBalance = false; //Check for sufficient balance to purchase boost
        String boost_name = "";
        eCoins = findViewById(R.id.tvCoins);

        try (Cursor cursor = dbUser.fetchUserBoost(email)) {
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    addTime_5_left = Integer.parseInt(cursor.getString(0));
                    addTime_10_left = Integer.parseInt(cursor.getString(1));
                    addTime_15_left = Integer.parseInt(cursor.getString(2));
                    hintBoost_left = Integer.parseInt(cursor.getString(3));
                }
            }
        }

        switch (v.getId()) {

            case R.id.btnBoost_1:
                price = 60;
                if (userBalance < price) {
                    checkBalance = false;
                } else {
                    bstUnit = addTime_5_left + 1;
                    boost_name = "addTime_5";
                    checkBalance = true;
                }
                break;


            case R.id.btnBoost_2:
                price = 100;
                if (userBalance < price) {
                    checkBalance = false;
                } else {
                    bstUnit =  addTime_10_left + 1;
                    boost_name = "addTime_10";
                    checkBalance = true;
                }
                break;

            case R.id.btnBoost_3:
                price = 150;
                if (userBalance < price) {
                    checkBalance = false;
                } else {
                    bstUnit = addTime_15_left + 1;
                    boost_name = "addTime_15";
                    checkBalance = true;
                }
                break;

            case R.id.btnBoost_4:
                price = 100;
                if (userBalance < price) {
                    checkBalance = false;
                } else {
                    bstUnit = hintBoost_left + 1;
                    boost_name = "showHintBoost";
                    checkBalance = true;
                }
                break;

            case R.id.btnBoost_5:
                price = 490;
                if (userBalance < price) {
                    checkBalance = false;
                } else {
                    bstUnit = hintBoost_left + 5;
                    boost_name = "showHintBoost";
                    checkBalance = true;
                }
                break;

            case R.id.btnBoost_6:
                price = 900;
                if (userBalance < price) {
                    checkBalance = false;
                } else {
                    bstUnit = hintBoost_left + 10;
                    boost_name = "showHintBoost";
                    checkBalance = true;
                }
                break;

            default:
                break;
        }

        if (checkBalance) {
            userBalance = userBalance - price;              //Calculate new balance after purchase
            dbUser.addBoost(email, boost_name, bstUnit);    //Add purchased boost into db
            dbUser.update_eCoins(email, userBalance);       //Update user balance in db

            Cursor cursor = dbUser.fetch_eCoins(email);      //Update the eCoin balance displayed
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    eCoins.setText(cursor.getString(0));
                }

                Toast.makeText(eShop.this, "Thank you for purchasing. Enjoy the game!!", Toast.LENGTH_LONG).show();
            }
        }
        else {
                Toast.makeText(eShop.this, "Insufficient balance! Please play more game.", Toast.LENGTH_LONG).show();
            }
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