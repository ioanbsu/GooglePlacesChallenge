package com.artigile.android.aroundme.placesapi.parser;

import com.artigile.android.aroundme.placesapi.model.PlaceEvent;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import javax.inject.Singleton;
import java.io.IOException;

/**
 * @author IoaN, 11/10/12 2:30 PM
 */
@Singleton
public class PlaceEventXmlToEntityParser extends AbstractXmlToEntityParser<PlaceEvent> {
    @Override
    protected void parseValue(XmlPullParser parser, PlaceEvent placeEvent, String name) throws IOException, XmlPullParserException {
        if (name.equals("event_id")) {
            placeEvent.setEventId(readText(parser));
        } else if (name.equals("start_time")) {
            placeEvent.setStartTime(readText(parser));
        } else if (name.equals("summary")) {
            placeEvent.setSummary(readText(parser));
        } else if (name.equals("url")) {
            placeEvent.setUrl(readText(parser));
        } else {
            skip(parser);
        }
    }
}
