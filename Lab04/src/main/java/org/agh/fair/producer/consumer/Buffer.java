package org.agh.fair.producer.consumer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {
    private ReentrantLock lock = new ReentrantLock();
    private int maxCapacity;
    private int counter = 0;
    private FileWriter fileWriter;
    private Condition[] conditions = new Condition[4];

    public Buffer(int limit, FileWriter fileWriter) {
        this.maxCapacity = 2 * limit;
        this.fileWriter = fileWriter;
        for (int i = 0; i < 4; i++) {
            conditions[i] = lock.newCondition();
        }
    }

    public void get(int amount) {
        lock.lock();
        try {
            long start = System.nanoTime();
            if (lock.hasWaiters(conditions[2])) {
                conditions[3].await();
            }
            while (counter < amount) {
                if (!conditions[2].await(100, TimeUnit.MILLISECONDS)) {
                    conditions[3].signal();
                    return;
                }
            }
            long end = System.nanoTime();
            counter -= amount;
            fileWriter.append("Consumer," + amount + "," + (end - start) + "\n");
            conditions[0].signal();
            conditions[3].signal();
        } catch (InterruptedException e) {
            try {
                fileWriter.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void put(int amount) {
        lock.lock();
        try {
            long start = System.nanoTime();
            if (lock.hasWaiters(conditions[0])) {
                conditions[1].await();
            }
            while (counter + amount > maxCapacity) {
                if (!conditions[0].await(100, TimeUnit.MILLISECONDS)) {
                    conditions[1].signal();
                    return;
                }
            }
            long end = System.nanoTime();
            counter += amount;
            fileWriter.append("Producer," + amount + "," + (end - start) + "\n");
            conditions[2].signal();
            conditions[1].signal();
        } catch (InterruptedException e) {
            try {
                fileWriter.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

}
