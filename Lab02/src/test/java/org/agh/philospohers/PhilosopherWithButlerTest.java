package org.agh.philospohers;

import org.agh.semaphores.BinarySemaphore;
import org.agh.semaphores.CountingSemaphore;
import org.junit.jupiter.api.Test;

class PhilosopherWithButlerTest {

    @Test
    void isPhilosophersWithButlerWorks() throws InterruptedException {
        int numPhilosophers = 5;
        BinarySemaphore[] forks = new BinarySemaphore[numPhilosophers];

        // Inicjalizujemy widelce jako semafory binarne
        for (int i = 0; i < numPhilosophers; i++) {
            forks[i] = new BinarySemaphore();
        }

        // 3. Rozwiązanie z lokajem
        CountingSemaphore butler = new CountingSemaphore(numPhilosophers - 1);
        PhilosopherWithButler[] butlerPhilosophers = new PhilosopherWithButler[numPhilosophers];

        for (int i = 0; i < numPhilosophers; i++) {
            butlerPhilosophers[i] = new PhilosopherWithButler(forks[i], forks[(i + 1) % numPhilosophers], butler);
            butlerPhilosophers[i].start();
        }

        for (PhilosopherWithButler philosopher : butlerPhilosophers) {
            philosopher.join();
        }
    }
}