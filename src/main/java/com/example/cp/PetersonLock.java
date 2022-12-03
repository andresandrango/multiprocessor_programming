package src.main.java.com.example.cp;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class PetersonLock implements Lock {

    // Note: Not pointed out in the chapter 2 example but these have to be set to "volatile"
    // TODO write the explanation
    private volatile boolean[] flag = new boolean[2];
    private volatile int victim;

    @Override
    public void lock() {
        // Unlike the book, the threads for this to work MUST be initialized with names "0" and "1"
        int i = Integer.parseInt(Thread.currentThread().getName());
        int j = 1 - i;

        flag[i] = true;         // I'm interested
        victim = i;             // you go first

        while (flag[j] && victim == i) {} // wait
    }


    @Override
    public void unlock() {
        int i = Integer.parseInt(Thread.currentThread().getName());
        flag[i] = false;
    }

    // We don't need to implement the rest of Lock functions

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(final long time, final TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
