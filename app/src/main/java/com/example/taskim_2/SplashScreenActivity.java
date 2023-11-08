package com.example.taskim_2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Handler splashHandler = new Handler();
        Runnable startMainActivityRunnable = new Runnable() {
            @Override
            public void run() {
                // Trocar para MainActivity.class
                Intent mainActivityIntent = new Intent(SplashScreenActivity.this, ListaTarefaActivity.class);

                startActivity(mainActivityIntent);

                finish();
            }
        };

        splashHandler.postDelayed(startMainActivityRunnable, 2000);
    }
}