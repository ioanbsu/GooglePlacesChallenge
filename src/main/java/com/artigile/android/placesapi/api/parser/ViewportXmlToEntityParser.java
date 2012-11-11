package com.artigile.android.placesapi.api.parser;

import com.artigile.android.placesapi.api.model.Location;
import com.artigile.android.placesapi.api.model.Viewport;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

/**
 * User: ioanbsu
 * Date: 11/9/12
 * Time: 4:03 PM
 */
@Singleton
public class ViewportXmlToEntityParser extends AbstractXmlToEntityParser<Viewport> {

    @Inject
    private LocationXmlToEntityParser locationXmlToEntityParser;

    @Override
    protected void parseValue(XmlPullParser parser, Viewport viewport, String name) throws IOException, XmlPullParserException {
        if (name.equals("southwest")) {
            viewport.setSouthwest(locationXmlToEntityParser.parse(parser, "southwest",new Location()));
        } else if (name.equals("northeast")) {
            viewport.setNortheast(locationXmlToEntityParser.parse(parser, "northeast",new Location()));
        } else {
            skip(parser);
        }
    }
}
