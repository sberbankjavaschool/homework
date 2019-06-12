package ru.sberbank.school.task10;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

class ScalableThreadPoolTest {
    private final ScalableThreadPool threadPool;

    {
        threadPool = new ScalableThreadPool(2, 5);
        threadPool.startMonitoringDaemon();
    }

    @Test
    void startFewTasksTest() throws Exception {
        int tasks = 20;
        int tasksIntervalMs = 500;
        int workDurationMs = 100;
        startTest(tasks, tasksIntervalMs, workDurationMs);
    }
    
    @Test
    void startNormalTasksTest() throws Exception {
        int tasks = 20;
        int tasksIntervalMs = 500;
        int workDurationMs = 1000;
        startTest(tasks, tasksIntervalMs, workDurationMs);
    }

    @Test
    void startManyTasksTest() throws Exception {
        int tasks = 50;
        int tasksIntervalMs = 100;
        int workDurationMs = 1000;
        startTest(tasks, tasksIntervalMs, workDurationMs);
    }

    void startTest(int tasks, int tasksIntervalMs, int workDurationMs) {
        threadPool.start();
        for (int i = 0; i < tasks; i++) {
            threadPool.execute(new TestCase(i, workDurationMs));

            try {
                Thread.sleep(tasksIntervalMs);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Stop now!");
        threadPool.stopNow();
    }

    @RequiredArgsConstructor
    private class TestCase implements Runnable {
        private final int counter;
        private final int workDurationMs;

        @Override
        public void run() {
            try {
                System.out.println("Runnable#" + counter + " start by " + Thread.currentThread().getName());
                Thread.sleep(workDurationMs);
                System.out.println("Runnable#" + counter + " done by " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                System.out.println("Test work interrupted for " + Thread.currentThread().getName());
            }
        }
    }
}