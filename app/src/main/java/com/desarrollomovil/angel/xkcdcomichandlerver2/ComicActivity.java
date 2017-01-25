package com.desarrollomovil.angel.xkcdcomichandlerver2;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.net.URI;

public class ComicActivity extends AppCompatActivity implements ImageHandler.ImageHandlerListener{

    ComicManager manager;
    ImageView comicView;
    ProgressBar progreso;
    Button btnSalir;
    Button btnTimer;
    Boolean controlTimer = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        comicView = (ImageView)findViewById(R.id.imageView);
        progreso = (ProgressBar)findViewById(R.id.progressBar);
        btnSalir = (Button)findViewById(R.id.btnSalir);
        btnTimer = (Button)findViewById(R.id.btnTimer);

        //TODO Inicializamos el Comic
        manager = new ComicManager(this,getApplicationContext());

        //TODO Descargamos el primer comic
        manager.init();
    }

    @Override
    public void onDownload(URI uri) {
        comicView.setImageDrawable(Drawable.createFromPath(uri.getPath()));
        progreso.setVisibility(View.INVISIBLE);//TODO Termina la descarga, el progresbar se oculta
    }

    @Override
    public void onProgress() {
        progreso.setVisibility(View.VISIBLE);//TODO Comienza la descarga, el progresbar se muestra
    }

    @Override
    public void onError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();//TODO recibirá y mostrará un mensaje personalizado del error
    }

    @Override
    public void onNotice() {
        Toast.makeText(this, "Se esta deteniendo la descarga automatica...", Toast.LENGTH_LONG).show();
    }

    // TODO botón de activar/desactivar Timer (manager.start(), manager.stop())
    public void setTimer(View v) {

        if(controlTimer){ //TODO 'controlTimer' controlará el estado del botón
            manager.stop();
            btnTimer.setText("Activar el timer");
            controlTimer = false;
        }else{
           // manager = new ComicManager(this,getApplicationContext());//TODO al detener el timer usamos '.quitSafely()' por lo que debemos volver a instanciar todo de nuevo
            manager.init();
            btnTimer.setText("Desactivar el timer");
            controlTimer = true;
        }
    }

    //TODO un botón para salir de la App
    public void exit(View v) {
        manager.exit(); //TODO detenemos la aplicación
        finish();
    }

}
