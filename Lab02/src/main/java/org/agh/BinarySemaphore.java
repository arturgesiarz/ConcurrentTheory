package org.agh;

class BinarySemaphore {
    private boolean isAvailable = true;

    public boolean isAvailable() {
        return isAvailable;
    }

    public synchronized void acquire()  {
        while (!isAvailable) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        isAvailable = false;
    }

    public synchronized void release() {
        isAvailable = true;
        notifyAll();
    }
}