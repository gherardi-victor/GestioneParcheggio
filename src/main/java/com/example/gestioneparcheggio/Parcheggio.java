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
                sleep(2000);
                if(Parcheggio.pieno){
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
                    parcheggiaAuto();
                    continue;
                }

                if(Math.random() < 0.7){
                    parcheggiaAuto();
                } else {
                    liberaParcheggio();
                }

                controllaPosti();
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

        for (int i = 608; i > 323; i--){
            car.setLayoutY(i);
            sleep(5);
        }
        //car.setLayoutY(323);
        sleep(200);
        for (int i = -90; i < 0; i++){
            car.setRotate(i);
            sleep(3);
        }
        //car.setRotate(0);

        sleep(200);

        // sposta la macchina alla stessa x del parcheggio scelto (random)
        int[] positions = {556, 721, 882, 1042};

        for(int i = 44; i < positions[numeroParcheggio % 4]; i++){
            car.setLayoutX(i);
            sleep(6);
        }
        //car.setLayoutX(positions[numeroParcheggio % 4]);
        sleep(200);

        // parcheggiare l'auto normale o in retro
        /*int rotazione = Math.random() < 0.5 ? 90 : -90;
        for(int i = 0; i != rotazione;){
            car.setRotate(i);
            if(rotazione == 90) i++;
            else i--;
            sleep(3);
        }*/
        int rotazione = Math.random() < 0.5 ? 90 : -90;
        int k = 0;
        while (k != rotazione) {
            car.setRotate(k);
            if (rotazione == 90) k++;
            else k--;
            car.setRotate(k);
            sleep(3);
        }
        //car.setRotate(rotazione);

        sleep(200);

        // parcheggiare l'auto nel posto
        /*int altezza = numeroParcheggio < 4 ? 92 : 562;
        for(int i = 323; i != altezza;){
            car.setLayoutY(i);
            if(altezza == 92) i--;
            else i++;
            sleep(5);
        }*/

        int altezza = numeroParcheggio < 4 ? 92 : 562;
        int i = 323;
        while (i != altezza) {
            car.setLayoutY(i);
            if (altezza == 92) i--;
            else i++;
            car.setLayoutY(i);
            sleep(5);
        }
        //car.setLayoutY(numeroParcheggio < 4 ? 92 : 562);

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

        int altezzaCorrente = (int)car.getLayoutY();
        int i = altezzaCorrente;
        while(i != 323){
            car.setLayoutY(i);
            if(altezzaCorrente < 323) i++;
            else i--;
            car.setLayoutY(i);
            sleep(5);
        }

        sleep(200);

        int rotazione = (int)car.getRotate();
        int k = rotazione;
        while(k != -180 && k != 180){
            car.setRotate(k);
            if(rotazione == 90) k++;
            else k--;
            car.setRotate(k);
            sleep(3);
        }

        sleep(200);

        for(int j = (int)car.getLayoutX(); j > 500; j--){
            car.setLayoutX(j);
            sleep(6);
        }
        sleep(200);
        //car.setLayoutX(300);
        Parcheggio.pieno = false;

        sleep(100);

        for(int j = (int)car.getLayoutX(); j != 44; j--){
            car.setLayoutX(j);
            Parcheggio.pieno = false;
            sleep(4);
        }

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
