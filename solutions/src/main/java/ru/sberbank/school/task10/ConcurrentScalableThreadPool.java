package ru.sberbank.school.task10;

import lombok.NonNull;
import ru.sberbank.school.util.Solution;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

@Solution(11)
public class ConcurrentScalableThreadPool implements ThreadPool {
    private final List<Executor> threadPool;
    private final BlockingQueue<FutureTask> tasks;
    private final int minThreadsCount;
    private final int maxThreadsCount;
    private boolean started;
    private final Queue<Integer> freeNames = new LinkedList<>();
    private final AtomicInteger freeCount;
    private final ReentrantLock lock;

    public ConcurrentScalableThreadPool(int minThreadsCount, int maxThreadsCount) {
        if (minThreadsCount <= 0) {
            throw new IllegalArgumentException("Минимальное количество потоков не может быть меньше одного");
        }
        if (maxThreadsCount <= 0) {
            throw new IllegalArgumentException("Максимальное количество потоков не может быть меньше одного");
        }
        if (maxThreadsCount < minThreadsCount) {
            throw new IllegalArgumentException("Максимальное количество потоков не может быть меньше минимального");
        }
        this.threadPool = new LinkedList<>();//CopyOnWriteArrayList<>();
        this.tasks = new LinkedBlockingDeque<>();
        this.minThreadsCount = minThreadsCount;
        this.maxThreadsCount = maxThreadsCount;
        this.freeCount = new AtomicInteger(0);
        lock = new ReentrantLock();
        started = false;


        for (int i = 0; i < maxThreadsCount; i++) {
            freeNames.add(i);
        }
    }

    @Override
    public void start() {
        if (started) {
            throw new IllegalStateException("ThreadPool уже запущен!");
        }

        for (int i = 0; i < minThreadsCount; i++) {
            threadPool.add(new Executor(freeNames.poll()));
            threadPool.get(i).start();
            freeCount.incrementAndGet();
        }

        started = true;
    }

    @Override
    public void stopNow() {
        if (!started) {
            throw new IllegalStateException("ThreadPool еще не был запущен!");
        }

        tasks.clear();

        for (Thread thread : threadPool) {
            thread.interrupt();
        }

        threadPool.clear();

        freeNames.clear();

        for (int i = 0; i < maxThreadsCount; i++) {
            freeNames.add(i);
        }

        freeCount.set(0);

        started = false;
    }

    @Override
    public void execute(@NonNull Runnable runnable) {
        if (!started) {
            throw new IllegalStateException("ThreadPool должен быть запущен!");
        }

        try {
            checkSize();
            freeCount.decrementAndGet();
            tasks.put(new FutureTask<>(runnable, null));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public <T> Future<T> execute(@NonNull Callable<T> callable) {
        if (!started) {
            throw new IllegalStateException("ThreadPool должен быть запущен!");
        }

        FutureTask<T> ft = new FutureTask<>(callable);

        try {
            checkSize();
            freeCount.decrementAndGet();
            tasks.put(ft);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return ft;
    }

    private void addExecutor() {
        lock.lock();
        try {
            Executor executor = new Executor(freeNames.poll());
            threadPool.add(executor);
            executor.start();
        } finally {
            lock.unlock();
        }
    }

    private void removeExecutors() {
        lock.lock();
        try {
            while (threadPool.size() > minThreadsCount) {
                freeNames.add(threadPool.get(threadPool.size() - 1).getIdx());
                threadPool.remove(threadPool.size() - 1).interrupt();
            }
            freeCount.set(minThreadsCount + 1);
        } finally {
            lock.unlock();
        }
    }

    private void checkSize() {
        if (threadPool.size() < maxThreadsCount && freeCount.get() <= 0) {
            addExecutor();
        } else if (tasks.isEmpty() && threadPool.size() > minThreadsCount) {
            removeExecutors();
        }
    }

    public int getThreadCount() {
        return threadPool.size();
    }

    private class Executor extends Thread {
        private int idx;

        Executor(int number) {
            super("ThreadPoolWorker-" + number);
            this.idx = number;
        }

        public int getIdx() {
            return idx;
        }

        @Override
        public void run() {
            Runnable task;
            while (!Thread.interrupted() ) {
                try {
                    task = tasks.take();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }

                try {
                    task.run();
                    freeCount.incrementAndGet();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
