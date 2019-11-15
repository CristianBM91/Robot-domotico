package com.example.lista2;

import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.MediaController;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

public class VideoFullscreen extends AppCompatActivity {

    WebView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_fullscreen);
        videoView = findViewById(R.id.videoView);

        String link = getIntent().getStringExtra("link");
        videoView.loadUrl(link);

        /*videoView.setMediaController(new MediaController(this));
        videoView.requestFocus();
        videoView.start();*/


        // Soporte Youtube
        videoView.setWebViewClient(new WebViewClient());
        videoView.setWebChromeClient(new WebChromeClient());
        videoView.getSettings().setJavaScriptEnabled(true);
    }

}
