package com.example.clientereto1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clientereto1.database.DatabaseHelper;
import com.example.clientereto1.fragments.ChangePasswordFragment;
import com.example.clientereto1.fragments.RegisterFragment;
import com.example.clientereto1.fragments.SignInFragment;
import com.example.clientereto1.models.User;
import com.example.clientereto1.models.UserResponse;
import com.example.clientereto1.network.CreateUserRequest;
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
                toolbarTitle.setText(getString(R.string.sign_in_txt));
                break;

            case "register":

                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView2, fragmentRegister).runOnCommit(new Runnable() {
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
    }

    public void register_onCreate(){
        findViewById(R.id.registerButtonRegister).setOnClickListener(v -> {
            System.out.println("Entro");
            if (registerFormIsValid()) {
                System.out.println("Formulario  Válido");
                String userDataJson = generateUserJson();
                UserResponse response = new NetworkUtilites(this).makeRequest(new CreateUserRequest(userDataJson));
                System.out.println(response);
                if (response == null)
                    Toast.makeText(this, getString(R.string.request_error), Toast.LENGTH_LONG).show();
                else {
                    Toast.makeText(this, getString(R.string.user_created), Toast.LENGTH_LONG).show();
                    setFragmentLayout("sign_in");
                }

            }
        });
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

    public boolean editTextIsValid(EditText editText, int minimumLength, boolean isEmail) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        if (text.length() == 0 && !isEmail) {
            System.out.println("1");
            editText.setError(getString(R.string.empty_form_field));
            return false;
        }

         if (text.length() > 0 && text.length() < minimumLength && !isEmail) {
             System.out.println("2");
             editText.setError(getString(R.string.short_form_filed) + " " + minimumLength + " " + getString(R.string.character));
             return false;
         }

         if (isEmail && !android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
             editText.setError(getString(R.string.email_required_form_field));
             return false;
         }

        return true;
    }

    public String generateUserJson() {
        return  "{" +
                "\"username\": \"" + ((EditText) findViewById(R.id.usernameTextViewRegister)).getText().toString() + "\"," +
                "\"firstname\": \"" + ((EditText) findViewById(R.id.firstNameTextViewRegister)).getText().toString() + "\"," +
                "\"lastnames\": \"" + ((EditText) findViewById(R.id.lastNamesTextViewRegister)).getText().toString() + "\"," +
                "\"email\": \"" + ((EditText) findViewById(R.id.emailTextViewRegister)).getText().toString() + "\"," +
                "\"password\": \"" + ((EditText) findViewById(R.id.passwordTextViewRegister)).getText().toString() + "\"" +
                "}";
    }




}