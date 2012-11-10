package com.artigile.android.placesapi.api.parser;

import com.artigile.android.placesapi.api.model.Geometry;
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
    public Geometry parse(XmlPullParser parser,String requiredTag) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, "",requiredTag);
        Geometry geometry = new Geometry();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("location")) {
                geometry.setLocation(locationXmlToEntityParser.parse(parser,"location"));
            } else if (name.equals("viewport")) {
                geometry.setViewport(viewportXmlToEntityParser.parse(parser,"viewport"));
            } else {
                skip(parser);
            }
        }
        return geometry;
    }
}
