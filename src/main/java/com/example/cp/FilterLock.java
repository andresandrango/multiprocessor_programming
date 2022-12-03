package src.main.java.com.example.cp;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class FilterLock implements Lock {

    int [] level;
    int [] victim;

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
        int me = Integer.parseInt(Thread.currentThread().getName());

        for (int i = 1; i < n; i++) {
            level[me] = i;
            victim[i] = me;

            while (atLeastOne(me, i)) {
//                System.out.printf("[%s] waiting ...\n", Thread.currentThread().getName());
                printme();
            } // wait
        }
    }

    void printme() {
//        System.out.print("levels:");
//        for (int l :level) {
//            System.out.printf("%d,", l);
//        }
//        System.out.println("");
//
//        System.out.print("victim:");
//        for (int v :victim) {
//            System.out.printf("%d,", v);
//        }
//        System.out.println("");
    }

    boolean atLeastOne(int me, int i) {
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
