package com.example.gestioneparcheggio;

import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class Parcheggio extends Thread{
    public Semaphore parcheggio = new Semaphore(8);
    public int[][] positions = {{}, {}};
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
                    System.out.println("sto liberando");
                    liberaParcheggio();
                    continue;
                }
                boolean parcheggioVuoto = true;
                for(Boolean b : posti) if(b) {
                    parcheggioVuoto = false;
                    break;
                }
                if (parcheggioVuoto){
                    System.out.println("sto parcheggiando");
                    parcheggiaAuto();
                    continue;
                }
                if(Math.random() < 0.5){
                    System.out.println("sto parcheggiando");
                    parcheggiaAuto();
                } else {
                    System.out.println("sto liberando");
                    liberaParcheggio();
                }
                System.out.println("sto aspettando");
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
        boolean trovato = false;
        int numeroParcheggio = (int) (Math.random() * this.posti.length);
        while(trovato){
            numeroParcheggio = (int) (Math.random() * this.posti.length);
            if(!this.posti[numeroParcheggio]) trovato = true;
        }

        // parcheggio l'auto
        car.setRotate(-90);
        car.setLayoutX(44);
        car.setLayoutY(608);
        car.setVisible(true);

        sleep(1000);

        car.setRotate(0);
        car.setLayoutY(323);

        sleep(1000);
        System.out.println("numero parcheggio: " + numeroParcheggio);

        if(numeroParcheggio == 0 || numeroParcheggio == 4){
            car.setLayoutX(556);
        }
        if(numeroParcheggio == 1 || numeroParcheggio == 5){
            car.setLayoutX(721);
        }
        if(numeroParcheggio == 2 || numeroParcheggio == 6){
            car.setLayoutX(882);
        }
        if(numeroParcheggio == 3 || numeroParcheggio == 7){
            car.setLayoutX(1042);
        }
        sleep(1000);

        // per far entrare la macchina in retro o normale
        if(Math.random() < 0.5){
            car.setRotate(90);
        } else {
            car.setRotate(-90);
        }
        sleep(1000);
        // parcheggiare l'auto nel posto
        if(numeroParcheggio < 4){
            car.setLayoutY(92);
        } else{
            car.setLayoutY(562);
        }

        macchine.put(car, numeroParcheggio);
        this.posti[numeroParcheggio] = true;
        parcheggio.acquire();
    }
    public void liberaParcheggio() throws Exception {
        // trovare il parcheggio da liberare
        boolean trovato = false;
        int index = 0;
        while(trovato){
            index = (int) (Math.random() * this.posti.length);
            if(this.posti[index]) trovato = true;
        }

        // trovare la macchina dal parcheggio
        ImageView car = null;
        System.out.println("valore da cercare: " + index);
        for (Map.Entry<ImageView, Integer> entry : macchine.entrySet()) {
            ImageView key = entry.getKey();
            Integer value = entry.getValue();
            System.out.println("Key=" + key + ", Value=" + value);
            if (value != null && value == index) {
                car = key;
                System.out.println("Key=" + key + ", Value=" + value);
                break;
            }
        }

        car.setLayoutX(308);
        car.setRotate(-180);
        sleep(2000);
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
