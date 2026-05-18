package org.malashenko.task;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.malashenko.task.model.Train;
import org.malashenko.task.util.impl.DataLoaderImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Starting train station simulation");

        List<DataLoaderImpl.TrainData> trainsData = DataLoaderImpl.loadTrains();
        logger.info("Loaded {} trains from file", trainsData.size());

        ExecutorService executorService = Executors.newFixedThreadPool(trainsData.size());
        List<Future<String>> futures = new ArrayList<>();

        for (DataLoaderImpl.TrainData data : trainsData) {
            Train train = new Train(data.name, data.wagonCount);
            futures.add(executorService.submit(train));
        }

        for (Future<String> future : futures) {
            try {
                String result = future.get();
                logger.info("Result: {}", result);
            } catch (InterruptedException | ExecutionException e) {
                logger.error("Train execution failed", e);
                Thread.currentThread().interrupt();
            }
        }

        executorService.shutdown();
        logger.info("Simulation finished");
    }
}