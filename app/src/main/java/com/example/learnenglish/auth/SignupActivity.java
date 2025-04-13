package com.example.learnenglish.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.learnenglish.R;
import com.example.learnenglish.database.AuthController;
import com.example.learnenglish.database.UserController;
import com.example.learnenglish.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;


public class SignupActivity extends AppCompatActivity {
    private AuthController authController;
    private UserController userController;
    private TextInputLayout signup_TF_email;
    private TextInputLayout signup_TF_firstname;
    private TextInputLayout signup_TF_lastname;
    private TextInputLayout signup_TF_password;
    private TextInputLayout signup_TF_confirmPassword;
    private Button signup_BTN_createAccount;
    private ProgressBar signup_progress_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViews();
        initVars();
    }
    private void findViews() {
        signup_TF_email = findViewById(R.id.signup_TF_email);
        signup_TF_firstname = findViewById(R.id.signup_TF_firstname);
        signup_TF_lastname = findViewById(R.id.signup_TF_lastname);
        signup_TF_password = findViewById(R.id.signup_TF_password);
        signup_TF_confirmPassword = findViewById(R.id.signup_TF_confirmPassword);
        signup_BTN_createAccount = findViewById(R.id.signup_BTN_createAccount);
        signup_progress_loading = findViewById(R.id.signup_progress_loading);

    }
    private void initVars() {
        this.userController = new UserController();
        this.authController = new AuthController();
        signup_BTN_createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //validate input
                if(!inputValidation()) {
                    Toast.makeText(SignupActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // check password
                String password = signup_TF_password.getEditText().getText().toString();
                String confirmPassword = signup_TF_confirmPassword.getEditText().getText().toString();
                if(!password.equals(confirmPassword)){
                    Toast.makeText(SignupActivity.this, "mismatch between password and confirm password", Toast.LENGTH_SHORT).show();
                    return;
                }
                signup_progress_loading.setVisibility(View.VISIBLE);
                // create account
                createNewAccount();

            }
        });
    }

    private void createNewAccount() {
        String email = signup_TF_email.getEditText().getText().toString();
        String firstname = signup_TF_firstname.getEditText().getText().toString();
        String lastname = signup_TF_lastname.getEditText().getText().toString();
        String password = signup_TF_password.getEditText().getText().toString();

        User user = new User().setEmail(email).setFirstname(firstname).setLastname(lastname).setPassword(password);
        this.authController.createNewUser(user)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            user.setUid(task.getResult().getUser().getUid());
                            saveUserInfo(user);
                        }else{
                            String err = task.getException().getMessage().toString();
                            Toast.makeText(SignupActivity.this, err, Toast.LENGTH_SHORT).show();
                            signup_progress_loading.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }

    private void saveUserInfo(User user) {
        this.userController.saveUser(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        signup_progress_loading.setVisibility(View.INVISIBLE);
                        if(task.isSuccessful()){
                            Toast.makeText(SignupActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();
                            authController.logout();
                            finish();
                        }else{
                            String err = task.getException().getMessage().toString();
                            Toast.makeText(SignupActivity.this, err, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean inputValidation() {
        String email = signup_TF_email.getEditText().getText().toString();
        String firstname = signup_TF_firstname.getEditText().getText().toString();
        String lastname = signup_TF_lastname.getEditText().getText().toString();
        String password = signup_TF_password.getEditText().getText().toString();
        String confirmPassword = signup_TF_confirmPassword.getEditText().getText().toString();
        if(email.isEmpty() || firstname.isEmpty() || lastname.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
            return false;
        }

        return true;
    }


}