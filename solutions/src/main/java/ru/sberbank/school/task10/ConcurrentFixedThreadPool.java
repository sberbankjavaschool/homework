package ru.sberbank.school.task10;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConcurrentFixedThreadPool implements ThreadPool {

    private Thread[] threads;
    private final BlockingQueue<Runnable> tasks;
    private boolean isEmpty;

    public ConcurrentFixedThreadPool(int sizeThreads) {
        if (sizeThreads <= 0) {
            throw new IllegalArgumentException("количество потоков не может быть <= нуля");
        }

        threads = new Thread[sizeThreads];
        tasks = new ArrayBlockingQueue<>(sizeThreads * 10);
        isEmpty = true;
    }

    @Override
    public void start() {
        if (!isEmpty()) {
            throw new IllegalStateException("старт уже был произведен");
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new ThreadWorker("ThreadPoolWorker-" + i);
            threads[i].start();
        }

        isEmpty = false;
    }

    @Override
    public void stopNow() {

        tasks.clear();
        for (int i = 0; i < threads.length; i++) {
            threads[i].interrupt();
            threads[i] = null;
        }

        isEmpty = true;
    }

    @Override
    public void execute(Runnable runnable) {
        try {
            tasks.put(runnable);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean isEmpty() {
        return isEmpty;
    }

    private class ThreadWorker extends Thread {

        ThreadWorker(String name) {
            super(name);
        }

        @Override
        public void run() {
            Runnable taskRun;
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    taskRun = tasks.take();
                    taskRun.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
