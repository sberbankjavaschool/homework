package ru.sberbank.school.task10;

import lombok.NonNull;
import ru.sberbank.school.task10.util.IdsManager;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class ScalableThreadPool implements ThreadPool {
    private final Queue<Runnable> taskQueue;

    private final int corePoolSize;
    private final int maximumPoolSize;
    private volatile int freeThreadsCount = 0;
    private volatile int workingThreadsCount = 0;
    private final List<ThreadWorker> threads;
    private final IdsManager idsManager;
    private final ThreadCollector freeThreadCollector = new ThreadCollector();
    private final Object mainLock = new Object();
    private final Object freeThreadLock = new Object();
    private ThreadWorker freeThread;

    public ScalableThreadPool(int corePoolSize, int maximumPoolSize) {
        if (corePoolSize <= 0 || maximumPoolSize <= 0 || corePoolSize > maximumPoolSize) {
            throw new IllegalArgumentException();
        }
        this.taskQueue = new LinkedList<>();
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.idsManager = new IdsManager(maximumPoolSize + 1);
        this.threads = new ArrayList<>(corePoolSize);
    }

    @Override
    public void start() {
        synchronized (mainLock) {
            synchronized (threads) {
                if (!threads.isEmpty()) {
                    throw new IllegalStateException("Пул уже запущен!");
                }
                for (int i = 0; i < corePoolSize; i++) {
                    ThreadWorker threadWorker = new ThreadWorker(null);
                    threads.add(threadWorker);
                    threadWorker.start();
                }
                freeThreadCollector.start();
                workingThreadsCount = corePoolSize;
            }
        }
    }

    @Override
    public void stopNow() {
        synchronized (mainLock) {
            synchronized (threads) {
                if (threads.isEmpty()) {
                    throw new IllegalStateException("Пул еще не запущен!");
                }
                taskQueue.clear();
                for (ThreadWorker t : threads) {
                    t.interrupt();
                }
                threads.clear();
            }
            idsManager.reset();
            freeThreadsCount = 0;
            freeThreadCollector.interrupt();
        }
    }

    @Override
    public void execute(@NonNull Runnable runnable) {
        synchronized (mainLock) {
            synchronized (threads) {
                if (threads.isEmpty()) {
                    throw new IllegalStateException("Пул еще не запущен!");
                }
            }

            if (freeThreadsCount == 0 && threads.size() <= maximumPoolSize) {
                ThreadWorker threadWorker = new ThreadWorker(runnable);
                threads.add(threadWorker);
                workingThreadsCount++;
                threadWorker.start();
                return;
            }

            synchronized (taskQueue) {
                taskQueue.add(runnable);
                taskQueue.notify();
            }
        }
    }

    @Override
    public <T> Future<T> execute(Callable<T> callable) {
        Future<T> result = new FutureTask<>(callable);
        execute((Runnable) result);
        return result;
    }

    public int getWorkingThreadsCount() {
        return workingThreadsCount;
    }


    class ThreadWorker extends Thread {

        private Runnable task;
        private int id;

        public int getThreadId() {
            return id;
        }

        public ThreadWorker(Runnable task) {
            this.id = idsManager.getId();
            this.task = task;
            this.setName("ThreadPoolWorker-" + this.id);
        }

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                if (task != null) {
                    task.run();
                    task = null;
                } else {
                    synchronized (taskQueue) {
                        if (taskQueue.isEmpty()) {
                            if (workingThreadsCount > corePoolSize) {
                                synchronized (freeThreadLock) {
                                    workingThreadsCount--;
                                    freeThread = this;
                                    freeThreadLock.notify();
                                }
                                Thread.currentThread().interrupt();
                                return;
                            } else {
                                freeThreadsCount++;
                                try {
                                    taskQueue.wait();
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                    return;
                                }
                            }
                        }
                        freeThreadsCount--;
                        task = taskQueue.poll();
                    }
                }
            }
        }
    }

    private class ThreadCollector extends Thread {
        @Override
        public void run() {
            while (!Thread.interrupted()) {
                synchronized (freeThreadLock) {
                    try {
                        freeThreadLock.wait();
                        idsManager.addFreeId(freeThread.getThreadId());
                        synchronized (threads) {
                            threads.remove(freeThread);
                        }
                        System.out.println("delete Thread - " + freeThread.getThreadId());
                        freeThread = null;
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }
    }
}
