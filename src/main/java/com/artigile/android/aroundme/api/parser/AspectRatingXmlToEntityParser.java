package com.artigile.android.aroundme.api.parser;

import com.artigile.android.aroundme.api.model.AspectRating;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import javax.inject.Singleton;
import java.io.IOException;

/**
 * @author IoaN, 11/10/12 2:54 PM
 */
@Singleton
public class AspectRatingXmlToEntityParser extends AbstractXmlToEntityParser<AspectRating> {
    @Override
    protected void parseValue(XmlPullParser parser, AspectRating aspectRating, String name) throws IOException, XmlPullParserException {
        if (name.equals("type")) {
            aspectRating.setType(readText(parser));
        } else if (name.equals("rating")) {
            aspectRating.setRating(readInteger(parser));
        }  else {
            skip(parser);
        }
    }
}
