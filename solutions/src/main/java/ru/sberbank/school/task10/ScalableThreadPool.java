package ru.sberbank.school.task10;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class ScalableThreadPool implements ThreadPool {

    private int min;
    private int max;

    private final ArrayList<Thread> threads;
    private final Queue<Runnable> tasks;
    public final Queue<Integer> threadID;

    private volatile int count;
    private volatile boolean stopped;


    public ScalableThreadPool(int min, int max) {
        this.min = min;
        this.max = max;
        this.tasks = new LinkedList<>();
        this.threads = new ArrayList<>(max);
        this.threadID = new LinkedList<>();
        this.count = min;
        this.stopped = false;


    }

    @Override
    public void start() {
        synchronized (threads) {

            if (!threads.isEmpty() && !stopped) {
                throw new IllegalStateException("This method is already start!");
            }
            synchronized (threadID) {
                for (int i = 0; i < min; i++) {
                    threadID.add(i + 1);
                    System.out.println(threadID.size());
                    newThread().start();
                }
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

            tasks.clear();

            synchronized (threads) {

                for (Thread t : threads) {
                    t.interrupt();
                }
                threads.clear();
            }
        }
        stopped = true;
    }

    @Override
    public void execute(Runnable runnable) {
        synchronized (threads) {
            if (threads.isEmpty()) {
                throw new NoSuchElementException("No threads running! Call start() first");
            }
        }
        synchronized (tasks) {

            tasks.add(runnable);

            synchronized (threadID) {
                if ((threads.size() < max) && (threadID.size() == 0)) {
                    if (count < max) {
                        ++count;
                        threadID.add(count);
                    }
                    newThread().start();
                }
            }

            tasks.notify();

        }

    }

    private Thread newThread() {


        int id = threadID.poll();

        Thread thread = new Thread("Thread-" + id) {

            @Override
            public void run() {
                Runnable runnable;
                while (!Thread.interrupted()) {
                    synchronized (tasks) {
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
                                System.out.println();
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
