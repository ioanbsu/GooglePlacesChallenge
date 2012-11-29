package com.artigile.android.aroundme.sfparkingapi;

import com.artigile.android.aroundme.app.util.UiStringUtil;
import com.artigile.android.aroundme.sfparkingapi.model.ParkingPlacesResult;
import com.artigile.android.aroundme.sfparkingapi.model.ParkingSpace;
import com.google.common.io.Files;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import static junit.framework.Assert.assertEquals;

/**
 * User: ioanbsu
 * Date: 11/27/12
 * Time: 9:48 AM
 */
@RunWith(JukitoRunner.class)
public class SfParkingParserTest {


    @Inject
    private SfParkingResponseParserImpl sfParkingResponseParser;
    @Inject
    private UiStringUtil uiStringUtil;

    @Test
    public void testParsing() {
        try {
            String fileContent = Files.toString(new File(getClass().getClassLoader().getResource("test.json").getPath()), Charset.defaultCharset());
            ParkingPlacesResult parkingPlacesResult = sfParkingResponseParser.parse(fileContent, 600);
            assertEquals(600, parkingPlacesResult.getNumRecords());
            assertEquals(600, parkingPlacesResult.getAvl().size());
            assertEquals("SUCCESS", parkingPlacesResult.getStatus());
            assertEquals("", parkingPlacesResult.getErrorCode());
            assertEquals("", parkingPlacesResult.getRequestId());
            assertEquals("", parkingPlacesResult.getUdf1());
            assertEquals("600 records found", parkingPlacesResult.getMessage());
            //later add tests for rates and opan hours....
        } catch (ParkingResultNotSuccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRating() {
        try {
            String fileContent = Files.toString(new File(getClass().getClassLoader().getResource("test.json").getPath()), Charset.defaultCharset());
            ParkingPlacesResult parkingPlacesResult = sfParkingResponseParser.parse(fileContent, 600);
            int i = 1;
            for (ParkingSpace parkingSpace : parkingPlacesResult.getAvl()) {
                System.out.println("=============" + i + "=============");
                System.out.println(uiStringUtil.buildRates(parkingSpace.getRates()));
                i++;
            }//no actual asserts here just testing that data get displayed correctly in concole
        } catch (ParkingResultNotSuccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testOperationHours() {
        try {
            String fileContent = Files.toString(new File(getClass().getClassLoader().getResource("test.json").getPath()), Charset.defaultCharset());
            ParkingPlacesResult parkingPlacesResult = sfParkingResponseParser.parse(fileContent, 600);
            int i = 1;
            for (ParkingSpace parkingSpace : parkingPlacesResult.getAvl()) {
                System.out.println("=============" + i + "=============");
                System.out.println(uiStringUtil.buildHoursOfOperation(parkingSpace.getOphrs()));
                i++;
            }//no actual asserts here just testing that data get displayed correctly in concole
        } catch (ParkingResultNotSuccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
