package org.agh.random.producer.consumer;

import java.io.FileWriter;
import java.io.IOException;

public class RandomProducerConsumer {

    public static void go(int bufferSize, int numberOfWorkers, FileWriter fileWriter)
            throws IOException, InterruptedException {

        Buffer buffer = new Buffer(bufferSize, fileWriter);

        Thread[] workers = new Thread[numberOfWorkers * 2];

        for (int i = 0; i < numberOfWorkers; i++) {
            workers[i] = new Thread(new Producer(buffer, bufferSize));
            workers[i + numberOfWorkers] = new Thread(new Consumer(buffer, bufferSize));
        }

        for (int i = 0; i < 2 * numberOfWorkers; i++) {
            workers[i].start();
        }

        for (int i = 0; i < 2 * numberOfWorkers; i++) {
            workers[i].join();
        }
    }

}
