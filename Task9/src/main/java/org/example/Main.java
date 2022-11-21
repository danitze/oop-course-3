package org.example;

public class Main {
    public static void main(String[] args) {

        CustomPhaser phaser = new CustomPhaser(1);
        new Thread(new PhaseThread(phaser, "PhaseThread 1")).start();
        new Thread(new PhaseThread(phaser, "PhaseThread 2")).start();

        int phase = phaser.getPhase();
        phaser.arriveAndAwaitAdvance();
        System.out.println("Фаза " + phase + " завершена");
        phase = phaser.getPhase();
        phaser.arriveAndAwaitAdvance();
        System.out.println("Фаза " + phase + " завершена");

        phase = phaser.getPhase();
        phaser.arriveAndAwaitAdvance();
        System.out.println("Фаза " + phase + " завершена");

        phaser.arriveAndDeregister();
    }
}

class PhaseThread implements Runnable{

    CustomPhaser phaser;
    String name;

    PhaseThread(CustomPhaser p, String n){

        this.phaser=p;
        this.name=n;
        phaser.register();
    }
    public void run(){

        System.out.println(name + " виконує фазу " + phaser.getPhase());
        phaser.arriveAndAwaitAdvance();

        System.out.println(name + " виконує фазу " + phaser.getPhase());
        phaser.arriveAndAwaitAdvance();

        System.out.println(name + " виконує фазу " + phaser.getPhase());
        phaser.arriveAndDeregister();
    }
}