package org.malashenko.task.model.state;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.malashenko.task.model.Train;

public class LeavingState implements TrainState {
    private static final Logger log = LogManager.getLogger(LeavingState.class);

    @Override
    public void handle(Train train) {
        log.info("{} LEAVING station", train.getName());
        train.getStation().leaveTrack(train);
        log.info("{} DEPARTED", train.getName());
        train.setState(null);
    }

    @Override
    public String getName() { return "LEAVING"; }
}