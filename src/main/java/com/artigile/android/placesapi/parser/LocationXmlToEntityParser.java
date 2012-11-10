package com.artigile.android.placesapi.parser;

import com.artigile.android.placesapi.model.Location;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import javax.inject.Singleton;
import java.io.IOException;

/**
 * User: ioanbsu
 * Date: 11/9/12
 * Time: 2:59 PM
 */
@Singleton
public class LocationXmlToEntityParser extends AbstractXmlToEntityParser<Location> {
    @Override
    public Location parse(XmlPullParser parser,String requiredTag) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, "",requiredTag);
        Location location = new Location();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("lat")) {
                location.setLat(readFloat(parser));
            }else if (name.equals("lng")) {
                location.setLng(readFloat(parser));
            } else {
                skip(parser);
            }
        }
        return location;
    }
}
