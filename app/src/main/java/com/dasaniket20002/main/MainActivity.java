package com.dasaniket20002.main;

import androidx.appcompat.app.AppCompatActivity;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.dasaniket20002.livewallpapertest01.R;
import com.dasaniket20002.utils.ScreenUtils;

public class MainActivity extends AppCompatActivity {

    private ImageView wallpaper_IV;
    private Button setWallpaper_BTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ScreenUtils.init(this);

        wallpaper_IV = findViewById(R.id.Wallpaper_IV);

        setWallpaper_BTN = findViewById(R.id.setWallpaper_BTN);
        setWallpaper_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick();
            }
        });
    }

    private void onButtonClick() {
        Intent intent = new Intent(
                WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                new ComponentName(this, LiveWallpaperService.class));
        startActivity(intent);
    }
}