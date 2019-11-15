package com.example.lista2;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class ViewHolder extends RecyclerView.ViewHolder {

    WebView videoView;
    Button button;


    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        videoView = itemView.findViewById(R.id.videoView);
        button = itemView.findViewById(R.id.fullscreen);

        // Soporte YouTube
        videoView.setWebViewClient(new WebViewClient());
        videoView.setWebChromeClient(new WebChromeClient());
        videoView.getSettings().setJavaScriptEnabled(true);

    }

}
