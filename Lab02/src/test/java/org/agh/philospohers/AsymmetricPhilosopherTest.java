package org.agh.philospohers;

import org.agh.semaphores.BinarySemaphore;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AsymmetricPhilosopherTest {

    @Test
    void isAsymmetricPhilosophersWorks() throws InterruptedException {
        int numPhilosophers = 5;
        BinarySemaphore[] forks = new BinarySemaphore[numPhilosophers];

        // Inicjalizujemy widelce jako semafory binarne
        for (int i = 0; i < numPhilosophers; i++) {
            forks[i] = new BinarySemaphore();
        }

        // 2. Rozwiązanie asymetryczne
        AsymmetricPhilosopher[] asymmetricPhilosophers = new AsymmetricPhilosopher[numPhilosophers];
        for (int i = 0; i < numPhilosophers; i++) {
            asymmetricPhilosophers[i] = new AsymmetricPhilosopher(i, forks[i], forks[(i + 1) % numPhilosophers]);
            asymmetricPhilosophers[i].start();
        }

        for (AsymmetricPhilosopher philosopher : asymmetricPhilosophers) {
            philosopher.join();
        }
    }
}