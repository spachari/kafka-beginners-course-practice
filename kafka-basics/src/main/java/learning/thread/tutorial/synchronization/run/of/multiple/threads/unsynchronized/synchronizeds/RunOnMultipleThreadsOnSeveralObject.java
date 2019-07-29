package learning.thread.tutorial.synchronization.run.of.multiple.threads.unsynchronized.synchronizeds;

class A implements Runnable {

    @Override
    synchronized public void run() {
        for (int i = 0; i < 3; i ++) {
            try {
                System.out.println("Hello from " + Thread.currentThread().getName() + " " + i);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}


public class RunOnMultipleThreadsOnSeveralObject {
    public static void main(String[] args) {
        A a = new A();

        Thread t1 = new Thread(a, "Thread 1");
        Thread t2 = new Thread(a, "Thread 2");
        Thread t3 = new Thread(a, "Thread 3");

        t1.start();
        t2.start();
        t3.start();
    }
}
