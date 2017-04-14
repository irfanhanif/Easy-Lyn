package com.example.ifirf.ez_lyn;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private Button register_button, login_button;
    private EditText email_et, password_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.getSupportActionBar().setTitle("Login");

        register_button = (Button)findViewById(R.id.register_button);
        login_button = (Button)findViewById(R.id.login_button);

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register_intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(register_intent);
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email_et = (EditText)findViewById(R.id.email_et);
                password_et = (EditText)findViewById(R.id.password_et);

                String email = email_et.getText().toString().trim();
                String password = password_et.getText().toString().trim();

                if(email.equals("")){
                    email_et.setError("Mohon diisi email");
                }
                if(password.equals("")){
                    password_et.setError("Mohon password diisi");
                }
            }
        });
    }
}
