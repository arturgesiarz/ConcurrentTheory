package org.agh;

public class Counter {
    private int secretValue;

    public Counter(int secretValue) {
        this.secretValue = secretValue;
    }

    public int getSecretValue() {
        return secretValue;
    }

    public void setSecretValue(int secretValue) {
        this.secretValue = secretValue;
    }

    public void increment() {
        secretValue++;
    }

    public void decrement() {
        secretValue--;
    }

    synchronized public void synchronizedIncrement() {
        secretValue++;
    }

    synchronized public void synchronizedDecrement() {
        secretValue--;
    }
}
