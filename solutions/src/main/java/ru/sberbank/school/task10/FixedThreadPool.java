package ru.sberbank.school.task10;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class FixedThreadPool implements ThreadPool {

    private int n;
    private List<Thread> threads;
    private final List<Runnable> runnableTasks;
    private List<FutureTask<?>> futures;

    public FixedThreadPool(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Parameter n must be greater than 0!");
        }
        this.n = n;
        threads = new ArrayList<>(n);
        runnableTasks = new LinkedList<>();
        futures = new LinkedList<>();
    }

    @Override
    public void start() {
        for (int i = 0; i < n; i++) {
            Thread thread = new ThreadPoolWorker("ThreadPoolWorker-" + i);
            threads.add(thread);
            thread.start();
        }
    }

    @Override
    public void stopNow() {
        synchronized (runnableTasks) {
            runnableTasks.clear();
            futures.clear();
        }
        for (int i = threads.size() - 1; i >= 0; i--) {
            threads.get(i).interrupt();
            threads.remove(i);
        }
    }

    @Override
    public void execute(Runnable runnable) {
        synchronized (runnableTasks) {
            runnableTasks.add(runnable);
            futures.add(null);
            runnableTasks.notify();
        }
    }

    @Override
    public <T> Future<T> execute(Callable<T> callable) {
        synchronized (runnableTasks) {
            runnableTasks.add(null);
            FutureTask<T> futureTask = new FutureTask<>(callable);
            futures.add(futureTask);
            runnableTasks.notify();
            return futureTask;
        }
    }

    public class ThreadPoolWorker extends Thread {

        public ThreadPoolWorker(String name) {
            super(name);
        }

        @Override
        public void run() {
            Runnable runnableTask;
            FutureTask<?> future;
            boolean keepGoOn = !isInterrupted();
            while (keepGoOn) {
                synchronized (runnableTasks) {
                    try {
                        if (runnableTasks.isEmpty()) {
                            runnableTasks.wait();
                        }
                        runnableTask = runnableTasks.get(0);
                        future = futures.get(0);
                        runnableTasks.remove(0);
                        futures.remove(0);
                    } catch (InterruptedException e) {
                        keepGoOn = false;
                        continue;
                    }
                }
                if (runnableTask != null) {
                    runnableTask.run();
                }
                if (future != null) {
                    future.run();
                }
                keepGoOn = !isInterrupted();
            }
        }
    }
}
