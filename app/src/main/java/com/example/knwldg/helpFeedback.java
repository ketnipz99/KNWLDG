package com.example.knwldg;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class helpFeedback extends AppCompatActivity {

    private TextView objective, objectiveDesc, aboutUs, aboutUsDesc, vision, visionDesc;
    private Button submit;
    private EditText feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_feedback);

        aboutUs = findViewById(R.id.tvAboutUs);
        aboutUsDesc = findViewById(R.id.tvAboutUsDescription);
        vision = findViewById(R.id.tvVision);
        visionDesc = findViewById(R.id.tvVisionDescription);
        objective = findViewById(R.id.tvObjective);
        objectiveDesc = findViewById(R.id.tvObjectiveDescription);

        submit =findViewById(R.id.btnSubmitFb);
        feedback = findViewById(R.id.etFeedback);

        aboutUs.setText(R.string.about_us);
        aboutUsDesc.setText(R.string.about_us_description);
        objective.setText(R.string.objective);
        objectiveDesc.setText(R.string.objective_desc);
        vision.setText(R.string.vision);
        visionDesc.setText(R.string.vision_desc);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(helpFeedback.this, "Thank you for contacting us", Toast.LENGTH_SHORT).show();
                feedback.setText("Type your feedback here");
            }
        });
    }
}