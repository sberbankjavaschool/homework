package ru.sberbank.school.task10;

import java.util.Queue;

public class ThreadPoolWorker extends Thread {

    private final Queue<Runnable> runnableQueue;
    private Runnable target;

    public ThreadPoolWorker(Queue<Runnable> runnableQueue, int index) {
        super("ThreadPoolWorker-" + index);
        this.runnableQueue = runnableQueue;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            if (target != null) {
                target.run();
            }
            synchronized (runnableQueue) {
                while (runnableQueue.peek() == null) {
                    try {
                        runnableQueue.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                target = runnableQueue.poll();
            }
        }
    }
}
