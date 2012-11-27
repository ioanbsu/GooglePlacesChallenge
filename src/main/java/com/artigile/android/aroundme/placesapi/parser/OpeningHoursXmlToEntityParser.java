package com.artigile.android.aroundme.placesapi.parser;

import com.artigile.android.aroundme.placesapi.model.OpeningHours;
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
    protected void parseValue(XmlPullParser parser, OpeningHours openingHours, String name) throws IOException, XmlPullParserException {
        if (name.equals("open_now")) {
            openingHours.setOpenNow(readBoolean(parser));
        } else {
            skip(parser);
        }
    }


}
