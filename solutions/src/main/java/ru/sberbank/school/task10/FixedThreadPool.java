package ru.sberbank.school.task10;


import lombok.NonNull;
import ru.sberbank.school.util.Solution;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

@Solution(10)
public class FixedThreadPool implements ThreadPool {
    private Thread[] threadPool;
    private final Queue<FutureTask> tasks;
    private final int threadsCount;


    public FixedThreadPool(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Количестов потоков не может быть меньше одного");
        }

        this.threadPool = new Thread[count];
        this.tasks = new LinkedList<>();
        this.threadsCount = count;
    }

    @Override
    public void start() {
        for (int i = 0; i < threadsCount; i++) {
            threadPool[i] = new Executor(i);
            threadPool[i].start();
        }
    }

    @Override
    public void stopNow() {
        synchronized (tasks) {
            tasks.clear();
        }

        for (int i = 0; i < threadsCount; i++) {
            threadPool[i].interrupt();
            threadPool[i] = null;
        }
    }

    @Override
    public void execute(@NonNull Runnable runnable) {
        synchronized (tasks) {
            tasks.add(new FutureTask<>(runnable, null));
            tasks.notifyAll();
        }
    }

    @Override
    public <T> Future<T> execute(@NonNull Callable<T> callable) {
        FutureTask<T> ft = new FutureTask<>(callable);

        synchronized (tasks) {
            tasks.add(ft);
            tasks.notify();
        }

        return ft;
    }

    private class Executor extends Thread {
        Executor(int number) {
            super("ThreadPoolWorker-" + number);
        }

        @Override
        public void run() {
            Runnable task;
            while (!Thread.interrupted() ) {
                synchronized (tasks) {
                    while (tasks.isEmpty()) {
                        try {
                            tasks.wait();
                        } catch (InterruptedException e) {
                            return;
                        }

                    }
                    task = tasks.poll();
                }

                try {
                    task.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
