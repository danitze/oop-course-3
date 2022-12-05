public class Barrier {
    private final int partiesAtStart;
    private int partiesAwait;

    public Barrier(int parties) {
        this.partiesAtStart = parties;
        this.partiesAwait = parties;
    }

    public synchronized void await() throws InterruptedException {
        partiesAwait--;
        while (partiesAwait > 0) this.wait();
        partiesAwait = partiesAtStart;
        notifyAll();
    }

    public int getNumberWaiting() {
        return partiesAtStart - partiesAwait;
    }

    public void reset() {
        partiesAwait = partiesAtStart;
        notifyAll();
    }
}