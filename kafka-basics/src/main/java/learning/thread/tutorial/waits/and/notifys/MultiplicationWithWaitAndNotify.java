package learning.thread.tutorial.waits.and.notifys;

class Multiply2 implements Runnable {
    int multiplier;

    public Multiply2(int m) {
        multiplier = m;
    }


    @Override
    public void run() {
        synchronized (this) {
            System.out.println("Printing the 5 multiplication table");
            for (int i = 1; i <= 10; i++) {
                System.out.println("5 * " + i + " = " + (5 * i));
            }
            notify();
        }
    }
}

public class MultiplicationWithWaitAndNotify {
    public static void main(String[] args) {

        Multiply2 m = new Multiply2(5);
        Thread t1 = new Thread(m, "Test Thread");

        t1.start();
        //System.out.println("Hello from main block");

        synchronized (m) {
            try {
                m.wait();
                System.out.println("Printing the 6 multiplication table");
                for (int i = 1; i <= 10; i ++) {
                    System.out.println("6 * " + i + " = " + (6 * i));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}

//Main thread enters the synchronized block and it gets the lock on the object(ob) of B class.

//Main thread calls wait() method, doing so, it releases the lock on the object of B & stops its execution.

//In the run() method, Thread2 enters the synchronized block & gets the lock on the same object of B.
// It prints the table of 5, calls notify() to notify the waiting main thread & releases the lock on object of B.