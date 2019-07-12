package ru.sberbank.school.task10;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentScalableThreadPool implements ThreadPool {

    private final List<Thread> threads;
    private final BlockingQueue<Runnable> tasks;
    private final ReentrantLock lock;
    private int minSize;
    private int maxSize;

    ConcurrentScalableThreadPool(int minSize, int maxSize) {
        if (minSize <= 0 || minSize > maxSize) {
            throw new IllegalArgumentException("некорректный ввод");
        }

        this.minSize = minSize;
        this.maxSize = maxSize;
        lock = new ReentrantLock();
        threads = new ArrayList<>(minSize);
        tasks = new ArrayBlockingQueue<>(maxSize * 10);
    }

    @Override
    public void start() {
        if (!threads.isEmpty()) {
            throw new IllegalStateException("старт уже был произведен");
        }

        for (int i = 0; i < minSize; i++) {
            ThreadWorker thread = new ThreadWorker("ThreadPoolWorker-" + i);
            threads.add(thread);
            thread.start();
        }
    }

    @Override
    public void stopNow() {
        for (Thread thread : threads) {
            thread.interrupt();
        }
        tasks.clear();
        threads.clear();
    }

    @Override
    public void execute(Runnable runnable) {

        if (tasks.size() != 0 && threads.size() < maxSize) {
            addThread();
        }

        try {
            tasks.put(runnable);
        } catch (InterruptedException e) {
            //e.printStackTrace();
        }
    }


    private void addThread() {

        lock.lock();

        try {
            ThreadWorker additionalThread = new ThreadWorker("ThreadPoolWorker-" + threads.size());
            threads.add(additionalThread);
            additionalThread.start();

        } finally {
            lock.unlock();
        }

    }

    private void deleteThread() {

        Thread.currentThread().interrupt();
        threads.remove(Thread.currentThread());
    }

    private class ThreadWorker extends Thread {

        ThreadWorker(String name) {
            super(name);
        }

        @Override
        public void run() {
            Runnable taskRun = null;
            while (!Thread.currentThread().isInterrupted()) {

                lock.lock();
                try {
                    if (tasks.isEmpty() && threads.size() > minSize) {
                        deleteThread();
                        return;
                    }
                } finally {
                    lock.unlock();
                }


                try {
                    taskRun = tasks.take();
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }
                if (taskRun != null) {
                    taskRun.run();
                }

            }
        }
    }
}


