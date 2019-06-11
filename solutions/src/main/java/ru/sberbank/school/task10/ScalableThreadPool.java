package ru.sberbank.school.task10;

import lombok.NonNull;
import ru.sberbank.school.util.Solution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Solution(10)
public class ScalableThreadPool implements ThreadPool {
    private final LinkedList<Runnable> tasks;
    private List<ThreadWorker> threads;
    private final List<ThreadWorker> freeThreads;
    private int min;
    private int max;
    private boolean[] nameThreads;

    public ScalableThreadPool(int min, int max) {
        if (max <= 0 || min <= 0) {
            throw new IllegalArgumentException("Максимальное и минимальное значения должны быть больше 0");
        }
        if (max < min) {
            throw new IllegalArgumentException("Максимальное значение должно быть не меньше минимального");
        }

        this.min = min;
        this.max = max;
        this.freeThreads = new ArrayList<>(min);
        tasks = new LinkedList<>();
        threads = new ArrayList<>(min);

        nameThreads = new boolean[max];
        Arrays.fill(nameThreads, false);
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
        synchronized (tasks) {
            tasks.clear();
        }

        for (ThreadWorker thread : threads) {
            thread.interrupt();
        }

        threads = new ArrayList<>(min);

        synchronized (freeThreads) {
            freeThreads.clear();
        }

        Arrays.fill(nameThreads, false);
    }

    @Override
    public void execute(@NonNull Runnable runnable) {

        if (threads.isEmpty()) {
            throw new IllegalStateException("Перед началом работы с пулом нужно вызвать start()");
        }

        synchronized (tasks) {
            synchronized (freeThreads) {
                if (freeThreads.size() == 0 && threads.size() < max) {
                    addThread();
                } else if (tasks.isEmpty() && threads.size() > min) {
                    deleteThreads();
                }
            }

            tasks.addLast(runnable);
            tasks.notify();
        }
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

                synchronized (freeThreads) {
                    freeThreads.add(this);
                }

                synchronized (tasks) {
                    while (tasks.isEmpty()) {
                        try {
                            tasks.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    r = tasks.removeFirst();
                }

                synchronized (freeThreads) {
                    freeThreads.remove(this);
                }

                try {
                    r.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void deleteThreads() {

        synchronized (freeThreads) {
            while (threads.size() > min && freeThreads.size() > 0) {
                ThreadWorker thread = freeThreads.get(0);

                threads.remove(thread);
                freeThreads.remove(thread);

                nameThreads[thread.index] = false;
                thread.interrupt();
                System.out.println("Deleted " + thread.getName());
            }
        }

    }

    private void addThread() {
        System.out.println("Add thread");
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

}
