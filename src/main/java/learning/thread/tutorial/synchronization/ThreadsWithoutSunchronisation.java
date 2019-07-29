package learning.thread.tutorial.synchronization;

import java.awt.font.TextHitInfo;

class A {
    public void sync() {
        for (int i = 0; i < 3; i ++) {
            System.out.println("Counter from " + Thread.currentThread().getName() + " " + i);
        }
    }
}

class B implements Runnable {
    A a;
    public B(A a) {
        this.a = a;
    }

    @Override
    public void run() {
        a.sync();
    }
}

public class ThreadsWithoutSunchronisation {
    public static void main(String[] args) {
        A a = new A();

        B b1 = new B(a);
        B b2 = new B(a);

        Thread t1 = new Thread(b1, "t1");
        Thread t2 = new Thread(b2, "t2");

        t1.start();
        t2.start();

    }
}
