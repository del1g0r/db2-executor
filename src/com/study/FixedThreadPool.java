package com.study;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class FixedThreadPool implements Executor {

    private BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();
    private List<ExecutorThread> threads = new ArrayList<>();

    public FixedThreadPool(int poolSize) {
        for (int i = 0; i < poolSize; i++) {
            ExecutorThread thread = new ExecutorThread();
            threads.add(thread);
            thread.start();
        }
    }

    @Override
    public synchronized void execute(Runnable command) {
        try {
            queue.put(command);
        } catch (InterruptedException e) {
            throw new RuntimeException("Can't queue a task", e);
        }
    }

    synchronized void shutdown() {
        for (ExecutorThread thread : threads) {
            thread.interrupt();
        }
        for (ExecutorThread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException("Can't join a task" + thread.getName(), e);
            }
        }
    }

    private class ExecutorThread extends Thread {

        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
                    System.out.println(getName() + " is listeninq queue...");
                    Runnable targetTask = queue.take();
                    targetTask.run();
                }
                System.out.println(getName() + " is interrupted because of request");
            } catch (
                    InterruptedException e) {
                System.out.println(getName() + " is interrupted because of InterruptedException");
            }
        }
    }
}