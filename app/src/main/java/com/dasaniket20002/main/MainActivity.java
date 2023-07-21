package com.dasaniket20002.main;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.dasaniket20002.main.managers.WallpaperPreviewManager;
import com.dasaniket20002.utils.BitmapUtils;
import com.dasaniket20002.utils.ScreenUtils;
import com.dasaniket20002.utils.WallpaperDataHolder;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private WallpaperPreviewManager wallpaperPreviewManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ScreenUtils.init(this);

        setupWallpaperPreview();
        setupWallpaperButton();
        setupOffsetEditText();
        setupEnableXRay();
        setupBGChanged_BTN();
        setupFGChanged_BTN();
    }

    private void setupWallpaperPreview() {
        Bitmap bg = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        Bitmap screenBG = BitmapUtils.getScreenSizeBitmap(bg);
        WallpaperDataHolder.getInstance().setBG(screenBG);
        bg.recycle();

        Bitmap fg = BitmapFactory.decodeResource(getResources(), R.drawable.fg);
        Bitmap screenFG = BitmapUtils.getScreenSizeBitmap(fg);
        WallpaperDataHolder.getInstance().setFG(screenFG);
        fg.recycle();

        wallpaperPreviewManager = new WallpaperPreviewManager(this, R.id.wallpaper_IV);
        wallpaperPreviewManager.setupWallpaperPreviewImageView();
    }

    private void setupWallpaperButton() {
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
                        WallpaperDataHolder.getInstance().setClockOffsetX(percent);
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
                        WallpaperDataHolder.getInstance().setClockOffsetY(percent);
                        wallpaperPreviewManager.redraw();
                    } catch (NumberFormatException e) {}
                }
            }
        });
    }

    private void setupEnableXRay() {
        CheckBox enableXRay_CHK = findViewById(R.id.enableXRay_CHK);
        enableXRay_CHK.setOnClickListener(
                v -> {
                    wallpaperPreviewManager.setXRayEnabled(enableXRay_CHK.isChecked());
                    wallpaperPreviewManager.redraw();
                }
        );
    }

    private void setupBGChanged_BTN() {
        Button setBG_BTN = findViewById(R.id.setBG_BTN);
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    if (uri != null) {
                        Bitmap bitmap = null;
                        try {
                            bitmap = BitmapUtils.getScreenSizeBitmap(
                                        MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri)
                                    );
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        finally {
                            WallpaperDataHolder.getInstance().setBG(bitmap);
                            wallpaperPreviewManager.createIVGraphics();
                            wallpaperPreviewManager.redraw();
                        }
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });

        setBG_BTN.setOnClickListener(
                v -> pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build())

        );
    }

    private void setupFGChanged_BTN() {
        Button setFG_BTN = findViewById(R.id.setFG_BTN);
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    if (uri != null) {
                        Bitmap bitmap = null;
                        try {
                            bitmap = BitmapUtils.getScreenSizeBitmap(
                                    MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri)
                            );
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        finally {
                            WallpaperDataHolder.getInstance().setFG(bitmap);
                            wallpaperPreviewManager.createIVGraphics();
                            wallpaperPreviewManager.redraw();
                        }
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });

        setFG_BTN.setOnClickListener(
                v -> pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build())
        );
    }

    @Override
    protected void onDestroy() {
        wallpaperPreviewManager.onDestroy();
        super.onDestroy();
    }
}