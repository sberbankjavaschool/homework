package ru.sberbank.school.task10;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ScalableThreadPool implements ThreadPool {
    private final LinkedList<Runnable> tasks;
    private List<ThreadExecute> threads;
    private AtomicInteger count;
    private int min;
    private int max;

    public ScalableThreadPool(int min, int max) {
        this.min = min;
        this.max = max;
        this.count = new AtomicInteger(min);
        tasks = new LinkedList<>();
        threads = new ArrayList<>(min);
        start();
    }

    @Override
    public void start() {
//        for (int i = 0; i < min; i++) {
//
//        }
    }

    @Override
    public void stopNow() {

    }

    @Override
    public void execute(Runnable runnable) {
        synchronized (tasks) {

        }
    }

    private class ThreadExecute extends Thread {
        ThreadExecute(String name) {
            super(name);
        }

        @Override
        public void run() {
            Runnable r;

            while (true) {

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
