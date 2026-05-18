package org.malashenko.task.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.malashenko.task.util.impl.DataLoaderImpl;

public class StationConfig {
    private static final Logger logger = LogManager.getLogger(StationConfig.class);

    private final int maxWagonCapacity;
    private final int trackCount;

    private StationConfig() {
        this.maxWagonCapacity = DataLoaderImpl.loadMaxWagonCapacity();
        this.trackCount = DataLoaderImpl.loadTrackCount();
        logger.info("Configuration loaded: tracks={}, capacity={}", trackCount, maxWagonCapacity);
    }

    private static final class Holder {
        private static final StationConfig INSTANCE = new StationConfig();
    }

    public static StationConfig getInstance() {
        return Holder.INSTANCE;
    }

    public int getMaxWagonCapacity() {
        return maxWagonCapacity;
    }

    public int getTrackCount() {
        return trackCount;
    }
}