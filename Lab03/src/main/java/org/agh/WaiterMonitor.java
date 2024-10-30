package org.agh;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WaiterMonitor {
    private final Lock lock = new ReentrantLock();
    private final Condition occupation = lock.newCondition();;
    private final Condition[] pairs;
    private final int[] booked;
    private int isOccupied = 0;


    public WaiterMonitor(int size) {
        pairs = new Condition[size];
        booked = new int[size];
        for (int i = 0; i < size; i++) {
            pairs[i] = lock.newCondition();
            booked[i] = 0;
        }
    }

    public void bookTable(int number) throws InterruptedException {
        lock.lock();
        try {
            while (isOccupied > 0) {
                occupation.await();
            }
            if (booked[number] == 0) {
                booked[number] = 1;
                while (booked[number] == 1) {
                    pairs[number].await();
                }
            } else {
                booked[number] = 0;
                isOccupied = 2;
                pairs[number].signal();
            }

        } finally {
            lock.unlock();
        }
    }

    public void releaseTable() {
        lock.lock();
        try {
            isOccupied--;
            if (isOccupied == 0) {
                occupation.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }
}
