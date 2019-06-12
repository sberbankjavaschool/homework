package ru.sberbank.school.task10;

import lombok.NonNull;
import ru.sberbank.school.util.Solution;

import java.util.concurrent.LinkedBlockingQueue;

@Solution(11)
public class ConcurrentFixedThreadPool implements ThreadPool {
    private boolean isRunning;
    private Thread[] threads;
    private final LinkedBlockingQueue<Runnable> tasks;

    public ConcurrentFixedThreadPool(int threadsCount) {
        if (threadsCount <= 0) {
            throw new IllegalArgumentException("Number of threads can't be <= 0");
        }
        threads = new Thread[threadsCount];
        tasks = new LinkedBlockingQueue<>();
    }

    @Override
    public void start() {
        if (isRunning) {
            throw new IllegalStateException("ThreadPool already is running");
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new ThreadWorker("ThreadPoolWorker-" + i);
            threads[i].start();
        }
        isRunning = true;

    }

    @Override
    public void stopNow() {
        if (!isRunning) {
            throw new IllegalStateException("ThreadPool is not running yet");
        }
        renew();
    }

    @Override
    public void execute(@NonNull Runnable runnable) {
        if (!isRunning) {
            throw new IllegalStateException("ThreadPool is not running yet");
        }
        addTask(runnable);
    }

    private class ThreadWorker extends Thread {
        ThreadWorker(String name) {
            super(name);
        }

        @Override
        public void run() {
            Runnable task;
            while (!Thread.interrupted()) {
                try {
                    task = tasks.take();
                    task.run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private void renew() {
        clearTasks();
        clearThreads();
        isRunning = false;
    }

    private void clearTasks() {
        tasks.clear();
    }

    private void clearThreads() {
        for (int i = 0; i < threads.length; i++) {
            if (threads[i].isAlive()) {
                threads[i].interrupt();
            }
            threads[i] = null;
        }
    }

    private void addTask(Runnable newTask) {
        tasks.add(newTask);
    }
}
