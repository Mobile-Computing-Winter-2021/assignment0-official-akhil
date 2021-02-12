package com.example.assignment2;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


import android.content.Context;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Activity2 extends AppCompatActivity {
    MediaPlayer mPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        Button check = findViewById(R.id.read_button);
        Button play = findViewById(R.id.play_button);
        Button stop = findViewById(R.id.stop_button);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File("/data/data/com.example.assignment2/files/song.mp3");
                if(file.exists())
                {
                    Toast.makeText(getApplicationContext(),"File Already Exists",Toast.LENGTH_SHORT).show();
                }
                else {
                    downloadmusic();
                }

            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play_stop_music("Start");
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play_stop_music("Stop");
            }
        });
    }


    private void play_stop_music(String act)
    {
        File file = new File("/data/data/com.example.assignment2/files/song.mp3");
        if(file.exists()) {
            if (act.equals("Start")) {
                try {
                    mPlayer.setDataSource("/data/data/com.example.assignment2/files/song.mp3");
                    mPlayer.prepare();
                } catch (IllegalArgumentException | IOException e) {
                    e.printStackTrace();
                }
                mPlayer.start();
            } else {
                mPlayer.stop();
                mPlayer.reset();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Download File first",Toast.LENGTH_SHORT).show();
        }
    }
    private void downloadmusic() {
        ConnectivityManager cman = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cman.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d("test", "Hello1");
            Toast.makeText(getApplicationContext(), "Network is up", Toast.LENGTH_LONG).show();
            new DownloadWebPageTask().execute("http://faculty.iiitd.ac.in/~mukulika/s1.mp3");
        } else {
            Toast.makeText(getApplicationContext(), "Network is down", Toast.LENGTH_LONG).show();
        }
    }

    private class DownloadWebPageTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... musicURL) {
            int count;
            try {
                URL url = new URL(musicURL[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                FileOutputStream output = openFileOutput("song.mp3", MODE_PRIVATE);

                byte data[] = new byte[1024];

                while ((count = input.read(data)) != -1) {
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getApplicationContext(), "Music Download complete.", Toast.LENGTH_LONG).show();
        }
    }
}