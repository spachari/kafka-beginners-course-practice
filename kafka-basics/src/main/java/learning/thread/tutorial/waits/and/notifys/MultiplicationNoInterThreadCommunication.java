package learning.thread.tutorial.waits.and.notifys;

class Multiply1 implements Runnable {
    int multiplier;
    public Multiply1 (int m) {
        multiplier = m;
    }


    @Override
    public void run() {
        synchronized (this) {
            System.out.println("Thread 2 running and printing table of 5");
            for (int i = 1; i <= 10; i++) {
                System.out.println(" 5 * " + i + " = " + (5 * i));
            }
        }
    }
}

public class MultiplicationNoInterThreadCommunication {
    public static void main(String[] args) {
        Multiply1 m = new Multiply1(5);
        Thread t1 = new Thread(m, "Thread 2");

        t1.start();

        synchronized (m) {
            System.out.println("Main thread running 6 multiplication program");
            for (int i = 1; i <= 10; i ++) {
                System.out.println(" 6 * " + i + " = " + (6 * i));
            }
        }

        //Main thread enters the synchronized block, gets the lock on the object(ob) of B class and prints table of 6.
        // It releases the lock on the object(ob) of B class.

        //In the run() method, Thread2 enters the synchronized block & gets the lock on the same object of B. It prints
        // the table of 5. Hence, table of 6 is printed before table of 5, bothering our ascending order.
    }
}
