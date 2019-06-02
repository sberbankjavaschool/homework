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
    private volatile AtomicInteger freeThreads;
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
        this.freeThreads = new AtomicInteger(0);
        tasks = new LinkedList<>();
        threads = new ArrayList<>(min);
        start();
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
        freeThreads = new AtomicInteger(0);
        start();
    }

    @Override
    public void execute(@NonNull Runnable runnable) {
        synchronized (tasks) {
            if (freeThreads.get() == 0 && threads.size() < max) {
                addThread();
            } else if (tasks.isEmpty() && threads.size() > min) {
                deleteThreads();
            }

            tasks.addLast(runnable);
            tasks.notify();
        }
    }

    private class ThreadWorker extends Thread {
        private volatile boolean isFree = true;

        ThreadWorker(@NonNull String name) {
            super(name);
        }

        @Override
        public void run() {
            Runnable r;

            while (true) {

                if (Thread.currentThread().isInterrupted()) {
                    break;
                }

                synchronized (tasks) {
                    isFree = true;
                    freeThreads.incrementAndGet();

                    while (tasks.isEmpty()) {
                        try {
                            tasks.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    r = tasks.removeFirst();
                }

                isFree = false;
                freeThreads.decrementAndGet();

                r.run();
            }
        }
    }

    private void deleteThreads() {
        for (int i = 0; i < threads.size(); i++) {
            if (threads.size() == min) {
                break;
            }
            if (threads.get(i).isFree) {
                threads.get(i).interrupt();
                threads.remove(threads.get(i));
            }
        }
    }

    private void addThread() {
        ThreadWorker t = new ThreadWorker("ThreadPoolWorker-" + count++);
        threads.add(t);
        t.start();
    }
}
