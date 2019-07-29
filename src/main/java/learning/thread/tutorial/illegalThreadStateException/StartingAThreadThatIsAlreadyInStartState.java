package learning.thread.tutorial.illegalThreadStateException;

class ThreadEx implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 4 ; i ++) {
            System.out.println(Thread.currentThread() + " " + i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class StartingAThreadThatIsAlreadyInStartState {
    public static void main(String[] args) {

        ThreadEx th = new ThreadEx();
        Runnable thread = new Thread(th, "Test Thread");

        ((Thread) thread).start();


        ((Thread) thread).start();

        System.out.println("hello from main thread");

    }
}