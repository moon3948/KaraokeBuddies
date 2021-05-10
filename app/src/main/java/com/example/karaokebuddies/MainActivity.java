package com.example.karaokebuddies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private ImageButton button_music;
    private Button button_friends;
    private Button button_myProfile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_music = findViewById(R.id.button_music);
        button_friends = findViewById(R.id.button_friends);
        button_myProfile = findViewById(R.id.button_myProfile);

        button_music.setOnClickListener(this::launchMusic);

        button_friends.setOnClickListener(v -> {
            launchFriends(v);
        });

        button_myProfile.setOnClickListener(v -> {
            launchMyProfile(v);
        });

    }

    private void launchMusic(View view) {
        Intent intent = new Intent(MainActivity.this, SelectMusic.class);
        startActivity(intent);
    }

    private void launchFriends(View view) {
        Intent intent = new Intent(MainActivity.this, MyFriends.class);
        startActivity(intent);
    }

    private void launchMyProfile(View view) {
        Intent intent = new Intent(MainActivity.this, MyProfile.class);
        startActivity(intent);
    }
}