package com.example.learnenglish.home;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.learnenglish.R;
import com.example.learnenglish.callback.UserCallBack;
import com.example.learnenglish.database.AuthController;
import com.example.learnenglish.database.UserController;
import com.example.learnenglish.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private HomeFragment homeFragment;
    private ProfileFragment profileFragment;
    private BottomNavigationView main_BN;
    private FrameLayout main_frame_home;
    private FrameLayout main_frame_status;
    private FrameLayout main_frame_profile;
    private AuthController authController;
    private UserController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        findViews();
        initViews();
    }

    private void findViews() {
        main_BN = findViewById(R.id.main_BN);
        main_frame_home = findViewById(R.id.main_frame_home);
        main_frame_profile = findViewById(R.id.main_frame_profile);
        main_frame_status = findViewById(R.id.main_frame_status);

    }
    private void initViews() {
        this.authController = new AuthController();
        this.userController = new UserController();

        profileFragment = new ProfileFragment(this);
        homeFragment = new HomeFragment(this);

        getSupportFragmentManager().beginTransaction().add(R.id.main_frame_home, homeFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame_profile, profileFragment).commit();

        main_frame_home.setVisibility(View.VISIBLE);

        main_BN.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.menu_home){
                    main_frame_profile.setVisibility(View.INVISIBLE);
                    main_frame_home.setVisibility(View.VISIBLE);
                    main_frame_status.setVisibility(View.INVISIBLE);
                }else if(item.getItemId() == R.id.menu_profile){
                    main_frame_profile.setVisibility(View.VISIBLE);
                    main_frame_home.setVisibility(View.INVISIBLE);
                    main_frame_status.setVisibility(View.INVISIBLE);
                }
                return true;
            }
        });

        FetchCurrentUserInfo();

    }

    private void FetchCurrentUserInfo(){
        userController.setUserCallBack(new UserCallBack() {
            @Override
            public void onUserInfoFetchComplete(User user) {
                profileFragment.setUser(user);
                homeFragment.setUser(user);
            }
        });

        String uid = authController.getCurrentUser().getUid();
        userController.getUserInfo(uid);
    }
}