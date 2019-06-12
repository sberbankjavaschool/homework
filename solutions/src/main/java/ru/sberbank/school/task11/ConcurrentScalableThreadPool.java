package ru.sberbank.school.task11;

import lombok.NonNull;
import ru.sberbank.school.task10.ThreadPool;
import ru.sberbank.school.util.Solution;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 09.06.2019
 * Реализация пула потоков на базе коллекций из пакета Concurrency.
 * В конструкторе задается минимальное и максимальное(int min, int max) число потоков,
 * количество запущенных потоков может быть увеличено от минимального к максимальному.
 *
 * @author Gregory Melnikov
 */

@Solution(11)
public class ConcurrentScalableThreadPool implements ThreadPool {

    private static final String THREAD_NAME = "ThreadPoolWorker-";
    private final int minPoolSize;
    private final int maxPoolSize;
    private final AtomicBoolean poolWorking = new AtomicBoolean(false);
    private final AtomicBoolean additionalFlag = new AtomicBoolean(false);

    private final List<PoolWorker> workers = new CopyOnWriteArrayList<>();
    private final Queue<String> threadNames = new LinkedBlockingQueue<>();
    private final BlockingQueue<FutureTask> futures = new LinkedBlockingQueue<>();

    public ConcurrentScalableThreadPool(@NonNull int minPoolSize, @NonNull int maxPoolSize) {
        if (minPoolSize < 0 || maxPoolSize < 0 || minPoolSize > maxPoolSize) {
            throw new IllegalArgumentException("MaxPoolSize can't be less than minPoolSize, both can't be negative");
        }
        this.minPoolSize = minPoolSize;
        this.maxPoolSize = maxPoolSize;

        for (int i = 0; i < maxPoolSize; ) {
            threadNames.add(THREAD_NAME + ++i);
        }
    }

