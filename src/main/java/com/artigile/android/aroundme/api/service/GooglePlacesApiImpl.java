package com.artigile.android.aroundme.api.service;

import android.util.Xml;
import com.artigile.android.aroundme.api.model.PlacesApiResponseEntity;
import com.artigile.android.aroundme.api.parser.PlaceXmlToEntityParserImpl;
import org.xmlpull.v1.XmlPullParser;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;

/**
 * User: ioanbsu
 * Date: 11/9/12
 * Time: 1:38 PM
 */
@Singleton
public class GooglePlacesApiImpl extends AbstractGooglePlacesApi {

    @Inject
    private PlacesUrlsXmlBuilderImpl placesUrlsXmlBuilder;

    @Inject
    private PlaceXmlToEntityParserImpl placeXmlParser;


    @Override
    protected PlacesUrlsBuilder getUrlBuilder() {
        return placesUrlsXmlBuilder;
    }

    @Override
    protected PlacesApiResponseEntity parseStreamResponse(InputStream is, String mainTag) throws IOException {
        PlacesApiResponseEntity placesApiResponseEntity = null;
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);
            parser.nextTag();
            placesApiResponseEntity = placeXmlParser.parse(parser, mainTag, new PlacesApiResponseEntity());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return placesApiResponseEntity;
    }
}


