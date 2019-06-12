package ru.sberbank.school.task10;

import lombok.NonNull;

import java.util.*;
import java.util.concurrent.*;

public class ConcurrentFixedThreadPool implements ThreadPool {
    private final BlockingQueue<Runnable> taskQueue;
    private final Set<ThreadWorker> threads;
    private volatile int corePoolSize;

    public ConcurrentFixedThreadPool(int corePoolSize) {
        if (corePoolSize <= 0) {
            throw new IllegalArgumentException();
        }
        this.taskQueue = new LinkedBlockingQueue<>();
        this.threads = ConcurrentHashMap.newKeySet();
        this.corePoolSize = corePoolSize;
    }

    @Override
    public synchronized void start() {
        if (!threads.isEmpty()) {
            throw new IllegalStateException("Пул уже запущен!");
        }
        for (int i = threads.size(); i < corePoolSize; i++) {
            ThreadWorker threadWorker = new ThreadWorker(i);
            threads.add(threadWorker);
            threadWorker.start();
        }
    }

    @Override
    public synchronized void stopNow() {
        if (threads.isEmpty()) {
            throw new IllegalStateException("Пул еще не запущен!");
        }
        taskQueue.clear();
        for (ThreadWorker t : threads) {
            t.interrupt();
        }
        threads.clear();
    }

    @Override
    public synchronized void execute(@NonNull Runnable runnable) {
        if (threads.isEmpty()) {
            throw new IllegalStateException("Пул еще не запущен!");
        }
        taskQueue.add(runnable);
    }

    @Override
    public <T> Future<T> execute(Callable<T> callable) {
        Future<T> result = new FutureTask<>(callable);
        execute((Runnable) result);
        return result;
    }



    class ThreadWorker extends Thread {

        public ThreadWorker(int id) {
            this.setName("ThreadPoolWorker-" + id);
        }

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                try {
                    Runnable task = taskQueue.take();
                    task.run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

}

