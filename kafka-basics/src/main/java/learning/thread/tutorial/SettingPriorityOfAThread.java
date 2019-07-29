package learning.thread.tutorial;

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

public class SettingPriorityOfAThread {
    public static void main(String[] args) {
        ThreadEx th = new ThreadEx();
        Thread thread = new Thread(th, "test thread");
        thread.start();

    }
}
