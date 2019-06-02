package ru.sberbank.school.task10;

import lombok.NonNull;
import ru.sberbank.school.util.Solution;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Solution(10)
public class ScalableThreadPool implements ThreadPool {
    private final LinkedList<Runnable> tasks;
    private List<ThreadWorker> threads;
    private final List<ThreadWorker> freeThreads;
    private int min;
    private int max;
    private int count;

    public ScalableThreadPool(int min, int max) {
        if (max <= 0 || min <= 0) {
            throw new IllegalArgumentException("Максимальное и минимальное значения должны быть больше 0");
        }
        if (max < min) {
            throw new IllegalArgumentException("Максимальное значение должно быть не меньше минимального");
        }

        this.min = min;
        this.max = max;
        this.freeThreads = new ArrayList<>(min);
        tasks = new LinkedList<>();
        threads = new ArrayList<>(min);
    }

    @Override
    public void start() {
        for (count = 0; count < min; count++) {
            ThreadWorker t = new ThreadWorker("ThreadPoolWorker-" + count);
            threads.add(t);
            t.start();
        }
    }

    @Override
    public void stopNow() {
        synchronized (tasks) {
            tasks.clear();
        }

        for (ThreadWorker thread : threads) {
            thread.interrupt();
        }

        threads = new ArrayList<>(min);
        freeThreads.clear();
    }

    @Override
    public void execute(@NonNull Runnable runnable) {
        synchronized (tasks) {
            synchronized (freeThreads) {
                if (freeThreads.size() == 0 && threads.size() < max) {
                    addThread();
                } else if (tasks.isEmpty() && threads.size() > min) {
                    deleteThreads();
                }
            }

            tasks.addLast(runnable);
            tasks.notify();
        }
    }

    private class ThreadWorker extends Thread {

        ThreadWorker(@NonNull String name) {
            super(name);
        }

        @Override
        public void run() {
            Runnable r;

            while (!Thread.interrupted()) {

                synchronized (tasks) {
                    synchronized (freeThreads) {
                        freeThreads.add(this);
                    }

                    while (tasks.isEmpty()) {
                        try {
                            tasks.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    r = tasks.removeFirst();
                }

                synchronized (freeThreads) {
                    freeThreads.remove(this);
                }

                try {
                    r.run();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void deleteThreads() {
        while (threads.size() > min && freeThreads.size() > 0) {
            freeThreads.remove(0);
        }
    }

    private void addThread() {
        ThreadWorker t = new ThreadWorker("ThreadPoolWorker-" + count++);
        threads.add(t);
        t.start();
    }

}
