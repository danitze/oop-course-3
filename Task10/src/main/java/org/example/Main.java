package org.example;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        CustomThreadPool pool = new CustomThreadPool(2);
        pool.execute(() -> {});
        pool.execute(() -> {});
        pool.execute(() -> {});
        Thread.sleep(5000);
        pool.shutdown();
    }


}