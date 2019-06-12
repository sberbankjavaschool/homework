package ru.sberbank.school.task10;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ConcurrentFixedThreadPool implements ThreadPool {

    private int amount;
    private List<Thread> threads;
    private final BlockingQueue<FutureTask<?>> tasks;

    public ConcurrentFixedThreadPool(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Parameter amount must be greater than 0!");
        }
        this.amount = amount;
        threads = new ArrayList<>(amount);
        tasks = new LinkedBlockingQueue<>();
    }

    @Override
    public void start() {
        if (!threads.isEmpty()) {
            throw new IllegalStateException("ThreadPool started!");
        }
        for (int i = 0; i < amount; i++) {
            Thread thread = new ThreadPoolWorker("ThreadPoolWorker-" + i);
            threads.add(thread);
            thread.start();
        }
    }

    @Override
    public void stopNow() {
        for (Thread thread : threads) {
            thread.interrupt();
        }
        threads.clear();
        tasks.clear();
    }

    @Override
    public void execute(Runnable runnable) {
        if (threads.isEmpty()) {
            throw new IllegalStateException("ThreadPool stopped!");
        }

        try {
            tasks.put(new FutureTask<>(runnable, null));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <T> Future<T> execute(Callable<T> callable) {
        if (threads.isEmpty()) {
            throw new IllegalStateException("ThreadPool stopped!");
        }
        FutureTask<T> futureTask = new FutureTask<>(callable);
        try {
            tasks.put(futureTask);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return futureTask;
    }


    public class ThreadPoolWorker extends Thread {

        public ThreadPoolWorker(String name) {
            super(name);
        }

        @Override
        public void run() {
            while (!isInterrupted()) {
                try {
                    Runnable task = tasks.take();
                    task.run();
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }
}

