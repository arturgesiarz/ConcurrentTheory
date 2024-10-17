package org.agh;

public class Consumer implements Runnable {
    private Buffer buffer;

    public Consumer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
   public void run() {
        for(int i = 0;  i < 10;   i++) {
            String message = buffer.take();
            System.out.println("Consuming.... - " + message);
        }
    }
}
