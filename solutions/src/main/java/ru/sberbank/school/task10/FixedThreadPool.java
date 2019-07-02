package ru.sberbank.school.task10;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class FixedThreadPool implements ThreadPool {

    private int scale;
    private Queue<Runnable> tasks;
    private ArrayList<Thread> threads;


    public FixedThreadPool(int scale) {
        this.scale = scale;
        this.tasks = new LinkedList<>();
        this.threads = new ArrayList<>(scale);
    }

    @Override
    public void start() {


        for (int i = 0; i < scale; i++) {
            Thread thread = new Thread("Thread-" + i) {

                @Override
                public void run() {
                    Runnable runnable;
                    while (!Thread.interrupted()) {
                        synchronized (tasks) {
                            if (tasks.isEmpty()) {
                                try {
                                    tasks.wait();
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
            thread.start();
        }

    }

    @Override
    public void stopNow() {

        synchronized (tasks) {

            tasks.clear();

            for (Thread t : threads) {
                t.interrupt();
            }

            threads.clear();
        }

    }

    @Override
    public void execute(Runnable runnable) {

        synchronized (tasks) {
            tasks.add(runnable);
            tasks.notify();
        }

    }

    @Override
    public <T> Future<T> execute(Callable<T> callable) {
        return null;
    }
}
