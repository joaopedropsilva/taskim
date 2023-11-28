package com.example.taskim;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

// Classe responsável pelo comportamento da atividade
// de inicialização do aplicativo
@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        // Handler para lidar com a execução da tarefa de abertura
        // da atividade principal ListagemListasActivity
        Handler splashHandler = new Handler();

        // Criação de um runnable responsável pela
        // execução do intent que chama a atividade principal
        Runnable startMainActivity = () -> {

            // Intent de criação da atividade principal
            Intent mainActivityIntent = new Intent(
                    SplashScreenActivity.this,
                    ListagemListasActivity.class);

            startActivity(mainActivityIntent);

            finish();
        };

        // Handler executa o runnable, que carrega a
        // atividade principal após dois segundos
        splashHandler.postDelayed(startMainActivity, 2000);
    }
}
