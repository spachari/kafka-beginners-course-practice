package learning.thread.tutorial;

/*
A thread is a program in execution that is performing some specific task. In Java, a thread is automatically created and started
when the Java Virtual Machine(JVM) loads and this thread is called main thread. In order to execute a Java program, the main
thread looks for the main() method in it. This thread is called main thread because the main() method of a Java program is the first
method that runs in it.

Note : The main thread is provided to us by default, even when we didn't manually create a thread in a program. Besides the default
given main thread, we can create our own thread by creating an object of Thread class. A thread(main thread or a user created
thread )is an object of java.lang.Thread class.
*/

public class MainThreadInfo {
    public static void main(String[] args) {

        Thread th = Thread.currentThread();
        System.out.println("Thread information " + th);

        th.setName("test Thread");
        System.out.println("Thread's new information " + th);
        System.out.println("New name of the current Thread " + th.getName());

    }
}
