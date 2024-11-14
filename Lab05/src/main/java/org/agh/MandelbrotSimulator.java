package org.agh;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MandelbrotSimulator {
    private int threadNo;
    private int taskNo;
    private int maxIter;
    private int height;
    private int width;
    private double zoom;
    private BufferedImage image;

    public MandelbrotSimulator(int threadNo, int taskNo, int maxIter, int height, int width, double zoom) {
        this.threadNo = threadNo;
        this.taskNo = taskNo;
        this.maxIter = maxIter;
        this.height = height;
        this.width = width;
        this.zoom = zoom;
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public void simulate() throws ExecutionException, InterruptedException {
        // Tworzenia threadPoola
        ExecutorService threadPool = Executors.newFixedThreadPool(threadNo);

        // Przechowuje zadania ktore sa wkracie wykonywania lub sa zakonczone z puli watkow
        List<Future<Integer>> codes = new ArrayList<>();

        if (taskNo != (height * width)) {

            int step = height / (taskNo * threadNo);
            int y1 = 0, y2;

            for (int i = 0; (i + 1) < (taskNo * threadNo); i++) {
                y2 = y1 + step - 1;
                MandelbrotWorker worker = new MandelbrotWorker(0, y1, width - 1, y2,
                        maxIter, width, height, zoom, image);

                // Rozpoczecie zadanie i dodanie do listy
                codes.add(threadPool.submit(worker));
                y1 += step;
            }

            MandelbrotWorker worker = new MandelbrotWorker(0, y1, width - 1, height - 1,
                    maxIter, width, height, zoom, image);

            codes.add(threadPool.submit(worker));
        } else {

            for (int y = 0; y < height; y++)
                for (int x = 0; x < width; x++) {
                    MandelbrotWorker worker = new MandelbrotWorker(
                            x, y, x, y, maxIter, width, height, zoom, image);
                    codes.add(threadPool.submit(worker));
                }
        }

        // Czekamy na zakończenie wszystkich zadan, a nastepnie zwracamy wynik
        for (Future<Integer> value : codes)
            value.get();

        // Zamykamy pule watkow
        threadPool.shutdown();
    }

}
