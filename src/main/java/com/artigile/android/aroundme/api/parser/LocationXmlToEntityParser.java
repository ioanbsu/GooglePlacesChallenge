package com.artigile.android.aroundme.api.parser;

import com.artigile.android.aroundme.api.model.Location;
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
    protected void parseValue(XmlPullParser parser, Location location, String name) throws IOException, XmlPullParserException {
        if (name.equals("lat")) {
            location.setLat(readFloat(parser));
        } else if (name.equals("lng")) {
            location.setLng(readFloat(parser));
        } else {
            skip(parser);
        }
    }
}
