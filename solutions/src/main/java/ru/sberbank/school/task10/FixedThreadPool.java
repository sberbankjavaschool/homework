package ru.sberbank.school.task10;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class FixedThreadPool implements ThreadPool {
    private final Queue<Runnable> taskQueue;
    private final List<ThreadWorker> threads;
    private volatile int corePoolSize;

    public FixedThreadPool(int corePoolSize) {
        if (corePoolSize <= 0) {
            throw new IllegalArgumentException();
        }
        this.taskQueue = new LinkedList<>();
        this.threads = new ArrayList<>(corePoolSize);
        this.corePoolSize = corePoolSize;
    }

    @Override
    public void start() {
        synchronized (threads) {
            if (!threads.isEmpty()) {
                throw new IllegalStateException("Пул уже запущен!");
            }
            for (int i = threads.size(); i < corePoolSize; i++) {
                ThreadWorker threadWorker = new ThreadWorker(i);
                threads.add(threadWorker);
                threadWorker.start();
            }
        }
    }

    @Override
    public void stopNow() {
        synchronized (taskQueue) {
            taskQueue.clear();
        }
        synchronized (threads) {
            if (threads.isEmpty()) {
                throw new IllegalStateException("Пул еще не запущен!");
            }
            for (ThreadWorker t : threads) {
                t.interrupt();
            }
            threads.clear();
        }
    }

    @Override
    public void execute(@NonNull Runnable runnable) {
        synchronized (threads) {
            if (threads.isEmpty()) {
                throw new IllegalStateException("Пул еще не запущен!");
            }
        }
        synchronized (taskQueue) {
            taskQueue.add(runnable);
            taskQueue.notify();
        }
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
                Runnable task;
                synchronized (taskQueue) {
                    if (taskQueue.isEmpty()) {
                        try {
                            taskQueue.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    task = taskQueue.poll();
                }
                if (task != null) {
                    task.run();
                }
            }
        }
    }

}

