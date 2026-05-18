package org.malashenko.task.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.malashenko.task.model.state.TrainState;
import org.malashenko.task.model.state.WaitingState;
import org.malashenko.task.station.Track;

import java.util.concurrent.Callable;

public class Train implements Callable<String> {
    private static final Logger logger = LogManager.getLogger(Train.class);

    private final String name;
    private final int wagonCount;
    private TrainState currentState;
    private Track currentTrack;

    public Train(String name, int wagonCount) {
        this.name = name;
        this.wagonCount = wagonCount;
        this.currentState = new WaitingState();
        logger.info("Train {} created with {} wagons", name, wagonCount);
    }

    public String getName() {
        return name;
    }

    public int getWagonCount() {
        return wagonCount;
    }

    public Track getCurrentTrack() {
        return currentTrack;
    }

    public void setCurrentTrack(Track currentTrack) {
        this.currentTrack = currentTrack;
    }

    public void setState(TrainState state) {
        this.currentState = state;
        if (state != null) {
            logger.info("Train {} changed state to {}", name, state.getStateName());
        } else {
            logger.info("Train {} completed all states", name);
        }
    }

    @Override
    public String call() throws Exception {
        logger.info("Train {} started execution", name);

        while (currentState != null) {
            logger.info("Train {} executing state: {}", name, currentState.getStateName());
            currentState.execute(this);
        }

        logger.info("Train {} finished successfully", name);
        return String.format("%s served with %d wagons", name, wagonCount);
    }
}