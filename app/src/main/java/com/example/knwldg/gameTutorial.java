package com.example.knwldg;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class gameTutorial extends AppCompatActivity {

    private ImageView image;
    private TextView tvTutorial;
    private TextView currentPageCount, timer;
    private ImageButton previousPage, nextPage;
    private Button startQuiz;

    private List<Tutorial> tutorialList;
    private int tutorialCounter = 0;
    private int tutorialCountTotal;
    private Tutorial currentTutorial;

    //Pass quesID for question query in game
    public int[]quesID = new int[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_tutorial);

        //Get the category clicked from gameCategory screen
        Intent intent = getIntent();
        String chosenCategory = intent.getStringExtra("category");

        image = findViewById(R.id.ivTutorial);
        tvTutorial = findViewById(R.id.tvTutorial);
        currentPageCount = findViewById(R.id.tvCurrentPage);
        previousPage = findViewById(R.id.btnPreviousTut);
        nextPage = findViewById(R.id.btnNextTut);
        startQuiz = findViewById(R.id.btnStartQuiz);

        //Access the database
        QuesDbAccessHelper quesDbAccessHelper = QuesDbAccessHelper.getInstance(this);
        quesDbAccessHelper.openDatabase();

        //Fetching tutorial's description into tutorialList
        if (chosenCategory.equals("random_shuffle")) {
            tutorialList = quesDbAccessHelper.getRandomTutorial();
        } else {
            tutorialList = quesDbAccessHelper.getTutorial(chosenCategory);
        }
        tutorialCountTotal = tutorialList.size();

        Collections.shuffle(tutorialList);      //Shuffle the tutorialList
        showNextTutorial();

        quesDbAccessHelper.closeDatabase();

        //Setting the buttons
        startQuiz.setEnabled(false);

        previousPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    tutorialCounter--;
                    showNextTutorial();
            }
        });

        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    tutorialCounter++;
                    showNextTutorial();
                }
        });

        startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pass the array of quesID to gameActivity to fetch the corresponding questionList
                Intent intent = new Intent (gameTutorial.this, gameActivity.class);
                intent.putExtra("quesIdList", quesID);
                startActivity(intent);
                finish();
            }
        });
    }

    public void showNextTutorial() {

        nextPage.setEnabled(true);
        previousPage.setEnabled(true);

        if (tutorialCounter < tutorialCountTotal) {
            currentTutorial = tutorialList.get(tutorialCounter);

            //Displaying the tutorial
            tvTutorial.setText(currentTutorial.getTutorial());
            image.setImageBitmap(currentTutorial.getImage());
            currentPageCount.setText(Integer.toString(tutorialCounter + 1));

            //Retrieving quesID and put into an array
            quesID[tutorialCounter] = currentTutorial.getQues_id();

            if (tutorialCounter != 0) {
                previousPage.setEnabled(true);
            } else {
                previousPage.setEnabled(false);
            }

            if (tutorialCounter != 9) {
                nextPage.setEnabled(true);
            } else {
                nextPage.setEnabled(false);
                startQuiz.setEnabled(true);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}