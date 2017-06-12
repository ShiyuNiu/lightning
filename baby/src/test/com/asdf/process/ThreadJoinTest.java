package com.asdf.process;

/**
 * Created by shiyu on 17-6-12.
 */
public class ThreadJoinTest {
    public static void main(String[] args) {

        System.out.println(Thread.currentThread().getName() + " start!");

        Thread t1 = new Thread("T1") {
            @Override
            public void run() {
                int i = 10;
                while (i > 0) {
                    System.out.println(this.getName() + " " + i--);
                }
            }
        };

        Thread t2 = new Thread("T2") {
            @Override
            public void run() {
                int i = 10;
                while (i > 0) {
                    System.out.println(this.getName() + " " + i--);
                }
            }
        };

        t1.start();
        System.out.println("1 and 2");
        t2.start();

//        boolean wait = false;
        boolean wait = true;
        if (wait) {
            try {
                t1.join();
                System.out.println("1. join return");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                t2.join();
                System.out.println("2. join return");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(Thread.currentThread().getName() + " stop!");
    }
}
