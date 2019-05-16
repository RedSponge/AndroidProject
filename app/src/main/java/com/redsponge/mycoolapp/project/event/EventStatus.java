package com.redsponge.mycoolapp.project.event;

import android.graphics.Color;

public enum EventStatus {

    TO_DO(0, "Not Started", Color.RED),
    IN_PROGRESS(1, "In Progress", Color.YELLOW),
    IN_TESTING(2, "In Testing", Color.GREEN - 0xCC00CC00),
    DONE(3, "Done", Color.GREEN);

    private final int id;
    private final String representation;
    private final int color;

    EventStatus(int id, String representation, int color) {
        this.id = id;
        this.representation = representation;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public String getRepresentation() {
        return representation;
    }

    public int getColor() {
        return color;
    }

    public static EventStatus fromId(int id) {
        for (EventStatus eventStatus : values()) {
            if(eventStatus.id == id) {
                return eventStatus;
            }
        }
        return null;
    }
}
