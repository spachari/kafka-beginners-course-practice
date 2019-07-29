package learning;

/*
addShutdownHook() will register some actions which is to be performed on a Program's termination.
The program that you start ends in two ways:

1. the main thread (Root) ends its running context;
2. the program meets some unexpected situation, so it cannot proceed further.

If you add a ShutdownHook, the hook will start a thread that will start running at time of termination only.
For example:

      Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        System.out.println("Running Shutdown Hook");
      }
    });

You might have many situations like:

1. your program had created many temporary files in filesystem you want to delete it;
2. you need to send a distress signal to another process/machine before terminating;
execute any clean-up actions, logging or after-error actions on unexpected behaviours.

*/


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShutDownHook {
    Logger  logger = LoggerFactory.getLogger(ShutDownHook.class.getName());
    public static void main(String[] args) {

        new ShutDownHook().run();
        //There are two ways of getting around accessing a method from s static context
        //1. Making the method static
        //2. Creating your own empty constructor and accessing the method from the constructor

    }

    private ShutDownHook() {

    }

        public void run() {

            Runnable myTest = new Thread(new Test());
            ((Thread) myTest).start();

            Runtime.getRuntime().addShutdownHook(new Thread( () ->
                    logger.info("Shutting down job")));

        }


        class Test implements Runnable {

            @Override
            public void run() {
                try {
                    while (true) {
                        System.out.println("Doing something ... ");
                        Thread.sleep(1000);
                    }
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

}
