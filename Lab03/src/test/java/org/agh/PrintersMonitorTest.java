package org.agh;

import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class PrintersMonitorTest {

    @Test
    void printerMonitorTest() throws InterruptedException {
        final int PRINTERS_NUMBER = 3;
        final int THREADS_NUMBER = 5;


        PrintersMonitor printersMonitor = new PrintersMonitor(PRINTERS_NUMBER);
        ExecutorService executor = Executors.newFixedThreadPool(THREADS_NUMBER);

        for (int i = 0; i < THREADS_NUMBER; i++) {
            int id = i;
            executor.submit(() -> {
                try {
                    Random rand = new Random();
                    while (true) {
                        System.out.println("Thread - " + id + ": creating task...");
                        Thread.sleep(rand.nextInt(500));

                        int nrDrukarki = printersMonitor.reserve();
                        System.out.println("Thread - " + id + ": printer - " + nrDrukarki + " is reserved");

                        System.out.println("Thread - " + id + ": printer - " + nrDrukarki + " is being worked");
                        Thread.sleep(rand.nextInt(1000));

                        printersMonitor.release(nrDrukarki);
                        System.out.println("Thread - " + id + ": printer - " + nrDrukarki + " is released");

                        Thread.sleep(rand.nextInt(500));
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        executor.shutdown();
        boolean _ = !executor.awaitTermination(2, TimeUnit.SECONDS);

    }
}