package com.study;

public class Test {
    static FixedThreadPool executor = new FixedThreadPool(4);

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            runTask(i);
            Thread.sleep(500);
        }

        executor.shutdown();
    }

    private static void runTask(int i) {

        Runnable runnable = new Runnable() {
            public void run() {
                System.out.println("The task " + i + " is being executed by " + Thread.currentThread().getName());
            }
        };
        executor.execute(runnable);
    }
}