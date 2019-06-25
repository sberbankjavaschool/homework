package ru.sberbank.school.task10;

import lombok.NonNull;

import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.LongStream;

/**
 * Created by Mart
 * 15.06.2019
 **/
public class MyScalableConcurrentThreadPool implements ThreadPool {
    private final int sizeMin;
    private final int sizeMax;
    private final LinkedBlockingQueue<Long> freeIndexForNewWorker = new LinkedBlockingQueue<>();
    private final ConcurrentHashMap<Long, ThreadWorker> poolMap = new ConcurrentHashMap<>();
    private final LinkedBlockingQueue<FutureTask> tasks = new LinkedBlockingQueue<>();

    private ReentrantLock tasksLock = new ReentrantLock();
    private ReentrantReadWriteLock poolLock = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock poolReadLock = poolLock.readLock();
    private ReentrantReadWriteLock.WriteLock poolWriteLock = poolLock.writeLock();

    public MyScalableConcurrentThreadPool(int sizeMin, int sizeMax) {
        if (sizeMin <= 0 || sizeMax <= 0 || sizeMax < sizeMin) {
            throw new IllegalArgumentException("Incorrect input size");
        }
        this.sizeMin = sizeMin;
        this.sizeMax = sizeMax;
        fillPoolMap(sizeMin);
    }

    public MyScalableConcurrentThreadPool(int fixedSize) {
        if (fixedSize <= 0) {
            throw new IllegalArgumentException("Incorrect input size");
        }
        this.sizeMin = fixedSize;
        this.sizeMax = fixedSize;
        fillPoolMap(fixedSize);
    }

    private void fillPoolMap(long size) {
        try {
            poolWriteLock.lock();
            LongStream.range(0L, size)
                    .forEach(i -> poolMap.putIfAbsent(i, new ThreadWorker("ThreadWorker " + i, i)));
        } finally {
            poolWriteLock.unlock();
        }
    }

    @Override
    public void start() {
        try {
            poolReadLock.lock();
            LongStream.range(0L, sizeMin)
                    .forEach(i -> poolMap.get(i).start());
        } finally {
            poolReadLock.unlock();
        }
    }

    @Override
    public void stopNow() {
        try {
            tasksLock.lock();
            tasks.clear();
        } finally {
            tasksLock.unlock();
        }

        try {
            poolWriteLock.lock();
            poolMap.forEach((key, worker) -> {
                if (worker != null) {
                    worker.interrupt();
                }
            });
            poolMap.clear();
            freeIndexForNewWorker.clear();
        } finally {
            poolWriteLock.unlock();
        }
    }

    @Override
    public void execute(@NonNull Runnable runnable) {
        try {
            tasksLock.lock();
            tasks.put(new FutureTask(runnable, true));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            tasksLock.unlock();
        }



    }

    @Override
    public <T> Future<T> execute(@NonNull Callable<T> callable) {
        FutureTask<T> futureTask = new FutureTask(callable);
        try {
            tasksLock.lock();
            tasks.put(futureTask);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            tasksLock.unlock();
        }
        return futureTask;
    }

    private boolean poolResize() throws InterruptedException {
        int taskSize;
        boolean needToInterrupt = false;
        try {
            tasksLock.lock();
            taskSize = tasks.size();
            try {
                poolWriteLock.lock();
                findFreeIndex();
                int activeThreads = poolMap.size();
                if (taskSize > activeThreads && taskSize > sizeMin && activeThreads < sizeMax) {

                    if (!freeIndexForNewWorker.isEmpty()) {
                        long id = freeIndexForNewWorker.take();

                        ThreadWorker worker = new ThreadWorker("ThreadWorker " + id, id);
                        poolMap.put(id, worker);
                        worker.start();
                    }
                }
                needToInterrupt = taskSize < activeThreads && activeThreads > sizeMin;
            } finally {
                poolWriteLock.unlock();
            }
        } finally {
            tasksLock.unlock();
        }
        return needToInterrupt;
    }

    private void findFreeIndex() {
        LongStream.range(0L, sizeMax)
                .forEach(i -> {
                    if (!poolMap.containsKey(i) && !freeIndexForNewWorker.contains(i)) {
                        try {
                            freeIndexForNewWorker.put(i);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public int getActiveThreads() {
        int count;
        try {
            poolReadLock.lock();
            count = poolMap.size();
        } finally {
            poolReadLock.unlock();
        }
        return count;
    }

    private class ThreadWorker extends Thread {
        private long id;

        private ThreadWorker(String name, long id) {
            super(name);
            this.id = id;
        }

        @Override
        public long getId() {
            return id;
        }

        @Override
        public void run() {
            while (!isInterrupted()) {
                try {
                    boolean needToInterrupt = poolResize();

                    if (needToInterrupt) {
                        System.out.println("ThreadWorker " + id + " interrupted");
                        poolWriteLock.lock();
                        poolMap.remove(Thread.currentThread().getId());
                        poolWriteLock.unlock();
                        Thread.currentThread().interrupt();
                        return;
                    }

                    FutureTask task = null;
                    task = tasks.take();

                    if (task != null) {
                        task.run();
                        System.out.println("ThreadWorker " + id + " ended");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        @Override
        public String toString() {
            return "ThreadWorker " + id;
        }
    }
}
