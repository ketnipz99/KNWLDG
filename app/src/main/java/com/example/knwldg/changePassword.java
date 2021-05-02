package com.example.knwldg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.provider.ContactsContract.Intents.Insert.EMAIL;

public class changePassword extends AppCompatActivity {

    //Pw == Password
    private EditText oldPw;
    private EditText newPw_1, newPw_2;
    private Button updatePw;
    private UserDatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        oldPw = findViewById(R.id.etOldPassword);
        newPw_1 = findViewById(R.id.etNewPassword1);
        newPw_2 = findViewById(R.id.etNewPassword2);
        updatePw = findViewById(R.id.btnUpdatePw);
        db = new UserDatabaseHelper(this);


        updatePw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Read the user's input
                String current_pw = oldPw.getText().toString();
                String new_pw = newPw_1.getText().toString();
                String re_pw = newPw_2.getText().toString();
                SharedPreferences preferencesEmail = getSharedPreferences(EMAIL, MODE_PRIVATE);
                String email = preferencesEmail.getString("email", "");


                //Condition check on user's input
                if (current_pw.equals("") || new_pw.equals("") || re_pw.equals("")) {
                    Toast.makeText(changePassword.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                }
                else if (new_pw.equals(current_pw)) {
                    Toast.makeText(changePassword.this, "New password cannot be similar as current password.", Toast.LENGTH_SHORT).show();
                }
                else if (!new_pw.equals(re_pw)) {
                    Toast.makeText(changePassword.this, "Confirmation password does not match with the new password", Toast.LENGTH_SHORT).show();
                }
                else {
                    //Use UserDatabaseHelper class to verify password
                    Boolean authenticateUser = db.checkPassword(email, current_pw);

                    if (!authenticateUser) {
                        Toast.makeText(changePassword.this, "Invalid current password", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Boolean updatePw = db.updatePassword(email, new_pw);

                        if (updatePw) {
                                Toast.makeText(changePassword.this, "Password updated successfully!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(changePassword.this, "Fail to update password", Toast.LENGTH_SHORT).show();
                            }
                    }
                }
            }
        });
    }
}