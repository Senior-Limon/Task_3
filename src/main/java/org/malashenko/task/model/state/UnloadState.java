package org.malashenko.task.model.state;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.malashenko.task.model.Train;

public class UnloadState implements TrainState {
    private static final Logger log = LogManager.getLogger(UnloadState.class);

    @Override
    public void handle(Train train) throws InterruptedException {

        log.info("{} UNLOADING", train.getName());
        train.unload();
        train.setState(new LeavingState());
    }

    @Override
    public String getName() { return "UNLOADING"; }
}