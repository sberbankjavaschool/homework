package ru.sberbank.school.task10;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class FixedThreadPool implements ThreadPool {

    private int scale;

    private final Queue<Runnable> tasks;
    private final ArrayList<Thread> threads;

    private volatile boolean stopped;


    public FixedThreadPool(int scale) {
        this.scale = scale;
        this.tasks = new LinkedList<>();
        this.threads = new ArrayList<>(scale);
        this.stopped = false;
    }

    @Override
    public void start() {
        synchronized (threads) {
            if (!threads.isEmpty() && !stopped) {
                throw new IllegalStateException("This method is already start!");
            }

            for (int i = 0; i < scale; i++) {
                newThread(i).start();
            }
            stopped = false;
        }
    }

    @Override
    public void stopNow() {
        if (stopped) {
            throw new IllegalStateException("This method is already invoke!");
        }

        synchronized (tasks) {
            synchronized (threads) {
                stopped = true;
                tasks.clear();

                for (Thread t : threads) {
                    t.interrupt();
                }

                threads.clear();
            }
        }

    }

    @Override
    public void execute(Runnable runnable) {

        synchronized (threads) {
            if (threads.isEmpty() && stopped) {
                throw new NoSuchElementException("No threads running! Call start() first");
            }
        }

        synchronized (tasks) {
            tasks.add(runnable);
            tasks.notify();
        }

    }

    private Thread newThread(int i) {
        Thread thread = new Thread("Thread-" + (i + 1)) {

            @Override
            public void run() {
                Runnable runnable;
                while (!Thread.interrupted()) {
                    synchronized (tasks) {
                        if (tasks.isEmpty()) {
                            try {
                                tasks.wait();
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }
                        runnable = tasks.poll();
                    }
                    try {
                        runnable.run();
                    } catch (NullPointerException ignored) {

                    }
                }
            }
        };
        threads.add(thread);
        return thread;
    }

    @Override
    public <T> Future<T> execute(Callable<T> callable) {
        return null;
    }
}
