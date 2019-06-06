package ru.sberbank.school.task10;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractThreadPool implements ThreadPool {

    protected final ArrayList<Worker> workers;
    protected final Queue<Runnable> tasks = new LinkedList<>();
    protected final int core;
    protected final int max;
    protected volatile AtomicInteger countFreeWorkers;


    protected AbstractThreadPool(int core, int max) {
        if (core < 1) {
            throw new IllegalArgumentException("минимальное количество потоков не может быть меньше 1");
        }
        if (max < core) {
            throw new IllegalArgumentException("максимальное количество потоков не может быть меньше минимального");
        }
        workers = new ArrayList<>(max);
        this.core = core;
        this.max = max;
        countFreeWorkers = new AtomicInteger(0);
    }

    /**
     * Запускает потоки, которыые бездействуют, до тех пор пока не появится новое задание в очереди (см. execute)
     */
    @Override
    public void start() {
        if (!workers.isEmpty()) {
            throw new IllegalStateException("ThreadPool уже был запущен");
        }
        for (int i = 0; i < core; i++) {
            workers.add(new Worker("ThreadPoolWorker-" + (i + 1)));
            workers.get(i).start();
            countFreeWorkers.incrementAndGet();
        }
    }

    /**
     * Отменяет запланированные задачи, останавливает запущенные задачи,
     * останавливает потоки, переходит в начальное состояние.
     */
    @Override
    public void stopNow() {
        if (workers.isEmpty()) {
            throw new IllegalStateException("ThreadPool уже был остановлен");
        }
        synchronized (tasks) {
            tasks.clear();
        }
        for (Worker worker : workers) {
            stopWorker(worker);
        }
        workers.clear();
        countFreeWorkers.set(0);
    }

    /**
     * Складывает задание в очередь. Освободившийся поток должен выполнить это задание.
     * Каждое задание должны быть выполнено ровно 1 раз
     *
     * @param runnable Задача для выполнения
     */
    @Override
    public abstract void execute(Runnable runnable);

    @Override
    public <T> Future<T> execute(Callable<T> callable) {
        Future<T> result = new FutureTask<>(callable);
        execute((Runnable) result);
        return result;
    }

    protected void stopWorker(Worker worker) {
        worker.interrupt();
        worker.setStopFlag(true);
    }

    protected class Worker extends Thread {

        //флаг защищает, когда после InterruptedException пользователь не пробросил прерывание дальше.
        boolean stopFlag;

        protected Worker(String name) {
            this.setName(name);
            stopFlag = false;
        }

        void setStopFlag(boolean flag) {
            stopFlag = flag;
        }

        @Override
        public void run() {
            Runnable task;
            while (!isInterrupted() && !stopFlag) {
                synchronized (tasks) {
                    while (tasks.isEmpty()) {
                        try {
                            tasks.wait();
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                    task = tasks.remove();
                    countFreeWorkers.decrementAndGet();
                }
                try {
                    task.run();
                } catch (Exception ignore) {
                    //log
                }
                countFreeWorkers.incrementAndGet();
            }
        }
    }
}
