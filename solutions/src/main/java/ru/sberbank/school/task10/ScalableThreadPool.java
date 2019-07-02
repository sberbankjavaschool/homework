package ru.sberbank.school.task10;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class ScalableThreadPool implements ThreadPool {

    private int min;
    private int max;

    private ArrayList<Thread> threads;
    private Queue<Runnable> tasks;
    private volatile HashMap<String , Boolean> nThreads;


    public ScalableThreadPool(int min, int max) {
        this.min = min;
        this.max = max;
        this.tasks = new LinkedList<>();
        this.threads = new ArrayList<>(max);
        this.nThreads = new HashMap<>() {{
            for (int i = 0; i < max; i++) {
                put("Thread-" + i, false);
            }
        }};
    }

    @Override
    public void start() {

        for (int i = 0; i < min; i++) {
            newThread().start();
        }
    }

    @Override
    public void stopNow() {

    }

    @Override
    public void execute(Runnable runnable) {
        if (threads.isEmpty()) {
            throw new NoSuchElementException("No threads running! Call start() first");
        }
        synchronized (tasks) {
            synchronized (threads) {
                if ((threads.size() >= min) && (threads.size() < max) && !(tasks.isEmpty())) {
                    newThread().start();
                }
            }
            tasks.add(runnable);
            tasks.notify();
        }

    }

    private Thread newThread() {

        Thread thread = new Thread("Thread-" + threads.size() + 1) {

            @Override
            public void run() {
                Runnable runnable;
                while (!Thread.interrupted()) {
                    synchronized (tasks) {
                        if (tasks.isEmpty()) {
                            try {
                                if (threads.size() > min) {
                                    Thread.currentThread().interrupt();
                                    threads.remove(Thread.currentThread());
                                } else {
                                    tasks.wait();
                                }

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        runnable = tasks.poll();
                    }
                    runnable.run();
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
