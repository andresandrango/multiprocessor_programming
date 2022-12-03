package src.main.java.com.example.cp;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class FilterLock implements Lock {

    volatile int [] level;
    volatile int [] victim;

    int n;

    public FilterLock(int n) {
        this.n = n;
        level = new int[n];
        victim = new int[n];
        for (int i = 0; i < n; i++) {
            level[i] = 0;
        }
    }

    @Override
    public void lock() {
        // Unlike the book, the threads for this to work MUST be initialized with names "0" and "1"
        int me = Integer.parseInt(Thread.currentThread().getName());

        for (int i = 1; i < n; i++) {
            level[me] = i;
            victim[i] = me;

            while (atLeastOne(me, i)) {} // wait
        }
    }

    boolean atLeastOne(int me, int i) {
        // This is not pointed in the book but k here has to start at i + 1 otherwise it does not work!
        for (int k = i + 1; k < n; k++) {
            if (k != me && level[k] >= i && victim[i] == me) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void unlock() {
        int me = Integer.parseInt(Thread.currentThread().getName());
        level[me] = 0;
    }

    // We don't need to implement the rest of Lock functions
    @Override
    public Condition newCondition() {
        return null;
    }

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
}
