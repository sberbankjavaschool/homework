package ru.sberbank.school.task10;

import lombok.NonNull;
import ru.sberbank.school.util.Solution;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Solution(10)
public class ScalableThreadPool implements ThreadPool {
    private int min;
    private int max;
    private boolean isRunning;
    private final LinkedList<Runnable> tasks;
    private final List<ThreadWorker> threads;
    private final List<ThreadWorker> freeThreads;


    public ScalableThreadPool(int min, int max) {
        if (min <= 0 || max <= 0) {
            throw new IllegalArgumentException("Number of threads can't be <= 0");
        }
        if (min > max) {
            throw new IllegalArgumentException("Max number of threads can't be lower then min");
        }
        this.min = min;
        this.max = max;
        tasks = new LinkedList<>();
        threads = new ArrayList<>(min);
        freeThreads = new ArrayList<>(min);
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
        synchronized (tasks) {
            tasks.clear();
        }
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
        synchronized (tasks) {
            synchronized (freeThreads) {
                if (freeThreads.size() <= 0 && threads.size() < max) {
                    newThread(threads.size());
                }
                if (tasks.isEmpty() && threads.size() > min) {
                    removeThreads();
                }
                tasks.addLast(newTask);
                tasks.notify();
            }
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
                synchronized (tasks) {
                    synchronized (freeThreads) {
                        freeThreads.add(this);
                    }
                    while (tasks.isEmpty()) {
                        try {
                            tasks.wait();
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                    task = tasks.removeFirst();
                }
                synchronized (freeThreads) {
                    freeThreads.remove(this);
                }
                task.run();
            }
        }
    }
}
