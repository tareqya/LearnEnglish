package com.example.learnenglish.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.learnenglish.R;
import com.example.learnenglish.auth.LoginActivity;
import com.example.learnenglish.database.AuthController;
import com.example.learnenglish.model.User;


public class ProfileFragment extends Fragment {
    public static final String USER_INFO = "USER_INFO";
    private AuthController authController;
    private Context context;
    private TextView profile_TV_name;
    private TextView profile_TV_email;
    private TextView profile_TV_points;
    private CardView profile_CV_editDetails;
    private CardView profile_CV_logout;
    private User currentUser;

    public ProfileFragment(Context context) {
        this.context = context;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        findViews(view);
        initVars();
        return view;
    }

    private void findViews(View view) {
        profile_TV_name = view.findViewById(R.id.profile_TV_name);
        profile_TV_email = view.findViewById(R.id.profile_TV_email);
        profile_TV_points = view.findViewById(R.id.profile_TV_points);
        profile_CV_editDetails = view.findViewById(R.id.profile_CV_editDetails);
        profile_CV_logout = view.findViewById(R.id.profile_CV_logout);

    }

    private void initVars() {
        authController = new AuthController();

        profile_CV_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authController.logout();
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
                ((Activity)context).finish();
            }
        });

        profile_CV_editDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditProfileActivity.class);
                intent.putExtra(USER_INFO, currentUser);
                startActivity(intent);
            }
        });
    }

    public void setUser(User user) {
        currentUser = user;
        showUserInfo();
    }

    private void showUserInfo() {
        profile_TV_name.setText(currentUser.getFirstname() + " " + currentUser.getLastname());
        profile_TV_email.setText(currentUser.getEmail());
        profile_TV_points.setText(currentUser.getPoints() + " Point");
    }
}