package learning.thread.tutorial.creating.a.newthread;

class MyThread extends Thread {

    MyThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        System.out.println("getting current thread info " + getName());
        try {
            System.out.println(getName() + " is going to sleep for 5 secs");
            currentThread().sleep(5000);
            System.out.println(getName() + " is finished sleeping for 5 secs");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class ExtendingTheThreadClass {
    public static void main(String[] args) {
        MyThread myThread = new MyThread("Test Thread");
        myThread.start();

        //In the main() method, an object of MyThread is created by calling it's constructor, which eventually calls its
        // superclass(Thread) constructor, passing it the name of thread - Test Thread.

        //Method start() automatically calls the overridden run() method, to start executing the new thread.
    }
}
