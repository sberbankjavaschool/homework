package ru.sberbank.school.task10;

public class ThreadPoolWorker extends Thread {

    private final Runnable target;
    private static int nameCounter;
    private final ThreadCompleteListener listener;

    public ThreadPoolWorker(Runnable target, ThreadCompleteListener listener) {
        super(target, "ThreadPoolWorker-" + nameCounter);
        nameCounter++;
        this.listener = listener;
        this.target = target;
    }

    @Override
    public void run() {
        try {
            target.run();
        } finally {
            listener.notifyOfThreadComplete(this);
        }
    }
}
