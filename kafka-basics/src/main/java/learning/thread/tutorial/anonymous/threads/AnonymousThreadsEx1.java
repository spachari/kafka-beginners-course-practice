package learning.thread.tutorial.anonymous.threads;



//Creating an anonymous thread by extending the Thread class.

public class AnonymousThreadsEx1 {
    public static void main(String[] args) {

        Thread t = new Thread() {
            @Override
            public void run() {
                setName("Test Thread");
                for(int i = 0; i < 3; i ++) {
                    System.out.println("Hello from " + Thread.currentThread().getName() + " " + i);
                }
            }
        };
        t.start();
        System.out.println("Main thread is " + Thread.currentThread().getName());
    }
}
