package ru.sberbank.school.task10;

import lombok.NonNull;

public class ConcurrentScalableThreadPool extends ConcurrentAbstractThreadPool {

    ConcurrentScalableThreadPool(int min, int max) {
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
        synchronized (workers) {
            if (countFreeWorkers.get() < tasks.size() && workers.size() < max) {
                createWorker();
            }
        }
        tasks.add(runnable);
    }
}
