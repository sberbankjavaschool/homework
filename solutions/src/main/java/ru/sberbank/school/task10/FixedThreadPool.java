package ru.sberbank.school.task10;

import lombok.NonNull;
import ru.sberbank.school.util.Solution;

import java.util.LinkedList;

@Solution(10)
public class FixedThreadPool implements ThreadPool {
    private Thread[] threads;
    private final LinkedList<Runnable> tasks;
    private int count;

    public FixedThreadPool(int countThreads) {
        if (countThreads <= 0) {
            throw new IllegalArgumentException("Количество потоков должно быть больше 0");
        }

        threads = new Thread[countThreads];
        tasks = new LinkedList<>();
        count = countThreads;
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
        ThreadWorker(@NonNull String name) {
            super(name);
        }

        @Override
        public void run() {
            Runnable r;

            while (!Thread.interrupted()) {

                synchronized (tasks) {

                    while (tasks.isEmpty()) {
                        try {
                            tasks.wait();
                        } catch (InterruptedException e) {
                            //Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    r = tasks.removeFirst();
                }

                try {
                    r.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
