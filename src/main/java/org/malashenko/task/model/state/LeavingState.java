package org.malashenko.task.model.state;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.malashenko.task.model.Train;
import org.malashenko.task.model.state.TrainState;
import org.malashenko.task.station.impl.TrainStationImpl;

public class LeavingState implements TrainState {
    private static final Logger logger = LogManager.getLogger(LeavingState.class);

    @Override
    public void execute(Train train) throws InterruptedException {
        logger.info("Train {} leaves station", train.getName());

        TrainStationImpl station = TrainStationImpl.getInstance();
        station.releaseTrack(train.getCurrentTrack(), train);

        train.setState(null);
    }

    @Override
    public String getStateName() {
        return "LEAVING";
    }
}