package org.agh.semaphores;

import org.agh.Counter;
import org.junit.jupiter.api.Test;


class BinarySemaphoreTest {

    @Test
    void isBinarySemaphoreWorks() throws InterruptedException {
        int INCREMENT_TIMES = 100000000;
        Counter counter = new Counter(0);

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < INCREMENT_TIMES; i++) {
                counter.synchronizedIncrement();
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < INCREMENT_TIMES; i++) {
                counter.synchronizedDecrement();
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        assert counter.getValue() == 0;
    }
}