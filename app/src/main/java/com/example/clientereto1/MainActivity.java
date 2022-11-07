package com.example.clientereto1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button signInBtn, registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        signInBtn = findViewById(R.id.signInButtonHome);
        registerBtn = findViewById(R.id.registerButtonHome);

        signInBtn.setOnClickListener(view -> setContentView(R.layout.layout_sign_in));

        registerBtn.setOnClickListener(view -> setContentView(R.layout.layout_register));
    }
}