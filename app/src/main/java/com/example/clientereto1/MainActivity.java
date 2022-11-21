package com.example.clientereto1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        findViewById(R.id.signInButtonHome).setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, UserForms.class);
            intent.putExtra("fragment", "sign_in");
            startActivity(intent);
        });

        findViewById(R.id.registerButtonHome).setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, UserForms.class);
            intent.putExtra("fragment", "register");
            startActivity(intent);
        });

        findViewById(R.id.esLangButton).setOnClickListener(view -> {
            Locale locale = new Locale("es");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

            Intent refresh = new Intent(this, MainActivity.class);
            startActivity(refresh);
            finish();
        });

        findViewById(R.id.enLangButton).setOnClickListener(view -> {
            Locale locale = new Locale("en");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

            Intent refresh = new Intent(this, MainActivity.class);
            startActivity(refresh);
            finish();
        });
    }
}