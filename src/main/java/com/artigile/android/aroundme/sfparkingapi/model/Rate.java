package com.artigile.android.aroundme.sfparkingapi.model;

import java.io.Serializable;
import java.util.Date;

/**
 * User: ioanbsu
 * Date: 11/27/12
 * Time: 9:35 AM
 */
public class Rate implements Serializable {

    /**
     * Indicates the begin time for this rate schedule
     */
    private String beg;
    /**
     * "END": "Indicates the end time for this rate schedule ",
     */
    private String end;
    /**
     * "RATE": "Applicable rate for this rate schedule",
     */
    private Double rate;
    /**
     * "DESC": "Used for descriptive rate information when not
     * possible to specify using BEG or END times for this rate schedule",
     */
    private String desc;
    /**
     * "RQ": "Rate qualifier for this rate schedule, e.g. Per
     * Hr",
     */
    private String rq;
    /**
     * "RR": "Rate restriction for this rate schedule, if any"
     */
    private String rr;

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

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRq() {
        return rq;
    }

    public void setRq(String rq) {
        this.rq = rq;
    }

    public String getRr() {
        return rr;
    }

    public void setRr(String rr) {
        this.rr = rr;
    }
}
