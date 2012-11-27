package com.artigile.android.aroundme.sfparkingapi.model;

import java.io.Serializable;

/**
 * User: ioanbsu
 * Date: 11/27/12
 * Time: 9:33 AM
 */
public class Ophrs implements Serializable {

    /**
     * "FROM": "Start day for this schedule, e.g., Monday",
     */
    private String from;
    /**
     * "TO": "End day for this schedule, e.g., Friday",
     */
    private String to;
    /**
     * "BEG": "Indicates the begin time for this schedule,
     * e.g., 4:00 AM",
     */
    private String beg;
    /**
     * "END": "Indicates the end time for this schedule, e.g.,
     * 10:00 PM"
     */
    private String end;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getBeg() {
        return beg;
    }

    public void setBeg(String beg) {
        this.beg = beg;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
