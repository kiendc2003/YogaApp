package com.example.yogaapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutUsActivity extends AppCompatActivity {

    private WebView webMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        // Sự kiện nhấn mở Google Maps
        TextView mapView = findViewById(R.id.openMap);
        mapView.setOnClickListener(v -> {
            String encodedQuery = Uri.encode("97-99 Võ Văn Tần, Phường Võ Thị Sáu, Quận 3, Hồ Chí Minh");
            String mapQuery = "geo:0,0?q=" + encodedQuery;
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mapQuery));
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        });

        // Hiển thị bản đồ OpenStreetMap
        webMap = findViewById(R.id.webMap);
        WebSettings webSettings = webMap.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webMap.setWebViewClient(new WebViewClient());
        webMap.loadUrl("https://www.openstreetmap.org/export/embed.html?bbox=106.6860%2C10.7715%2C106.7005%2C10.7795&layer=mapnik&marker=10.7755%2C106.6935");
    }

    public void aboutusactivity(View view) {
        finish(); // Quay về màn trước
    }
}
