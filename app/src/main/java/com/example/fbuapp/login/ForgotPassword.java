package com.example.fbuapp.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fbuapp.R;
import com.parse.ParseException;
import com.parse.ParseUser;

public class ForgotPassword extends AppCompatActivity {

    private EditText etEmail;
    private Button btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etEmail = findViewById(R.id.etEmail);
        btnReset = findViewById(R.id.btnResetPassword);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                try {
                    ParseUser.requestPasswordReset(email);
                    Toast.makeText(ForgotPassword.this, "Email was sent with reset instructions", Toast.LENGTH_SHORT).show();
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(ForgotPassword.this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });





    }

}
