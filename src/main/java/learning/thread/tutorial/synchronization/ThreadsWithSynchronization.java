package learning.thread.tutorial.synchronization;

class A1 {
    synchronized public void sync() {
        for(int i = 0; i < 3; i ++) {
            System.out.println("Hello from " + Thread.currentThread().getName() + " " + i);
        }
    }
}

class B1 implements Runnable {
    A1 a1;

    public B1(A1 a1) {
        this.a1 = a1;
    }

    @Override
    public void run() {
        a1.sync();
    }
}

public class ThreadsWithSynchronization {
    public static void main(String[] args) {

        A1 a = new A1();

        B1 b1 = new B1(a);
        B1 b2 = new B1(a);

        Thread t1 = new Thread(b1, "b1");
        Thread t2 = new Thread(b1, "b2");

        t1.start();
        t2.start();

    }
}

//Method sync() is synchronized, so the first thread to enter this method gets its sole access and only after it finishes
// its execution of method sync(), is when the other thread is able to enter this synchronized method to starts its execution,
// thereby avoiding a conflict between two threads..