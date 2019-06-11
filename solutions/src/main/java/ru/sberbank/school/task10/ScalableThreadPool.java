package ru.sberbank.school.task10;

import java.util.ArrayList;
import java.util.LinkedList;

//ScalableThreadPool в конструкторе задается минимальное и максимальное(int min, int max) число потоков,
//        количество запущенных потоков может быть увеличено от минимального к максимальному,
//        если при добавлении нового задания в очередь нет свободного потока для исполнения этого задания.
//        При отсутствии задания в очереди, количество потоков опять должно быть уменьшено до значения min.
//        (Должен быть конструктор с двумя параметрами типа int)
public class ScalableThreadPool implements ThreadPool {
    private int maxCountThreads;
    private int minCountThreads;
    private volatile boolean finish;
    private volatile int freeThreads;
    private ArrayList<Thread> threads;
    private LinkedList<Runnable> tasks;

    public ScalableThreadPool(int min, int max) {
        if (max < min || min <= 0) {
            throw new IllegalArgumentException();
        }
        maxCountThreads = max;
        minCountThreads = min;
        freeThreads = min;
        threads = new ArrayList<>(minCountThreads);
        tasks = new LinkedList<>();
    }

    /**
     * Запускает потоки, которыые бездействуют, до тех пор пока не появится новое задание в очереди (см. execute)
     */
    @Override
    public void start() {
        for (int i = 0; i < minCountThreads; i++) {
            threads.add(new ThreadWorker(i, ""));
            threads.get(i).start();
        }
    }

    /**
     * Отменяет запланированные задачи, останавливает запущенные задачи, останавливает потоки,
     * переходит в начальное состояние.
     */
    @Override
    public void stopNow() {
        synchronized (tasks) {
            tasks.clear();
            finish = true;
        }
        for (Thread t : threads) {
            if (!t.isInterrupted()) {
                t.interrupt();
            }
        }
        freeThreads = minCountThreads;
    }

    public boolean checkScalableThreads() throws IllegalStateException {
        for (Thread t : threads) {
            Thread.State currState = t.getState();
            //System.out.println(currState);
            if (currState == Thread.State.RUNNABLE) {
                return false;
            }
        }
        return true;
    }

    /**
     * Складывает задание в очередь. Освободившийся поток должен выполнить это задание.
     * Каждое задание должны быть выполнено ровно 1 раз
     *
     * @param runnable Задача для выполнения
     */
    @Override
    public void execute(Runnable runnable) {
        if (finish) {
            throw new RuntimeException("Work is already finished");
        }
        synchronized (tasks) {
            if ((threads.size() < maxCountThreads) && (freeThreads <= 0)) {
                if (threads.get(threads.size() - 1).getName() == ("ThreadPoolWorker-" + maxCountThreads)) {
                    threads.add(new ThreadWorker(threads.size(), "m"));
                } else {
                    threads.add(new ThreadWorker(threads.size(), ""));
                }

                threads.get(threads.size() - 1).start();
            } else if ((threads.size() > minCountThreads) && tasks.isEmpty()) {
                while (threads.size() > minCountThreads) {
                    threads.remove(0);
                }
                freeThreads = minCountThreads + 1;
            }
            freeThreads--;
//            if (freeThreads > 0) {
//                freeThreads--;
//            }
            tasks.addLast(runnable);
            tasks.notify();
        }
    }


    private class ThreadWorker extends Thread {
        String name;

        ThreadWorker(int i, String letter) {
            name = "ThreadPoolWorker-" + letter + i;
        }

        public void run() {
            Runnable task;
            while (!Thread.interrupted()) {
                synchronized (tasks) {
                    while (tasks.isEmpty()) {
                        try {
                            tasks.wait();
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                    System.out.println(Thread.currentThread().getName() + " is running..");
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
