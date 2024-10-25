package org.agh.semaphores;

public class CountingSemaphore {
    private int signals;

    public CountingSemaphore(int initial) {
        this.signals = initial;
    }

    public synchronized void acquire()  {
        while (signals == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        signals--;
    }

    public synchronized void release() {
        signals++;
        notifyAll();
    }
}