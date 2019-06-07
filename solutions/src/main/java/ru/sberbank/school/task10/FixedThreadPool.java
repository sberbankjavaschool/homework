package ru.sberbank.school.task10;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class FixedThreadPool implements ThreadPool {

    private final List<Thread> threadPool;
    private final Queue<Runnable> runnableQueue = new LinkedList<>();
    private int threadUniqueNumber;
    private final int poolSize;

    public FixedThreadPool(int poolSize) {
        this.poolSize = poolSize;
        threadPool = new ArrayList<>(poolSize);
    }

    @Override
    public void start() {
        while (threadPool.size() < poolSize) {
            Thread thread = new ThreadPoolWorker(runnableQueue, threadUniqueNumber++);
            threadPool.add(thread);
            thread.start();
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
