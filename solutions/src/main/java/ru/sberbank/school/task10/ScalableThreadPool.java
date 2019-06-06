package ru.sberbank.school.task10;

import lombok.NonNull;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class ScalableThreadPool implements ThreadPool {
    private final Queue<FutureTask> tasks = new LinkedList<>();
    private PoolWorker[] threads;
    private int minSizePool;

    public ScalableThreadPool(int min, int max) {
        if (min <= 0 || max <= 0) {
            throw new IllegalArgumentException("Amount of threads must be more than zero");
        }
        if (min > max) {
            throw new IllegalArgumentException("Minimum amount of threads must be less than maximum");
        }

        threads = new PoolWorker[max];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new PoolWorker("ThreadPoolWorker-" + i);
        }

        minSizePool = min;
    }

    @Override
    public void start() {
        for (int i = 0; i < minSizePool; i++) {
            threads[i].start();
        }
    }

    @Override
    public void stopNow() {
        if (threads == null) {
            throw new NullPointerException("Thread pool not running");
        }

        for (PoolWorker thread : threads) {
            thread.interrupt();
        }

        threads = null;

        synchronized (tasks) {
            tasks.clear();
        }
    }

    @Override
    public void execute(@NonNull Runnable runnable) {
        if (threads == null) {
            throw new NullPointerException("Thread pool not running");
        }

        synchronized (tasks) {
            tasks.add(new FutureTask<>(runnable, true));

            correctSizeThreadPool();

            tasks.notifyAll();
        }
    }

    @Override
    public <T> Future<T> execute(@NonNull Callable<T> callable) {
        if (threads == null) {
            throw new NullPointerException("Thread pool not running");
        }

        synchronized (tasks) {
            FutureTask<T> future = new FutureTask<>(callable);

            tasks.add(future);

            correctSizeThreadPool();

            tasks.notifyAll();

            return future;
        }
    }

    //Корректирует размер пула в зависимости от количества задач в очереди
    private void correctSizeThreadPool() {
        for (int i = minSizePool; i < threads.length; i++) {
            PoolWorker thread = threads[i];

            if (tasks.size() > 1) {
                if (thread.getState() == Thread.State.NEW) {
                    thread.start();
                    break;
                }
            } else {
                if (thread.getState() != Thread.State.NEW) {
                    thread.interrupt();
                    threads[i] = new PoolWorker("ThreadPoolWorker-" + i);
                }
            }
        }
    }

    public class PoolWorker extends Thread {

        public PoolWorker(String name) {
            super(name);
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                Runnable task = null;

                synchronized (tasks) {
                    while (tasks.isEmpty()) {
                        try {
                            tasks.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                    if (!Thread.currentThread().isInterrupted()) {
                        task = tasks.poll();
                    }

                }
                if (task != null) {
                    task.run();
                }
            }
        }
    }
}
