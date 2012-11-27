package com.artigile.android.aroundme.sfparkingapi.model;

import java.util.Date;
import java.util.List;

/**
 * @author IoaN, 11/26/12 8:47 PM
 */
public class ParkingPlacesResult {

    /**
     * Response indicating SUCCESS or ERROR
     */
    private String status;
    /**
     * Identifier that is returned if passed in request
     */
    private String requestId;
    /**
     * UDF1: User defined field identifier that is returned if passed in request
     */
    private String udf1;
    /**
     * Returns a SFMTA internal Error code if encountered Error
     */
    private String errorCode;
    private int numRecords;
    private String message;
    private Date availabilityUpdated;
    private Date availabilityRequest;
    private List<ParkingSpace> avl;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getUdf1() {
        return udf1;
    }

    public void setUdf1(String udf1) {
        this.udf1 = udf1;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public int getNumRecords() {
        return numRecords;
    }

    public void setNumRecords(int numRecords) {
        this.numRecords = numRecords;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getAvailabilityUpdated() {
        return availabilityUpdated;
    }

    public void setAvailabilityUpdated(Date availabilityUpdated) {
        this.availabilityUpdated = availabilityUpdated;
    }

    public Date getAvailabilityRequest() {
        return availabilityRequest;
    }

    public void setAvailabilityRequest(Date availabilityRequest) {
        this.availabilityRequest = availabilityRequest;
    }

    public List<ParkingSpace> getAvl() {
        return avl;
    }

    public void setAvl(List<ParkingSpace> avl) {
        this.avl = avl;
    }

    @Override
    public String toString() {
        return "ParkingPlacesResult{" +
                "status='" + status + '\'' +
                ", requestId='" + requestId + '\'' +
                ", udf1='" + udf1 + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", numRecords=" + numRecords +
                ", message='" + message + '\'' +
                ", availabilityUpdated=" + availabilityUpdated +
                ", availabilityRequest=" + availabilityRequest +
                ", avl=" + avl +
                '}';
    }
}
