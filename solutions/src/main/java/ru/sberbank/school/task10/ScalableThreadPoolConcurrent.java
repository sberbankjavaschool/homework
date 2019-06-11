package ru.sberbank.school.task10;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class ScalableThreadPoolConcurrent implements ThreadPool {
    private final int maxCountThreads;
    private final int minCountThreads;
    private AtomicInteger freeThreads;
    private boolean[] threadNames;
    private final AtomicBoolean finish = new AtomicBoolean(false);
    private final CopyOnWriteArrayList<Thread> threads;
    private final LinkedBlockingQueue<Runnable> tasks;
    private ReentrantLock rLock;

    public ScalableThreadPoolConcurrent(int min, int max) {
        if (max < min || min <= 0) {
            throw new IllegalArgumentException();
        }
        maxCountThreads = max;
        minCountThreads = min;
        freeThreads = new AtomicInteger(min);
        threads = new CopyOnWriteArrayList();
        tasks = new LinkedBlockingQueue();
        rLock = new ReentrantLock();
        threadNames = new boolean[maxCountThreads];
        for (int i = 0; i < threadNames.length; i++) {
            threadNames[i] = false;
        }
    }

    /**
     * Запускает потоки, которыые бездействуют, до тех пор пока не появится новое задание в очереди (см. execute)
     */
    @Override
    public void start() {
        if (finish.get()) {
            throw new IllegalStateException("FixedTP is already started");
        }
        finish.set(true);
        for (int i = 0; i < minCountThreads; i++) {
            threads.add(new ScalableThreadPoolConcurrent.ThreadWorker(i));
            threads.get(i).start();
            threadNames[i] = true;
        }
    }

    /**
     * Отменяет запланированные задачи, останавливает запущенные задачи, останавливает потоки,
     * переходит в начальное состояние.
     */
    @Override
    public void stopNow() {
        if (!finish.get()) {
            throw new IllegalStateException("FixedTP is already stopped or still isn't started");
        }
        rLock.lock();
        try {
            tasks.clear();
            finish.set(false);
        } finally {
            rLock.unlock();
        }

        for (int i = 0; i < threadNames.length; i++) {
            threadNames[i] = false;
        }
        threads.forEach(Thread::interrupt);
        freeThreads.set(minCountThreads);
    }

    public boolean checkScalableThreads() throws IllegalStateException {
        for (Thread t : threads) {
            Thread.State currState = t.getState();
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
        if (!finish.get()) {
            throw new IllegalStateException("Work is already finished");
        }
        try {
            if ((threads.size() < maxCountThreads) && (freeThreads.get() <= 0)) {
                addThread();
            } else if ((threads.size() > minCountThreads) && tasks.isEmpty()) {
                deleteThread();
            }
            freeThreads.getAndDecrement();
            tasks.put(runnable);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private void addThread() {
        rLock.lock();
        try {
            int index = 0;
            for (int i = 0; i < threadNames.length; i++) {
                if (!threadNames[i]) {
                    index = i;
                    threadNames[i] = true;
                    break;
                }
            }
            ThreadWorker t = new ThreadWorker(index);
            if (index > maxCountThreads) {
                System.out.println("Ooops");
            }
            threads.add(t);
            t.start();
        } finally {
            rLock.unlock();
        }

    }

    private void deleteThread() {
        rLock.lock();
        try {
            int index = threads.size() - 1;
            while (threads.size() > minCountThreads) {
                Thread t = threads.remove(index);
                t.interrupt();
                threadNames[index] = false;
                index--;
            }
            freeThreads.set(minCountThreads + 1);
        } finally {
            rLock.unlock();
        }

    }

    public CopyOnWriteArrayList<Thread> getThreads() {
        return threads;
    }

    private class ThreadWorker extends Thread {
        String name;

        ThreadWorker(int i) {
            name = "ThreadPoolWorker-" + i + " ";
        }

        public void run() {
            Runnable task;
            while (!Thread.interrupted()) {
                try {
                    task = tasks.take();
                    System.out.println(Thread.currentThread().getName() + " is running");

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                try {
                    task.run();
                } catch (RuntimeException ex) {
                    ex.printStackTrace();
                }
            }

        }

    }
}
