package ru.sberbank.school.task10;

import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class ConcurrentAbstractThreadPool implements ThreadPool {

    protected final ArrayList<Worker> workers;
    protected final BlockingQueue<Runnable> tasks = new LinkedBlockingQueue<>();
    protected final int core;
    protected final int max;
    protected final boolean[] indexes;
    protected volatile AtomicInteger countFreeWorkers;

    protected ConcurrentAbstractThreadPool(int core, int max) {
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
        indexes = new boolean[max];
    }

    /**
     * Запускает потоки, которыые бездействуют, до тех пор пока не появится новое задание в очереди (см. execute)
     */
    @Override
    public void start() {
        synchronized (workers) {
            if (!workers.isEmpty()) {
                throw new IllegalStateException("ThreadPool уже был запущен");
            }
            for (int i = 0; i < core; i++) {
                createWorker();
            }
        }
    }

    protected void createWorker() {
        synchronized (indexes) {
            int index = getFreeIndex();
            if (index >= 0) {
                workers.add(new Worker(index));
                workers.get(workers.size() - 1).start();
                countFreeWorkers.incrementAndGet();
                indexes[index] = true;
            }
        }
    }

    private int getFreeIndex() {
        for (int i = 0; i < indexes.length; i++) {
            if (!indexes[i]) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Отменяет запланированные задачи, останавливает запущенные задачи,
     * останавливает потоки, переходит в начальное состояние.
     */
    @Override
    public void stopNow() {
        synchronized (workers) {
            if (workers.isEmpty()) {
                throw new IllegalStateException("ThreadPool уже был остановлен");
            }
            for (int i = workers.size() - 1; i >= 0; i--) {
                removeWorker(workers.get(i));
            }
        }
        tasks.clear();
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

    private void removeWorker(Worker worker) {
        worker.interrupt();
        worker.setStopFlagTrue();
        indexes[worker.getIndex()] = false;
        workers.remove(worker);
        countFreeWorkers.decrementAndGet();
    }

    private void removeFreeWorkers() {
        for (int i = workers.size() - 1; i >= 0 && workers.size() > core && tasks.isEmpty(); i--) {
            if (!workers.get(i).isWorking()) {
                removeWorker(workers.get(i));
            }
        }
    }

    protected class Worker extends Thread {

        //флаг защищает, когда после InterruptedException пользователь не пробросил прерывание дальше.
        boolean stopFlag;

        //Помогает определить, занят ли поток задачей
        boolean working;

        int index;

        Worker(int index) {
            this.setName("ThreadPoolWorker-" + index);
            this.index = index;
            stopFlag = false;
        }

        public boolean isWorking() {
            return working;
        }

        int getIndex() {
            return index;
        }

        void setStopFlagTrue() {
            stopFlag = true;
        }

        @Override
        public void run() {
            Runnable task;
            while (!isInterrupted() && !stopFlag) {
                try {
                    task = tasks.take();
                } catch (InterruptedException e) {
                    return;
                }
                working = true;
                countFreeWorkers.decrementAndGet();
                try {
                    task.run();
                } catch (Exception ignore) {
                    //log
                }
                countFreeWorkers.incrementAndGet();
                working = false;
                synchronized (workers) {
                    removeFreeWorkers();
                }
            }
        }
    }
}
