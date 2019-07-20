package ru.sberbank.school.task10;

import java.util.NoSuchElementException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentScalablePool implements ThreadPool {

    private int min;
    private int max;

    private final BlockingQueue<Thread> threads;
    private final BlockingQueue<Runnable> tasks;
    private final BlockingQueue<Integer> threadID;
    private final ReentrantLock lock;

    private volatile int count;
    private volatile boolean stopped;


    public ConcurrentScalablePool(int min, int max) {
        this.min = min;
        this.max = max;
        this.tasks = new ArrayBlockingQueue<>(max);
        this.threads = new ArrayBlockingQueue<>(max);
        this.threadID = new ArrayBlockingQueue<>(max);
        this.count = min;
        this.lock = new ReentrantLock();
        this.stopped = false;


    }

    @Override
    public void start() {

        lock.lock();
        try {
            if (!threads.isEmpty() && !stopped) {
                throw new IllegalStateException("This method is already start!");
            }
            for (int i = 0; i < min; i++) {
                threadID.add(i + 1);
                newThread().start();
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
            stopped = true;
            lock.unlock();
        }

    }

    @Override
    public void execute(Runnable runnable) {

        if (threads.isEmpty() && stopped) {
            throw new NoSuchElementException("No threads running! Call start() first");
        }


        tasks.add(runnable);
        lock.lock();
        try {
            if ((threads.size() < max) && (threadID.size() == 0)) {
                if (count < max) {
                    ++count;
                    threadID.add(count);
                }
                newThread().start();
            }
        } finally {
            lock.unlock();
        }


    }

    private Thread newThread() {

        int id = threadID.poll();

        Thread thread = new Thread("Thread-" + id) {

            @Override
            public void run() {
                Runnable runnable;
                while (!Thread.interrupted()) {

                    if (tasks.isEmpty()) {
                        try {
                            threadID.add(id);
                            if (threads.size() > min) {
                                threads.remove(Thread.currentThread());
                                Thread.currentThread().interrupt();
                            } else {
                                tasks.wait();
                            }

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