package org.example;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class CustomThreadPool {
    private final BlockingQueue<Runnable> runnableQueue;

    private final AtomicBoolean isThreadPoolShutDown;

    public CustomThreadPool(final int numOfThreads) {
        this.runnableQueue = new LinkedBlockingQueue<>();
        this.isThreadPoolShutDown = new AtomicBoolean(false);
        for (int i = 0; i < numOfThreads; ++i) {
            WorkerThread thread = new WorkerThread();
            thread.setName("Worker Thread - " + i);
            thread.start();
        }
    }

    public void execute(Runnable runnableTask) throws InterruptedException {
        if (!isThreadPoolShutDown.get()) {
            runnableQueue.put(runnableTask);
            synchronized (this) {
                notifyAll();
            }
        } else {
            throw new InterruptedException("Thread Pool is shutdown, unable to execute task");
        }
    }

    public void shutdown() {
        isThreadPoolShutDown.set(true);
    }

    private class WorkerThread extends Thread {
        @Override
        public void run() {
            while (!(isThreadPoolShutDown.get())) {
                Runnable runnableTask;
                while ((runnableTask = runnableQueue.poll()) != null) {
                    runnableTask.run();
                }
            }
        }
    }
}
