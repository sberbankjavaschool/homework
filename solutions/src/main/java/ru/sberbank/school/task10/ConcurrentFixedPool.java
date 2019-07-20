package ru.sberbank.school.task10;

import java.util.NoSuchElementException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentFixedPool implements ThreadPool {

    private int scale;

    private final BlockingQueue<Runnable> tasks;
    private final BlockingQueue<Thread> threads;
    private final ReentrantLock lock;

    private volatile boolean stopped;


    public ConcurrentFixedPool(int scale) {
        if (scale <= 0) {
            throw new IllegalArgumentException("The scale of pool dont may be < 0!");
        }
        this.scale = scale;
        this.lock = new ReentrantLock();
        this.threads = new ArrayBlockingQueue<>(scale);
        this.tasks = new ArrayBlockingQueue<>(scale * 10);
        this.stopped = false;
    }

    @Override
    public void start() {
        lock.lock();
        try {
            if (!threads.isEmpty() && !stopped) {
                throw new IllegalStateException("This method is already start!");
            }

            for (int i = 0; i < scale; i++) {
                newThread(i).start();
            }
        } finally {
            lock.unlock();
            stopped = false;
        }

    }

    @Override
    public void stopNow() {

        if (stopped) {
            throw new IllegalStateException("This method is already invoke!");
        }

        lock.lock();
        try {
            tasks.clear();

            for (Thread t : threads) {
                t.interrupt();
            }

            threads.clear();
        } finally {
            lock.unlock();
            stopped = true;
        }


    }

    @Override
    public void execute(Runnable runnable) {

        if (threads.isEmpty() && stopped) {
            throw new NoSuchElementException("No threads running! Call start() first");
        }

        tasks.add(runnable);
        tasks.notify();

    }

    private Thread newThread(int i) {
        Thread thread = new Thread("Thread-" + (i + 1)) {

            @Override
            public void run() {
                Runnable runnable;
                while (!Thread.interrupted()) {

                    if (tasks.isEmpty()) {
                        try {
                            tasks.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    runnable = tasks.poll();

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
