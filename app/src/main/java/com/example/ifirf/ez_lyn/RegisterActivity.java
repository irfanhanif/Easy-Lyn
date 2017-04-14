package com.example.ifirf.ez_lyn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
