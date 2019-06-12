package ru.sberbank.school.task10;

import ru.sberbank.school.task10.ThreadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentScalableThreadPool implements ThreadPool {

    private int min;
    private int max;
    private int countWorker;
    private boolean[] idWorkers;
    private List<Thread> threads;
    private final BlockingQueue<FutureTask<?>> tasks;
    private final ReentrantLock lock;

    public ConcurrentScalableThreadPool(int min, int max) {
        if (min <= 0) {
            throw new IllegalArgumentException("Parameter min must be greater than 0!");
        }
        if (max < min) {
            throw new IllegalArgumentException("Parameter max must be greater than min!");
        }

        this.min = min;
        this.max = max;
        countWorker = 0;
        idWorkers = new boolean[max];
        threads = new ArrayList<>();
        tasks = new LinkedBlockingQueue<>();
        lock = new ReentrantLock();
    }

    public int getCountWorker() {
        return countWorker;
    }

    public int getThreadSize() {
        return threads.size();
    }

    private void pr(String s) {
        System.out.println("countWorker=" + countWorker + " threads=" + threads.size() + " " + s);
    }

    @Override
    public void start() {
        if (!threads.isEmpty()) {
            throw new IllegalStateException("ThreadPool started!");
        }

        for (int i = 0; i < min; i++) {
            Thread thread = new ThreadPoolWorker("ThreadPoolWorker-" + i);
            idWorkers[i] = true;
            threads.add(thread);
            thread.start();
        }
    }

    @Override
    public void stopNow() {
        lock.lock();
        try {
            for (Thread thread : threads) {
                thread.interrupt();
            }
        } finally {
            lock.unlock();
        }
        threads.clear();
        tasks.clear();
        countWorker = 0;
    }

    @Override
    public void execute(Runnable runnable) {
        if (threads.isEmpty()) {
            throw new IllegalStateException("ThreadPool stopped!");
        }
        try {
            tasks.put(new FutureTask<>(runnable, null));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        addThread();
    }

    @Override
    public <T> Future<T> execute(Callable<T> callable) {
        if (threads.isEmpty()) {
            throw new IllegalStateException("ThreadPool stopped!");
        }

        FutureTask<T> futureTask = new FutureTask<>(callable);
        try {
            tasks.put(futureTask);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        addThread();
        return futureTask;
    }

    private void addThread() {
        lock.lock();
        try {
            if (countWorker + tasks.size() > threads.size() && threads.size() < max) {
                int i = 0;
                Thread thread;
                while (idWorkers[i]) {
                    i++;
                }

                thread = new ThreadPoolWorker("ThreadPoolWorker-" + i);
                idWorkers[i] = true;
                threads.add(thread);
                pr("add thread");
                thread.start();
            }
        } finally {
            lock.unlock();
        }
    }

    private boolean deleteThread() {
        lock.lock();
        try {
            if (tasks.isEmpty()) {
                pr("empty");
                if (threads.size() > min) {
                    idWorkers[getCurrentIndexThread()] = false;
                    pr("idWorkers=false");
                    threads.remove(Thread.currentThread());
                    pr("remove this");
                    return true;
                }
            }
        } finally {
            lock.unlock();
        }
        return false;
    }

    private int getCurrentIndexThread() {
        String name = Thread.currentThread().getName();
        int index = name.lastIndexOf("-");
        return Integer.valueOf(name.substring(index + 1));
    }

    public class ThreadPoolWorker extends Thread {

        public ThreadPoolWorker(String name) {
            super(name);
        }

        private void pr(String s) {
            System.out.println(getName() + " id=" + this.getId() + " countWorker=" + countWorker
                    + " thread=" + threads.size() + " " + s);
        }

        @Override
        public void run() {
            pr("start run");
            Runnable task;
            while (!isInterrupted()) {
                try {
                    //после выполнения задачи тред будет удален, если список задач пуст и кол-во тредов > min
                    if (deleteThread()) {
                        return;
                    }

                    task = tasks.take();

                    lock.lockInterruptibly();
                    try {
                        countWorker++;
                    } finally {
                        lock.unlock();
                    }

                    task.run();

                    lock.lockInterruptibly();
                    try {
                        countWorker--;
                    } finally {
                        lock.unlock();
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }
            pr("finish run");
        }
    }
}
