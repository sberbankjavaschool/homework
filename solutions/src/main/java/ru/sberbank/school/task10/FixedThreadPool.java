package ru.sberbank.school.task10;

import lombok.NonNull;
import ru.sberbank.school.util.Solution;

import java.util.LinkedList;

@Solution(10)
public class FixedThreadPool implements ThreadPool {
    private Thread[] threads;
    private final LinkedList<Runnable> tasks;
    private boolean isExist;

    public FixedThreadPool(int countThreads) {
        if (countThreads <= 0) {
            throw new IllegalArgumentException("Количество потоков должно быть больше 0");
        }

        threads = new Thread[countThreads];
        tasks = new LinkedList<>();
        isExist = false;
    }

    @Override
    public void start() {
        if (isExist) {
            throw new IllegalStateException("Пул уже был запущен");
        }

        isExist = true;

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new ThreadWorker("ThreadPoolWorker-" + i);
            threads[i].start();
        }
    }

    @Override
    public void stopNow() {
        synchronized (tasks) {
            tasks.clear();
        }

        for (int i = 0; i < threads.length; i++) {
            threads[i].interrupt();
            threads[i] = null;
        }
    }

    @Override
    public void execute(Runnable runnable) {
        if (!isExist) {
            throw new IllegalStateException("Перед началом работы с пулом нужно вызвать start()");
        }

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
