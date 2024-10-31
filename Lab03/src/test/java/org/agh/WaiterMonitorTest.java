package org.agh;

import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class WaiterMonitorTest {

    @Test
    void waiterMonitorTest() {
        final int PAIRS_NUMBER = 4;

        WaiterMonitor waiterMonitor = new WaiterMonitor(PAIRS_NUMBER);
        ExecutorService executor = Executors.newFixedThreadPool(PAIRS_NUMBER);

        for (int i = 0; i < PAIRS_NUMBER * 2; i++) {
            int id = i;
            executor.submit(() -> {
               try {
                   Random rand = new Random();

                   Thread.sleep(rand.nextInt(500));
                   System.out.println("Booking table for  " + id / 2);
                   waiterMonitor.bookTable(id / 2);

                   Thread.sleep(rand.nextInt(500));
                   waiterMonitor.releaseTable();
                   System.out.println("Finished  " + id / 2);

               } catch (InterruptedException e) {
                   Thread.currentThread().interrupt();
               }
            });

        }

        executor.shutdown();
        while (!executor.isTerminated()) {}

    }
}