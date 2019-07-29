package learning.thread.tutorial.illegalThreadStateException;

class THRun implements Runnable {

    @Override
    public void run() {
        for (int i = 0; i < 3; i ++) {
            System.out.println(Thread.currentThread().getName() + " " + i);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class StartingAThreadThatIsInDeadState {
    public static void main(String[] args) {
        THRun th = new THRun();
        Thread thread = new Thread(th, "Test Thread");

        thread.start();

        try {
            System.out.println("This thread is going to sleep");
            thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //at learning.thread.tutorial.
        // illegalThreadStateException.StartingAThreadThatIsInDeadState.main(StartingAThreadThatIsInDeadState.java:33)

        thread.start();
    }
}
