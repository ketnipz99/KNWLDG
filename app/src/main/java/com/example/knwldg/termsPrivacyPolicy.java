package com.example.knwldg;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class termsPrivacyPolicy extends AppCompatActivity {

    private TextView termsPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_privacy_policy);

        termsPolicy = findViewById(R.id.tvTermsPolicy);

        termsPolicy.setText(R.string.terms);
    }
}