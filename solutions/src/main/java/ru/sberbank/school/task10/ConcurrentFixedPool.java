package ru.sberbank.school.task10;

import lombok.NonNull;
import ru.sberbank.school.util.Solution;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Solution(12)
public class ConcurrentFixedPool implements ThreadPool {
    private Thread[] threads;
    private final BlockingQueue<Runnable> tasks;
    private boolean isExist;

    public ConcurrentFixedPool(int countThreads) {
        if (countThreads <= 0) {
            throw new IllegalArgumentException("Количество потоков должно быть больше 0");
        }

        threads = new Thread[countThreads];
        tasks = new ArrayBlockingQueue<>(countThreads);
        isExist = false;
    }

    public Thread[] getThreads() {
        return threads;
    }

    public int getTasksCount() {
        return tasks.size();
    }

    public boolean isExist() {
        return isExist;
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
        if (!isExist) {
            throw new IllegalStateException("Пул не может быть остановлен, если он не был запущен");
        }

        synchronized (tasks) {
            tasks.clear();
        }

        isExist = false;

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

        try {
            tasks.put(runnable);
        } catch (InterruptedException e) {
            e.printStackTrace();
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

                try {
                    r = tasks.take();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
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

