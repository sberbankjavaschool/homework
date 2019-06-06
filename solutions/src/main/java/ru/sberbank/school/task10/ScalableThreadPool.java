package ru.sberbank.school.task10;

import lombok.NonNull;

public class ScalableThreadPool extends AbstractThreadPool {

    ScalableThreadPool(int min, int max) {
        super(min, max);
    }

    /**
     * Складывает задание в очередь. Освободившийся поток должен выполнить это задание.
     * Каждое задание должны быть выполнено ровно 1 раз
     *
     * @param runnable Задача для выполнения
     */
    @Override
    public void execute(@NonNull Runnable runnable) {
        if (workers.isEmpty()) {
            throw new IllegalStateException("нельзя добавить задачу в остановленный ThreadPool");
        }
        synchronized (tasks) {
            synchronized (workers) {
                if (countFreeWorkers.get() < tasks.size() && workers.size() < max) {
                    workers.add(new Worker("ThreadPoolWorker-" + (workers.size())));
                    workers.get(workers.size() - 1).start();
                    countFreeWorkers.incrementAndGet();
                }

                while (workers.size() > core && countFreeWorkers.get() > 1 && tasks.isEmpty()) {
                    stopWorker(workers.remove(workers.size() - 1));
                    countFreeWorkers.decrementAndGet();
                }
            }
            tasks.add(runnable);
            tasks.notify();
        }
    }
}
