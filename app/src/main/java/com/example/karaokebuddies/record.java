package com.example.karaokebuddies;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class record extends AppCompatActivity {
    private Button play, stop, record, stopPlaying, startMusic, stopBackground;
    private MediaRecorder myAudioRecorder;
    private String AudioSavePathInDevice = null;
    Random random;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final int RequestPermissionCode = 1;
    private MediaPlayer mediaPlayer;
    private MediaPlayer backgroundPlayer;
    boolean isRecording = false;
    String folderName, url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record);

        play = findViewById(R.id.play);
        stop = findViewById(R.id.stop);
        record = findViewById(R.id.record);
        stopPlaying = findViewById(R.id.button_stopPlaying);
        startMusic = findViewById(R.id.button_playInBackground);
        stopBackground = findViewById(R.id.button_stopMusicBackground);

        stop.setEnabled(false);
        play.setEnabled(false);
        stopPlaying.setEnabled(false);
        stopBackground.setEnabled(false);

        random = new Random();

        folderName = "MyAudio";
        if (ActivityCompat.checkSelfPermission(record.this, WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            CreateFolder();
        } else {
            ActivityCompat.requestPermissions(record.this, new String[]{WRITE_EXTERNAL_STORAGE}, 100);
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        url = bundle.getString("url");

        stopBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                record.setEnabled(true);
                stopPlaying.setEnabled(true);

                if (backgroundPlayer != null) {
                    backgroundPlayer.stop();
                    backgroundPlayer.release();
                    startMusic.setEnabled(true);
                    stopBackground.setEnabled(false);
                    stopPlaying.setEnabled(false);
                }
            }
        });

        startMusic.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                backgroundPlayer = new MediaPlayer();
                stopBackground.setEnabled(true);
                Uri myUri = Uri.parse(url);
                backgroundPlayer.setAudioAttributes(
                        new AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .build()
                );
                try {
                    backgroundPlayer.setDataSource(getApplicationContext(), myUri);
                    backgroundPlayer.prepare();
                    backgroundPlayer.start();
                    startMusic.setEnabled(false);
                    play.setEnabled(false);
                    stopPlaying.setEnabled(false);
                    Toast.makeText(getApplicationContext(),"Music is playing.", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkPermission()) {
                    AudioSavePathInDevice = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + folderName + "/" + CreateRandomAudioFileName(5) + "AudioRecording.3gp";
                    MediaRecorderReady();
                    try {
                        myAudioRecorder.prepare();
                        myAudioRecorder.start();
                        isRecording = true;
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    record.setEnabled(false);
                    stop.setEnabled(true);
                    play.setEnabled(false);
                    Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_SHORT).show();
                } else {
                    requestPermission();
                }
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecording) {
                    myAudioRecorder.stop();
                }
                myAudioRecorder.release();
                isRecording = false;
                stop.setEnabled(false);
                play.setEnabled(true);
                record.setEnabled(true);
                stopPlaying.setEnabled(false);
                Toast.makeText(getApplicationContext(),  "Saved " + CreateRandomAudioFileName(5) + "AudioRecording.3gp", Toast.LENGTH_SHORT).show();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) throws IllegalArgumentException, SecurityException, IllegalStateException {
//                MediaPlayer mediaPlayer = new MediaPlayer();

                stop.setEnabled(false);
                record.setEnabled(false);
                stopPlaying.setEnabled(true);

                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(AudioSavePathInDevice);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();
                play.setEnabled(false);
                startMusic.setEnabled(false);
                Toast.makeText(getApplicationContext(), "Playing Audio", Toast.LENGTH_SHORT).show();
            }
        });

        stopPlaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop.setEnabled(false);
                record.setEnabled(true);
                stopPlaying.setEnabled(true);
                play.setEnabled(true);

                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
//                    MediaRecorderReady();
                    stopPlaying.setEnabled(false);
                }
            }
        });

    }

    public void MediaRecorderReady() {
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(AudioSavePathInDevice);
    }

    public String CreateRandomAudioFileName(int string) {
        StringBuilder stringBuilder = new StringBuilder(string);
        int i = 0;
        while (i < string) {
            stringBuilder.append(RandomAudioFileName.charAt(random.nextInt(RandomAudioFileName.length())));
            i ++;
        }
        return stringBuilder.toString();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(record.this, new String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(record.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(record.this, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                if (requestCode == 100 && (grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    CreateFolder();
                }
                break;
        }
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void CreateFolder() {
        File file = new File (Environment.getExternalStorageDirectory(), folderName);
        if (!file.exists()) {
            file.mkdirs();
            Toast.makeText(record.this, "Folder Created", Toast.LENGTH_LONG).show();
        }
    }
}
