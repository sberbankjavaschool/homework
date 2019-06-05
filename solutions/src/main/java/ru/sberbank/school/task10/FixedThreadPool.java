package ru.sberbank.school.task10;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.LinkedList;

//FixedThreadPool - Количество потоков задается в конструкторе и не меняется.
// (Должен быть конструктор с одним параметром типа int)
public class FixedThreadPool implements ThreadPool {
    private int countOfThreads;
    private volatile boolean finish;
    private ArrayList<Thread> threads;
    private LinkedList<Runnable> tasks;

    public FixedThreadPool(@NonNull int countOfThreads) {
        if (countOfThreads <= 0) {
            throw new IllegalArgumentException();
        }
        this.countOfThreads = countOfThreads;
        threads = new ArrayList<>(countOfThreads);
        tasks = new LinkedList<>();
    }

    /**
     * Запускает потоки, которыые бездействуют, до тех пор пока не появится новое задание в очереди (см. execute)
     */
    @Override
    public void start() {
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
        synchronized (tasks) {
            tasks.clear();
            finish = true;
        }
        synchronized (threads) {
            for (Thread t : threads) {
                if (!t.isInterrupted()) {
                    t.interrupt();
                }
            }
        }

    }

    /**
     * Складывает задание в очередь. Освободившийся поток должен выполнить это задание.
     * Каждое задание должны быть выполнено ровно 1 раз
     *
     * @param runnable Задача для выполнения
     */
    @Override
    public void execute(@NonNull Runnable runnable) {
        if (finish) {
            throw new RuntimeException("Work is already finished");
        }
        synchronized (tasks) {
            tasks.addLast(runnable);
            tasks.notify();
        }
    }

    public boolean checkThreads() throws IllegalStateException {
        for (Thread t : threads) {
            Thread.State currState = t.getState();
            //System.out.println(currState);
            if (currState == Thread.State.RUNNABLE)  {
                return false;
            }
        }
        return true;
    }

    private class ThreadWorker extends Thread {
        private String name;

        ThreadWorker(int i) {
            name = "ThreadPoolWorker-" + i;
        }

        public void run() {
            Runnable task;
            while (!Thread.interrupted()) {
                synchronized (tasks) {
                    while (tasks.isEmpty()) {
                        try {
                            tasks.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    System.out.println(Thread.currentThread().getName() + " is running");
                    task = tasks.removeFirst();
                }
                try {
                    task.run();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                }

            }

        }

    }
}



