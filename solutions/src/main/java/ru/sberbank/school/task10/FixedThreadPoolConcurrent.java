package ru.sberbank.school.task10;

import lombok.NonNull;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

//FixedThreadPool - Количество потоков задается в конструкторе и не меняется.
// (Должен быть конструктор с одним параметром типа int)
public class FixedThreadPoolConcurrent implements ThreadPool {
    private int countOfThreads;
    private final AtomicBoolean finish = new AtomicBoolean(false);
    private final CopyOnWriteArrayList<Thread> threads;
    private final ConcurrentLinkedQueue<Runnable> tasks;
//    private ReadWriteLock rwl;
//    private Lock readLock;
//    private Lock writeLock;


    public FixedThreadPoolConcurrent(@NonNull int countOfThreads) {
        if (countOfThreads <= 0) {
            throw new IllegalArgumentException();
        }
        this.countOfThreads = countOfThreads;
        threads = new CopyOnWriteArrayList<>();
        tasks = new ConcurrentLinkedQueue<>();
//        rwl = new ReentrantReadWriteLock();
//        readLock = rwl.readLock();
//        writeLock = rwl.writeLock();
    }

    /**
     * Запускает потоки, которыые бездействуют, до тех пор пока не появится новое задание в очереди (см. execute)
     */
    @Override
    public void start() {
        if (!finish.compareAndSet(false, true)) {
            throw new IllegalStateException("FixedTP is already started");
        }
        for (int i = 0; i < countOfThreads; i++) {
            threads.add(new ThreadWorker(i));
            threads.get(i).start();
        }
    }

    /**
     * Отменяет запланированные задачи, останавливает запущенные задачи,
     * останавливает потоки, переходит в начальное состояние.
     */
    @Override
    public void stopNow() {
        if (!finish.compareAndSet(true, false)) {
            throw new IllegalStateException("FixedTP is already stopped or still isn't started");
        }
        synchronized (tasks) {
            tasks.clear();
        }
        threads.forEach(Thread::interrupt);
    }

    /**
     * Складывает задание в очередь. Освободившийся поток должен выполнить это задание.
     * Каждое задание должны быть выполнено ровно 1 раз
     *
     * @param runnable Задача для выполнения
     */
    @Override
    public void execute(@NonNull Runnable runnable) {
        if (!finish.compareAndSet(true, true)) {
            throw new IllegalStateException("Work is already finished or isn't started");
        }
        synchronized (tasks) {
            tasks.add(runnable);
            tasks.notify();
        }

    }

    public int getThreadsSize() {
        return threads.size();
    }

    private class ThreadWorker extends Thread {
        private String name;

        ThreadWorker(int i) {
            name = "ThreadPoolWorker-" + i;
        }

        public void run() {
            Runnable task;
            while (!Thread.interrupted()) {
                task = tasks.poll();
                if (task == null) {
                    Thread.currentThread().interrupt();
                    return;
                }
                System.out.println(Thread.currentThread().getName() + " is running");
                try {
                    task.run();
                } catch (RuntimeException ex) {
                    ex.printStackTrace();
                }
            }


//            while (!Thread.interrupted()) {
//                try {
//                    readLock.lockInterruptibly();
//                    try {
//                        task = tasks.poll();
//                        if (task == null) {
//                            Thread.currentThread().interrupt();
//                            return;
//                        }
//                        System.out.println(Thread.currentThread().getName() + " is running");
//                    } finally {
//                        readLock.unlock();
//                    }
//                } catch (InterruptedException e) {
//                    return;
//                }
//                try {
//                    task.run();
//                } catch (RuntimeException ex) {
//                    ex.printStackTrace();
//                }
//            }

        }

    }


}



