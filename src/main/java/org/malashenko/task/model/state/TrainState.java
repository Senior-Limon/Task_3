package org.malashenko.task.model.state;

import org.malashenko.task.model.Train;

public interface TrainState {
    void handle(Train train) throws InterruptedException;
    String getName();
}
