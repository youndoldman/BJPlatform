package com.donno.nj.util;

import org.junit.Test;

public class TempTest {
    @Test
    public void testJoin() throws Exception {
        String threadName = Thread.currentThread().getName();
        System.out.println("JoinTestDemo: "+threadName + " start.");
        CustomThread1 t1 = new CustomThread1();
        CustomThread ct = new CustomThread(t1);
        try {
            t1.start();
            Thread.sleep(2000);

            ct.start();
            //ct.join();
        } catch (Exception e) {
            System.out.println("Exception from main");
        }
        System.out.println("JoinTestDemo: "+threadName + " end!");

    }


    @Test
    public void testWhile() throws Exception {
        System.out.println("start");
//        waitForAll();
        System.out.println("aaaa");
    }

    private void waitForAll() {
        int a = 0;
        while (true) {
            if (a > 10) {
              return;
            }
            System.out.println("++");
            a++;
        }
    }
}

class CustomThread1 extends Thread {
    public CustomThread1() {
        super("[CustomThread1] Thread");
    }

    public void run() {
        String threadName = Thread.currentThread().getName();
        System.out.println("CustomThread1: "+threadName + " start.");
        try {
            for (int i = 0; i < 5; i++) {
                System.out.println(threadName + " loop at " + i);
                Thread.sleep(1000);
                //this.join();
            }
            System.out.println("CustomThread1: "+threadName + " end.");
        } catch (Exception e) {
            System.out.println("Exception from " + threadName + ".run");
        }
    }
}

class CustomThread extends Thread {
    CustomThread1 t1;

    public CustomThread(CustomThread1 t1) {
        super("[CustomThread] Thread");
        this.t1 = t1;
    }

    public void run() {
        String threadName = Thread.currentThread().getName();
        System.out.println("CustomThread: "+threadName + " start.");
        try {
            //System.out.println("start CustomThread join");
            for (int i = 0; i < 5; i++) {
                System.out.println(threadName + " Custom at " + i);

            }
            t1.join();
            for (int i = 6; i < 10; i++) {
                System.out.println(threadName + " Custom at " + i);

            }
            System.out.println("CustomThread: "+threadName + " end.");
        } catch (Exception e) {
            System.out.println("Exception from " + threadName + ".run");
        }
    }
}
