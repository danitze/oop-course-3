package org.example;

public class CustomReentrantLock {

    private static boolean isLocked = false;

    public synchronized void lock() {
        if (isLocked) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
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
        notifyAll();
        isLocked = false;
    }
}
