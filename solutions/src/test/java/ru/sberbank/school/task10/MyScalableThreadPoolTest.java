package ru.sberbank.school.task10;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import static org.junit.jupiter.api.Assertions.*;

class MyScalableThreadPoolTest {
    private MyScalableThreadPool pool;

    private void fillScalablePool() {
        pool = new MyScalableThreadPool(4, 10);
    }

    private void fillFixedPool() {
        pool = new MyScalableThreadPool(2);
    }

    private void fillTasks() {
        Runnable runnable = () -> {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread() + " running");
        };

        Callable<Integer> callable = () -> {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread() + " running");
            return null;
        };

        for (int i = 0; i < 10; i++) {
            pool.execute(runnable);
        }

        for (int i = 0; i < 10; i++) {
            pool.execute(callable);
        }
    }

    @Test
    @DisplayName("start() method")
    void start() {
        fillScalablePool();
        pool.start();
        assertEquals(4, pool.getActiveThreads());
    }

    @Test
    @DisplayName("adding new ThreadWorkers if need")
    void addNewThreads() {
        fillScalablePool();
        fillTasks();
        pool.start();
        fillTasks();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(10, pool.getActiveThreads());
    }

    @Test
    @DisplayName("deleting ThreadWorkers if need")
    void deleteThreads() {
        pool = new MyScalableThreadPool(1, 10);
        pool.start();
        fillTasks();

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(1, pool.getActiveThreads());
    }

    @Test
    @DisplayName("stopNow() method")
    void stopNow() {
        fillScalablePool();
        fillTasks();
        pool.start();
        pool.stopNow();

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(0, pool.getActiveThreads());
    }

    @Test
    @DisplayName("execute(Runnable) method")
    @SuppressWarnings("unchecked")
    void executeRunnable() {
        fillScalablePool();
        fillTasks();
        pool.execute(() -> System.out.println(Thread.currentThread() + " running"));

        try {
            Field field = pool.getClass().getDeclaredField("tasks");
            Queue<FutureTask> tasks = null;
            boolean changedAccess = false;
            if (!field.isAccessible()) {
                changedAccess = true;
                field.setAccessible(true);
            }
            try {
                tasks = (Queue<FutureTask>) field.get(pool);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (changedAccess) {
                field.setAccessible(false);
            }
            int taskSize = tasks.size();
            assertEquals(21, taskSize);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("execute(Callable) method")
    void executeCallable() {
        fillScalablePool();
        fillTasks();
        pool.execute((Callable<Integer>) () -> {
            System.out.println(Thread.currentThread() + " running");
            return null;
        });

        try {
            Field field = pool.getClass().getDeclaredField("tasks");
            Queue<FutureTask> tasks = null;
            boolean changedAccess = false;
            if (!field.isAccessible()) {
                changedAccess = true;
                field.setAccessible(true);
            }
            try {
                tasks = (Queue<FutureTask>) field.get(pool);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (changedAccess) {
                field.setAccessible(false);
            }
            int taskSize = tasks.size();
            assertEquals(21, taskSize);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("fixed pool initialization and start() method")
    void fixedPoolStart() {
        fillFixedPool();
        pool.start();
        assertEquals(2, pool.getActiveThreads());
    }

    @Test
    @DisplayName("fixed pool stopNow() method")
    void fixedPoolStopNow() {
        fillFixedPool();
        fillTasks();
        pool.start();
        pool.stopNow();

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(0, pool.getActiveThreads());
    }

    @Test
    @DisplayName("checking fixed pool size in fixed pool")
    void fixedPoolCheck() {
        fillFixedPool();
        pool.start();
        fillTasks();
        assertEquals(2, pool.getActiveThreads());
    }
}