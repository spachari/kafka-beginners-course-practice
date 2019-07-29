package learning.thread.tutorial.synchronization.run.of.multiple.threads.unsynchronized.synchronizeds;

class B implements Runnable {

    @Override
    synchronized public void run() {
        for (int i = 0; i < 3; i ++) {
            System.out.println("Hello from " + Thread.currentThread().getName() + " " + i);
        }
    }
}

public class RunOnMultipleThreadsOnOneObject {
    public static void main(String[] args) {
        B b = new B();
        B b1 = new B();
        B b2 = new B();

        Thread t1 = new Thread(b, "Thread 1");
        Thread t2 = new Thread(b, "Thread 2");
        Thread t3 = new Thread(b, "Thread 3");

        t1.start();
        t2.start();
        t3.start();
    }
}
