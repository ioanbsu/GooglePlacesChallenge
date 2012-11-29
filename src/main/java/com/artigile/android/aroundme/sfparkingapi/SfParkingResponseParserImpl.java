package com.artigile.android.aroundme.sfparkingapi;

import com.artigile.android.aroundme.sfparkingapi.model.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Singleton;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * User: ioanbsu
 * Date: 11/27/12
 * Time: 8:55 AM
 */
@Singleton
public class SfParkingResponseParserImpl implements SfParkingResponseParser {

    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    private static final String STATUS = "STATUS";
    private static final String NUM_RECORDS = "NUM_RECORDS";
    private static final String MESSAGE = "MESSAGE";
    private static final String AVAILABILITY_UPDATED_TIMESTAMP = "AVAILABILITY_UPDATED_TIMESTAMP";
    private static final String AVAILABILITY_REQUEST_TIMESTAMP = "AVAILABILITY_REQUEST_TIMESTAMP";
    private static final String AVL = "AVL";
    private static final String REQUESTID = "REQUESTID";
    private static final String UDF_1 = "UDF1";
    private static final String ERROR_CODE = "ERROR_CODE";
    //    =================PARKING SPACE CONSTANTS=======================
    private static final String PARKING_SPACE_TYPE = "TYPE";
    private static final String PARKING_SPACE_NAME = "NAME";
    private static final String PARKING_SPACE_DESC = "DESC";
    private static final String PARKING_SPACE_INTER = "INTER";
    private static final String PARKING_SPACE_TEL = "TEL";
    private static final String PARKING_SPACE_OSPIF = "OSPID";
    private static final String PARKING_SPACE_BFID = "BFID";
    private static final String PARKING_SPACE_OCC = "OCC";
    private static final String PARKING_SPACE_OPER = "OPER";
    private static final String PARKING_SPACE_PTS = "PTS";
    private static final String PARKING_SPACE_LOC = "LOC";
    private static final String PARKING_SPACE_OPHRS = "OPHRS";
    private static final String PARKING_SPACE_RATES = "RATES";
    //    =================RATES CONSTANTS=======================
    private static final String RATE_BEG = "BEG";
    private static final String RATE_END = "END";
    private static final String RATE_RATE = "RATE";
    private static final String RATE_DESC = "DESC";
    private static final String RATE_RQ = "RQ";
    private static final String RATE_RR = "RR";
    private static final String RATE_RS = "RS";
    //    =================OPERATION HOURS CONSTANTS=======================
    private static final String OPHRS_FROM = "FROM";
    private static final String OPHRS_END = "END";
    private static final String OPHRS_TO = "TO";
    private static final String OPHRS_BEG = "BEG";
    private static final String OPHRS_OPS = "OPS";

