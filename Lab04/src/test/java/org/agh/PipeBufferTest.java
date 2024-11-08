package org.agh;


import org.agh.pipebuffer.Buffer;
import org.agh.pipebuffer.Consumer;
import org.agh.pipebuffer.Processor;
import org.agh.pipebuffer.Producent;
import org.junit.jupiter.api.Test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class PipeBufferTest {

    @Test
    void pipeBufferTest() throws InterruptedException {
        final int BUFFER_SIZE = 100;

        Lock lock = new ReentrantLock();
        Condition[] notifiers = new Condition[6];

        for (int i = 0; i < 6; i++) {
            notifiers[i] = lock.newCondition();
        }

        Buffer buffer = new Buffer(BUFFER_SIZE, lock, notifiers);
        Thread producent = new Thread(new Producent(buffer, 0));
        Thread[] processors = new Thread[5];

        for (int i = 1; i <= 5; i++) {
            processors[i - 1] = new Thread(new Processor(buffer, i));
        }

        Thread consumer = new Thread(new Consumer(buffer, 6));
        producent.start();

        for (int i = 1; i <= 5; i++) {
            processors[i - 1].start();
        }

        consumer.start();

        producent.join();

        for (Thread processor : processors)
            processor.join();

        consumer.join();
    }
}