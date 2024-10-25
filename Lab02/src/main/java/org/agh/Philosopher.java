package org.agh;

public class Philosopher extends Thread {
    private final BinarySemaphore leftFork;
    private final BinarySemaphore rightFork;

    public Philosopher(BinarySemaphore leftFork, BinarySemaphore rightFork) {
        this.leftFork = leftFork;
        this.rightFork = rightFork;
    }

    @Override
    public void run() {
        while (true) {
            // filozof myśli
            think();

            // próbuje zdobyć oba widelce
            leftFork.acquire();
            rightFork.acquire();

            // filozof je
            eat();

            // zwalnia oba widelce
            leftFork.release();
            rightFork.release();
        }
    }

    private void think() {
        System.out.println(Thread.currentThread().getName() + " myśli...");
        try {
            Thread.sleep((int) (Math.random() * 100)); // symulacja myślenia
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void eat() {
        System.out.println(Thread.currentThread().getName() + " je...");
        try {
            Thread.sleep((int) (Math.random() * 100)); // symulacja jedzenia
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
