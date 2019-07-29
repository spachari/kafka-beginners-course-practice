package learning.thread.tutorial.anonymous.threads;

import org.omg.PortableServer.THREAD_POLICY_ID;

public class AnomyousThreadsEx2 {
    public static void main(String[] args) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Thread t = Thread.currentThread();
                t.setName("Anomyous Thread");
                for (int i = 0; i < 3; i ++) {
                    System.out.println("Hello from " + Thread.currentThread().getName() + " " + i);
                }
            }
        };

        Thread t = new Thread(r);
        t.start();

        System.out.println("Hello from " + Thread.currentThread().getName());
    }
}
