package org.malashenko.task.station.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.malashenko.task.config.StationConfig;
import org.malashenko.task.model.Train;
import org.malashenko.task.station.Track;
import org.malashenko.task.station.TrainStation;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TrainStationImpl implements TrainStation {
    private static final Logger logger = LogManager.getLogger(TrainStationImpl.class);

    private static final AtomicReference<TrainStationImpl> INSTANCE = new AtomicReference<>();

    private final Queue<Track> availableTracks;
    private final int maxWagonCapacity;
    private int currentWagonLoad;
    private int servedTrainsCount;

    private final ReentrantLock stationLock;
    private final Condition stationCondition;

    private TrainStationImpl(int trackCount, int maxWagonCapacity) {
        this.availableTracks = new ConcurrentLinkedQueue<>();
        this.maxWagonCapacity = maxWagonCapacity;
        this.currentWagonLoad = 0;
        this.servedTrainsCount = 0;
        this.stationLock = new ReentrantLock(true);
        this.stationCondition = stationLock.newCondition();

        for (int i = 1; i <= trackCount; i++) {
            availableTracks.add(new Track(i));
        }

        logger.info("Station created: {} tracks, max capacity {} wagons", trackCount, maxWagonCapacity);
        logStationStatus();
    }

    public static TrainStationImpl getInstance() {
        TrainStationImpl instance = INSTANCE.get();
        if (instance == null) {
            StationConfig config = StationConfig.getInstance();
            TrainStationImpl newInstance = new TrainStationImpl(
                    config.getTrackCount(),
                    config.getMaxWagonCapacity()
            );
            if (INSTANCE.compareAndSet(null, newInstance)) {
                instance = newInstance;
            } else {
                instance = INSTANCE.get();
            }
        }
        return instance;
    }

    @Override
    public Track acquireTrack(Train train) throws InterruptedException {
        stationLock.lockInterruptibly();
        try {
            logger.info("Train {} with {} wagons requests track", train.getName(), train.getWagonCount());
            logStationStatus();

            while (availableTracks.isEmpty() ||
                    currentWagonLoad + train.getWagonCount() > maxWagonCapacity) {

                logger.info("Train {} waiting. Free tracks: {}, current load: {}/{}",
                        train.getName(), availableTracks.size(), currentWagonLoad, maxWagonCapacity);

                stationCondition.await();
            }

            Track track = availableTracks.poll();
            currentWagonLoad += train.getWagonCount();

            logger.info("Train {} occupied {}. Station load: {}/{}",
                    train.getName(), track, currentWagonLoad, maxWagonCapacity);
            logStationStatus();

            return track;
        } finally {
            stationLock.unlock();
        }
    }

    @Override
    public void releaseTrack(Track track, Train train) {
        stationLock.lock();
        try {
            availableTracks.add(track);
            currentWagonLoad -= train.getWagonCount();
            servedTrainsCount++;

            logger.info("Train {} released {}. Station load: {}/{}",
                    train.getName(), track, currentWagonLoad, maxWagonCapacity);
            logStationStatus();

            stationCondition.signalAll();
        } finally {
            stationLock.unlock();
        }
    }

    private void logStationStatus() {
        logger.info("Station status - free tracks: {}, load: {}/{}, served: {}",
                availableTracks.size(), currentWagonLoad, maxWagonCapacity, servedTrainsCount);
    }
}