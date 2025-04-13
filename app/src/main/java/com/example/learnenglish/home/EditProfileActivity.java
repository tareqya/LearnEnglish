package com.example.learnenglish.home;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.learnenglish.database.UserController;
import com.example.learnenglish.R;
import com.example.learnenglish.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

public class EditProfileActivity extends AppCompatActivity {

    private TextInputLayout profileEdit_TF_firstName;
    private TextInputLayout profileEdit_TF_lastName;
    private Button editProfile_BTN_update;
    private User userInfo;
    private UserController userController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Intent intent = getIntent();
        userInfo = (User)  intent.getSerializableExtra(ProfileFragment.USER_INFO);
        findViews();
        initVars();
    }

    private void findViews() {
        profileEdit_TF_firstName = findViewById(R.id.profileEdit_TF_firstName);
        profileEdit_TF_lastName = findViewById(R.id.profileEdit_TF_lastName);
        editProfile_BTN_update = findViewById(R.id.editProfile_BTN_update);
    }

    private void initVars() {

        profileEdit_TF_firstName.getEditText().setText(userInfo.getFirstname());
        profileEdit_TF_lastName.getEditText().setText(userInfo.getLastname());
        userController = new UserController();

        editProfile_BTN_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userInfo.setFirstname(profileEdit_TF_firstName.getEditText().getText().toString());
                userInfo.setLastname(profileEdit_TF_lastName.getEditText().getText().toString());
                userController.saveUser(userInfo)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    finish();
                                }else{
                                    String err = task.getException().getMessage().toString();
                                    Toast.makeText(EditProfileActivity.this, err, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }


}