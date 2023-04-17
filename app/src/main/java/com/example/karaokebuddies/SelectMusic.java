package com.example.karaokebuddies;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SelectMusic extends AppCompatActivity {

    private TextView textView_title;
    private RecyclerView recyclerView;
    private ImageButton imageButton_record;
    private ArrayList<Track> tracks;
    MediaPlayer mediaPlayer;

    private static final String api_url = "https://shazam.p.rapidapi.com/charts/track?locale=en-US&pageSize=20&startFrom=0";
    AsyncHttpClient client = new AsyncHttpClient();

    String title;
    String subtitle;
    String url;
    String imageURL;
    int currentIndex;

    TrackAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_music);

        recyclerView = findViewById(R.id.recyclerView_selectMusic);
        imageButton_record = findViewById(R.id.imageButton_record);
        textView_title = findViewById(R.id.textView_title_selectMusic);

        tracks = new ArrayList<>();

        adapter = new TrackAdapter(tracks);
        recyclerView.setLayoutManager(new LinearLayoutManager(SelectMusic.this));
        recyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(SelectMusic.this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(adapter);

        client.addHeader("x-rapidapi-key", "c6a7192fc9msh4931c462d8c53c4p1ced90jsn45cd3d660151");
        client.addHeader("x-rapidapi-host", "shazam.p.rapidapi.com");
        client.get(api_url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("api response", new String(responseBody));
                try {
                    JSONObject object = new JSONObject(new String(responseBody));
                    JSONArray array = object.getJSONArray("tracks");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject tracksObject = array.getJSONObject(i);
                        title = tracksObject.getString("title");
                        subtitle = tracksObject.getString("subtitle");
                        JSONObject imagesObject = tracksObject.getJSONObject("images");
                        for (int j = 0; j < imagesObject.length(); j++) {
                            imageURL = imagesObject.getString("background");
                        }
                        url = tracksObject.getString("url");

                        Track track = new Track(title, subtitle, imageURL, url);
                        tracks.add(track);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("failure", "failure");
            }
        });
    }

}
