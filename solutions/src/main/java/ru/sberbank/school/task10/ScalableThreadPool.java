package ru.sberbank.school.task10;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class ScalableThreadPool implements ThreadPool {

    private int min;
    private int max;
    private int n;
    private int ids;
    private List<Thread> threads;
    private final List<Runnable> runnableTasks;
    private final List<FutureTask<?>> futures;
    private final Object nLock = new Object();

    public ScalableThreadPool(int min, int max) {
        if (min <= 0) {
            throw new IllegalArgumentException("Parameter n must be greater than 0!");
        }
        if (max < min) {
            throw new IllegalArgumentException("Parameter max must be greater than min!");
        }

        this.min = min;
        this.max = max;
        n = 0;
        ids = 0;
        threads = new ArrayList<>(min);
        runnableTasks = new LinkedList<>();
        futures = new LinkedList<>();
    }

    public int getN() {
        return n;
    }

    public int getThreadSize() {
        return threads.size();
    }

    private void pr(String s) {
        System.out.println("n=" + n + " threads=" + threads.size() + " " + s);
    }

    @Override
    public void start() {
        for (int i = 0; i < min; i++) {
            addThread();
        }
    }

    private void addThread() {
        ids++;
        Thread thread = new ThreadPoolWorker("ThreadPoolWorker-" + ids);
        threads.add(thread);
        thread.start();
    }

    @Override
    public void stopNow() {
        synchronized (runnableTasks) {
            runnableTasks.clear();
            futures.clear();
        }

        synchronized (nLock) {
            for (int i = threads.size() - 1; i >= 0; i--) {
                threads.get(i).interrupt();
                threads.remove(i);
            }
        }

        ids = 0;
    }

    @Override
    public void execute(Runnable runnable) {
        execute(runnable, null);
    }

    @Override
    public <T> Future<T> execute(Callable<T> callable) {
        FutureTask<T> futureTask;
        futureTask = new FutureTask<>(callable);
        execute(null, futureTask);
        return futureTask;
    }

    private <T> void execute(Runnable runnable, FutureTask<T> future) {
        synchronized (runnableTasks) {
            synchronized (nLock) {
                pr("execute");
                pr("runnableTasks=" + runnableTasks.size());
                //Проверяем количество задач в работе и в очереди задач и при необходтммости создаем треды
                if (n + runnableTasks.size() + 1 > threads.size() && threads.size() < max) {
                    addThread();
                    pr("execute addThread");
                }
            }
            runnableTasks.add(runnable);
            futures.add(future);
            runnableTasks.notify();
            pr("execute end");
        }
    }

    public class ThreadPoolWorker extends Thread {

        public ThreadPoolWorker(String name) {
            super(name);
        }

        private void pr(String s) {
            System.out.println(getName() + " id=" + this.getId() + " n=" + n + " thread=" + threads.size() + " " + s);
        }

        @Override
        public void run() {
            pr("start run");
            Runnable runnableTask;
            FutureTask<?> future;
            boolean keepGoOn = !isInterrupted();
            while (keepGoOn) {
                synchronized (runnableTasks) {
                    try {
                        if (runnableTasks.isEmpty()) {
                            pr("empty");
                            //после выполнения run() тред будет удален, если список задач пуст и кол-во тредов > min
                            synchronized (nLock) {
                                if (threads.size() > min) {
                                    threads.remove(this);
                                    pr("remove this");
                                    return;
                                }
                            }
                            runnableTasks.wait();
                        }
                        runnableTask = runnableTasks.get(0);
                        future = futures.get(0);
                        runnableTasks.remove(0);
                        futures.remove(0);
                    } catch (InterruptedException e) {
                        pr("InterruptedException");
                        keepGoOn = false;
                        continue;
                    }
                }
                //увеличиваем счетчик тредов,которые выполняют задачу
                synchronized (nLock) {
                    n++;
                    pr("n++");
                }

                if (runnableTask != null) {
                    pr("-runnableTask.run");
                    runnableTask.run();
                    pr("+runnableTask.run");
                }
                if (future != null) {
                    pr("-future.run");
                    future.run();
                    pr("+future.run");
                }

                //уменьшаем счетчик тредов,которые выполняют задачу
                synchronized (nLock) {
                    n--;
                    pr("n--");
                }

                keepGoOn = !isInterrupted();
            }
            pr("finish run");
        }
    }
}
