package learning.thread.tutorial.interrupting.threads;

class Thread2 implements Runnable {

    @Override
    public void run() {
        for (int i = 0; i < 3; i ++) {
            System.out.println("Hello from " + Thread.currentThread().getName() + " " + i);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class InterruptingASleepingThread {
    public static void main(String[] args) {
        Thread2 th = new Thread2();
        Thread thread = new Thread(th, "Test thread 2");

        thread.start();
        thread.interrupt();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Hello from " + Thread.currentThread().getName());

    }
}
