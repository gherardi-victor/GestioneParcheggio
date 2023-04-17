package com.example.gestioneparcheggio;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.concurrent.Semaphore;

public class Controller extends Thread{
    public Rectangle sbarra_chiusa;
    public Rectangle sbarra_aperta;
    public Circle led_verde;
    public Circle led_rosso;
    public Semaforo semaforo;
    public Button bottone;
    public Parcheggio parcheggio;
    // ---------------------------------------------------
    public ImageView car_purple_1;
    public ImageView car_purple_2;
    public ImageView car_white_1;
    public ImageView car_white_2;
    public ImageView car_yellow_1;
    public ImageView car_yellow_2;
    public ImageView car_green_1;
    public ImageView car_green_2;
    public ImageView car_blue_1;
    public ImageView car_blue_2;
    public ImageView car_celeste_1;
    public ImageView car_celeste_2;
    public ImageView car_red_1;
    public ImageView car_red_2;
    public ImageView[] macchine;

    public void initialize() {
        int TIME = 0;
        int TIME_MAX = 10;
        boolean caricato = Application.caricato;
        while(caricato){
            try {
                if(TIME > TIME_MAX) return;
                sleep(1000);
                TIME++;
                caricato = Application.caricato;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        macchine = new ImageView[]{car_purple_1, car_purple_2, car_white_1, car_white_2, car_yellow_1, car_yellow_2, car_green_1, car_green_2, car_blue_1, car_blue_2, car_celeste_1, car_celeste_2, car_red_1, car_red_2};
        parcheggio = new Parcheggio(macchine);
        parcheggio.start();

        semaforo = new Semaforo(led_rosso, led_verde, sbarra_aperta, sbarra_chiusa);
        semaforo.start();

    }

    public void switchParcheggio(ActionEvent actionEvent) {
        Parcheggio.pieno = !Parcheggio.pieno;
    }
}