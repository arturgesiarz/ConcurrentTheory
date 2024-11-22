package org.agh.fair.producer.consumer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {
    private ReentrantLock lock = new ReentrantLock();
    private int maxCapacity;
    private int counter = 0;

    private FileWriter fileWriter;

    // Tablica warunków aby rozróżnić producentów i konsumentów.
    // conditions[0]: Oczekiwanie producentów, gdy bufor jest pełny.
    // conditions[1]: Sygnalizowanie producentom, gdy konsumenci zwolnią miejsce.
    // conditions[2]: Oczekiwanie konsumentów, gdy bufor jest pusty.
    // conditions[3]: Sygnalizowanie konsumentom, gdy producenci wstawią dane.
    private Condition[] conditions = new Condition[4];

    public Buffer(int limit, FileWriter fileWriter) {
        this.maxCapacity = 2 * limit;
        this.fileWriter = fileWriter;

        for (int i = 0; i < 4; i++) {
            conditions[i] = lock.newCondition();
        }
    }

    public void get(int amount) {
        lock.lock();

        try {
            long start = System.nanoTime();

            // Sprawdzamy czy na warunku conditions[2] czekają konsumenci
            if (lock.hasWaiters(conditions[2])) {

                // Jeśli tak, konsument bieżący czeka na sygnał od producenta (conditions[3].await()),
                // aby umożliwić równoważny dostęp do bufora.
                conditions[3].await();
            }

            // Oczekiwanie na odpowiednią liczbę elementów w buforze
            while (counter < amount) {

                // Konsument czeka maksymalnie 100 ms na sygnał od producenta.
                if (!conditions[2].await(100, TimeUnit.MILLISECONDS)) {

                    // Konsument sygnalizuje innym konsumentom
                    conditions[3].signal();
                    return;
                }
            }
            long end = System.nanoTime();

            counter -= amount;

            fileWriter.append("Consumer," + amount + "," + (end - start) + "\n");

            //  Informuje producentów, że zwolniono miejsce w buforze.
            conditions[0].signal();

            // Informuje innych konsumentów, że mogą próbować pobrać elementy.
            conditions[3].signal();

        } catch (InterruptedException e) {
            try {
                fileWriter.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void put(int amount) {
        lock.lock();
        try {
            long start = System.nanoTime();

            // Sprawdza, czy na warunku conditions[0] czekają producenci.
            if (lock.hasWaiters(conditions[0])) {

                // Bieżący producent czeka na sygnał od konsumenta (conditions[1].await()),
                // aby zapewnić równoważny dostęp.
                conditions[1].await();
            }

            // Producent czeka, jeśli bufor jest pełny
            while (counter + amount > maxCapacity) {

                // Producent czeka maksymalnie 100 ms na sygnał od konsumenta.
                if (!conditions[0].await(100, TimeUnit.MILLISECONDS)) {

                    // Jeśli nie ma miejsca w buforze, sygnalizuje innym producentom i kończy swoje działanie
                    conditions[1].signal();
                    return;
                }
            }

            long end = System.nanoTime();

            counter += amount;

            fileWriter.append("Producer," + amount + "," + (end - start) + "\n");

            // Informuje konsumentów, że w buforze pojawiły się nowe elementy.
            conditions[2].signal();

            // Informuje innych producentów o potencjalnym miejscu w buforze.
            conditions[1].signal();

        } catch (InterruptedException e) {
            try {
                fileWriter.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

}
