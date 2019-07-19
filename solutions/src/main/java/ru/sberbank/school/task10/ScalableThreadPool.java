package ru.sberbank.school.task10;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class ScalableThreadPool implements ThreadPool {

    private int min;
    private int max;

    private final ArrayList<Thread> threads;
    private final Queue<Runnable> tasks;
    private final Queue<Integer> threadID;
    private volatile int count;
    private volatile int countThreads;




    public ScalableThreadPool(int min, int max) {
        this.min = min;
        this.max = max;
        this.tasks = new LinkedList<>();
        this.threads = new ArrayList<>(max);
        this.threadID = new LinkedList<>();
        this.count = min;


    }

    @Override
    public void start() {
        synchronized (threads) {

            if (!threads.isEmpty()) {
                throw new IllegalStateException("This method is already start!");
            }

            for (int i = 0; i < min; i++) {
                threadID.add(i + 1);
                newThread().start();
            }
        }
    }

    @Override
    public void stopNow() {
        synchronized (tasks) {

            tasks.clear();

            synchronized (threads) {

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
            if (threads.isEmpty()) {
                throw new NoSuchElementException("No threads running! Call start() first");
            }
        }
        synchronized (tasks) {

            if ((threads.size() < max) && !(tasks.isEmpty())) {
                if (count > max) {
                    count++;
                    threadID.add(count);
                }
                newThread().start();
            }

            tasks.add(runnable);

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
                                if (threads.size() > min) {
                                    threadID.add(id);
                                    threads.remove(Thread.currentThread());
                                    Thread.currentThread().interrupt();
                                } else {
                                    tasks.wait();
                                }

                            } catch (InterruptedException e) {
                                e.printStackTrace();
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
