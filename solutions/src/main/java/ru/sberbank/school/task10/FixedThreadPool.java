package ru.sberbank.school.task10;

import java.util.LinkedList;

public class FixedThreadPool implements ThreadPool {
    private Thread[] threads;
    private final LinkedList<Runnable> tasks;
    private int count;

    public FixedThreadPool(int countThreads) {
        threads = new Thread[countThreads];
        tasks = new LinkedList<>();
        count = countThreads;
        start();
    }

    @Override
    public void start() {
        for (int i = 0; i < count; i++) {
            threads[i] = new ThreadWorker("ThreadPoolWorker-" + i);
            threads[i].start();
        }
    }

    @Override
    public void stopNow() {
        synchronized (tasks) {
            tasks.clear();
        }

        for (int i = 0; i < count; i++) {
            threads[i].interrupt();
            threads[i] = null;
        }
    }

    @Override
    public void execute(Runnable runnable) {
        synchronized (tasks) {
            tasks.addLast(runnable);
            tasks.notify();
        }
    }
    
    private class ThreadWorker extends Thread {
        ThreadWorker(String name) {
            super(name);
        }

        @Override
        public void run() {
            Runnable r;

            while (true) {

                if (Thread.interrupted()) {
                    break;
                }

                synchronized (tasks) {
                    while (tasks.isEmpty()) {
                        try {
                            tasks.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    r = tasks.removeFirst();
                }

                r.run();
            }
        }
    }

}
