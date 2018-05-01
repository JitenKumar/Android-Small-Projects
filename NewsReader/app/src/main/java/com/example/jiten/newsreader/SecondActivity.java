package com.example.jiten.newsreader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        WebView webView = findViewById(R.id.webView);
        webView.getSettings().getJavaScriptEnabled();
        webView.setWebViewClient(new WebViewClient());
        Intent intent = getIntent();
        webView.loadData(intent.getStringExtra("content"),"text/html","UTF-8");

    }
}
