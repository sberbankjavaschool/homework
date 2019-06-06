package ru.sberbank.school.task10;

import org.junit.Before;
import org.junit.Test;

public class FixedThreadPoolTest {
    private ThreadPool pool;


    @Before
    public void init() {
        pool = new FixedThreadPool(3);
    }

    @Test(expected = IllegalStateException.class)
    public void executeBeforeStart() {
        pool.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("done");
            }
        });
    }

    @Test(expected = IllegalStateException.class)
    public void stopBeforeStart() {
        pool.stopNow();
    }

    @Test(expected = IllegalArgumentException.class)
    public void numberOfThreads() {
        pool = new FixedThreadPool(0);
    }

    @Test(expected = IllegalStateException.class)
    public void doubleStart() {
        pool.start();
        pool.start();
    }

    @Test(expected = IllegalStateException.class)
    public void doubleStop() {
        pool.start();
        pool.stopNow();
        pool.stopNow();
    }

    @Test(expected = IllegalStateException.class)
    public void stopAndRenewWithoutStart() {
        pool.start();
        for (int i = 0; i < 4; i++) {
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            });
        }
        pool.stopNow();
        pool.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("done");
            }
        });
    }

    @Test
    public void stopAndRenewNormal() {
        pool.start();
        for (int i = 0; i < 4; i++) {
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            });
        }
        pool.stopNow();
        pool.start();
        pool.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("done");
            }
        });
    }

    @Test
    public void normalWork() {
        pool.start();
        for (int i = 0; i < 15; i++) {
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " done");
                }
            });
        }
    }
}
