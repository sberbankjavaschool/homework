package ru.sberbank.school.task10;

import lombok.NonNull;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.stream.LongStream;

/**
 * Created by Mart
 * 04.06.2019
 **/
public class MyScalableThreadPool implements ThreadPool {
    private final int sizeMin;
    private final int sizeMax;
    private final Queue<Long> freeIndexForNewWorker = new LinkedList<>();
    private final Map<Long, ThreadWorker> poolMap = new HashMap<>();
    private final Queue<FutureTask> tasks = new LinkedList<>();

    public MyScalableThreadPool(int sizeMin, int sizeMax) {
        if (sizeMin <= 0 || sizeMax <= 0 || sizeMax < sizeMin) {
            throw new IllegalArgumentException("Incorrect input size");
        }

        this.sizeMin = sizeMin;
        this.sizeMax = sizeMax;
        fillPoolMap(sizeMin);
    }

    public MyScalableThreadPool(int fixedSize) {
        if (fixedSize <= 0) {
            throw new IllegalArgumentException("Incorrect input size");
        }
        this.sizeMin = fixedSize;
        this.sizeMax = fixedSize;
        fillPoolMap(fixedSize);
    }

    private void fillPoolMap(long size) {
        LongStream.range(0L, size)
                .forEach(i -> poolMap.put(i, new ThreadWorker("ThreadWorker " + i, i)));

        LongStream.range(size, sizeMax)
                .forEach(i -> {
                    poolMap.put(i, null);
                    freeIndexForNewWorker.add(i);
                });
    }

    @Override
    public void start() {
        LongStream.range(0L, sizeMin)
                .forEach(i -> poolMap.get(i).start());
    }

    @Override
    public void stopNow() {
        synchronized (this) {
            tasks.clear();
            poolMap.forEach((key, worker) -> {
                if (worker != null) {
                    worker.interrupt();
                }
            });
            poolMap.clear();
            freeIndexForNewWorker.clear();
        }
    }

    @Override
    public void execute(@NonNull Runnable runnable) {
        synchronized (tasks) {
            tasks.add(new FutureTask(runnable, true));
            tasks.notifyAll();
        }
    }

    @Override
    public <T> Future<T> execute(@NonNull Callable<T> callable) {
        synchronized (tasks) {
            FutureTask<T> futureTask = new FutureTask(callable);
            tasks.add(futureTask);
            tasks.notifyAll();
            return futureTask;
        }
    }

    /*
     *return true if needed to interrupt, otherwise return false
     *  */
    private boolean poolResize() {
        int taskSize = tasks.size();

        synchronized (poolMap) {
            int activeThreads = getActiveThreads();
            if (taskSize > activeThreads && taskSize > sizeMin && activeThreads < sizeMax) {

                if (!freeIndexForNewWorker.isEmpty()) {
                    long id = freeIndexForNewWorker.poll();

                    ThreadWorker worker = new ThreadWorker("ThreadWorker " + id, id);
                    poolMap.put(id, worker);
                    worker.start();
                }
            }
            return taskSize < activeThreads && activeThreads > sizeMin;
        }
    }

    private void findFreeIndex() {
        poolMap.forEach((key, worker) -> {
            if (worker == null || !worker.isInterrupted()) {
                if (!freeIndexForNewWorker.contains(key)) {
                    freeIndexForNewWorker.offer(key);
                }
            }
        });
    }

    public int getActiveThreads() {
        synchronized (poolMap) {
            findFreeIndex();
            int count = 0;
            for (Long i : poolMap.keySet()) {
                if (poolMap.get(i) != null) {
                    count++;
                }
            }
            return count;
        }
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
                synchronized (tasks) {
                    while (tasks.isEmpty()) {
                        try {
                            tasks.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }

                    if (!isInterrupted()) {
                        boolean needToInterrupt = poolResize();

                        if (needToInterrupt) {
                            System.out.println("ThreadWorker " + id + " interrupted");
                            Thread.currentThread().interrupt();
                            synchronized (poolMap) {
                                poolMap.put(Thread.currentThread().getId(), null);
                            }
                            return;
                        }

                        FutureTask task = tasks.poll();

                        if (task != null) {
                            task.run();
                            System.out.println("ThreadWorker " + id + " ended");
                        }
                    }
                }
            }
        }

        @Override
        public String toString() {
            return "ThreadWorker " + id;
        }
    }
}
