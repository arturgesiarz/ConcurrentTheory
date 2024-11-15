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

        // Tworzenie współdzielonego bufora
        Buffer buffer = new Buffer(BUFFER_SIZE, lock, notifiers);

         // Tworzenie producenta
        Thread producent = new Thread(new Producent(buffer, 0));

         // Tworzenie uszeregowanych procesów przetwarzających
        Thread[] processors = new Thread[5];

        for (int i = 1; i <= 5; i++) {
            // Każdy proces pobiera dane od poprzedniego procesu,
            // przetwarza je i sygnalizuje następnemu procesowi
            processors[i - 1] = new Thread(new Processor(buffer, i));
        }

         // Tworzenie i uruchamianie konsumenta
         // Konsument pobiera dane od ostatniego procesu i kończy działanie
        Thread consumer = new Thread(new Consumer(buffer, 6));

        // Startuje producent
        producent.start();

        // Uruchamianie wątków
        for (int i = 1; i <= 5; i++) {
            processors[i - 1].start();
        }

        // Startuje konsument
        consumer.start();

         // Oczekiwanie na zakończenie wszystkich wątków
        producent.join();

        for (Thread processor : processors)
            processor.join();

        consumer.join();
    }
}