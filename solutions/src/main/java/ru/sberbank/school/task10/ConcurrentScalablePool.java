package ru.sberbank.school.task10;

import lombok.NonNull;
import ru.sberbank.school.util.Solution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

@Solution(12)
public class ConcurrentScalablePool implements ThreadPool {

    private final BlockingQueue<Runnable> tasks;
    private List<ThreadWorker> threads;
    private final List<ThreadWorker> freeThreads;
    private final ReentrantLock lock;
    private int min;
    private int max;
    private boolean[] nameThreads;

    public ConcurrentScalablePool(int min, int max) {
        if (max <= 0 || min <= 0) {
            throw new IllegalArgumentException("Максимальное и минимальное значения должны быть больше 0");
        }
        if (max < min) {
            throw new IllegalArgumentException("Максимальное значение должно быть не меньше минимального");
        }

        this.min = min;
        this.max = max;
        this.freeThreads = new ArrayList<>(min);
        tasks = new LinkedBlockingQueue<>(max);
        threads = new ArrayList<>(min);
        lock = new ReentrantLock();

        nameThreads = new boolean[max];
        Arrays.fill(nameThreads, false);
    }

    public int getThreadsCount() {
        return threads.size();
    }

    public int getTasksCount() {
        return tasks.size();
    }

    @Override
    public void start() {
        if (threads.size() >= min) {
            throw new IllegalStateException("Пул уже был запущен");
        }

        for (int i = 0; i < min; i++) {
            ThreadWorker t = new ThreadWorker("ThreadPoolWorker-" + i, i);
            threads.add(t);
            t.start();
            nameThreads[i] = true;
        }
    }

    @Override
    public void stopNow() {
        if (threads.isEmpty()) {
            throw new IllegalStateException("Пул не может быть остановлен, если он не был запущен");
        }

        tasks.clear();

        for (ThreadWorker thread : threads) {
            thread.interrupt();
        }

        threads = new ArrayList<>(min);

        freeThreads.clear();

        Arrays.fill(nameThreads, false);
    }

    @Override
    public void execute(@NonNull Runnable runnable) {

        if (threads.isEmpty()) {
            throw new IllegalStateException("Перед началом работы с пулом нужно вызвать start()");
        }

        try {
            if (freeThreads.size() == 0 && threads.size() < max) {
                addThread();
            } else if (tasks.isEmpty() && threads.size() > min) {
                deleteThreads();
            }

            tasks.put(runnable);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void deleteThreads() {

        lock.lock();
        try {
            while (threads.size() > min && freeThreads.size() > 0) {
                ThreadWorker thread = freeThreads.get(0);

                threads.remove(thread);
                freeThreads.remove(thread);

                nameThreads[thread.index] = false;
                thread.interrupt();
            }
        } finally {
            lock.unlock();
        }
    }

    private void addThread() {
        int index;

        for (index = 0; index < nameThreads.length; index++) {
            if (!nameThreads[index]) {
                nameThreads[index] = true;
                break;
            }
        }

        ThreadWorker t = new ThreadWorker("ThreadPoolWorker-" + index, index);
        threads.add(t);
        t.start();
    }


    private class ThreadWorker extends Thread {

        private int index;

        ThreadWorker(@NonNull String name, int index) {
            super(name);
            this.index = index;
        }

        @Override
        public void run() {
            Runnable r;

            while (!Thread.interrupted()) {

                try {
                    lock.lockInterruptibly();
                    try {
                        freeThreads.add(this);
                    } finally {
                        lock.unlock();
                    }

                    r = tasks.take();

                    lock.lockInterruptibly();
                    try {
                        freeThreads.remove(this);
                    } finally {
                        lock.unlock();
                    }

                    r.run();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

}
