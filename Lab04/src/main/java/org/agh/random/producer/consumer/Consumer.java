package org.agh.random.producer.consumer;

public class Consumer extends Worker {
    public Consumer(Buffer buffer, int limit) {
            super(buffer, limit);
    }

    @Override
    public void doIt(int randomInt) {
        this.buffer.get(randomInt);
    }
}
