package org.agh.fair.producer.consumer;


public class Producer extends Worker {
    public Producer(Buffer buffer, int limit) {
        super(buffer, limit);
    }

    @Override
    public void doIt(int randomInt) {
        this.buffer.put(randomInt);
    }

}
