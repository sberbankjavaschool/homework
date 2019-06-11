package ru.sberbank.school.task11;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

class ConcurrentScalableThreadPoolTest {

    private final int minPoolSize = 2;
    private final int maxPoolSize = 5;
    private ConcurrentScalableThreadPool threadPool;

    private boolean poolWorking = false;
    private boolean additionFlag = false;
    private List<Thread> workers = null;
    private BlockingQueue<FutureTask> futures = null;

    {
        threadPool = new ConcurrentScalableThreadPool(minPoolSize, maxPoolSize);
        threadPool.startMonitoringDaemon();
    }

    @AfterEach
    void stop() throws Exception {
        threadPool.stopNow();
        TimeUnit.SECONDS.sleep(1);
    }

    @Test
    void illegalArgumentInitializationTest() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new ConcurrentScalableThreadPool(2, -1));
    }

    @Test
    void poolNotRunningTest() {
        Assertions.assertThrows(IllegalStateException.class,
                () -> threadPool.execute(new TestCase(1, 1)));
    }

    @Test
    void poolRunningTest() throws Exception {
        threadPool.start();
        TimeUnit.SECONDS.sleep(2);

        getFields();
        Assertions.assertTrue(poolWorking);
        Assertions.assertTrue(additionFlag);
    }

    @Test
    void poolShutDownTest() throws Exception {
        startFewTasksTest();
        threadPool.stopNow();
        TimeUnit.SECONDS.sleep(2);

        getFields();
        Assertions.assertFalse(poolWorking);
        Assertions.assertFalse(additionFlag);
        Assertions.assertTrue(futures.isEmpty());
        Assertions.assertTrue(workers.isEmpty());
    }

    @Test
    void minPoolSizeTest() throws Exception {
        startFewTasksTest();
        getFields();
        Assertions.assertEquals(minPoolSize, workers.size());
    }

    @Test
    void maxPoolSizeTest() throws Exception {
        startManyTasksTest();
        getFields();
        Assertions.assertEquals(maxPoolSize, workers.size());
    }

    private void getFields() {
        Class clazz = ConcurrentScalableThreadPool.class;
        Field poolWorkingField = null;
        Field additionalFlagField = null;
        Field futuresField = null;
        Field workersField = null;
        AtomicBoolean aBoolean = null;

        try {
            poolWorkingField = clazz.getDeclaredField("poolWorking");
            poolWorkingField.setAccessible(true);
            aBoolean = (AtomicBoolean) poolWorkingField.get(threadPool);
            poolWorking = aBoolean.get();

            additionalFlagField = clazz.getDeclaredField("additionalFlag");
            additionalFlagField.setAccessible(true);
            aBoolean = (AtomicBoolean) additionalFlagField.get(threadPool);
            additionFlag = aBoolean.get();

            futuresField = clazz.getDeclaredField("futures");
            futuresField.setAccessible(true);
            futures = (BlockingQueue) futuresField.get(threadPool);

            workersField = clazz.getDeclaredField("workers");
            workersField.setAccessible(true);
            workers = (List<Thread>) workersField.get(threadPool);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            poolWorkingField.setAccessible(false);
            additionalFlagField.setAccessible(false);
            futuresField.setAccessible(false);
            workersField.setAccessible(false);
        }
    }

    @Test
    void startFewTasksTest() throws Exception {
        int tasks = 5;
        int tasksIntervalMs = 500;
        int workDurationMs = 100;
        startTest(tasks, tasksIntervalMs, workDurationMs);
    }

    @Test
    void startNormalTasksTest() throws Exception {
        int tasks = 15;
        int tasksIntervalMs = 500;
        int workDurationMs = 1000;
        startTest(tasks, tasksIntervalMs, workDurationMs);
    }

    @Test
    void startManyTasksTest() throws Exception {
        int tasks = 500;
        int tasksIntervalMs = 10;
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