package com.example.clientereto1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView resetText;
    private Button signInBtn, registerBtn;
    private ImageButton resetPasswordBack;
    private ImageButton signInBack;
    private ImageButton registerBack;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        createMainButtons();

    }

    public void fromMainToSingIn(){
        //Estamos dentro de signIn
        setContentView(R.layout.layout_sign_in);

        resetText = findViewById(R.id.resetTextView);
        signInBack = findViewById(R.id.signInBack);

        resetText.setOnClickListener(viewReset -> {
            fromSignInToResetPassword();
        });
        signInBack.setOnClickListener(viewBack -> {
            fromSignInToMain();
        });
    }
    public void fromMainToRegister(){
        setContentView(R.layout.layout_register);
        registerBack = findViewById(R.id.registerBack);
        registerBack.setOnClickListener(viewBackToMain -> {
            fromRegisterToMain();
        });
    }
    public void fromSignInToResetPassword(){
        //Estamos dentro de ResetPassword
        setContentView(R.layout.layout_reset_password);
        resetPasswordBack = findViewById(R.id.resetPasswordBack);
        resetPasswordBack.setOnClickListener(viewBackToMain -> {
            fromResetPasswordToMain();
        });
    }
    public void fromSignInToMain(){
        setContentView(R.layout.activity_main);
        createMainButtons();
    }
    public void fromResetPasswordToMain(){
        setContentView(R.layout.activity_main);
        createMainButtons();
    }
    public void fromRegisterToMain(){
        setContentView(R.layout.activity_main);
        createMainButtons();
    }
    public void createMainButtons(){
        signInBtn = findViewById(R.id.signInButtonHome);
        registerBtn = findViewById(R.id.registerButtonHome);

        signInBtn.setOnClickListener(viewSignIn -> {
            fromMainToSingIn();
        });

        registerBtn.setOnClickListener(view -> {
            fromMainToRegister();
        });
    }
}