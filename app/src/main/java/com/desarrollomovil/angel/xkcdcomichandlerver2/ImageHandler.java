package com.desarrollomovil.angel.xkcdcomichandlerver2;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.net.URI;


/**
 * Created by angel on 25/01/2017.
 */
public class ImageHandler extends Handler {
    Handler responseHandler;
    ImageHandlerListener listener;
    Context context;
    private boolean timerActive;             // Controlamos si el timer está activo o no
    private int seconds;                     // Segundos del timer
    Bundle b;
    URI uri;

    public ImageHandler(Looper looper, ImageHandlerListener listener) {
        super(looper);
        this.listener = listener;
    }

    public interface ImageHandlerListener {
        void onDownload(URI uri);

        void onProgress();

        void onError(String error);

        void onNotice();
    }

    public void initTimer(int seconds) {
        this.timerActive = true;
        this.seconds = seconds * 1000;
    }

    public void disableTimer() {
        this.timerActive=false;
        this.seconds=0;
    }

    public void setResponseHandler(DownloadHandler responseHandler) {
        this.responseHandler = responseHandler;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void handleMessage(Message msg) {
        switch(msg.what) {
            case (Constantes.LOAD_IMAGE):
                // TODO: Obtenemos la URI del archivo temporal y cargamos el imageView
                b = msg.getData();
                uri = (URI) b.get("uri");
                listener.onDownload(uri);

                // si está activo el timer posteriormente enviaremos un mensaje retardado de DOWNLOAD_COMIC al HandlerThread, solo si está activo el Timer.
                if (timerActive) {
                    // TODO: terminar de construir el mensaje DOWNLOAD_COMIC
                    Message msgTemporizado = responseHandler.obtainMessage(Constantes.DOWNLOAD);
                    responseHandler.sendMessageDelayed(msgTemporizado, seconds);
                }
                break;
            case (Constantes.PROGRESS):
                //TODO actualizaremos el progressBar
                listener.onProgress();
                break;

            case (Constantes.ERROR):
                b = msg.getData();
                int codError = (int) b.get("codError");
                //TODO devolvemos un mensaje personalizado que se mostrará en un Toast.
                switch(codError){
                    case 0:
                        listener.onError("Error al descargar la imagen");
                        break;
                    case 1:
                        listener.onError("Error en la conexion");
                        break;
                }
                //TODO Cancelamos el Timer para evitar errores posteriores
                disableTimer();
                break;

            case (Constantes.NOTICE)://TODO Procesa avisos que no son errores. ej. 'La descarga automatica se ha detenido'
                listener.onNotice();
                break;
        }
    }

}
