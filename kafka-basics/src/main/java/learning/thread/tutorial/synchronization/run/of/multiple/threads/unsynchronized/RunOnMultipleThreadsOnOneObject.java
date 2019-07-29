package learning.thread.tutorial.synchronization.run.of.multiple.threads.unsynchronized;


class A implements Runnable {

    @Override
    public void run() {
        for (int i = 0; i < 3; i ++) {
            System.out.println("Hello from " + Thread.currentThread().getName() + " " + i);
        }
    }
}


public class RunOnMultipleThreadsOnOneObject {
    public static void main(String[] args) {
        //object
        A a = new A();
        Thread t1 = new Thread(a, "Thread A");
        Thread t2 = new Thread(a, "Thread B");
        Thread t3 = new Thread(a, "Thread C");

        t1.start();
        t2.start();
        t3.start();

        //Each thread will run only for a short while, before it is replaced by another thread, in the middle of its execution,
        // this is called unsychronized run of multiple threads
    }
}
