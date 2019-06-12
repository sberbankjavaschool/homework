package ru.sberbank.school.task10;

import lombok.NonNull;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentScalableThreadPool implements ThreadPool {
    private final BlockingQueue<FutureTask> tasks = new LinkedBlockingQueue<>();
    private PoolWorker[] threads;
    private int minSizePool;
    private AtomicBoolean isAliveThreadPool = new AtomicBoolean(false);
    private ReentrantLock lock = new ReentrantLock();

    public ConcurrentScalableThreadPool(int min, int max) {
        if (min <= 0 || max <= 0) {
            throw new IllegalArgumentException("Amount of threads must be more than zero");
        }
        if (min > max) {
            throw new IllegalArgumentException("Minimum amount of threads must be less than maximum");
        }

        threads = new PoolWorker[max];

        minSizePool = min;
    }

    @Override
    public void start() {
        if (isAliveThreadPool.get()) {
            throw new IllegalStateException("Thread pool running");
        }

        try {
            lock.lock();

            for (int i = 0; i < threads.length; i++) {
                threads[i] = new PoolWorker("ThreadPoolWorker-" + i);
            }

            for (int i = 0; i < minSizePool; i++) {
                threads[i].start();
            }
        } finally {
            lock.unlock();
        }

        isAliveThreadPool.set(true);
    }

    @Override
    public void stopNow() {
        if (!isAliveThreadPool.get()) {
            throw new IllegalStateException("Thread pool not running");
        }
        try {
            lock.lock();

            for (PoolWorker thread : threads) {
                thread.interrupt();
            }
        } finally {
            lock.unlock();
        }

        isAliveThreadPool.set(false);

        tasks.clear();
    }

    @Override
    public void execute(@NonNull Runnable runnable) {
        if (!isAliveThreadPool.get()) {
            throw new IllegalStateException("Thread pool not running");
        }
        if (tasks.size() != 0) {
            extendPoolIfNotFreeThreads();
        }

        try {
            tasks.put(new FutureTask<>(runnable, true));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public <T> Future<T> execute(@NonNull Callable<T> callable) {
        if (!isAliveThreadPool.get()) {
            throw new IllegalStateException("Thread pool not running");
        }
        if (tasks.size() != 0) {
            extendPoolIfNotFreeThreads();
        }

        FutureTask<T> future = new FutureTask<>(callable);

        try {
            tasks.put(future);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return future;
    }

    //Увеличивает размер пула если нет ожидающих нитей
    private void extendPoolIfNotFreeThreads() {
        lock.lock();
        try {
            for (int i = minSizePool; i < threads.length; i++) {
                PoolWorker thread = threads[i];
                if (!isWaitingThread() || tasks.size() > minSizePool) {
                    if (thread.getState() == Thread.State.NEW) {
                        thread.start();
                        break;
                    }
                    if (thread.getState() == Thread.State.TERMINATED) {
                        threads[i] = new PoolWorker("ThreadPoolWorker-" + i);
                        threads[i].start();
                        break;
                    }
                }
            }
        } finally {
            lock.unlock();
        }
    }

    //Уменьшает размер пула если нет задач в очереди
    private void reducePoolIfNotTasks() {
        try {
            lock.lock();

            for (int i = minSizePool; i < threads.length; i++) {
                Thread thread = threads[i];
                if (thread.getState() != Thread.State.NEW) {
                    thread.interrupt();
                }
            }
        } finally {
            lock.unlock();
        }
    }

    private boolean isWaitingThread() {
        for (Thread thread : threads) {
            if (thread.getState() == Thread.State.WAITING) {
                return true;
            }
        }
        return false;
    }

    public class PoolWorker extends Thread {

        public PoolWorker(String name) {
            super(name);
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                //Сужаем пул, если нет задач в очереди
                if (tasks.size() == 0) {
                    reducePoolIfNotTasks();
                }

                Runnable task = null;
                try {
                    task = tasks.take();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                if (task != null) {
                    task.run();
                }
            }
        }
    }
}
