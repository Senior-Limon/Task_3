package org.malashenko.task.util.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class DataLoaderImpl {
    private static final Logger log = LogManager.getLogger(DataLoaderImpl.class);

    private static final String CONFIG_FILE = "src/main/resources/config.txt";
    private static final String TRAINS_FILE = "src/main/resources/trains.txt";

    public static int loadMaxWagonCapacity() {
        return loadIntFromConfig("maxWagonCapacity");
    }

    public static int loadTrackCount() {
        return loadIntFromConfig("trackCount");
    }

    private static int loadIntFromConfig(String key) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(CONFIG_FILE));
            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                if (line.startsWith(key + "=")) {
                    int value = Integer.parseInt(line.split("=")[1].trim());
                    log.debug("Loaded config: {} = {}", key, value);
                    return value;
                }
            }
        } catch (IOException e) {
            log.error("Failed to load config file: {}", CONFIG_FILE, e);
            throw new RuntimeException("Failed to load config file", e);
        }
        throw new RuntimeException(key + " not found in config file");
    }

    public static List<TrainData> loadTrains() {
        try {
            return Files.readAllLines(Paths.get(TRAINS_FILE))
                    .stream()
                    .filter(line -> !line.trim().isEmpty() && !line.trim().startsWith("#"))
                    .map(line -> {
                        String[] parts = line.split(",");
                        String name = parts[0].trim();
                        int wagonCount = Integer.parseInt(parts[1].trim());
                        log.debug("Loaded train: {} with {} wagons", name, wagonCount);
                        return new TrainData(name, wagonCount);
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("Failed to load trains file: {}", TRAINS_FILE, e);
            throw new RuntimeException("Failed to load trains file", e);
        }
    }

    public static class TrainData {
        public final String name;
        public final int wagonCount;

        public TrainData(String name, int wagonCount) {
            this.name = name;
            this.wagonCount = wagonCount;
        }
    }
}