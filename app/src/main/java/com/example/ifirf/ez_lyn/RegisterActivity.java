package com.example.ifirf.ez_lyn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    private EditText email_register_et, name_register_et, password_register_et, password_conf_register_et;
    private Button register_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email_register_et = (EditText) findViewById(R.id.email_register_et);
        name_register_et = (EditText) findViewById(R.id.name_register_et);
        password_register_et = (EditText) findViewById(R.id.password_register_et);
        password_conf_register_et = (EditText) findViewById(R.id.password_conf_register_et);

        register_button = (Button) findViewById(R.id.register_now);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = email_register_et.getText().toString().trim();
                String name = name_register_et.getText().toString().trim();
                String password = password_register_et.getText().toString().trim();
                String password_conf = password_conf_register_et.getText().toString().trim();

                if (email.equals("")){
                    email_register_et.setError("Email harus diisi");
                    return;
                }
                if (name.equals("")){
                    name_register_et.setError("Nama harus diisi");
                    return;
                }
                if (password.equals("")){
                    password_register_et.setError("Password harus diisi");
                }
                if (password_conf.equals("")){
                    password_conf_register_et.setError("Konfirmasi password harus diini");
                    return;
                }
                if (!password.equals(password_conf)){
                    password_register_et.setError("Kombinasi password tidak sama");
                    password_conf_register_et.setError("Kombinasi password harus sama");
                    return;
                }
            }
        });

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setTitle("Pendaftaran");
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent to_intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(to_intent);
        finish();
        return true;
    }
}
