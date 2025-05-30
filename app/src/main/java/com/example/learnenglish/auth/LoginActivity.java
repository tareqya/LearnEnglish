package com.example.learnenglish.auth;

import android.content.Intent;
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
import com.example.learnenglish.home.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout login_TF_email;
    private TextInputLayout login_TF_password;
    private Button login_BTN_loginAction;
    private Button login_BTN_signup;
    private AuthController authController;
    private ProgressBar login_progress_loading;
    public static  boolean isLogged=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViews();
        initVars();
    }

    private void findViews() {
        login_TF_email = findViewById(R.id.login_TF_email);
        login_TF_password = findViewById(R.id.login_TF_password);
        login_BTN_loginAction = findViewById(R.id.login_BTN_loginAction);
        login_BTN_signup = findViewById(R.id.login_BTN_signup);
        login_progress_loading = findViewById(R.id.login_progress_loading);

    }

    private void initVars() {
        this.authController = new AuthController();


        // check if user already logged in
        if(this.authController.getCurrentUser() != null){
            openHomeScreen();
        }

        login_BTN_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        login_BTN_loginAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // validate input
                String email = login_TF_email.getEditText().getText().toString();
                String password = login_TF_password.getEditText().getText().toString();
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "You must enter email and password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                loginUser(email, password);
            }
        });
    }

    private void openHomeScreen(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void loginUser(String email, String password) {
        login_progress_loading.setVisibility(View.VISIBLE);
        authController.loginUser(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        login_progress_loading.setVisibility(View.INVISIBLE);
                        if(task.isSuccessful()){
                            openHomeScreen();
                        }else{
                            String err = task.getException().getMessage().toString();
                            Toast.makeText(LoginActivity.this, err, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}