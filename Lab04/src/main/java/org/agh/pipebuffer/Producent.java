package org.agh.pipebuffer;

public class Producent extends Processor {
    public Producent(Buffer buffer, int id) {
        super(buffer, id);
    }

    @Override
    public void method(int i) {
        super.method(i);
        System.out.println("Producent have created cell number: " + i);
    }
}
