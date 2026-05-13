package org.malashenko.task.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.malashenko.task.model.state.LeavingState;
import org.malashenko.task.model.state.TrainState;
import org.malashenko.task.model.state.WaitingState;
import org.malashenko.task.station.TrainStation;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class Train implements Callable<String> {

    private static final Logger log = LogManager.getLogger(Train.class);


    private final String name;
    private final int wagonCount;
    private final TrainStation station;
    private TrainState state;

    public Train(String name, int wagonCount, TrainStation station) {
        this.name = name;
        this.wagonCount = wagonCount;
        this.station = station;
        this.state = new WaitingState();
        log.info("Train {} created with {} wagons", name, wagonCount);

    }

    public String getName() {
        return name;
    }

    public int getWagonCount() {
        return wagonCount;
    }

    public TrainStation getStation() {
        return station;
    }

    public void setState(TrainState state) {
        this.state = state;
        if (state != null) {
            log.info("{} → state: {}", name, state.getName());
        } else {
            log.info("{} → state: COMPLETED (null)", name);
        }
    }

    public void unload() throws InterruptedException {
        long unloadingTime = wagonCount * 200L;
        log.info("{} unloading {} wagons ({}ms)", name, wagonCount, unloadingTime);
        TimeUnit.MILLISECONDS.sleep(unloadingTime);
        log.info("{} finished unloading", name);
    }

    @Override
    public String call() throws Exception {
        log.info("{} started execution", name);

        while (this.state != null) {
            log.info("{} calling handle() for state: {}", name, this.state.getName());
            this.state.handle(this);
        }

        return name + " served successfully";
    }
}
