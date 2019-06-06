package ru.sberbank.school.task10;

import lombok.NonNull;
import ru.sberbank.school.util.Solution;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

@Solution(10)
public class ScalableThreadPool implements ThreadPool {
    private final List<Thread> threadPool;
    private final Queue<FutureTask> tasks;
    private final int minThreadsCount;
    private final int maxThreadsCount;

    public ScalableThreadPool(int minThreadsCount, int maxThreadsCount) {
        if (minThreadsCount <= 0) {
            throw new IllegalArgumentException("Минимальное количество потоков не может быть меньше одного");
        }
        if (maxThreadsCount <= 0) {
            throw new IllegalArgumentException("Максимальное количество потоков не может быть меньше одного");
        }
        if (maxThreadsCount < minThreadsCount) {
            throw new IllegalArgumentException("Максимальное количество потоков не может быть меньше минимального");
        }
        this.threadPool = new ArrayList<>();
        this.tasks = new LinkedList<>();
        this.minThreadsCount = minThreadsCount;
        this.maxThreadsCount = maxThreadsCount;
    }

    @Override
    public void start() {
        for (int i = 0; i < minThreadsCount; i++) {
            threadPool.add(new Executor(i));
        }

        for (Thread thread : threadPool) {
            thread.start();
        }
    }

    @Override
    public void stopNow() {
        synchronized (tasks) {
            tasks.clear();
        }

        for (Thread thread : threadPool) {
            thread.interrupt();
        }

        threadPool.clear();
    }

    @Override
    public void execute(@NonNull Runnable runnable) {
        synchronized (tasks) {
            checkSize();
            tasks.add(new FutureTask<>(runnable, null));
            tasks.notifyAll();
        }
    }



    @Override
    public <T> Future<T> execute(@NonNull Callable<T> callable) {
        FutureTask<T> ft = new FutureTask<>(callable);

        synchronized (tasks) {
            checkSize();
            tasks.add(ft);
            tasks.notify();
        }

        return ft;
    }

    private void checkSize() {
        if (tasks.isEmpty()) {
            while (threadPool.size() > minThreadsCount) {
                threadPool.remove(threadPool.size() - 1).interrupt();
            }
        } else if (threadPool.size() < maxThreadsCount && checkAllExecutorsWork()) {
            threadPool.add(new Executor(threadPool.size()));
            threadPool.get(threadPool.size() - 1).start();
        }
    }

    private boolean checkAllExecutorsWork() {
        for (Thread thread : threadPool) {
            boolean result = thread.getState().equals(Thread.State.RUNNABLE);
            if (!result) {
                return false;
            }
        }

        return true;
    }

    private class Executor extends Thread {
        Executor(int number) {
            super("ThreadPoolWorker-" + number);
        }

        @Override
        public void run() {
            Runnable task;
            while (!interrupted() ) {
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
