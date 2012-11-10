package com.artigile.android.placesapi.parser;

import com.artigile.android.placesapi.model.Viewport;
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
    public Viewport parse(XmlPullParser parser, String requiredTag) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, "", requiredTag);
        Viewport viewport = new Viewport();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("southwest")) {
                viewport.setSouthwest(locationXmlToEntityParser.parse(parser, "southwest"));
            } else if (name.equals("northeast")) {
                viewport.setNortheast(locationXmlToEntityParser.parse(parser, "northeast"));
            } else {
                skip(parser);
            }
        }
        return viewport;
    }
}
