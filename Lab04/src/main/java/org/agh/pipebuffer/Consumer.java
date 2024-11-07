package org.agh.pipebuffer;


public class Consumer extends Processor {
    public Consumer(Buffer buffer, int id) {
        super(buffer, id);
    }

    @Override
    public void method(int i) {
        super.method(i);
        System.out.println("Consumer have eaten cell nr " + i);
    }
}