    @Override
    public void start() {
        if (!poolWorking.get() && !additionalFlag.get()) {
            poolWorking.set(true);
            Runnable runnable = () -> {
                System.out.println("ConcurrentScalableThreadPoolService starting");
                int queueSize;
                int workerSize;
                int loadFactor;
                loadFactorExceeded:
                while (poolWorking.get()) {
                    queueSize = futures.size();
                    workerSize = workers.size();
                    loadFactor = queueSize / maxPoolSize;

                    if ((queueSize > 1 && workerSize < maxPoolSize)
                            || workerSize < minPoolSize) {
                        buildWorker();
                    }
                    if ((queueSize == 0 && workerSize > minPoolSize)
                            || queueSize > 1) {
                        garbageWorkers();
                    }
                    if (queueSize > 0) {
                        while (!futures.isEmpty()) {
                            for (PoolWorker worker : workers) {
                                if (worker.getFuturesSize() > loadFactor) {
                                    continue loadFactorExceeded;
                                }
                                if (!worker.isInterrupted()) {
                                    try {
                                        worker.putFuture(futures.take());
                                    } catch (InterruptedException e) {
                                        System.err.println("PoolService can't transfer future to: " + worker.getName());
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                }
                Thread.currentThread().interrupt();
            };
            Thread serviceThread = new Thread(runnable, "poolService");
            serviceThread.start();
            additionalFlag.set(true);
        }
    }

    @Override
    public void stopNow() {
        if (poolWorking.get() && additionalFlag.get()) {
            poolWorking.set(false);
            Runnable runnable = () -> {
                System.out.println("ConcurrentScalableThreadPoolService shutting down");
                for (FutureTask future : futures) {
                    future.cancel(false);
                }
                futures.clear();

                for (PoolWorker worker : workers) {
                    worker.clearFutures();
                }
                boolean haveSurvivors = true;
                while (haveSurvivors) {
                    haveSurvivors = false;
                    for (PoolWorker worker : workers) {
                        if (worker.isAlive() && !worker.isInterrupted()) {
                            System.out.println(worker.getName() + " stopTask executing");
                            worker.interrupt();
                            haveSurvivors = true;
                        }
                    }
                }
                garbageWorkers();
                additionalFlag.set(false);
                System.out.println("ConcurrentScalableThreadPoolService shut down");
                Thread.currentThread().interrupt();
            };
            Thread serviceThread = new Thread(runnable, "poolShutDownService");
            serviceThread.start();
        }
    }

    @Override
    public void execute(Runnable runnable) {
        execute(new FutureTask<>(runnable, null));
    }

    @Override
    public <T> Future<T> execute(Callable<T> callable) {
        return execute(new FutureTask<>(callable));
    }

    private <T> Future<T> execute(FutureTask<T> future) {
        if (poolWorking.get()) {
            futures.add(future);
        } else {
            throw new IllegalStateException("ConcurrentScalableThreadPool is not running");
        }
        return future;
    }

    public boolean isRunning() {
        return poolWorking.get() || additionalFlag.get();
    }

    //Находит и убирает отвалившиеся треды
    private void garbageWorkers() {
        for (PoolWorker worker : workers) {
            if (!worker.isAlive() || worker.isInterrupted()) {
                workers.remove(worker);
                threadNames.add(worker.getName());
            }
        }
    }

    //Создает и добавляет нового воркера в список тредов, запускает его тред.
    private void buildWorker() {
        PoolWorker worker = new PoolWorker();
        workers.add(worker);
        worker.setName(threadNames.poll());
        worker.start();
    }

    /**
     * PoolWorker наследуется от Thread. Каждый воркер обладает собственной очередью localFutures на основе
     * LinkedBlockingDeque.
     * В переопределенном методе run крутится бесконечный цикл, в котором проверяется наличие задач в очереди
     * localFutures. При наличии задач из очереди забирается одна задача, которая запускается на исполнение.
     * Если в локальной очереди нет задач, то вокрер переходит в сервисный режим и начинает перекидывать задачи из
     * общей очереди пула futures в локальные очереди всех воркеров. Если задач нет ни в локальной очереди ни в общей,
     * то воркер пытается украсть задачу из хвоста очереди другого воркера. При удачном воровстве задачи начинается
     * новая итерация бесконечного цикла, чтобы приступить к выполнию задачи.
     * Если задачу не удается получить ни одним из вышеперечисленных способов и общее количество воркеров больше
     * минимального, то воркер останавливается, помечается в качестве interrupt и удаляется из списка тредов.
     * Если количество воркеров не превышено, то воркер перейдет в режим ожидания при попытке забрать элемент из
     * пустой блокирующей очереди.
     */
    private class PoolWorker extends Thread {
        private final BlockingDeque<FutureTask> localFutures = new LinkedBlockingDeque<>();
        private Thread localThread;

        @Override
        public void run() {
            localThread = currentThread();
            System.out.println(localThread.getName() + " running, daemon: " + localThread.isDaemon());
            stolenNewFuture:
            while (poolWorking.get()) {
                try {
                    if (localFutures.isEmpty()) {
                        if (!futures.isEmpty()) {
                            PoolWorker poolWorker;
                            Iterator<PoolWorker> iterator = workers.iterator();
                            while (!futures.isEmpty()) {
                                if (!iterator.hasNext()) {
                                    iterator = workers.listIterator(0);
                                }
                                poolWorker = iterator.next();
                                if (!poolWorker.isInterrupted()) {
                                    poolWorker.putFuture(futures.take());
                                }
                            }
                            continue;
                        } else if (futures.isEmpty()) {
                            for (PoolWorker poolWorker : workers) {
                                if (poolWorker.getFuturesSize() > 0) {
                                    FutureTask future = poolWorker.futureStealing();
                                    if (!Objects.isNull(future)) {
                                        localFutures.put(future);
                                        continue stolenNewFuture;
                                    }
                                }
                            }
                        }
                        if (workers.size() > minPoolSize) {
                            break;
                        }
                    }
                    if (!localThread.isInterrupted()) {
                        FutureTask task = localFutures.take();
                        task.run();
                    }
                } catch (InterruptedException e) {
                    System.err.println(localThread.getName() + " interrupting");
                }
            }
            localThread.interrupt();
            threadNames.add(localThread.getName());
            clearFutures();
        }

        private void clearFutures() {
            for (FutureTask future : localFutures) {
                future.cancel(true);
            }
            localFutures.clear();
        }

        private int getFuturesSize() {
            return localFutures.size();
        }

        private void putFuture(FutureTask future) throws InterruptedException {
            localFutures.put(future);
        }

        private FutureTask futureStealing() {
            return localFutures.pollLast();
        }
    }

    //Демон для тестов, выводит на консоль количество задач в очереди и статус живых тредов
    public void startMonitoringDaemon() {
        Runnable runnable = () -> {
            while (!Thread.currentThread().isInterrupted()) {
                Thread checkedThread;
                Iterator<PoolWorker> iterator = workers.iterator();
                while (iterator.hasNext()) {
                    checkedThread = iterator.next();
                    System.out.println("MonitoringDaemon says: "
                            + checkedThread.getName() + " " + checkedThread.getState());
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