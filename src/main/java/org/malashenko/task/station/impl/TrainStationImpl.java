package org.malashenko.task.station.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.malashenko.task.model.Train;
import org.malashenko.task.singleton.StationConfig;
import org.malashenko.task.station.TrainStation;
import org.malashenko.task.util.impl.DataLoaderImpl;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TrainStationImpl implements TrainStation {

    private static final Logger log = LogManager.getLogger(TrainStationImpl.class);

    private final int maxTracks;
    private int availableTracks;
    private final int maxWagonCapacity;
    private int currentWagonLoad;


    private final Queue<Train> waitingTrains = new LinkedList<>();

    private final Lock lock = new ReentrantLock(true);

    private final Condition trackAvailable = lock.newCondition();

    public TrainStationImpl() {
        StationConfig config = StationConfig.getInstance();
        this.maxWagonCapacity = config.getMaxWagonCapacity();
        this.maxTracks = config.getTrackCount();
        this.availableTracks = maxTracks;
        this.currentWagonLoad = 0;
        log.info("Station created with {} tracks, max wagon capacity: {}", maxTracks, maxWagonCapacity);
    }

    @Override
    public void acceptTrain(Train train) throws InterruptedException {
        lock.lockInterruptibly();
        try {
            waitingTrains.add(train);
            log.info("{} added to queue. Queue size: {}", train.getName(), waitingTrains.size());

            while (availableTracks == 0 ||
                    (currentWagonLoad + train.getWagonCount() > maxWagonCapacity) ||
                    !waitingTrains.peek().equals(train)) {

                log.info("{} waiting... (tracks left: {}, current load: {}/{}, first in queue: {})",
                        train.getName(), availableTracks, currentWagonLoad, maxWagonCapacity,
                        waitingTrains.peek() != null ? waitingTrains.peek().getName() : "none");

                trackAvailable.await();
            }

            waitingTrains.poll();
            availableTracks--;
            currentWagonLoad += train.getWagonCount();
            log.info("{} entered track. Tracks left: {}, wagon load: {}/{}",
                    train.getName(), availableTracks, currentWagonLoad, maxWagonCapacity);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void leaveTrack(Train train) {
        lock.lock();
        try {
            availableTracks++;
            currentWagonLoad -= train.getWagonCount();
            log.info("{} left track. Tracks free: {}, wagon load: {}/{}",
                    train.getName(), availableTracks, currentWagonLoad, maxWagonCapacity);
            trackAvailable.signalAll();
        } finally {
            lock.unlock();
        }
    }
}