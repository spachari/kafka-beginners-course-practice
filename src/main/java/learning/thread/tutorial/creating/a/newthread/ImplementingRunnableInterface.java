package learning.thread.tutorial.creating.a.newthread;

class RunnableThread implements Runnable {

    @Override
    public void run() {
        Thread th = Thread.currentThread();
        System.out.println("Thread's name is " + th.getName());

        try {
            System.out.println(th.getName() + " is going to sleep for 5 secs");
            Thread.currentThread().sleep(5000);
            System.out.println(th.getName() + " is finished sleeping for 5 secs");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class ImplementingRunnableInterface {
    public static void main(String[] args) {
        Runnable thread = new RunnableThread();
        Thread newTHread = new Thread(thread, "test thread");
        newTHread.start();

        //In the main() method, we have created an object of RunnableThread class and have passed its reference(thread),
        // name of new thread- New Thread to Thread's constructor, to create a new thread.
    }
}
