package org.agh;

import org.agh.semaphores.BinarySemaphore;

public class Counter {
    private int value;
    private final BinarySemaphore semaphore = new BinarySemaphore();

    public int getValue() {
        return value;
    }

    public Counter(int initialValue) {
        this.value = initialValue;
    }

    public void synchronizedIncrement() {
        semaphore.acquire();
        value++;
        semaphore.release();
    }

    public void synchronizedDecrement() {
        semaphore.acquire();
        value--;
        semaphore.release();
    }


}