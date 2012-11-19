package com.artigile.android.aroundme.api.parser;

import com.artigile.android.aroundme.api.model.Geometry;
import com.artigile.android.aroundme.api.model.Location;
import com.artigile.android.aroundme.api.model.Viewport;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

/**
 * User: ioanbsu
 * Date: 11/9/12
 * Time: 2:36 PM
 */
@Singleton
public class GeometryXmlToEntityParser extends AbstractXmlToEntityParser<Geometry> {
    @Inject
    private LocationXmlToEntityParser locationXmlToEntityParser;

    @Inject
    private ViewportXmlToEntityParser viewportXmlToEntityParser;

    @Override
    protected void parseValue(XmlPullParser parser, Geometry geometry, String name) throws IOException, XmlPullParserException {
        if (name.equals("location")) {
            geometry.setLocation(locationXmlToEntityParser.parse(parser,"location",new Location()));
        } else if (name.equals("viewport")) {
            geometry.setViewport(viewportXmlToEntityParser.parse(parser,"viewport", new Viewport()));
        } else {
            skip(parser);
        }
    }
}
