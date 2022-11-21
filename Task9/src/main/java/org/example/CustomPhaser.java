package org.example;

public class CustomPhaser {
    private int partiesAtStart;
    private int partiesAwait;
    private int phase;

    public CustomPhaser(int partiesAtStart) {
        this.partiesAtStart = partiesAtStart;
        this.partiesAwait = partiesAtStart;
        this.phase = 0;
    }

    public synchronized void register() {
        ++partiesAtStart;
        ++partiesAwait;
    }

    public synchronized void arriveAndAwaitAdvance() {
        --partiesAwait;
        if (partiesAwait > 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            reset();
        }
    }

    public synchronized void arriveAndDeregister() {
        --partiesAwait;
        --partiesAtStart;
        if(partiesAwait == 0) {
            reset();
        }
    }

    public int getPhase() {
        return phase;
    }

    private void reset() {
        partiesAwait = partiesAtStart;
        ++phase;
        notifyAll();
    }
}
