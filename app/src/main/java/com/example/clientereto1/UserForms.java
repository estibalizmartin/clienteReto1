package com.example.clientereto1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clientereto1.database.DatabaseHelper;
import com.example.clientereto1.fragments.ChangePasswordFragment;
import com.example.clientereto1.fragments.RegisterFragment;
import com.example.clientereto1.fragments.SignInFragment;


public class UserForms extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    FragmentTransaction fragmentTransaction;
    Fragment fragmentSignIn, fragmentRegister;
    TextView toolbarTitle, usernameSignIn, passwordSignIn;
    CheckBox checkRemember;
    String username, password;
    Boolean isRemembered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_forms);
        getSupportActionBar().hide();

        toolbarTitle = findViewById(R.id.userFormTitle);
        fragmentSignIn = new SignInFragment();
        fragmentRegister = new RegisterFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        isRemembered = false;

        setFragmentLayout();

        findViewById(R.id.userFormBack).setOnClickListener(v -> {
            finish();
        });
    }

    public void setFragmentLayout() {

        if(getIntent().getExtras() != null) {
            switch (getIntent().getExtras().getString("fragment")){

                case "sign_in":
                    getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainerView2, fragmentSignIn).runOnCommit(new Runnable() {
                        @Override
                        public void run() {
                            sign_in_onCreate();
                        }
                    }).commit();

                    fragmentTransaction.addToBackStack(null);
                    break;

                case "register":

                    getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainerView2, fragmentRegister).commit();
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
        usernameSignIn = findViewById(R.id.usernameTextViewSignIn);
        passwordSignIn = findViewById(R.id.passwordTextViewSignIn);
        checkRemember = findViewById(R.id.rememberCheckBox);

        username = usernameSignIn.getText().toString();
        password = passwordSignIn.getText().toString();

        databaseHelper = new DatabaseHelper(this);

        findViewById(R.id.resetTextView).setOnClickListener(v -> {
            fragmentTransaction.replace(R.id.fragmentContainerView2, new ChangePasswordFragment()).commit();
            fragmentTransaction.addToBackStack(null);
            toolbarTitle.setText(getString(R.string.reset_password_txt));
        });

        findViewById(R.id.signInButtonSignIn).setOnClickListener(view -> {
            if (checkRemember.isChecked()) {
                if (databaseHelper.createUser(username, password)) {

                }
            }
        });
    }
}