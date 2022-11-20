package org.example;

public class CustomReentrantLock {

    private static boolean isLocked = false;

    private static final Object synchronizer = new Object();

    public void lock() {
        synchronized (synchronizer) {
            if(isLocked) {
                try {
                    synchronizer.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else {
                isLocked = true;
            }
        }
    }

    public boolean tryLock() {
        synchronized (synchronizer) {
            if(!isLocked) {
                lock();
                return true;
            }
            return false;
        }
    }

    public void unlock() {
        synchronized (synchronizer) {
            synchronizer.notifyAll();
            isLocked = false;
        }
    }
}
