package org.motorbook.model;

import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    public static void main(String[] args) {
        Thread t = new A();
        t.start();
    }

    static class A extends Thread{

        private static AtomicBoolean first = new AtomicBoolean(true);

        public synchronized void wantToRun() {
            System.out.println("Gerard");
        }

        public synchronized void keepingBusy() {
            System.out.println("A keepingBusy");
            Runnable t1 = new Runnable() {
                @Override
                public void run() {
                  B b = new B(A.this);
                  b.start();
                }
            };
            t1.run();
            try {
                System.out.println("Holding the lock for 10sec (DSMInitialize)");
                Thread.sleep(10000);
                System.out.println("Releasing the lock");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void run() {
            keepingBusy();
        }
    }

    static class B extends Thread{

        private A parent;

        public B (A parent) {
            this.parent = parent;
        }

        @Override
        public void run() {
            try {

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Execute want to run");
            parent.wantToRun();
            System.out.println("EXECUTED want to run");
        }
    }
}
