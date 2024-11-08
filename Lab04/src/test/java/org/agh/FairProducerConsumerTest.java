package org.agh;

import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;

import static org.agh.random.producer.consumer.RandomProducerConsumer.go;

public class FairProducerConsumerTest {

    @Test
    public void fairProducerConsumerTest() throws IOException, InterruptedException {
        FileWriter fileWriter = new FileWriter("src/test/resources/fair-producer-consumer-results.csv");
        go(1000, 10, fileWriter);
        go(10000, 100, fileWriter);
        go(100000, 1000, fileWriter);
        fileWriter.close();
    }
}
