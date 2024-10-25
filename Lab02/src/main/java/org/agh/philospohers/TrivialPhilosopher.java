package org.agh.philospohers;

import org.agh.semaphores.BinarySemaphore;

public class TrivialPhilosopher extends Thread {
    private final BinarySemaphore leftFork;
    private final BinarySemaphore rightFork;

    public TrivialPhilosopher(BinarySemaphore leftFork, BinarySemaphore rightFork) {
        this.leftFork = leftFork;
        this.rightFork = rightFork;
    }

    @Override
    public void run() {
        while (true) {
            think();
            rightFork.acquire(); // Podnosi prawy widelec
            leftFork.acquire();  // Podnosi lewy widelec
            eat();
            rightFork.release(); // Odkłada prawy widelec
            leftFork.release();  // Odkłada lewy widelec
        }
    }

    private void think() {
        System.out.println(Thread.currentThread().getName() + " myśli...");
    }

    private void eat() {
        System.out.println(Thread.currentThread().getName() + " je...");
    }
}
