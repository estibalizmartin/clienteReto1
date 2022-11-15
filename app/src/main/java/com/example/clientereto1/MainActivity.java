package com.example.clientereto1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView resetText;
    private Button signInBtn, registerBtn;
    private ImageButton resetPasswordBack;
    private ImageButton signInBack;
    private ImageButton registerBack;



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
    }
}