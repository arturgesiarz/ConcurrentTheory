package org.agh;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PrintersMonitor {
    private final boolean[] printers;
    private final Lock lock = new ReentrantLock();
    private final Condition availablePrinter = lock.newCondition();

    public PrintersMonitor(int printersNumber) {
        printers = new boolean[printersNumber];
    }

    public int reserve() throws InterruptedException {
        lock.lock();
        try {
            while (true) {
                for (int i = 0; i < printers.length; i++) {
                    if (!printers[i]) {
                        printers[i] = true;
                        return i;
                    }
                }
                availablePrinter.await();
            }
        } finally {
            lock.unlock();
        }
    }

    public void release(int printerId) {
        lock.lock();
        try {
            printers[printerId] = false;
            availablePrinter.signal();
        } finally {
            lock.unlock();
        }
    }
}
