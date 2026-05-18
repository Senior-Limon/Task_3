package org.malashenko.task.station;

import org.malashenko.task.model.Train;

public interface TrainStation {
    Track acquireTrack(Train train) throws InterruptedException;
    void releaseTrack(Track track, Train train);
}