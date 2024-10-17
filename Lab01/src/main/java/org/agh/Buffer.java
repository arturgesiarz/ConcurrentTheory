package org.agh;

import java.util.LinkedList;
import java.util.Queue;

public class Buffer {
    private final Queue<String> messages = new LinkedList<>();

    synchronized public String take() {
        while (messages.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        String messageTaken = messages.remove();
        notifyAll();
        return messageTaken;
    }

    synchronized public void put(String message) {
        while (!messages.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("Produced - " + message);
        messages.add(message);
        notifyAll();
    }

}
