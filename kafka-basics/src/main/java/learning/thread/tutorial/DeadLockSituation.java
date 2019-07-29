package learning.thread.tutorial;

class A {
    synchronized public void m(A a, B b) {
        System.out.println(Thread.currentThread().getName());
        System.out.println("thread is in A's method, trying to call B's m");
        b.m(a,b);
    }

}

class B {
    synchronized public void m(A a, B b) {
        System.out.println(Thread.currentThread().getName());
        System.out.println("thread is in B's method, trying to call A's m");
        a.m(a,b);
    }
}

class C implements Runnable {
    A a = new A();
    B b = new B();

    Thread t;
    C() {
        t = new Thread(this, "Test thread");
        t.start();
        a.m(a,b);
    }


    @Override
    public void run() {
        b.m(a,b);
    }
}

public class DeadLockSituation {
    public static void main(String[] args) {
        new C();

    }
}
