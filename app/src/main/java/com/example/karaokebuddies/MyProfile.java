package com.example.karaokebuddies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyProfile extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private EditText editText;
    private Button capture, saveImage, button_openFileManager;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    private static final String LAST_TEXT = "";
    private static final String KEY_VISIBILITY = "image_visibility";
    private Uri photoURI;
    private String folderName = "myPhotos";

    private String imagePath = "";

    private SharedPreferences sharedPreferences;

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        sharedPreferences = getPreferences(Context.MODE_PRIVATE);

        capture = findViewById(R.id.button_captureImage);
        saveImage = findViewById(R.id.button_saveImage);
        imageView = findViewById(R.id.imageView_myProfile);
        editText = findViewById(R.id.editText_myName);
        button_openFileManager = findViewById(R.id.button_openFileManager);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editText.setText(preferences.getString(LAST_TEXT, ""));
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                preferences.edit().putString(LAST_TEXT, s.toString()).apply();
            }
        });


        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setImageURI(photoURI);

            }
        });

        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

                CreateFolder();

                Date date = new Date();
                DateFormat df = new SimpleDateFormat("-mm-ss");

                String newPicFile = "myPhoto"+ df.format(date) + ".jpg";
                String outPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + folderName + "/" + newPicFile;
                File outFile = new File(outPath);

                photoURI = FileProvider.getUriForFile(MyProfile.this, getApplicationContext().getPackageName() + ".provider", outFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        });

        button_openFileManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri imageUri = Uri.parse(Environment.getExternalStorageDirectory() + "/" + "myPhotos" ) ;
                startActivity(new Intent(Intent.ACTION_GET_CONTENT).setDataAndType(imageUri, "*/*"));
            }
        });
    }

    private void CreateFolder() {
        File file = new File (Environment.getExternalStorageDirectory(), folderName);
        if (!file.exists()) {
            file.mkdirs();
            Toast.makeText(MyProfile.this, "Folder Created", Toast.LENGTH_LONG).show();
        }
    }
}