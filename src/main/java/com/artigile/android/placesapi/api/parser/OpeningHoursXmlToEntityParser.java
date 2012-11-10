package com.artigile.android.placesapi.api.parser;

import com.artigile.android.placesapi.api.model.OpeningHours;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import javax.inject.Singleton;
import java.io.IOException;

/**
 * User: ioanbsu
 * Date: 11/9/12
 * Time: 2:39 PM
 */
@Singleton
public class OpeningHoursXmlToEntityParser extends AbstractXmlToEntityParser<OpeningHours> {
    @Override
    public OpeningHours parse(XmlPullParser parser,String requiredTag) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, "", requiredTag);
        OpeningHours openingHours = new OpeningHours();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("open_now")) {
                openingHours.setOpenNow(readBoolean(parser));
            } else {
                skip(parser);
            }
        }
        return openingHours;
    }
}
