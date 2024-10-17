package org.agh;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // ex 1 - witchout synchronized

        int INCREMENT_TIMES = 100000000;
        Counter counter = new Counter(0);

        Thread incrementThreadNonSynchronized = new Thread(() -> {
            for (int i = 0; i < INCREMENT_TIMES; i++) {
                counter.increment();
            }
        });
        Thread decrementThreadNonSynchronized = new Thread(() -> {
            for (int i = 0; i < INCREMENT_TIMES; i++) {
                counter.decrement();
            }
        });

        incrementThreadNonSynchronized.start();
        decrementThreadNonSynchronized.start();

        incrementThreadNonSynchronized.join();
        decrementThreadNonSynchronized.join();

        System.out.println("Ex1: Non synchronized result: " + counter.getSecretValue());

        // ex 2 - witch synchronized
        counter.setSecretValue(0);

        Thread incrementThreadSynchronized = new Thread(() -> {
            for (int i = 0; i < INCREMENT_TIMES; i++) {
                counter.synchronizedIncrement();
            }
        });
        Thread decrementThreadSynchronized = new Thread(() -> {
            for (int i = 0; i < INCREMENT_TIMES; i++) {
                counter.synchronizedDecrement();
            }
        });

        incrementThreadSynchronized.start();
        decrementThreadSynchronized.start();

        incrementThreadSynchronized.join();
        decrementThreadSynchronized.join();

        System.out.println("Ex2: Synchronized result: " + counter.getSecretValue());
//
//        // ex 3
//        Buffer buffer = new Buffer();
//        Producer producer = new Producer(buffer);
//        Consumer consumer = new Consumer(buffer);
    }
}