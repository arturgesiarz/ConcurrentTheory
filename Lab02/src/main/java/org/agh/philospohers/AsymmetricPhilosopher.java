package org.agh.philospohers;

import org.agh.semaphores.BinarySemaphore;

public class AsymmetricPhilosopher extends Thread {
    private final BinarySemaphore leftFork;
    private final BinarySemaphore rightFork;
    private final int philosopherId;

    public AsymmetricPhilosopher(int philosopherId, BinarySemaphore leftFork, BinarySemaphore rightFork) {
        this.philosopherId = philosopherId;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
    }

    @Override
    public void run() {
        while (true) {
            think();

            // Parzysty filozof zaczyna od prawego widelca
            if (philosopherId % 2 == 0) {
                rightFork.acquire();
                leftFork.acquire();
            }

            // Nieparzysty filozof zaczyna od lewego widelca
            else {
                leftFork.acquire();
                rightFork.acquire();
            }

            eat();

            rightFork.release();
            leftFork.release();
        }
    }

    private void think() {
        System.out.println(Thread.currentThread().getName() + " myśli...");
    }

    private void eat() {
        System.out.println(Thread.currentThread().getName() + " je...");
    }
}
