package com.example.karaokebuddies;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MyFriends extends AppCompatActivity {

    private Button button_share;
    private String parentFolder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "myAudio";
    private String fileName = Environment.getExternalStorageDirectory().toString() + "/myAudio";
    private String selectedFile;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friends);

        button_share = findViewById(R.id.button_share);
        final LinearLayout linearLayout = findViewById(R.id.linearLayout_friend);

        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        button_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonShareFile(v);
            }
        });

        List<File> masterFile = getListFiles(new File(parentFolder));
        for (int i = 0; i < masterFile.size(); i++) {
            TextView textView = new TextView(this);
            String fileName = masterFile.get(i).toString();
            textView.setText(fileName.substring(fileName.lastIndexOf("/") + 1));
            textView.setTextSize(18);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedFile = (String) textView.getText();
                    Log.d("selectedFile is:", selectedFile);
                }
            });

            linearLayout.addView(textView);

        }


    }


    public void buttonShareFile (View view) {
        File file = new File(parentFolder + "/" + selectedFile);
        if (!file.exists()) {
            Toast.makeText(this, "File doesn't exists", Toast.LENGTH_SHORT).show();
        }

        Intent intentShare = new Intent(Intent.ACTION_SEND);
        intentShare.setType("video/3gp");
        intentShare.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + file));
        startActivity(Intent.createChooser(intentShare, "Share the file ..."));

    }

    List<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                inFiles.addAll(getListFiles(file));
            } else {
                inFiles.add(file);
            }
        }
        return inFiles;
    }

}
