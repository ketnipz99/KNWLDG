package com.example.knwldg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import static android.provider.ContactsContract.Intents.Insert.EMAIL;

public class gameSummary extends AppCompatActivity {

    private TextView resultComment, coinEarned, finalScore;
    private Button backHome;

    private UserDatabaseHelper dbUser;

    String comment = "";

    Animation anim_comment, anim_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_summary);

        int eCoinBalance = 0;
        int eCoin = Integer.parseInt(getIntent().getExtras().get("eCoinScore").toString());
        int score = eCoin / 2;       //Calculate score per 100%

        //Share email across different activities for SQLite query
        SharedPreferences preferencesEmail = this.getSharedPreferences(EMAIL, MODE_PRIVATE);
        String email = preferencesEmail.getString("email", "");

        resultComment = findViewById(R.id.tvResultComment);
        coinEarned = findViewById(R.id.tvCoinEarned);
        finalScore = findViewById(R.id.tvScore);
        backHome = findViewById(R.id.btnSummaryHome);
        anim_comment = AnimationUtils.loadAnimation(this, R.anim.animation1);
        anim_score = AnimationUtils.loadAnimation(this, R.anim.animation2);

        //UserDatabaseHelper instance
        dbUser = new UserDatabaseHelper(this);

        //Setting animation for result comment and total coin earned in the game
        resultComment.setAnimation(anim_comment);
        coinEarned.setAnimation(anim_score);

        if (score < 50) {
           resultComment.setTextColor(Color.DKGRAY);
           eCoin = 0; //Cannot earn any eCoin if failed
           comment = "UNFORTUNATELY...YOU FAILED AND NO COIN WILL BE REWARDED...TOO BAD :(";
           coinEarned.setText("You have earned NOTHING");

        } else if (score < 100){
            resultComment.setTextColor(Color.BLACK);
            comment = "CONGRATULATION!!! YOU HAVE PASSED THE QUIZ WITH FLYING COLORS";
            coinEarned.setText("You have earned " + eCoin + " eCoins");
        } else if (score == 100) {
            resultComment.setTextColor(Color.GREEN);
            comment = "MWAHH PERFFECTOOO!!";
            coinEarned.setText("You have earned a full bucket of " + eCoin + " eCoins");
        }
        resultComment.setText(comment);
        finalScore.setText("Score: " + score + "%");

        //Fetch user's previous eCoin balance first
        Cursor cursor_eCoins = dbUser.fetch_eCoins(email);

        if( cursor_eCoins.getCount() >= 1) {
            while (cursor_eCoins.moveToNext()) {
                cursor_eCoins.moveToFirst();
                eCoinBalance = Integer.parseInt(cursor_eCoins.getString(0));
            }
            cursor_eCoins.close();
        }

        eCoinBalance = eCoinBalance + eCoin;        //Add up prev eCoin balance with eCoin earned
        dbUser.update_eCoins(email, eCoinBalance);  //Update the user profile with latest eCoin balance after the game

        updateStreak(email, score);                 //Function to update user's statistic

        backHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (gameSummary.this, home.class);
                startActivity (intent);
                finish();
            }
        });

    }

    public void updateStreak (String email, int score) {
        //Retrieve user previous statistics

        int passStreak = 0;
        int distinctionStreak = 0;
        int fullmarkStreak = 0;
        Cursor cursorStreak = dbUser.fetchStatistics(email);

        if( cursorStreak.getCount() >= 1) {
            while (cursorStreak.moveToNext()) {
                passStreak = Integer.parseInt(cursorStreak.getString(0));
                distinctionStreak = Integer.parseInt(cursorStreak.getString(1));
                fullmarkStreak = Integer.parseInt(cursorStreak.getString(2));
            }
            cursorStreak.close();
        }

        if (score < 50) {
            distinctionStreak = 0;
            passStreak = 0;
            fullmarkStreak = 0;
        } else if (score < 80 ) {
            //score above 50 means pass
            passStreak = passStreak + 1;
            //set to zero because no more streak for scoring > 80 marks
            distinctionStreak = 0;
            fullmarkStreak = 0;
        } else if (score < 100) {
            //score above 80 means distinction
            distinctionStreak = distinctionStreak + 1;
            passStreak = passStreak + 1;
            fullmarkStreak = 0;
        } else if (score == 100) {
            //100% = fullmark
            fullmarkStreak = fullmarkStreak + 1;
            distinctionStreak = distinctionStreak + 1;
            passStreak = passStreak + 1;
        }
        //Update user db after operation
        dbUser.updateStatistics(email, passStreak, distinctionStreak, fullmarkStreak);
    }
}