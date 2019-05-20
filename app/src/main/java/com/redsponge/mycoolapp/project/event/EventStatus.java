package com.redsponge.mycoolapp.project.event;

import android.graphics.Color;
import android.widget.TextView;

public enum EventStatus {

    TO_DO(0, "Not Started", Color.RED),
    IN_PROGRESS(1, "In Progress", 0xFFce8002),
    IN_TESTING(2, "In Testing", 0xFFe5bd0b),
    DONE(3, "Done", 0xFF3aad05);

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

    public void displayOnTV(TextView tv) {
        tv.setText(representation);
        tv.setTextColor(color);
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
