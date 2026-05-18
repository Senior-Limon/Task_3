package org.malashenko.task.singleton;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.malashenko.task.util.DataLoader;
import org.malashenko.task.util.impl.DataLoaderImpl;

public class StationConfig {
    private static final Logger log = LogManager.getLogger(StationConfig.class);

    private final int maxWagonCapacity;
    private final int trackCount;

    private StationConfig() {
        this.maxWagonCapacity = DataLoaderImpl.loadMaxWagonCapacity();
        this.trackCount = DataLoaderImpl.loadTrackCount();
        log.info("StationConfig loaded: maxWagonCapacity={}, trackCount={}",
                maxWagonCapacity, trackCount);
    }

    /// ////////
    private static class Holder {
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