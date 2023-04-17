package com.example.gestioneparcheggio;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Semaforo extends Thread{
    public String ROSSO_ACCESO = "#ff0000";
    public String ROSSO_SPENTO = "#770000";
    public String VERDE_ACCESO = "#00ff00";
    public String VERDE_SPENTO = "#007700";
    public Circle rosso;
    public Circle verde;
    public Rectangle aperta;
    public Rectangle chiusa;

    public Semaforo(Circle led_rosso, Circle led_verde, Rectangle sbarra_aperta, Rectangle sbarra_chiusa){
        rosso = led_rosso;
        verde = led_verde;
        aperta = sbarra_aperta;
        chiusa = sbarra_chiusa;
    }
    public void run(){
        try {
            while (true) {
                if (Parcheggio.pieno) {
                    rosso.setFill(Paint.valueOf(ROSSO_ACCESO));
                    verde.setFill(Paint.valueOf(VERDE_SPENTO));

                    aperta.setVisible(false);
                    chiusa.setVisible(true);
                } else {
                    rosso.setFill(Paint.valueOf(ROSSO_SPENTO));
                    verde.setFill(Paint.valueOf(VERDE_ACCESO));

                    aperta.setVisible(true);
                    chiusa.setVisible(false);
                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
