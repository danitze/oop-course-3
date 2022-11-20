package org.example;

public class Main {

    private static int counter = 0;

    public static void main(String[] args) {
        CustomReentrantLock customReentrantLock = new CustomReentrantLock();
        new Thread(new AddRunnable(customReentrantLock)).start();
        new Thread(new SubtractRunnable(customReentrantLock)).start();
    }

    private static class AddRunnable implements Runnable {

        private CustomReentrantLock customReentrantLock;

        public AddRunnable(CustomReentrantLock customReentrantLock) {
            this.customReentrantLock = customReentrantLock;
        }

        @Override
        public void run() {
            while (true) {
                customReentrantLock.lock();
                System.out.println("Locked add");
                ++counter;
                System.out.println("Add counter. New value is " + counter);
                System.out.println("Unlock add");
                customReentrantLock.unlock();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static class SubtractRunnable implements Runnable {

        private CustomReentrantLock customReentrantLock;

        public SubtractRunnable(CustomReentrantLock customReentrantLock) {
            this.customReentrantLock = customReentrantLock;
        }

        @Override
        public void run() {
            while (true) {
                customReentrantLock.lock();
                System.out.println("Locked subtract");
                --counter;
                System.out.println("Subtracted counter. New value is " + counter);
                System.out.println("Unlock subtract");
                customReentrantLock.unlock();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}