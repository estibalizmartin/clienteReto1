package com.example.clientereto1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clientereto1.fragments.ChangePasswordFragment;
import com.example.clientereto1.fragments.RegisterFragment;
import com.example.clientereto1.fragments.SignInFragment;

public class UserForms extends AppCompatActivity {

    FragmentTransaction fragmentTransaction;
    Fragment fragmentSignIn, fragmentRegister, fragmentChangePassword;
    TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_forms);
        getSupportActionBar().hide();

        toolbarTitle = findViewById(R.id.userFormTitle);

        fragmentSignIn = new SignInFragment();
        fragmentRegister = new RegisterFragment();
        fragmentChangePassword = new ChangePasswordFragment();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        String fragment = (getIntent().getExtras() != null) ? getIntent().getExtras().getString("fragment") : "";
        setFragmentLayout(fragment);

    }

    public void setFragmentLayout(String fragment) {

        switch (fragment){

            case "sign_in":
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView2, fragmentSignIn).runOnCommit(new Runnable() {
                    @Override
                    public void run() {
                        sign_in_onCreate();
                    }
                }).commit();

                fragmentTransaction.addToBackStack(null);
                toolbarTitle.setText(getString(R.string.sign_in_txt));
                break;

            case "register":
                getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainerView2, fragmentRegister).runOnCommit(new Runnable() {
                    @Override
                    public void run() {
                        register_onCreate();
                    }
                }).commit();

                fragmentTransaction.addToBackStack(null);
                toolbarTitle.setText(getString(R.string.register_txt));
                break;

            default:
                showErrorInForm();
                break;
        }

        findViewById(R.id.userFormBack).setOnClickListener(v -> {
            finish();
        });
    }

    public void showErrorInForm() {
        toolbarTitle.setText(getString(R.string.error));
        new Toast(this).makeText(this, "Ocurríó un error inesperado", Toast.LENGTH_LONG).show();
    }

    public void sign_in_onCreate() {
        findViewById(R.id.resetTextView).setOnClickListener(v -> {

            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView2, fragmentChangePassword).runOnCommit(new Runnable() {
                @Override
                public void run() {
                    changePassword_onCreate();
                }
            }).commit();

            fragmentTransaction.addToBackStack(null);
            toolbarTitle.setText(getString(R.string.reset_password_txt));
        });

        findViewById(R.id.signInButtonSignIn).setOnClickListener(view -> {
            //submit del formulario
        });
    }

    public void register_onCreate() {

    }

    public void changePassword_onCreate() {
        findViewById(R.id.userFormBack).setOnClickListener(v -> {
            setFragmentLayout("sign_in");
        });
    }
}