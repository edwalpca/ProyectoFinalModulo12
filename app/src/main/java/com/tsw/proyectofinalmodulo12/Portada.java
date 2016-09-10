package com.tsw.proyectofinalmodulo12;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by ealpizar on 9/9/2016.
 */public class Portada extends Activity {


    protected boolean _active = true;
    protected int _splashTime = 3000; // Tiempo de Espera del Splash

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.portada);

        /**
         * Confecciono un hilo para que muestre durante 3 segundos el intro de la aplicacion
         * Luego de los 3 segundos levanta el MainActivity del sistema.
         */
        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (_active && (waited < _splashTime)) {
                        sleep(100);
                        if (_active) {
                            waited += 100;
                        }
                    }
                } catch (Exception e) {

                } finally {

                    startActivity(new Intent(Portada.this,
                            MainActivity.class));
                    finish();
                }
            };
        };
        splashTread.start();


    }

}