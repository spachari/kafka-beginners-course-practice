package learning.thread.tutorial;

public class ThreadSleep {
    public static void main(String[] args) {
        Thread th = Thread.currentThread();
        System.out.println("Current thread information " + th.getName());
        System.out.println("Name of the thread " + th.getName());


        try {
            System.out.println("Thread sleeping for 5 secs");
            Thread.sleep(5000);
            System.out.println("Main thread awakened");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}


//A process is simply a program in execution. For example a WordPad program used being used to edit a document is a process.

//thread is a part of a program that is running concurrently with other parts of the program.
// For example, when you are using the WordPad program, you can edit a document and can also print another document at the same time.