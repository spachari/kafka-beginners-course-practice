package learning.thread.tutorial.synchronization.run.of.multiple.threads.unsynchronized;

class B implements Runnable {

    @Override
    public void run() {
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

public class RunOnMultipleThreadsOnSeveralObjects {
    public static void main(String[] args) {
        B b1 = new B();
        B b2 = new B();
        B b3 = new B();

        Thread t1 = new Thread(b1, "Thread b1");
        Thread t2 = new Thread(b2, "Thread b2");
        Thread t3 = new Thread(b3, "Thread b3");

        t1.start();
        t2.start();
        t3.start();

        //You may see in output, it has been an unsynchronized run of threads, as each thread was replaced by another thread in the middle
        // of its execution of the run() method, when sleep() method was called on it.
    }
}
