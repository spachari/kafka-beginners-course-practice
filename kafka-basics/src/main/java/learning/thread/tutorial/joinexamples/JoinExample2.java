package learning.thread.tutorial.joinexamples;

class Thread1 implements Runnable {

    @Override
    public void run() {
        for (int i = 0; i < 3; i ++) {
            System.out.println(Thread.currentThread().getName() + " " + i);
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Thread2 implements Runnable {
    @Override
    public void run() {
        Thread1 th = new Thread1();
        Thread thread = new Thread(th, "test thread 1");

        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 10; i ++) {
            System.out.println(Thread.currentThread().getName() + " " + i);
            try {
                Thread.currentThread().sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class JoinExample2 {
    public static void main(String[] args) {
        Thread2 th = new Thread2();
        Thread thread = new Thread(th, "test thread 2");

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Hello from main");
    }
}
