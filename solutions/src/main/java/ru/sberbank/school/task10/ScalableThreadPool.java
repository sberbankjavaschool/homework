package ru.sberbank.school.task10;

import lombok.NonNull;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * 04.06.2019
 * Реализация пула потоков. В конструкторе задается минимальное и максимальное(int min, int max) число потоков,
 * количество запущенных потоков может быть увеличено от минимального к максимальному.
 *
 * @author Gregory Melnikov
 */

public class ScalableThreadPool implements ThreadPool {

    private final int minPoolSize;
    private final int maxPoolSize;

    private volatile List<Thread> threads = new LinkedList<>();
    private volatile Queue<FutureTask> futures = new LinkedList<>();

    public ScalableThreadPool(@NonNull int minPoolSize, @NonNull int maxPoolSize) {
        if (minPoolSize < 0 || maxPoolSize < 0 || minPoolSize > maxPoolSize) {
            throw new IllegalArgumentException("MaxPoolSize can't be less than minPoolSize, both can't be negative");
        }
        this.minPoolSize = minPoolSize;
        this.maxPoolSize = maxPoolSize;
    }

    @Override
    public void start() {
        Runnable runnable = new InternalPoolService();
        Thread serviceThread = new Thread(runnable, "serviceDaemon");
        serviceThread.setDaemon(true);
        serviceThread.start();
        threads.add(serviceThread);
    }

    @Override
    public void stopNow() {
        synchronized (threads) {
            for (Thread thread : threads) {
                thread.interrupt();
            }
        }
        threads.clear();

        for (FutureTask future : futures) {
            future.cancel(true);
        }
        futures.clear();
    }

    @Override
    public void execute(Runnable runnable) {
        futures.add(new FutureTask<Void>(runnable, null));
    }

    @Override
    public <T> Future<T> execute(Callable<T> callable) {
        FutureTask<T> future = new FutureTask<>(callable);
        futures.add(future);
        return future;
    }

    private class InternalPoolService implements Runnable {
        private final String threadName = "ThreadPoolWorker-";
        private int threadCount;

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    synchronized (threads) {
                        if (!Thread.currentThread().isInterrupted()) {
                            buildThreads();
                            garbageThreads();
                        }
                    }
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void buildThreads() {
            if (threads.size() <= maxPoolSize && futures.size() > 0) {
                Thread thread = new Thread(futures.poll(), threadName + ++threadCount);
                thread.start();
                threads.add(thread);
            }
        }

        private void garbageThreads() {
            Thread checkedThread;
            Iterator<Thread> iterator = threads.iterator();
            while (iterator.hasNext()) {
                checkedThread = iterator.next();
                if (!checkedThread.isAlive()) {
                    iterator.remove();
                    threadCount--;
                }
            }
        }
    }
}