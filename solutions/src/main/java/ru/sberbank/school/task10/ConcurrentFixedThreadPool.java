package ru.sberbank.school.task10;


import lombok.NonNull;
import ru.sberbank.school.util.Solution;
import java.util.concurrent.*;

@Solution(11)
public class ConcurrentFixedThreadPool implements ThreadPool {
    private Thread[] threadPool;
    private final BlockingQueue<FutureTask> tasks;
    private boolean started;


    public ConcurrentFixedThreadPool(int count) {
        if (count <= 0) {
            throw new IllegalArgumentException("Количестов потоков не может быть меньше одного");
        }

        this.threadPool = new Thread[count];
        this.tasks = new LinkedBlockingDeque<>();
        this.started = false;
    }

    @Override
    public void start() {
        if (started) {
            throw new IllegalStateException("ThreadPool уже запущен!");
        }

        for (int i = 0; i < threadPool.length; i++) {
            threadPool[i] = new Executor(i);
            threadPool[i].start();
        }

        started = true;
    }

    @Override
    public void stopNow() {
        if (!started) {
            throw new IllegalStateException("ThreadPool еще не был запущен!");
        }

        tasks.clear();

        for (int i = 0; i < threadPool.length; i++) {
            threadPool[i].interrupt();
            threadPool[i] = null;
        }

        started = false;
    }

    @Override
    public void execute(@NonNull Runnable runnable) {
        if (!started) {
            throw new IllegalStateException("ThreadPool должен быть запущен!");
        }

        try {
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
            tasks.put(ft);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return ft;
    }

    private class Executor extends Thread {
        Executor(int number) {
            super("ThreadPoolWorker-" + number);
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
