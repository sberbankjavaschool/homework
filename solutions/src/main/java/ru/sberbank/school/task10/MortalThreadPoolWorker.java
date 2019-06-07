package ru.sberbank.school.task10;

import java.util.Queue;

public class MortalThreadPoolWorker extends Thread {
    private final ScalableThreadPool threadPool;
    private final Queue<Runnable> runnableQueue;
    private Runnable target;

    public MortalThreadPoolWorker(Queue<Runnable> runnableQueue, int index, ScalableThreadPool threadPool) {
        super("ThreadPoolWorker-" + index);
        this.runnableQueue = runnableQueue;
        this.threadPool = threadPool;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            if (target != null) {
                target.run();
            }
            synchronized (runnableQueue) {
                while (runnableQueue.peek() == null) {
                    threadPool.canBeKilled(this);
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
