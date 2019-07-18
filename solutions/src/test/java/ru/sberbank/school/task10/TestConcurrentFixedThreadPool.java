package ru.sberbank.school.task10;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestConcurrentFixedThreadPool extends TestFixedThreadPool {

    private ThreadPool pool;

    @Before
    public void initialize() {
        pool = new ConcurrentFixedThreadPool(3);
        pool.start();
    }

    @Test
    public void checkRepeatStart() {
        super.checkRepeatStart(pool);
    }

    @Test
    public void checkIncrement() throws InterruptedException {
        super.checkIncrement(pool, 1000000, 1000);
    }

    @Test
    public void checkAdequacy() throws InterruptedException {
        super.checkAdequacy(pool, 10000, 2000);
    }

    @After
    public void stopPool() {
        pool.stopNow();
    }

}
