package com.example.learnenglish;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.learnenglish.auth.LoginActivity;
import com.example.learnenglish.home.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {
    ImageView logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser currentUser=FirebaseAuth.getInstance().getCurrentUser();
       if( currentUser!=null) {
           Intent intent = new Intent(SplashScreen.this, MainActivity.class);
           startActivity(intent);
           finish(); //
       }
       else {
           EdgeToEdge.enable(this);
           setContentView(R.layout.activity_splash_screen);
           // ربط الشعار من XML
           logo = findViewById(R.id.logo);

           // تحميل الأنيميشن وتطبيقه على الشعار
           Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_animation);
           logo.startAnimation(animation);


           new Handler().postDelayed(new Runnable() {
               @Override
               public void run() {

                   Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                   startActivity(intent);
                   finish(); // إغلاق شاشة البداية حتى لا يعود إليها عند الضغط على زر الرجوع
               }
           }, 3000);


       }
    }
}