package org.malashenko.task.model.state;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.malashenko.task.model.Train;

public class WaitingState implements TrainState {

    private static final Logger log = LogManager.getLogger(WaitingState.class);

    @Override
    public void handle(Train train) throws InterruptedException {

        log.info("{} WAITING for free track", train.getName());
        train.getStation().acceptTrain(train);
        train.setState(new UnloadState());
    }

    @Override
    public String getName() { return "WAITING"; }
}