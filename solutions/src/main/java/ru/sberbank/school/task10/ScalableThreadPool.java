package ru.sberbank.school.task10;

import lombok.NonNull;
import ru.sberbank.school.util.Solution;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * Реализация пула потоков, в которой треды переиспользуются.
 * В конструкторе задается минимальное и максимальное(int min, int max) число потоков,
 * количество запущенных потоков может быть увеличено от минимального к максимальному.
 *
 * @author Gregory Melnikov
 */

@Solution(10)
public class ScalableThreadPool implements ThreadPool {

    private final int minPoolSize;
    private final int maxPoolSize;
    private final String threadName = "ThreadPoolWorker-";

    private volatile Queue<FutureTask> futures = new LinkedList<>();
    private volatile List<Thread> threads = new LinkedList<>();
    private volatile Queue<String> threadNames = new LinkedList<>();

    public ScalableThreadPool(@NonNull int minPoolSize, @NonNull int maxPoolSize) {
        if (minPoolSize < 0 || maxPoolSize < 0 || minPoolSize > maxPoolSize) {
            throw new IllegalArgumentException("MaxPoolSize can't be less than minPoolSize, both can't be negative");
        }
        this.minPoolSize = minPoolSize;
        this.maxPoolSize = maxPoolSize;

        for (int i = 0; i < maxPoolSize; ) {
            threadNames.add(threadName + ++i);
        }
    }

    private void preStart(FutureTask future) {
        synchronized (futures) {
            futures.add(future);
            futures.notify();
        }
        start();
    }

    @Override
    public void start() {
        if ((futures.size() == 0 && threads.size() > minPoolSize)
                || futures.size() > 0) {
            garbageThreads();
        }

        if ((futures.size() > 0 && threads.size() < maxPoolSize)
                || threads.size() < minPoolSize) {
            buildThreads();
        }
    }

    @Override
    public void stopNow() {
        synchronized (threads) {
            for (Thread thread : threads) {
                thread.interrupt();
            }
        }
        garbageThreads();

        synchronized (futures) {
            for (FutureTask future : futures) {
                future.cancel(true);
            }
            futures.clear();
        }
    }

    @Override
    public void execute(Runnable runnable) {
        FutureTask<Void> future = new FutureTask<>(runnable, null);
        preStart(future);
    }

    @Override
    public <T> Future<T> execute(Callable<T> callable) {
        FutureTask<T> future = new FutureTask<>(callable);
        preStart(future);
        return future;
    }

    //Вызывает итератор, который находит и убирает отвалившиеся треды
    private void garbageThreads() {
        Thread checkedThread;
        synchronized (threads) {
            Iterator<Thread> iterator = threads.iterator();
            while (iterator.hasNext()) {
                checkedThread = iterator.next();
                if (!checkedThread.isAlive() || checkedThread.isInterrupted()) {
                    iterator.remove();
                    synchronized (threadNames) {
                        threadNames.add(checkedThread.getName());
                    }
                }
            }
        }
    }

    //Создает и добавляет нового воркера в список тредов, запускает его тред.
    private void buildThreads() {
        Thread thread = new PoolWorker();
        synchronized (threads) {
            threads.add(thread);
        }
        synchronized (threadNames) {
            thread.setName(threadNames.poll());
        }
        thread.start();
    }

    /**
     * PoolWorker наследуется от Thread, в переопределенном методе run крутится бесконечный цикл, в котором
     * проверяется наличие задач в очереди futures.
     * При наличии задач из очереди забирается одна задача, которая
     * запускается на исполнение.
     * Если в очереди нет задач и общее количество тредов больше минимального, то тред
     * помечается в качестве interrupt и удаляется из списка тредов.
     * В остальных случаях тред синхронизируется по монитору futures и переходит в режим ожидания.
     */
    private class PoolWorker extends Thread {
        @Override
        public void run() {
            while (!currentThread().isInterrupted()) {
                if (futures.size() > 0) {
                    FutureTask task;
                    synchronized (futures) {
                        task = futures.poll();
                    }
                    if (!Objects.isNull(task)) {
                        task.run();
                    }
                } else if (threads.size() > minPoolSize) {
                    synchronized (threads) {
                        if (threads.size() > minPoolSize) {
                            currentThread().interrupt();
                            threads.remove(currentThread());
                            synchronized (threadNames) {
                                threadNames.add(currentThread().getName());
                            }
                        }
                    }
                } else {
                    synchronized (futures) {
                        try {
                            futures.wait();
                        } catch (InterruptedException e) {
                            System.err.println(currentThread().getName() + " interrupted in ScalableThreadPool");
                        }
                    }
                }
            }
        }
    }

    //Демон для тестов, выводит на консоль количество задач в очереди и статус живых тредов
    public void startMonitoringDaemon() {
        Runnable runnable = () -> {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("Tasks queue size: " + futures.size());
                Thread checkedThread;
                synchronized (threads) {
                    Iterator<Thread> iterator = threads.iterator();
                    while (iterator.hasNext()) {
                        checkedThread = iterator.next();
                        System.out.println(checkedThread.getName() + " " + checkedThread.getState());
                    }
                }
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread serviceThread = new Thread(runnable, "monitoringDaemon");
        serviceThread.setDaemon(true);
        serviceThread.start();
    }
}