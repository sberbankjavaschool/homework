package ru.sberbank.school.task10;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.*;

public class FixedThreadPool implements ThreadPool {

    private final Queue<Thread> threadPool = new ConcurrentLinkedQueue<>();
    private final BlockingQueue<Runnable> runnableQueue = new LinkedBlockingQueue<>();
    private final Queue<Integer> threadNumbers = new LinkedList<>();
    private final int poolSize;
    private boolean poolWorking;

    public FixedThreadPool(int poolSize) {
        this.poolSize = poolSize;
    }

    @Override
    public void start() {
        while (threadPool.size() < poolSize) {
            startNewThread(false);
        }
        poolWorking = true;
    }

    @Override
    public void stopNow() {
        runnableQueue.clear();
        poolWorking = false;
        threadPool.forEach(Thread::interrupt);
        threadPool.clear();
    }

    @Override
    public void execute(Runnable runnable) {
        Objects.requireNonNull(runnable);
        synchronized (runnableQueue) {
            runnableQueue.offer(runnable);
            runnableQueue.notifyAll();
        }
    }

    @Override
    public <T> Future<T> execute(Callable<T> callable) {
        Objects.requireNonNull(callable);
        FutureTask<T> futureTask = new FutureTask<>(callable);
        execute(futureTask);
        return futureTask;
    }

    void startNewThread(boolean temporary) {
        if ((!temporary && threadPool.size() >= poolSize)
                || (temporary && (!isPoolFull() || !poolWorking))) {
            return;
        }

        int threadUniqueNumber = Optional.ofNullable(threadNumbers.poll()).orElse(threadPool.size());
        Thread thread = new ThreadPoolWorker(runnableQueue, threadUniqueNumber, temporary);
        threadPool.add(thread);
        thread.setUncaughtExceptionHandler(this::threadDeadBabyThreadDead);
        thread.start();
    }

    private void threadDeadBabyThreadDead(Thread thread, Throwable ex) {
        if (poolWorking) {
            threadNumbers.offer(Integer.valueOf(thread.getName().split("-")[1]));
            threadPool.remove(thread);
            if (!((ThreadPoolWorker) thread).isTemporary()) {
                startNewThread(((ThreadPoolWorker) thread).isTemporary());
            }
            if (!ex.getClass().equals(HasNoWorkException.class)) {
                ex.printStackTrace();
            }
        }
    }

    int threadPoolSize() {
        return threadPool.size();
    }

    private boolean isPoolFull() {
        return threadPool.stream().noneMatch(thread -> thread.getState() == Thread.State.WAITING);
    }

    public Queue<Integer> getThreadNumbers() {
        return new LinkedList<>(threadNumbers);
    }
}