package ru.sberbank.school.task10;

import lombok.NonNull;
import ru.sberbank.school.util.Solution;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

@Solution(11)
public class ConcurrentScalableThreadPool implements ThreadPool {
    private int min;
    private int max;
    private boolean isRunning;
    private final ReentrantLock lock;
    private final LinkedBlockingQueue<Runnable> tasks;
    private final List<ThreadWorker> threads;
    private final List<ThreadWorker> freeThreads;


    public ConcurrentScalableThreadPool(int min, int max) {
        if (min <= 0 || max <= 0) {
            throw new IllegalArgumentException("Number of threads can't be <= 0");
        }
        if (min > max) {
            throw new IllegalArgumentException("Max number of threads can't be lower then min");
        }
        this.min = min;
        this.max = max;
        tasks = new LinkedBlockingQueue<>();
        threads = new ArrayList<>(min);
        freeThreads = new ArrayList<>(min);
        lock = new ReentrantLock();
    }

    @Override
    public void start() {
        if (isRunning) {
            throw new IllegalStateException("ThreadPool already is running");
        }
        for (int i = 0; i < min; i++) {
            newThread(i);
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

    private void newThread(int count) {
        ThreadWorker tmp = new ThreadWorker("ThreadPollWorker-" + count);
        threads.add(tmp);
        tmp.start();
    }

    private void removeThreads() {
        while (threads.size() > min && freeThreads.size() > 0) {
            ThreadWorker tmp = freeThreads.get(0);
            tmp.interrupt();
            freeThreads.remove(tmp);
            threads.remove(tmp);
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
        for (int i = 0; i < threads.size(); i++) {
            if (threads.get(i).isAlive()) {
                threads.get(i).interrupt();
            }
            threads.remove(i);
        }
    }

    private void addTask(Runnable newTask) {
        try {
            lock.lockInterruptibly();
            if (freeThreads.size() <= 0 && threads.size() < max) {
                newThread(threads.size());
            }
            if (tasks.isEmpty() && threads.size() > min) {
                removeThreads();
            }
            tasks.add(newTask);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
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

                    lock.lockInterruptibly();
                    try {
                        freeThreads.add(this);
                    } finally {
                        lock.unlock();
                    }

                    task = tasks.take();

                    lock.lockInterruptibly();
                    try {
                        freeThreads.remove(this);
                    } finally {
                        lock.unlock();
                    }

                    task.run();

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
