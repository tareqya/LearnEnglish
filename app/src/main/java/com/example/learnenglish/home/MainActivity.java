package com.example.learnenglish.home;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.learnenglish.NotificationReceiver;
import com.example.learnenglish.R;
import com.example.learnenglish.callback.UserCallBack;
import com.example.learnenglish.database.AuthController;
import com.example.learnenglish.database.UserController;
import com.example.learnenglish.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Calendar;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static String CHANNEL_ID = "2025";
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!checkPermissions()) {
                requestPermissions();
            }
        }

        createNotificationChannel();
        Calendar randomNextDay = getRandomDayInNextWeek();
        scheduleNotification(randomNextDay, "Did you learn english today?");
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public boolean checkPermissions() {
        return (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED);
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{
                        Manifest.permission.POST_NOTIFICATIONS,
                },
                100
        );
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

    public static void createNotification(Context context, String body) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.learneng)
                .setContentTitle("LearnEnglish - Reminder")
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(1, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "LearnEnglish APP";
            String description = "LearnEnglish reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void scheduleNotification(Calendar calendar, String msg) {

        // Create an intent to trigger the BroadcastReceiver
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("body", msg);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Schedule the alarm
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    public Calendar getRandomDayInNextWeek() {
        Calendar calendar = Calendar.getInstance(); // Current date and time
        Random random = new Random();

        int randomDaysToAdd = random.nextInt(7) + 1; // Random number between 1 and 7
        calendar.add(Calendar.DAY_OF_YEAR, randomDaysToAdd);

        // Optional: set a random time too, like random hour and minute
        calendar.set(Calendar.HOUR_OF_DAY, random.nextInt(24)); // 0-23 hours
        calendar.set(Calendar.MINUTE, random.nextInt(60));      // 0-59 minutes
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }
}