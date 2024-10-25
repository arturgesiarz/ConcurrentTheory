package org.agh.philospohers;

import org.agh.semaphores.BinarySemaphore;
import org.junit.jupiter.api.Test;


class TrivialPhilosopherTest {

    @Test
    void isTrivialPhilosophersWorks() throws InterruptedException {
        int numPhilosophers = 5;
        BinarySemaphore[] forks = new BinarySemaphore[numPhilosophers];

        // Inicjalizujemy widelce jako semafory binarne
        for (int i = 0; i < numPhilosophers; i++) {
            forks[i] = new BinarySemaphore();
        }
        // 1. Rozwiązanie trywialne
        TrivialPhilosopher[] trivialPhilosophers = new TrivialPhilosopher[numPhilosophers];
        for (int i = 0; i < numPhilosophers; i++) {
            trivialPhilosophers[i] = new TrivialPhilosopher(forks[i], forks[(i + 1) % numPhilosophers]);
            trivialPhilosophers[i].start();
        }

        for (TrivialPhilosopher philosopher : trivialPhilosophers) {
            philosopher.join();
        }
    }
}