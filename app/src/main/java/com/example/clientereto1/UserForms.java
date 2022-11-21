package com.example.clientereto1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clientereto1.database.DatabaseHelper;
import com.example.clientereto1.fragments.ChangePasswordFragment;
import com.example.clientereto1.fragments.RegisterFragment;
import com.example.clientereto1.fragments.SignInFragment;
import com.example.clientereto1.models.RequestResponse;
import com.example.clientereto1.models.User;
import com.example.clientereto1.models.UserResponse;
import com.example.clientereto1.network.ChangePasswordRequest;
import com.example.clientereto1.network.CreateUserRequest;

import com.example.clientereto1.network.LogInRequest;

import com.example.clientereto1.network.NetConfiguration;
import com.example.clientereto1.network.NetworkUtilites;

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

        if(getIntent().getExtras() != null)
            setFragmentLayout(getIntent().getExtras().getString("fragment"));
        else
            showErrorInForm();

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

                break;

            case "register":

                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView2, fragmentRegister).runOnCommit(new Runnable() {
                    @Override
                    public void run() {
                      register_onCreate();
                    }
                }).commit();

                fragmentTransaction.addToBackStack(null);

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

        toolbarTitle.setText(getString(R.string.sign_in_txt));

        CheckBox checkRemember = findViewById(R.id.rememberCheckBox);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        if (!databaseHelper.isEmpty()) {
            User user = databaseHelper.getAllUsers();
            ((EditText)findViewById(R.id.usernameTextViewSignIn)).setText(user.getUsername());
            ((EditText)findViewById(R.id.passwordTextViewSignIn)).setText(user.getPassword());
        }

        findViewById(R.id.signInButtonSignIn).setOnClickListener(view -> {
            Intent intent = new Intent(this, SongList.class);

            String username = ((EditText)findViewById(R.id.usernameTextViewSignIn)).getText().toString();
            String password = ((EditText)findViewById(R.id.passwordTextViewSignIn)).getText().toString();

            if (checkRemember.isChecked()) {
                if ((!databaseHelper.isEmpty() && databaseHelper.deleteUser() == 1) || databaseHelper.isEmpty()) {
                    if (databaseHelper.createUser(username, password)) {
                        //Toast.makeText(this, username + " created.", Toast.LENGTH_SHORT).show();
                    }
                }
            }


            if (signInFormIsValid()) {

                UserResponse loginResponse = new NetworkUtilites(this).makeRequest(new LogInRequest(generateLogInJson(), this));

                if (loginResponse.isAccess()) {

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("user_id", loginResponse.getId());
                    editor.putString("username", loginResponse.getUsername());
                    editor.commit();

                    Toast.makeText(this, loginResponse.getMessage(), Toast.LENGTH_LONG).show();
                    startActivity(intent);
                    finish();
                } else
                    Toast.makeText(this, loginResponse.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.resetTextView).setOnClickListener(v -> {
            ChangePasswordFragment changePasswordFragment = new ChangePasswordFragment();
            getSupportFragmentManager().beginTransaction().replace( R.id.fragmentContainerView2,
                    changePasswordFragment).runOnCommit(() -> changePassword_onCreate()).commit();

            fragmentTransaction.addToBackStack(null);
            toolbarTitle.setText(getString(R.string.reset_password_txt));
        });
    }

    private void changePassword_onCreate() {
        findViewById(R.id.userFormBack).setOnClickListener(v -> {
            setFragmentLayout("sign_in");
        });

        (findViewById(R.id.resetButton)).setOnClickListener(v -> {
            if (changePasswordFormIsValid()){
                RequestResponse response = new NetworkUtilites(this).makeRequest(new ChangePasswordRequest(generateChangePasswordJson(), this));
                Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();

                if (response.isAccess()) {
                    setFragmentLayout("sign_in");
                }
            }
        });
    }

    public void register_onCreate(){
        toolbarTitle.setText(getString(R.string.register_txt));
        findViewById(R.id.registerButtonRegister).setOnClickListener(v -> {

            if (registerFormIsValid()) {

                String userDataJson = generateRegisterJson();
                UserResponse response = new NetworkUtilites(this).makeRequest(new CreateUserRequest(userDataJson));

                if (response == null)
                    Toast.makeText(this, getString(R.string.request_error), Toast.LENGTH_LONG).show();
                else {
                    Toast.makeText(this, getString(R.string.user_created), Toast.LENGTH_LONG).show();
                    setFragmentLayout("sign_in");
                }


            }
        });
    }

    private boolean signInFormIsValid() {
        boolean isValid = true;

        if (!editTextIsValid(findViewById(R.id.usernameTextViewSignIn), 5, false)) isValid = false;
        if (!editTextIsValid(findViewById(R.id.passwordTextViewSignIn), 5, false)) isValid = false;

        return isValid;

    }

    private boolean registerFormIsValid() {
        boolean isValid = true;

        EditText password = ((EditText)findViewById(R.id.passwordTextViewRegister));
        EditText confirmPassword = ((EditText)findViewById(R.id.confirmPasswordTextViewRegister));

        if (!editTextIsValid(findViewById(R.id.usernameTextViewRegister), 5, false)) isValid = false;
        if (!editTextIsValid(findViewById(R.id.firstNameTextViewRegister), 1, false)) isValid = false;
        if (!editTextIsValid(findViewById(R.id.emailTextViewRegister), 5, true)) isValid = false;
        if (!editTextIsValid(findViewById(R.id.confirmPasswordTextViewRegister), 5, false)) isValid = false;
        if (!editTextIsValid(findViewById(R.id.passwordTextViewRegister), 5, false)) isValid = false;
        if (!editTextIsValid(findViewById(R.id.lastNamesTextViewRegister), 1, false)) isValid = false;

        if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
            password.setError(getString(R.string.passwords_do_not_match));
            confirmPassword.setError(getString(R.string.passwords_do_not_match));
            isValid = false;
        }

        return isValid;
    }

    private boolean changePasswordFormIsValid() {
        boolean isValid = true;

        EditText password = ((EditText)findViewById(R.id.newPasswordTextViewReset));
        EditText confirmPassword = ((EditText)findViewById(R.id.confirmNewPasswordTextViewReset));

        if (!editTextIsValid(findViewById(R.id.usernameEditTextChangePass), 5, false)) isValid = false;
        if (!editTextIsValid(findViewById(R.id.oldPasswordTextViewReset), 5, false)) isValid = false;
        if (!editTextIsValid(password, 5, false)) isValid = false;
        if (!editTextIsValid(confirmPassword, 5, false)) isValid = false;

        if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
            password.setError(getString(R.string.passwords_do_not_match));
            confirmPassword.setError(getString(R.string.passwords_do_not_match));
            isValid = false;
        }

        return isValid;
    }

    public boolean editTextIsValid(EditText editText, int minimumLength, boolean isEmail) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        if (text.length() == 0 && !isEmail) {
            editText.setError(getString(R.string.empty_form_field));
            return false;
        }

         if (text.length() > 0 && text.length() < minimumLength && !isEmail) {
             editText.setError(getString(R.string.short_form_filed) + " " + minimumLength + " " + getString(R.string.character));
             return false;
         }

         if (isEmail && !android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
             editText.setError(getString(R.string.email_required_form_field));
             return false;
         }

        return true;
    }

    private String generateRegisterJson() {

        return  "{" +
                "\"username\": \"" + ((EditText) findViewById(R.id.usernameTextViewRegister)).getText().toString() + "\"," +
                "\"firstname\": \"" + ((EditText) findViewById(R.id.firstNameTextViewRegister)).getText().toString() + "\"," +
                "\"lastnames\": \"" + ((EditText) findViewById(R.id.lastNamesTextViewRegister)).getText().toString() + "\"," +
                "\"email\": \"" + ((EditText) findViewById(R.id.emailTextViewRegister)).getText().toString() + "\"," +
                "\"password\": \"" + ((EditText) findViewById(R.id.passwordTextViewRegister)).getText().toString() + "\"" +
                "}";
    }

    private String generateLogInJson() {

        return  "{" +
                "\"username\": \"" + ((EditText) findViewById(R.id.usernameTextViewSignIn)).getText().toString() + "\"," +
                "\"password\": \"" + ((EditText) findViewById(R.id.passwordTextViewSignIn)).getText().toString() + "\"" +
                "}";
    }

    private String generateChangePasswordJson() {
        return  "{" +
                "\"username\": \"" + ((EditText) findViewById(R.id.usernameEditTextChangePass)).getText().toString() + "\"," +
                "\"oldPassword\": \"" + ((EditText) findViewById(R.id.oldPasswordTextViewReset)).getText().toString() + "\"," +
                "\"newPassword\": \"" + ((EditText) findViewById(R.id.newPasswordTextViewReset)).getText().toString() + "\"" +
                "}";
    }




}