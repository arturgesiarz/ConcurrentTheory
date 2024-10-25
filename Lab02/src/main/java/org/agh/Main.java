package org.agh;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // Ex 1 - Zaimplementowac semafor binarny za pomoca metod wait i notify/notifyall,
        // uzyc go do synchronizacji wyscigu z poprzedniego laboratorium.

        int INCREMENT_TIMES = 100000000;
        Counter counter = new Counter(0);

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < INCREMENT_TIMES; i++) {
                counter.synchronizedIncrement();
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < INCREMENT_TIMES; i++) {
                counter.synchronizedDecrement();
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println(counter.getValue());

    }
}
