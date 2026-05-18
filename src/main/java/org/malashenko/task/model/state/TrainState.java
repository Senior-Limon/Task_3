package org.malashenko.task.model.state;

import org.malashenko.task.model.Train;

public interface TrainState {
    void execute(Train train) throws InterruptedException;
    String getStateName();
}