package org.malashenko.task.station;

import org.malashenko.task.model.Train;

public interface TrainStation {
    void acceptTrain(Train train) throws InterruptedException;
    void leaveTrack(Train train);
}
