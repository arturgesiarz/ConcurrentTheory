package org.agh.pipebuffer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Buffer {
    private int[] productionLine;  // Przechowuje stan każdej komórki bufora
    private Lock blocker;  // Blokada do synchronizacji procesów
    private Condition[] notifiers;  // Warunki dla każdego procesu

    public Buffer(int size, Lock blocker, Condition[] notifiers) {
        this.productionLine = new int[size];
        this.blocker = blocker;
        this.notifiers = notifiers;

        for (int i = 0; i < size; i++) {
            productionLine[i] = -1;
        }
    }

    public void startProcess(int cell, int processorId) throws InterruptedException {
        blocker.lock();
        try {
            while (productionLine[cell] != processorId - 1) {
                notifiers[processorId - 1].await();  // Oczekiwanie na proces poprzedzający
            }
            System.out.println("I got the cell " + cell + " process " + processorId);
        } finally {
            blocker.unlock();
        }
    }

    public void endProcess(int cell, int processorId) {
        blocker.lock();
        try {
            productionLine[cell] = processorId;
            if(processorId < 6) {
                notifiers[processorId].signal();
            }
        } finally {
            blocker.unlock();
        }
    }

    public int getSize() {
        return productionLine.length;
    }
}
