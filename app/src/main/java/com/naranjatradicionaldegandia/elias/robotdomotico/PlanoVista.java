package com.naranjatradicionaldegandia.elias.robotdomotico;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

import static com.naranjatradicionaldegandia.elias.robotdomotico.Direccion.ARRIBA;
import static com.naranjatradicionaldegandia.elias.robotdomotico.Direccion.DERECHA;
import static com.naranjatradicionaldegandia.elias.robotdomotico.Direccion.IZQUIERDA;

public class PlanoVista extends View {

    private double zoom = 0.01;
    private int ancho, alto;
    List<String> estados;
    List<Long> tiempos;
    Paint paint = new Paint();

    public PlanoVista(Context context, AttributeSet attrs) {
        super(context, attrs);
        //Inicializa la vista
        //OjO: Aún no se conocen sus dimensiones
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    public void setDatos(List<String> estados, List<Long> tiempos) {
        this.estados = estados;
        this.tiempos = tiempos;
    }

    @Override
    protected void onSizeChanged(int ancho, int alto,
                                 int ancho_anterior, int alto_anterior) {
        //Te informan del ancho y la altura

        this.ancho = ancho;
        this.alto = alto;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        //Dibuja aquí la vista
        int x = ancho - 80; //Mejor pasar 80 a constante
        int y = alto / 2;
        Direccion direccion = ARRIBA;
        int xAnt = x;
        int yAnt = y;
        String estadoAnt = estados.get(0);
        long tiempoAnt = tiempos.get(0);
        for (int n = 1; n < estados.size(); n++) {
            String estado = estados.get(n);
            long tiempo = tiempos.get(n) - tiempoAnt;
            tiempoAnt = tiempos.get(n);
            switch (estadoAnt) {
                case "AVANZA":
                    switch (direccion) {
                        case ARRIBA:
                            y -= tiempo * zoom; break;
                        case ABAJO:
                            //... break;
                        case DERECHA:
                            //... break;
                        case IZQUIERDA:
                            //... break;
                    }
                    canvas.drawLine(xAnt, yAnt, x, y, paint);
                    break;
                case "GIRO_IZQ":
                    switch (direccion) {
                        case ARRIBA:
                            direccion = IZQUIERDA; break;
                        //...

                    }
                    break;
                case "GIRO_DER":
                    switch (direccion) {
                        case ARRIBA:
                            direccion = DERECHA; break;
                        //...
                    }
            }
            xAnt = x;
            yAnt = y;
            estadoAnt = estado;
        }
    }


}