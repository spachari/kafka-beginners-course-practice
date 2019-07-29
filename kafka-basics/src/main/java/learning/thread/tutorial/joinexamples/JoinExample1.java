package learning.thread.tutorial.joinexamples;

class ThreadEx implements Runnable {

    @Override
    public void run() {
        Thread th = Thread.currentThread();
        System.out.println("Current thread's name is " + th.getName());
        System.out.println("Current Thread's priority is " + th.getPriority());
        th.setPriority(Thread.MIN_PRIORITY);
        System.out.println("Current Thread's priority is " + th.getPriority());
        th.setPriority(Thread.MAX_PRIORITY);
        System.out.println("Current Thread's priority is " + th.getPriority());
    }
}

public class JoinExample1 {
    public static void main(String[] args) {
        ThreadEx threadEx = new ThreadEx();
        Thread thread = new Thread(threadEx, "test thread");

        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Hello from main");

    }
}


//join() method call is made inside the main() method, hence it makes the main
// thread(default thread) wait for Thread2 to finish its execution