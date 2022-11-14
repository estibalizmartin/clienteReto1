package com.example.clientereto1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clientereto1.adapters.MyTableAdapter;
import com.example.clientereto1.connection.SongsRequest;
import com.example.clientereto1.models.Song;

import java.util.ArrayList;

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
        ArrayList<Song> listado = new ArrayList<>();
        MyTableAdapter myTableAdapter = new MyTableAdapter (this, R.layout.myrow_layout, listado);


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

        findViewById(R.id.signInButtonSignIn ).setOnClickListener( v -> {
            if (isConnected()) {
                SongsRequest songsRequest = new SongsRequest();
                Thread thread = new Thread( songsRequest );
                try {
                    thread.start();
                    thread.join(); // Awaiting response from the server...
                } catch (InterruptedException e) {
                    // Nothing to do here...
                }
                // Processing the answer
                ArrayList<Song> listSongs = songsRequest.getResponse();
                System.out.println(listSongs);
                listado.addAll( listSongs );
                ((ListView) findViewById( R.id.allSongsListView)).setAdapter (myTableAdapter);
            }
        });
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

    public boolean isConnected() {
        boolean ret = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext()
                    .getSystemService( Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if ((networkInfo != null) && (networkInfo.isAvailable()) && (networkInfo.isConnected()))
                ret = true;
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_communication), Toast.LENGTH_SHORT).show();
        }
        return ret;
    }
}