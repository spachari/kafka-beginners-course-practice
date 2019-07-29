package learning.thread.tutorial.synchronization;

class A2 {
    public void sync() {
        for (int i = 4; i < 7; i ++) {
            System.out.println("Hello from " + Thread.currentThread().getName() + " " + i);
        }
    }
}

class B2 implements Runnable {

    A2 a;
    public B2(A2 a) {
        this.a = a;
    }

    @Override
    public void run() {

        for (int i = 0; i < 3; i ++) {
            System.out.println("Hello from " + Thread.currentThread().getName() + " " + i);
        }

        synchronized (a) {
            System.out.println("Starting synchronized for " + Thread.currentThread().getName());
            a.sync();
            System.out.println("Exiting synchronized for " + Thread.currentThread().getName());
        }

        //Synchronizing a block is useful in a situation when we want to synchronize an access to a class that doesn't have
        // any synchronized method and nor do we have access to its code to modify its method with synchronized keyword.
    }
}

public class SynchronizedBlock {
    public static void main(String[] args) {
        A2 a = new A2();

        B2 b1 = new B2(a);
        B2 b2 = new B2(a);

        Thread t1 = new Thread(b1, "t1 thread");
        Thread t2 = new Thread(b2, "t2 thread");

        t1.start();
        t2.start();
    }
}
