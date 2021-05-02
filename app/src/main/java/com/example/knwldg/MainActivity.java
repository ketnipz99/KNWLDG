package com.example.knwldg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button Login, Signup;
    private ImageView Logo;

    //Initialize animation
    Animation anim1_welcome, anim2_buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Login = findViewById(R.id.btnLogin);
        Signup = findViewById(R.id.btnSignup);

        //Link anim file with java
        anim1_welcome = AnimationUtils.loadAnimation(this, R.anim.animation1);
        anim2_buttons = AnimationUtils.loadAnimation(this, R.anim.animation2);

        //Link the TextView message
        Logo = findViewById(R.id.appsLogo);

        //setAnimation
        Logo.setAnimation (anim1_welcome);
        Signup.setAnimation(anim2_buttons);
        Login.setAnimation(anim2_buttons);

        //Access the RememberMe checkbox value
        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        String checkbox = preferences.getString("remember", "");

        //If not logged out, skip welcome page and proceed to home
        if(checkbox.equals("true")) {
            Intent intent = new Intent(MainActivity.this, home.class);
            startActivity(intent);
            finish();
        }

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (MainActivity.this, logIn.class);
                startActivity(intent);
                finish();
            }
        });

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (MainActivity.this, signUp.class);
                startActivity(intent);
                finish();
            }
        });
    }
}