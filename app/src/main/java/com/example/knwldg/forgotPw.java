package com.example.knwldg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class forgotPw extends AppCompatActivity {

    private EditText EmailReset;
    private Button Submit;
    private UserDatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pw);

        EmailReset = findViewById(R.id.etEmailReset);
        Submit = findViewById(R.id.btnEmailReset);
        db = new UserDatabaseHelper(this);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = EmailReset.getText().toString();
                Boolean checkUser = db.checkUser(email);

                if(!checkUser) {
                    Toast.makeText(forgotPw.this, "Email not registered. Please sign up.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(forgotPw.this, signUp.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(forgotPw.this, "Request sent! Do check your email inbox", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(forgotPw.this, logIn.class);
                    startActivity(intent);
                }
            }
        });



    }
}