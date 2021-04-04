package dskm.action;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.time.LocalTime;

public class VouseEvent {

    private Actions.ACT action;
    private int x, y;
    private LocalTime time;

    /**
     * Constructor
     * @param action Action type
     * @param x X coordinate
     * @param y Y coordinate
     * @param time Time of the event
     */
    public VouseEvent(Actions.ACT action, int x, int y, LocalTime time) {
        this.action = action;
        this.x = x;
        this.y = y;
        this.time = time;
    }

    /**
     * Get the string for the event
     * @return String
     */
    public String toString() {
        return String.format("Action=%s - Position=%d,%d - Time=%s - ", action, x, y, time.toString());
    }

}
