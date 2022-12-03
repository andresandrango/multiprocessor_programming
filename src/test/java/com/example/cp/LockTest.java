package src.test.java.com.example.cp;

import org.junit.Test;
import src.main.java.com.example.cp.FilterLock;
import src.main.java.com.example.cp.PetersonLock;

import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class MyRunnable implements Runnable {
    Lock lock;
    String message;

    public MyRunnable(Lock lock, String message) {
        this.lock = lock;
        this.message = message;
    }

    @Override
    public void run() {
        System.out.printf("[%d] starting... \n", Thread.currentThread().getId());
        while (printLetter()) {}
        System.out.printf("[%d] done\n", Thread.currentThread().getId());
    }

    boolean printLetter() {
        if (message.length() == 0) return false;
        lock.lock();
        System.out.printf("[%d] %c\n", Thread.currentThread().getId(), message.charAt(0));
        message = message.substring(1);

        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            System.out.println("issues with sleep " + ex);
        }

        lock.unlock();
        return true;
    }
}


public class LockTest {

    @Test
    public void test_success_with_regular_lock() {
        Lock lock = new ReentrantLock(true);

        lockTestFactory(lock);
    }

    @Test
    public void test_success_with_filterlock() {
        Lock lock = new FilterLock(2);

        lockTestFactory(lock);
    }

    @Test
    public void test_success_with_peterson_lock() {
        Lock lock = new PetersonLock();

        lockTestFactory(lock);
    }

    void lockTestFactory(Lock lock) {

        MyRunnable r1 = new MyRunnable(lock, "abcde");
        MyRunnable r2 = new MyRunnable(lock, "xyz");

        Thread A = new Thread(r1, "0");
        Thread B = new Thread(r2, "1");
        A.start();
        B.start();

        try {
            Thread.sleep(3000);
        } catch (Exception ex) {
            System.out.println("issues with sleep" + ex);
        }

        assert Objects.equals(r1.message, "");
        assert Objects.equals(r2.message, "");
    }
}
