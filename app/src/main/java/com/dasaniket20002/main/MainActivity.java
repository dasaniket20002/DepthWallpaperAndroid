package com.dasaniket20002.main;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.dasaniket20002.main.managers.WallpaperPreviewManager;
import com.dasaniket20002.utils.ScreenUtils;

public class MainActivity extends AppCompatActivity {
    private WallpaperPreviewManager wallpaperPreviewManager;
    private CheckBox enableXRay_CHK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ScreenUtils.init(this);

        setupWallpaperPreview();
        setupButton();
        setupOffsetEditText();
        setupEnableXRay();
    }

    private void setupWallpaperPreview() {
        wallpaperPreviewManager = new WallpaperPreviewManager(this, R.id.wallpaper_IV);
        wallpaperPreviewManager.setupWallpaperPreviewImageView();
    }

    private void setupButton() {
        Button setWallpaper_BTN = findViewById(R.id.setWallpaper_BTN);
        setWallpaper_BTN.setOnClickListener(v -> onButtonClick());
    }

    private void onButtonClick() {
        Intent intent = new Intent(
                WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                new ComponentName(this, LiveWallpaperService.class));
        startActivity(intent);
    }

    private void setupOffsetEditText() {
        EditText offsetX_ET = findViewById(R.id.offsetX_ET);
        EditText offsetY_ET = findViewById(R.id.offsetY_ET);

        offsetX_ET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() != 0) {
                    try {
                        float percent = Float.parseFloat(s.toString());
                        wallpaperPreviewManager.getComponentPositions().setClockOffsetX(percent);
                        wallpaperPreviewManager.redraw();
                    } catch(NumberFormatException e) {}
                }
            }
        });

        offsetY_ET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {
                    try {
                        float percent = Float.parseFloat(s.toString());
                        wallpaperPreviewManager.getComponentPositions().setClockOffsetY(percent);
                        wallpaperPreviewManager.redraw();
                    } catch (NumberFormatException e) {}
                }
            }
        });
    }

    private void setupEnableXRay() {
        enableXRay_CHK = findViewById(R.id.enableXRay_CHK);
        enableXRay_CHK.setOnClickListener(
                v -> {
                    wallpaperPreviewManager.setXRayEnabled(enableXRay_CHK.isChecked());
                    wallpaperPreviewManager.redraw();
                }
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wallpaperPreviewManager.onDestroy();
    }
}