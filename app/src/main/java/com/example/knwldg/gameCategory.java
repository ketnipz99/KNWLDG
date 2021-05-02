package com.example.knwldg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.material.navigation.NavigationView;

import static android.provider.MediaStore.Video.VideoColumns.CATEGORY;

public class gameCategory extends AppCompatActivity implements View.OnClickListener {

    private Button randomShuffle, food, history, festivals, traditions, places, nature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_category);

        randomShuffle = findViewById(R.id.random_shuffle);
        randomShuffle.setOnClickListener(this);

        food = findViewById(R.id.choice_1);
        food.setOnClickListener(this);

        history = findViewById(R.id.choice_2);
        history.setOnClickListener(this);

        festivals = findViewById(R.id.choice_3);
        festivals.setOnClickListener(this);

        traditions = findViewById(R.id.choice_4);
        traditions.setOnClickListener(this);

        places  = findViewById(R.id.choice_5);
        places.setOnClickListener(this);

        nature = findViewById(R.id.choice_6);
        nature.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        String category = "";

        switch (view.getId()) {

            case R.id.random_shuffle:
                category = "random_shuffle";
                break;

            case R.id.choice_1:
                category = "Traditions";
                break;

            case R.id.choice_2:
                category = "History";
                break;

            case R.id.choice_3:
                category = "Festivals";
                break;

            case R.id.choice_4:
                category = "Food";
                break;

            case R.id.choice_5:
                category = "Places";
                break;

            case R.id.choice_6:
                category = "Nature";
                break;

            default:
                break;
        }
        //Passing the category value to next intent (gameTutorial)
        //to choose the corresponding group of tutorial
        Intent intent = new Intent(gameCategory.this, gameTutorial.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
