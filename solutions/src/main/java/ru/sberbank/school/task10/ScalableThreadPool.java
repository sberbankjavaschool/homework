package ru.sberbank.school.task10;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class ScalableThreadPool implements ThreadPool {
    private final List<Thread> threadPool;
    private final Queue<Runnable> runnableQueue = new LinkedList<>();
    private final int minPoolSize;
    private final int maxPoolSize;

    public ScalableThreadPool(int minPoolSize, int maxPoolSize) {
        this.minPoolSize = minPoolSize;
        this.maxPoolSize = maxPoolSize;
        threadPool = new ArrayList<>(minPoolSize);
    }

    private void startNewThread() {
        Thread thread = new MortalThreadPoolWorker(runnableQueue, threadPool.size(), this);
        threadPool.add(thread);
        thread.start();
    }

    private boolean checkAvailablePoolCapacity() {
        for (Thread thread : threadPool) {
            if (thread.getState() == Thread.State.WAITING) {
                return false;
            }
        }
        return true;
    }

    void canBeKilled(MortalThreadPoolWorker thread) {
        if (threadPool.size() > minPoolSize) {
            thread.interrupt();
        }
    }


    @Override
    public void start() {
        while (threadPool.size() < minPoolSize) {
            startNewThread();
        }
    }

    @Override
    public void stopNow() {
        threadPool.forEach(Thread::interrupt);
        runnableQueue.clear();
    }

    @Override
    public void execute(Runnable runnable) {
        Objects.requireNonNull(runnable);
        synchronized (runnableQueue) {
            if (checkAvailablePoolCapacity() && threadPool.size() < maxPoolSize) {
                startNewThread();
            }
            runnableQueue.offer(runnable);
            runnableQueue.notifyAll();
        }
    }

    @Override
    public <T> Future<T> execute(Callable<T> callable) {
        Objects.requireNonNull(callable);
        FutureTask<T> futureTask = new FutureTask<>(callable);
        execute(futureTask);
        return futureTask;
    }
}
