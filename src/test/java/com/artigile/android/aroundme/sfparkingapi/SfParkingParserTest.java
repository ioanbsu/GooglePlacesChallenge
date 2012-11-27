package com.artigile.android.aroundme.sfparkingapi;

import com.artigile.android.aroundme.sfparkingapi.model.ParkingPlacesResult;
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

    @Test
    public void testParsing() {
        try {
            String fileContent = Files.toString(new File(getClass().getClassLoader().getResource("test.json").getPath()), Charset.defaultCharset());
            ParkingPlacesResult parkingPlacesResult = sfParkingResponseParser.parse(fileContent);
            assertEquals(72, parkingPlacesResult.getNumRecords());
            assertEquals(72, parkingPlacesResult.getErrorCode().length());
            assertEquals("SUCCESS", parkingPlacesResult.getMessage());
            assertEquals("", parkingPlacesResult.getErrorCode());
            assertEquals("", parkingPlacesResult.getRequestId());
            assertEquals("", parkingPlacesResult.getUdf1());
            assertEquals("72 records found", parkingPlacesResult.getMessage());
            //later add tests fro rates and opan hours....
        } catch (ParkingResultNotSuccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
