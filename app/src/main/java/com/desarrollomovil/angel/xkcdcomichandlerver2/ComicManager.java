package com.desarrollomovil.angel.xkcdcomichandlerver2;

import android.content.Context;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

/**
 * Created by angel on 25/01/2017.
 */
public class ComicManager {

    private HandlerThread downloadHandlerThread;
    private DownloadHandler downloadHandler;        //TODO Funcionará asociado al Worker Thread (HandlerThread)
    private ImageHandler imageHandler;            //TODO Funcionará asociado al UI Thread

    public ComicManager(ImageHandler.ImageHandlerListener listener,Context contexto) {
        downloadHandlerThread = new HandlerThread("myHandlerThread");
        downloadHandlerThread.start();
        imageHandler = new ImageHandler(Looper.getMainLooper(), listener);
        downloadHandler = new DownloadHandler(downloadHandlerThread.getLooper(),contexto);

        imageHandler.setResponseHandler(downloadHandler);
        downloadHandler.setResponseHandler(imageHandler);
    }

    public void init() {
        //TODO Configuramos el tiempo en imageHandler
        imageHandler.initTimer(10);

        //TODO Forzamos una descarga inicial
        downloadComic();
    }

    public void stop() {
        // TODO: Enviamos un mensaje a imageHandler para crear un Toast de que se está parando la descarga automática
        Message msg = imageHandler.obtainMessage(Constantes.NOTICE);
        imageHandler.sendMessage(msg);

        //TODO Desactivamos el timer de imageHandler para que evite enviar mensajes retardados
        imageHandler.disableTimer();

        //TODO Detiene el encolado de mensajes
        downloadHandlerThread.interrupt();
        // TODO: Paramos el HandlerThread, limpiando su cola de mensajes
        imageHandler.removeCallbacksAndMessages(downloadHandlerThread);
    }

    // enviamos un mensaje para descargar un Comic (cuando pulsemos sobre el imageView)
    public void downloadComic() {
        // TODO: crear mensaje para la descarga imagen
         Message msg = downloadHandler.obtainMessage(Constantes.DOWNLOAD);
         downloadHandler.sendMessage(msg);

    }

    public void exit(){
        downloadHandlerThread.quitSafely();//TODO Detiene por completo
    }
}
