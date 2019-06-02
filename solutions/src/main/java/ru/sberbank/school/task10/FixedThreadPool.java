package ru.sberbank.school.task10;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

public class FixedThreadPool implements ThreadPool, ThreadCompleteListener {

    private final ArrayBlockingQueue<Thread> runnedThreads;
    private final LinkedBlockingQueue<Runnable> waitingRunnables = new LinkedBlockingQueue<>();
    private boolean isInterrupted = false;

    public FixedThreadPool(int poolSize) {
        runnedThreads = new ArrayBlockingQueue<>(poolSize);
    }

    @Override
    public void start() {
        new Thread(() -> {
            while (!isInterrupted) {
                try {
                    Runnable runnable = waitingRunnables.take();
                    Thread thread = new ThreadPoolWorker(runnable, FixedThreadPool.this);
                    runnedThreads.put(thread);
                    thread.start();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void stopNow() {
        isInterrupted = true;
    }

    @Override
    public void execute(Runnable runnable) {
        try {
            waitingRunnables.put(runnable);
        } catch (InterruptedException e) {
            //Shouldn't happen ever, coz waitingRunnables have no upper bound
            e.printStackTrace();
        }
    }

    @Override
    public <T> Future<T> execute(Callable<T> callable) {
        return null;
    }

    @Override
    public void notifyOfThreadComplete(Thread thread) {
        System.out.println("Removing thread " + thread.getName());
        runnedThreads.remove(thread);
    }
}
