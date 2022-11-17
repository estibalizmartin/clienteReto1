package com.example.clientereto1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clientereto1.database.DatabaseHelper;
import com.example.clientereto1.fragments.ChangePasswordFragment;
import com.example.clientereto1.fragments.RegisterFragment;
import com.example.clientereto1.fragments.SignInFragment;
import com.example.clientereto1.models.User;

public class UserForms extends AppCompatActivity {

    FragmentTransaction fragmentTransaction;
    Fragment fragmentSignIn, fragmentRegister;
    TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_forms);
        getSupportActionBar().hide();

        toolbarTitle = findViewById(R.id.userFormTitle);
        fragmentSignIn = new SignInFragment();
        fragmentRegister = new RegisterFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        setFragmentLayout();

        findViewById(R.id.userFormBack).setOnClickListener(v -> {
            finish();
        });

    }

    public void setFragmentLayout() {

        if(getIntent().getExtras() != null) {
            switch (getIntent().getExtras().getString("fragment")){

                case "sign_in":

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView2, fragmentSignIn).runOnCommit(new Runnable() {
                        @Override
                        public void run() {
                            sign_in_onCreate();
                        }
                    }).commit();

                    fragmentTransaction.addToBackStack(null);
                    break;

                case "register":

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView2, fragmentRegister).commit();
                    fragmentTransaction.addToBackStack(null);
                    toolbarTitle.setText(getString(R.string.register_txt));
                    break;
                default:
                    showErrorInForm();
                    break;
            }
        } else
            showErrorInForm();
    }



    public void showErrorInForm() {
        toolbarTitle.setText(getString(R.string.error));
        new Toast(this).makeText(this, "Ocurríó un error inesperado", Toast.LENGTH_LONG).show();
    }

    public void sign_in_onCreate() {
        TextView usernameSignIn = findViewById(R.id.usernameTextViewSignIn);
        TextView passwordSignIn = findViewById(R.id.passwordTextViewSignIn);
        CheckBox checkRemember = findViewById(R.id.rememberCheckBox);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        if (!databaseHelper.isEmpty()) {
            User user = databaseHelper.getAllUsers();
            usernameSignIn.setText(user.getUsername());
            passwordSignIn.setText(user.getPassword());
        }

        findViewById(R.id.signInButtonSignIn).setOnClickListener(view -> {
            Intent intent = new Intent(this, SongList.class);

            String username = usernameSignIn.getText().toString();
            String password = passwordSignIn.getText().toString();

            if (checkRemember.isChecked()) {
                if ((!databaseHelper.isEmpty() && databaseHelper.deleteUser() == 1) || databaseHelper.isEmpty()) {
                    if (databaseHelper.createUser(username, password)) {
                        Toast.makeText(this, username + " created.", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                databaseHelper.deleteUser();
            }

                if(username == "" || username == null){
                    ((EditText) findViewById(R.id.usernameTextViewSignIn)).setError("Este campo no puede estar vacío");
                }
                if(username.length() < 5 || username.length() > 70){
                    ((EditText) findViewById(R.id.usernameTextViewSignIn)).setError("Este campo tiene que contener más de 5 caracteres");
                }
                if(password == "" || password == null){
                    ((EditText) findViewById(R.id.passwordTextViewSignIn)).setError("Este campo no puede estar vacío");
                }
                if(password.length() < 5 || password.length() > 70){
                    ((EditText) findViewById(R.id.passwordTextViewSignIn)).setError("Este campo tiene que contener más de 5 caracteres");
                }else{
                    startActivity(intent);

                    finish();
                //Toast.makeText(this, "Hay algún error en el usuario o la contraseña", Toast.LENGTH_SHORT).show();
        }

        });

        findViewById(R.id.resetTextView).setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView2, new ChangePasswordFragment()).runOnCommit(() -> changePassword_onCreate()).commit();

            fragmentTransaction.addToBackStack(null);
            toolbarTitle.setText(getString(R.string.reset_password_txt));
        });
    }

    private void changePassword_onCreate() {
    }
}