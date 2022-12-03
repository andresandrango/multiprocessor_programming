package src.main.java.com.example.cp;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class PetersonLock implements Lock {

    private volatile boolean[] flag = new boolean[2];
    private volatile int victim;

    @Override
    public void lock() {
        log("lock");
        int i = Integer.parseInt(Thread.currentThread().getName());
        int j = 1 - i;

        flag[i] = true;         // I'm interested
        victim = i;             // you go first

        while (flag[j] && victim == i) {} // wait
        log("done waiting");
    }


    @Override
    public void unlock() {
        int i = Integer.parseInt(Thread.currentThread().getName());
        flag[i] = false;
        log("unlock");
    }

    void log(String str) {
//        System.out.printf("[%s:%s] flag: %s\n", Thread.currentThread().getName(), str, Arrays.toString(flag));
//        System.out.printf("[%s:%s] victim: %s\n", Thread.currentThread().getName(), str, victim);
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

    @Override
    public Condition newCondition() {
        return null;
    }
}
