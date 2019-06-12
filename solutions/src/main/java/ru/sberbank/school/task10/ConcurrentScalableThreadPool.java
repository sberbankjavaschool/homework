package ru.sberbank.school.task10;

import lombok.NonNull;
import ru.sberbank.school.task10.util.IdsManager;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentScalableThreadPool implements ThreadPool {
    private final BlockingQueue<Runnable> taskQueue;

    private final int corePoolSize;
    private final int maximumPoolSize;
    private volatile AtomicInteger freeThreadsCount = new AtomicInteger(0);
    private final ConcurrentHashMap<Integer ,ThreadWorker> threads;
    private final IdsManager idsManager;

    public ConcurrentScalableThreadPool(int corePoolSize, int maximumPoolSize) {
        if (corePoolSize <= 0 || maximumPoolSize <= 0 || corePoolSize > maximumPoolSize) {
            throw new IllegalArgumentException();
        }
        this.taskQueue = new LinkedBlockingQueue<>();
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.idsManager = new IdsManager(maximumPoolSize + 1);
        this.threads = new ConcurrentHashMap<>(corePoolSize);
    }

    @Override
    public void start() {
        synchronized (threads) {
            if (!threads.isEmpty()) {
                throw new IllegalStateException("Пул уже запущен!");
            }
            for (int i = 0; i < corePoolSize; i++) {
                int id = idsManager.getId();
                ThreadWorker threadWorker = new ThreadWorker(id, null);
                threads.put(id, threadWorker);
                threadWorker.start();
            }
        }
    }

    @Override
    public void stopNow() {
        synchronized (threads) {
            if (threads.isEmpty()) {
                throw new IllegalStateException("Пул еще не запущен!");
            }
            taskQueue.clear();
            for (ThreadWorker t : threads.values()) {
                t.interrupt();
            }
            threads.clear();
        }
        idsManager.reset();
        freeThreadsCount.set(0);
    }

    @Override
    public void execute(@NonNull Runnable runnable) {
        if (threads.isEmpty()) {
            throw new IllegalStateException("Пул еще не запущен!");
        }

        if (freeThreadsCount.get() == 0 && threads.size() <= maximumPoolSize) {
            int id = idsManager.getId();
            ThreadWorker threadWorker = new ThreadWorker(id, runnable);
            threads.put(id, threadWorker);
            threadWorker.start();
            return;
        }

        taskQueue.add(runnable);
    }

    @Override
    public <T> Future<T> execute(Callable<T> callable) {
        Future<T> result = new FutureTask<>(callable);
        execute((Runnable) result);
        return result;
    }

    public int getWorkingThreadsCount() {
        return threads.size();
    }

    public void deleteThread(int id) {
        threads.remove(id);
        idsManager.addFreeId(id);
    }


    class ThreadWorker extends Thread {

        private Runnable task;
        private int id;

        public ThreadWorker(int id, Runnable task) {
            this.id = id;
            this.task = task;
            this.setName("ThreadPoolWorker-" + id);
        }

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                if (task != null) {
                    freeThreadsCount.getAndDecrement();
                    task.run();
                    task = null;
                } else {
                    freeThreadsCount.getAndIncrement();
                    if (taskQueue.isEmpty()) {
                        if (getWorkingThreadsCount() > corePoolSize) {
                            freeThreadsCount.getAndDecrement();
                            deleteThread(id);
                            Thread.currentThread().interrupt();
                            return;
                        } else {
                            try {
                                task = taskQueue.take();
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                return;
                            }
                        }
                    } else {
                        task = taskQueue.poll();
                    }
                }
            }
        }
    }
}
