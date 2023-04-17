package com.example.gestioneparcheggio;

import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class Parcheggio extends Thread{
    public Semaphore parcheggio = new Semaphore(8);
    public boolean[] posti;
    // macchina, posto in cui è parcheggiata
    HashMap<ImageView, Integer> macchine = new HashMap<>();
    static boolean pieno = false;
    public Parcheggio(ImageView[] cars) {
        this.posti = new boolean[]{false, false, false, false, false, false, false, false};
        Arrays.stream(cars).forEach(macchina -> macchine.put(macchina, null));

    }
    public void run() {
        try {
            sleep(2000);
            while (true){
                controllaPosti();
                if(Parcheggio.pieno){
                    System.out.println("PARCHEGGIO PIENO, LIBERO");
                    liberaParcheggio();
                    continue;
                }
                boolean parcheggioVuoto = true;
                for(Boolean b : posti) {
                    if (b) {
                        parcheggioVuoto = false;
                        break;
                    }
                }
                if (parcheggioVuoto){
                    System.out.println("PARCHEGGIO PIENO, PARCHEGGIO");
                    parcheggiaAuto();
                    continue;
                }

                if(Math.random() < 0.7){
                    System.out.println("PARCHEGGIO");
                    parcheggiaAuto();
                } else {
                    System.out.println("LIBERO");
                    liberaParcheggio();
                }

                controllaPosti();
                sleep(2000);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void parcheggiaAuto() throws Exception {
        // trova l'auto da parcheggiare
        ImageView car = null;
        for(ImageView macchina : macchine.keySet()){
            if(macchine.get(macchina) == null){
                car = macchina;
                break;
            }
        }
        // trova il posto random in cui parcheggiare
        int numeroParcheggio;
        do {
            numeroParcheggio = (int) (Math.random() * this.posti.length);
        } while (this.posti[numeroParcheggio]);

        // parcheggio l'auto
        assert car != null;
        car.setRotate(-90);
        car.setLayoutX(44);
        car.setLayoutY(608);
        car.setVisible(true);

        sleep(1000);

        car.setRotate(0);
        car.setLayoutY(323);

        sleep(1000);

        // sposta la macchina alla stessa x del parcheggio scelto (random)
        int[] positions = {556, 721, 882, 1042};
        car.setLayoutX(positions[numeroParcheggio % 4]);
        sleep(1000);

        // parcheggiare l'auto normale o in retro
        car.setRotate(Math.random() < 0.5 ? 90 : -90);

        sleep(1000);

        // parcheggiare l'auto nel posto
        car.setLayoutY(numeroParcheggio < 4 ? 92 : 562);

        // associare il parcheggio alla macchina
        macchine.put(car, numeroParcheggio);
        // occupare il parcheggio
        this.posti[numeroParcheggio] = true;
        // fare l'acquire del Semaphore
        parcheggio.acquire();
    }
    public void liberaParcheggio() throws Exception {
        // trovare il parcheggio da liberare
        int index;
        do {
            index = (int) (Math.random() * this.posti.length);
        } while (!this.posti[index]);

        // trovare la macchina dal parcheggio
        ImageView car = null;
        for (ImageView key : macchine.keySet()) {
            if(macchine.get(key) == null) continue;
            int value = macchine.get(key);
            if (value == index) {
                car = key;
                break;
            }
        }

        car.setLayoutY(323);
        car.setRotate(-180);

        sleep(1000);

        car.setLayoutX(300);

        sleep(1000);

        car.setVisible(false);

        macchine.put(car, null);
        this.posti[index] = false;

        parcheggio.release();
    }

    public void controllaPosti(){
        // se almeno un posto è false(quindi è libero), la funzione ritorna false perché c'è almeno un posto libero
        for(boolean posto : this.posti) {
            if (!posto) {
                Parcheggio.pieno = false;
                return;
            }
        };
        Parcheggio.pieno = true;
    }
}
