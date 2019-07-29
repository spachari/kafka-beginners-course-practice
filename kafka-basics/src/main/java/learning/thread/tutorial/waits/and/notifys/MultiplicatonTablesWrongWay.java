package learning.thread.tutorial.waits.and.notifys;

class Multiply implements Runnable {

    int m;
     public Multiply(int multiplier) {
        m = multiplier;
    }

    @Override
    synchronized public void run() {
        for (int i = 1; i <= 10; i ++) {
            System.out.println(i + " * " + m + " = " + i * m);
        }
    }
}

public class MultiplicatonTablesWrongWay {
    public static void main(String[] args) {
        Multiply m = new Multiply(5);
        Multiply m1 = new Multiply(6);

        Thread t1 = new Thread(m, "5 Multiplier");
        Thread t2 = new Thread(m1, "6 Multiplier");

        t1.start();
        t2.start();
    }
}
