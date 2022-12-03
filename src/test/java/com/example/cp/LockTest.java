package src.test.java.com.example.cp;

import org.junit.Test;
import src.main.java.com.example.cp.FilterLock;
import src.main.java.com.example.cp.PetersonLock;

import java.util.ArrayList;
import java.util.List;
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

        lockTestFactory(lock, 2);
    }

    @Test
    public void test_success_with_filterlock() {
        Lock lock = new FilterLock(2);

        lockTestFactory(lock, 2);
    }

    @Test
    public void test_success_with_filterlock_gt_2() {
        Lock lock = new FilterLock(10);

        lockTestFactory(lock, 10);
    }

    @Test
    public void test_success_with_peterson_lock() {
        Lock lock = new PetersonLock();

        lockTestFactory(lock, 2);
    }

    void lockTestFactory(Lock lock, int n){

        List<Thread> threads = new ArrayList<>();
        List<MyRunnable> runnables = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            runnables.add(new MyRunnable(lock, String.valueOf(i) + "abc"));
            threads.add(new Thread(runnables.get(i), String.valueOf(i)));
        }

        threads.forEach(Thread::start);

        try {
            Thread.sleep(3000);
        } catch (Exception ex) {
            System.out.println("issues with sleep" + ex);
        }

        runnables.forEach(r -> {
            assert Objects.equals(r.message, "");
        });


    }
}
