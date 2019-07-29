package learning.thread.tutorial.interrupting.threads;

class ThEx1 implements Runnable {

    @Override
    public void run() {
        for (int i = 0; i < 300; i ++) {
            System.out.println( "Hello from " + Thread.currentThread().getName() + " " + i);

            if (Thread.interrupted()) {
                System.out.println("Thread is interrupted !!!");
            }
            //Method interrupted() is called on the Thread2 to check the status of its interrupt flag and it returns true,
            // which prints message "Thread is interrupted" at the console.
        }
    }
}

public class InterruptingAThread {
    public static void main(String[] args) {
        ThEx1 th = new ThEx1();
        Thread thread = new Thread(th, "test thread");

        thread.start();
        thread.interrupt();
        //While the Thread2 is executing for-loop in run() method, the main thread calls interrupt() on it, which sets the interrupt
        // flag of Thread2 to true but Thread2 doesn't throw an InterruptedException.

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Hello from " + Thread.currentThread().getName());
    }
}
