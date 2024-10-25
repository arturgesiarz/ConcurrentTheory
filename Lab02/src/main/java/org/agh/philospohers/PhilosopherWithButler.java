package org.agh.philospohers;

import org.agh.semaphores.BinarySemaphore;
import org.agh.semaphores.CountingSemaphore;

public class PhilosopherWithButler extends Thread {
    private final BinarySemaphore leftFork;
    private final BinarySemaphore rightFork;
    private final CountingSemaphore butler;

    public PhilosopherWithButler(BinarySemaphore leftFork, BinarySemaphore rightFork, CountingSemaphore butler) {
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.butler = butler;
    }

    @Override
    public void run() {
        while (true) {
            think();
            butler.acquire(); // Filozof prosi o pozwolenie od lokaja
            leftFork.acquire();
            rightFork.acquire();
            eat();
            leftFork.release();
            rightFork.release();
            butler.release(); // Zwalnia lokaja, pozwalając innym filozofom jeść
        }
    }

    private void think() {
        System.out.println(Thread.currentThread().getName() + " myśli...");
    }

    private void eat() {
        System.out.println(Thread.currentThread().getName() + " je...");
    }
}