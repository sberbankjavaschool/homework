package ru.sberbank.school.task10;

import java.util.concurrent.BlockingQueue;

public class ThreadPoolWorker extends Thread {
    private final boolean temporary;
    private final BlockingQueue<Runnable> runnableQueue;

    ThreadPoolWorker(BlockingQueue<Runnable> runnableQueue, int index, boolean temporary) {
        super("ThreadPoolWorker-" + index);
        this.runnableQueue = runnableQueue;
        this.temporary = temporary;
    }

    boolean isTemporary() {
        return temporary;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            if (temporary && runnableQueue.isEmpty()) {
                Thread.currentThread().interrupt();
                throw new HasNoWorkException(getName() + " has no work to do.");
            }
            try {
                runnableQueue.take().run();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}