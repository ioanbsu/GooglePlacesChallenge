package com.artigile.android.aroundme.placesapi.parser;

import com.artigile.android.aroundme.placesapi.model.AddressComponent;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author IoaN, 11/10/12 2:24 PM
 */
@Singleton
public class AddressComponentXmToEntityParser extends AbstractXmlToEntityParser<AddressComponent> {
    @Override
    protected void parseValue(XmlPullParser parser, AddressComponent address, String name) throws IOException, XmlPullParserException {
        if (name.equals("type")) {
            if (address.getTypes() == null) {
                address.setTypes(new ArrayList<String>());
            }
            address.getTypes().add(readText(parser));
        } else if (name.equals("long_name")) {
            address.setLongName(readText(parser));
        } else if (name.equals("short_name")) {
            address.setShortName(readText(parser));
        } else {
            skip(parser);
        }
    }
}
