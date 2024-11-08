package org.agh.random.producer.consumer;

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
    private Condition consumerCond;
    private Condition producerCond;

    public Buffer(int limit, FileWriter fileWriter) {
        this.consumerCond = lock.newCondition();
        this.producerCond = lock.newCondition();
        this.maxCapacity = 2 * limit;
        this.fileWriter = fileWriter;
    }

    public void get(int amount) {
        lock.lock();
        try {
            long start = System.nanoTime();
            while (counter < amount) {
                if (!consumerCond.await(100, TimeUnit.MILLISECONDS)) {
                    consumerCond.signal();
                    return;
                }
            }
            long end = System.nanoTime();
            counter -= amount;
            fileWriter.append("Consumer," + amount + "," + (end - start) + "\n");
            producerCond.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            lock.unlock();
        }
    }

    public void put(int amount) {
        lock.lock();
        try {
            long start = System.nanoTime();
            while (counter + amount > maxCapacity) {
                if (!producerCond.await(100, TimeUnit.MILLISECONDS)) {
                    producerCond.signal();
                    return;
                }
            }
            long end = System.nanoTime();
            counter += amount;
            fileWriter.append("Producer," + amount + "," + (end - start) + "\n");
            consumerCond.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

}
