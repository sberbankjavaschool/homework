package ru.sberbank.school.task10;

import java.util.LinkedList;
import java.util.Queue;

public class FixedThreadPool implements ThreadPool {

    private Thread[] threads;
    private final Queue<Runnable> tasks;
    private boolean isEmpty;

    public FixedThreadPool(int sizeThreads) {
        if (sizeThreads <= 0) {
            throw new IllegalArgumentException("количество потоков не может быть <= нуля");
        }
        threads = new Thread[sizeThreads];
        tasks = new LinkedList<>();
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
        synchronized (tasks) {
            tasks.clear();
            for (int i = 0; i < threads.length; i++) {
                threads[i].interrupt();
                threads[i] = null;
            }
        }
        isEmpty = true;
    }

    @Override
    public void execute(Runnable runnable) {
        synchronized (tasks) {
            tasks.add(runnable);
            tasks.notify();
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

                synchronized (tasks) {

                    if (tasks.isEmpty()) {
                        try {
                            tasks.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    taskRun = tasks.poll();
                }
                if (taskRun != null) {
                    taskRun.run();
                }
            }
        }
    }
}
