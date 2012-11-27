package com.artigile.android.aroundme.sfparkingapi.model;

import java.io.Serializable;
import java.util.List;

/**
 * User: ioanbsu
 * Date: 11/27/12
 * Time: 9:30 AM
 */
public class ParkingSpace implements Serializable {

    /**
     * Specifies whether on or off street parking
     */
    private ParkingSpaceType type;
    /**
     * Name of parking location or street with from and to address, if available
     */
    private String name;
    /**
     * returned for OSP only – usually address for the parking location, if available
     */
    private String desc;
    /**
     * Returned for OSP only – usually cross street intersection parking location, if available
     */
    private String inter;
    /**
     * Returned for OSP only – Contact telephone number for parking location, if available
     */
    private String tel;
    /**
     * Unique SFMTA Identifier for the parking location for off street parking type
     */
    private String ospid;
    /**
     * Unique SFMTA Identifier for on street block face parking
     */
    private String bfid;
    /**
     * Number of spaces currently operational for this location
     */
    private Integer occ;
    /**
     * Number of spaces currently operational for this location
     */
    private Integer oper;
    /**
     * Number of location points returned for this record. Usually 1 for OSP and 2 for on-street parking location
     */
    private int pts;
    /**
     * Comma separated Longitude and Latitude values of points for this location.
     * Currently a max of two points are specified for a parking location which
     * being the start and end points
     */
    private String loc;
    /**
     * The OPHRS Element indicated above has the following child elements.
     * Note: Operating hour’s information is returned as an array of one or more OPS
     * sub-elements. If there is no schedule information in the response, it
     * indicates that the system did not retrieve any current valid operating hour’s
     * information for this location.
     */
    private List<Ophrs> ophrs;
    /**
     * The RATES element indicated above has the following child elements.
     * Note: RATES information is returned as an array of one or more RS or rate
     * schedule elements.
     * If there is no RATE information found for a location the response will return
     * a RATE of 0 with a rate qualifier (RQ) message indicating “See Meter” or “See
     * Garage” for off street garage locations.
     */
    private List<Rate> rates;

    public ParkingSpaceType getType() {
        return type;
    }

    public void setType(ParkingSpaceType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getInter() {
        return inter;
    }

    public void setInter(String inter) {
        this.inter = inter;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getOspid() {
        return ospid;
    }

    public void setOspid(String ospid) {
        this.ospid = ospid;
    }

    public String getBfid() {
        return bfid;
    }

    public void setBfid(String bfid) {
        this.bfid = bfid;
    }

    public Integer getOcc() {
        return occ;
    }

    public void setOcc(Integer occ) {
        this.occ = occ;
    }

    public Integer getOper() {
        return oper;
    }

    public void setOper(Integer oper) {
        this.oper = oper;
    }

    public int getPts() {
        return pts;
    }

    public void setPts(int pts) {
        this.pts = pts;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public List<Ophrs> getOphrs() {
        return ophrs;
    }

    public void setOphrs(List<Ophrs> ophrs) {
        this.ophrs = ophrs;
    }

    public List<Rate> getRates() {
        return rates;
    }

    public void setRates(List<Rate> rates) {
        this.rates = rates;
    }
}
