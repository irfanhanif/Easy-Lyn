package com.example.ifirf.ez_lyn;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ifirf.ez_lyn.pengemudi.Menuu;
import com.example.ifirf.ez_lyn.pengemudi.PengemudiActivity;
import com.example.ifirf.ez_lyn.penumpang.PenumpangActivity;

public class LoginActivity extends AppCompatActivity {

    private Button login_button;
    private EditText email_et, password_et;
    private TextView wrong_login, register_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.getSupportActionBar().setTitle("Login");

        register_button = (TextView) findViewById(R.id.register_button);
        login_button = (Button)findViewById(R.id.login_button);

        register_button.setPaintFlags(register_button.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
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
                    email_et.setError("Email harus diisi");
                    return;
                }
                if(password.equals("")){
                    password_et.setError("Password harus diisi");
                    return;
                }

                if(email.equals("penumpang") && password.equals("penumpang")){
                    Toast.makeText(LoginActivity.this, "Yeay! Anda berhasil login", Toast.LENGTH_LONG).show();
                    Intent penumpang_intent = new Intent(LoginActivity.this, PenumpangActivity.class);

                    InputMethodManager imm = (InputMethodManager)getSystemService(LoginActivity.this.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                    startActivity(penumpang_intent);
                    finish();
                }
                else if(email.equals("pengemudi") && password.equals("pengemudi")){
                    Toast.makeText(LoginActivity.this, "Yeay! Anda berhasil login", Toast.LENGTH_LONG).show();
                    Intent pengemudi_intent = new Intent(LoginActivity.this, PengemudiActivity.class);
                    startActivity(pengemudi_intent);
                    finish();
                }
                else{
                    Toast.makeText(LoginActivity.this, "Ups! Coba lagi.", Toast.LENGTH_SHORT).show();
                    wrong_login = (TextView) findViewById(R.id.wrong_login);
                    wrong_login.setVisibility(View.VISIBLE);
                    email_et.setText("");
                    password_et.setText("");
                }
            }
        });
    }
}
