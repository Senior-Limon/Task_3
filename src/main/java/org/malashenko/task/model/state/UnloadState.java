package org.malashenko.task.model.state;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.malashenko.task.model.Train;
import org.malashenko.task.model.state.LeavingState;

import java.util.concurrent.TimeUnit;

public class UnloadState implements TrainState {
    private static final Logger logger = LogManager.getLogger(UnloadState.class);

    @Override
    public void execute(Train train) throws InterruptedException {
        logger.info("Train {} starts unloading {} wagons", train.getName(), train.getWagonCount());

        long unloadingTime = train.getWagonCount() * 200L;
        TimeUnit.MILLISECONDS.sleep(unloadingTime);

        logger.info("Train {} finished unloading", train.getName());
        train.setState(new LeavingState());
    }

    @Override
    public String getStateName() {
        return "UNLOADING";
    }
}