    @Override
    public ParkingPlacesResult parse(String response,int maxResults) throws ParkingResultNotSuccessException {
        try {
            JSONObject jObj = new JSONObject(response);
            ParkingPlacesResult parkingPlacesResult = new ParkingPlacesResult();
            parkingPlacesResult.setStatus(jObj.getString(STATUS));
            if (!"SUCCESS".equals(parkingPlacesResult.getStatus())) {
                throw new ParkingResultNotSuccessException();
            }
            parkingPlacesResult.setRequestId(jObj.optString(REQUESTID));
            parkingPlacesResult.setNumRecords(jObj.optInt(NUM_RECORDS));
            parkingPlacesResult.setMessage(jObj.optString(MESSAGE));
            parkingPlacesResult.setUdf1(jObj.optString(UDF_1));
            parkingPlacesResult.setErrorCode(jObj.optString(ERROR_CODE));
            try {
                parkingPlacesResult.setAvailabilityUpdated(DATE_TIME_FORMAT.parse(jObj.optString(AVAILABILITY_UPDATED_TIMESTAMP)));
                parkingPlacesResult.setAvailabilityRequest(DATE_TIME_FORMAT.parse(jObj.optString(AVAILABILITY_REQUEST_TIMESTAMP)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            parkingPlacesResult.setAvl(getAvlFromJson(jObj.optJSONArray(AVL),maxResults));
            return parkingPlacesResult;
        } catch (JSONException e) {
            return null;
        }
    }

    private List<ParkingSpace> getAvlFromJson(JSONArray parkingSpacesJsonArray,int maxResults) {
        List<ParkingSpace> parkingSpaceList = new ArrayList<ParkingSpace>();
        if (parkingSpacesJsonArray != null) {
            for (int i = 0; i < parkingSpacesJsonArray.length(); i++) {
                try {
                    JSONObject parkingSpaceJson = parkingSpacesJsonArray.getJSONObject(i);
                    ParkingSpace parkingSpace = new ParkingSpace();
                    parkingSpace.setType(ParkingSpaceType.valueOf(parkingSpaceJson.getString(PARKING_SPACE_TYPE)));
                    parkingSpace.setName(parkingSpaceJson.getString(PARKING_SPACE_NAME));
                    parkingSpace.setDesc(parkingSpaceJson.optString(PARKING_SPACE_DESC));
                    parkingSpace.setInter(parkingSpaceJson.optString(PARKING_SPACE_INTER));
                    parkingSpace.setTel(parkingSpaceJson.optString(PARKING_SPACE_TEL));
                    parkingSpace.setOspid(parkingSpaceJson.optString(PARKING_SPACE_OSPIF));
                    parkingSpace.setBfid(parkingSpaceJson.optString(PARKING_SPACE_BFID));
                    parkingSpace.setOcc(parkingSpaceJson.optInt(PARKING_SPACE_OCC));
                    parkingSpace.setOper(parkingSpaceJson.optInt(PARKING_SPACE_OPER));
                    parkingSpace.setPts(parkingSpaceJson.optInt(PARKING_SPACE_PTS));
                    parkingSpace.setLoc(parkingSpaceJson.optString(PARKING_SPACE_LOC));
                    parkingSpace.setOphrs(getOpenHoursFromJson(parkingSpaceJson.optJSONObject(PARKING_SPACE_OPHRS)));
                    parkingSpace.setRates(getRatesFromJson(parkingSpaceJson.optJSONObject(PARKING_SPACE_RATES)));
                    parkingSpaceList.add(parkingSpace);
                    if (i > maxResults-2) {
                        break;
                    }

                } catch (JSONException e) {
//                    Log.w("Parking space parsing.", "Failed to parse parking place with index: " + i + ". Array data: " + parkingSpacesJsonArray);
                }
            }
        }
        return parkingSpaceList;
    }

    private List<Ophrs> getOpenHoursFromJson(JSONObject ophrsJsonObject) {
        if (ophrsJsonObject == null) {
            return null;
        }
        JSONArray ophrsJsonArray = null;
        try {
            ophrsJsonArray = ophrsJsonObject.getJSONArray(OPHRS_OPS);
        } catch (JSONException e) {
            return null;
        }
        List<Ophrs> ophrsList = new ArrayList<Ophrs>();
        if (ophrsJsonArray != null) {
            for (int i = 0; i < ophrsJsonArray.length(); i++) {
                try {
                    JSONObject ophrsJson = ophrsJsonArray.getJSONObject(i);
                    Ophrs ophrs = new Ophrs();
                    ophrs.setFrom(ophrsJson.optString(OPHRS_FROM));
                    ophrs.setTo(ophrsJson.optString(OPHRS_TO));
                    ophrs.setBeg(ophrsJson.optString(OPHRS_BEG));
                    ophrs.setEnd(ophrsJson.optString(OPHRS_END));

                    ophrsList.add(ophrs);
                } catch (JSONException e) {
//                    Log.w("Open hours parsing.", "Failed to parse rates with index: " + i + ". Array data: " + ophrsJsonArray);
                }
            }
        }

        return ophrsList;
    }

    private List<Rate> getRatesFromJson(JSONObject ratesJsonObject) {
        if (ratesJsonObject == null) {
            return null;
        }

        JSONArray ratesJsonArray = null;
        try {
            ratesJsonArray = ratesJsonObject.getJSONArray(RATE_RS);
        } catch (JSONException e) {
            return null;
        }
        List<Rate> rateList = new ArrayList<Rate>();
        if (ratesJsonArray != null) {
            for (int i = 0; i < ratesJsonArray.length(); i++) {
                try {
                    JSONObject rateJson = ratesJsonArray.getJSONObject(i);
                    Rate rate = new Rate();
                    rate.setBeg(rateJson.optString(RATE_BEG));
                    rate.setEnd(rateJson.optString(RATE_END));

                    rate.setRate(rateJson.optDouble(RATE_RATE));
                    rate.setDesc(rateJson.optString(RATE_DESC));
                    rate.setRq(rateJson.optString(RATE_RQ));
                    rate.setRr(rateJson.optString(RATE_RR));
                    rateList.add(rate);
                } catch (JSONException e) {
//                    Log.w("Rate parsing.", "Failed to parse rates with index: " + i + ". Array data: " + ratesJsonArray);

                }
            }
        }

        return rateList;
    }
}
