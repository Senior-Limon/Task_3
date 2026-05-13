package org.malashenko.task;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.malashenko.task.model.Train;
import org.malashenko.task.singleton.StationConfig;
import org.malashenko.task.station.TrainStation;
import org.malashenko.task.station.impl.TrainStationImpl;
import org.malashenko.task.util.impl.DataLoaderImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final Logger log = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            log.info("=== Starting Railway Station Simulation ===");

            // Singleton setup from file
            StationConfig config = StationConfig.getInstance();
            log.info("Station Configuration: maxTracks={}, maxWagonCapacity={}",
                    config.getTrackCount(), config.getMaxWagonCapacity());



            // Create Station
            TrainStation station = new TrainStationImpl();

            List<DataLoaderImpl.TrainData> trainsData = DataLoaderImpl.loadTrains();
            log.info("Loaded {} trains from file", trainsData.size());


            try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
                List<Future<String>> futures = new ArrayList<>();

                for (DataLoaderImpl.TrainData data : trainsData) {
                    Train train = new Train(data.name, data.wagonCount, station);
                    log.debug("Submitting train: {} with {} wagons", data.name, data.wagonCount);
                    futures.add(executor.submit(train));
                    // interval
                    TimeUnit.MILLISECONDS.sleep(300);
                }

                // wait all trains and see res
                for (Future<String> future : futures) {
                    String result = future.get();
                    log.info("Result: {}", result);
                }
            }

            log.info("=== All trains processed. Simulation finished ===");


        } catch (Exception e) {
            log.error("Fatal error in main application", e);
            e.printStackTrace();
        }
    }
}