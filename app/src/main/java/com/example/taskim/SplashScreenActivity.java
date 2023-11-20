package com.example.taskim;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        Handler splashHandler = new Handler();
        Runnable startMainActivityRunnable = new Runnable() {
            @Override
            public void run() {
                Intent mainActivityIntent = new Intent(
                        SplashScreenActivity.this,
                        ListagemListasActivity.class);

                startActivity(mainActivityIntent);

                finish();
            }
        };

        splashHandler.postDelayed(startMainActivityRunnable, 2000);
    }
}
