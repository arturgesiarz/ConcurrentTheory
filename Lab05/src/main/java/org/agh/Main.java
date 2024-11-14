package org.agh;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException, IOException {
        // Obliczamy ilosc rdzeni procesora
        int coreNo = Runtime.getRuntime().availableProcessors();

        // Liczba watkow
        int[] threadNumbers = { 1, coreNo, 4 * coreNo };

        // Maksymalna iteracja
        int[] maxIters = { 200, 1000, 5000, 10000 };

        // Konfiguracja podstawowa
        final int runs = 10;
        final int width = 500;
        final int height = 500;
        final int zoom = 150;

        // Zapis CSV
        FileWriter fileWriter = new FileWriter("src/main/resources/results.csv");

        // Obliczanie przypadkow
        for (int maxIter : maxIters) {

            // Obliczanie w zaleznosci od liczby watkow
            for (int threadNo : threadNumbers) {

                // Liczba zadan do wykonania
                int[] taskNumbers = { threadNo, 10 * threadNo, width * height};

                for (int taskNo : taskNumbers) {

                    // Tablica otrzymanych czasow
                    ArrayList<Long> times = new ArrayList<>();
                    System.out.println(threadNo + " " + taskNo);

                    // Mierzenie 10 razy czasu dla tego samego przypadku
                    for (int i = 0; i < runs; i++) {
                        MandelbrotSimulation simulator = new MandelbrotSimulation(threadNo, taskNo,
                                maxIter, height, width, zoom);

                        long startTime = System.nanoTime();

                        simulator.simulate();

                        long endTime = System.nanoTime();
                        long elapsedTime = (endTime - startTime);

                        times.add(elapsedTime);
                    }

                    // Srednia
                    int avgTime = (int) times.stream().mapToDouble(Long::doubleValue).average().orElse(0);

                    // Odchylenie standardowe
                    int devTime = (int) Math.sqrt((times.stream().mapToDouble(i -> (i - avgTime) * (i - avgTime)).sum()) / (times.size()));

                    fileWriter.append(maxIter + "," + threadNo + "," + taskNo + "," + avgTime + "," + devTime+"\n");
                }
            }
        }
        fileWriter.close();
    }

}
