package org.malashenko.task.station;

public class Track {
    private final int id;

    public Track(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Track " + id;
    }
}