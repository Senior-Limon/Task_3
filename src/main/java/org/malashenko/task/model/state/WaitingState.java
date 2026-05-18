package org.malashenko.task.model.state;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.malashenko.task.model.Train;
import org.malashenko.task.model.state.TrainState;
import org.malashenko.task.station.Track;
import org.malashenko.task.station.impl.TrainStationImpl;

public class WaitingState implements TrainState {
    private static final Logger logger = LogManager.getLogger(WaitingState.class);

    @Override
    public void execute(Train train) throws InterruptedException {
        logger.info("Train {} enters waiting state", train.getName());

        TrainStationImpl station = TrainStationImpl.getInstance();
        Track track = station.acquireTrack(train);

        train.setCurrentTrack(track);
        train.setState(new UnloadState());
    }

    @Override
    public String getStateName() {
        return "WAITING";
    }
}