package com.example.knwldg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static android.provider.ContactsContract.Intents.Insert.EMAIL;

public class editProfile extends AppCompatActivity {

    private EditText newName;
    private Button updateProfile;
    private Button changePw;
    private ImageView profilePic;
    private UserDatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        newName = findViewById(R.id.etEditName);
        updateProfile = findViewById(R.id.btnUpdateProfile);
        changePw = findViewById(R.id.btnChgePw);
        profilePic = findViewById(R.id.ivEditProfile);
        db = new UserDatabaseHelper(this);



    updateProfile.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Get user's input
            String new_name = newName.getText().toString();
            SharedPreferences preferencesEmail = getSharedPreferences(EMAIL, MODE_PRIVATE);
            String email = preferencesEmail.getString("email", "");

            Boolean updateProfile = db.updateProfile (email, new_name);

            if(updateProfile) {
                Toast.makeText(editProfile.this, "Profile updated.", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(editProfile.this, "Failed to update profile. ", Toast.LENGTH_SHORT).show();
            }

        }
    });

    changePw.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent (editProfile.this, changePassword.class);
            startActivity(intent);
        }
    });
    }
}