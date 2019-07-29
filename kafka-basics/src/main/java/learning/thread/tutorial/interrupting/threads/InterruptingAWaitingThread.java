package learning.thread.tutorial.interrupting.threads;

public class InterruptingAWaitingThread {
    public static void main(String[] args) {
        Thread th = Thread.currentThread();

        th.interrupt();

        try {
            th.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Hello from " + th.getName());
    }
}

//Method interrupt() is called on the main thread, which sets its interrupt flag to true.
// Now, the main thread throw InterruptedException if it calls wait(), sleep() or join() method in the upcoming code.