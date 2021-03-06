package de.hskl.gatav.flappybender.views;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import de.hskl.gatav.flappybender.sound.Discman;
import de.hskl.gatav.flappybender.sound.Music;
import de.hskl.gatav.flappybender.util.Data;

public abstract class OwnActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!Discman.wasCreated()) {
            Intent intent = new Intent(this, Discman.class);
            startService(intent);
        }

        try {
            getSupportActionBar().hide();
        } catch (Exception e) {
            Log.e("ACTIVITY", "Failed removing supportActionBar: " + e.getMessage());
        }

        int currentApiVersion = android.os.Build.VERSION.SDK_INT;

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        if(currentApiVersion >= Build.VERSION_CODES.KITKAT)
        {
            getWindow().getDecorView().setSystemUiVisibility(flags);
            final View decorView = getWindow().getDecorView();
            decorView
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
                    {
                        @Override
                        public void onSystemUiVisibilityChange(int visibility)
                        {
                            if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
                            {
                                decorView.setSystemUiVisibility(flags);
                            }
                        }
                    });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Discman.wasCreated()) {
            Discman.getInstance().onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!Data.wasLoaded()) {
            Data.loadData(this);
        }
        if (Discman.wasCreated()) {
            if(getMusic() != null) {
                Discman.getInstance().setSong(getMusic());
            } else {
                Discman.getInstance().onResume();
            }
        }
    }

    protected abstract String getMusic();
}
