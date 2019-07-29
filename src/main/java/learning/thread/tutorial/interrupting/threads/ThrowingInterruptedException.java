package learning.thread.tutorial.interrupting.threads;

class Thread1 implements Runnable {

    @Override
    public void run() {
        for (int i = 0; i < 10; i ++) {
            System.out.println(Thread.currentThread().getName() + " " + i);

            try {
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }

    }
}

public class ThrowingInterruptedException {
    public static void main(String[] args) {
        Thread1 th = new Thread1();
        Thread thread = new Thread(th, "Test Thread");

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
