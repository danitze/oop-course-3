package org.example;

public class CustomReentrantLock {

    private static boolean isLocked = false;

    public synchronized void lock() {
        while (isLocked) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        isLocked = true;
    }

    public synchronized boolean tryLock() {
        if (!isLocked) {
            lock();
            return true;
        }
        return false;
    }

    public synchronized void unlock() {
        notify();
        isLocked = false;
    }
}
