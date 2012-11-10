package com.artigile.android.placesapi.parser;

import com.artigile.android.placesapi.model.Place;
import com.artigile.android.placesapi.model.PlacesApiResponseEntity;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: ioanbsu
 * Date: 11/9/12
 * Time: 2:31 PM
 */
@Singleton
public class PlaceXmlToEntityParserImpl extends AbstractXmlToEntityParser<PlacesApiResponseEntity> {

    @Inject
    private GeometryXmlToEntityParser geometryXmlParser;

    @Inject
    private OpeningHoursXmlToEntityParser openingHoursXmlParser;


    @Override
    public PlacesApiResponseEntity parse(XmlPullParser parser,String requiredTag) throws XmlPullParserException, IOException {
        PlacesApiResponseEntity placesApiResponseEntity = new PlacesApiResponseEntity();
        List<Place> entries = new ArrayList<Place>();
        parser.require(XmlPullParser.START_TAG, "", requiredTag);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("status")) {
                placesApiResponseEntity.setStatus(readText(parser));
            } else if (name.equals("next_page_token")) {
                placesApiResponseEntity.setNextPageToken(readText(parser));
            } else if (name.equals("html_attribution")) {
                placesApiResponseEntity.setHtmlAttribution(readText(parser));
            } else if (name.equals("result")) {
                entries.add(parseSingleEntity(parser));
            } else {
                skip(parser);
            }
        }
        placesApiResponseEntity.setPlaceList(entries);
        return placesApiResponseEntity;
    }


    private Place parseSingleEntity(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, "", "result");
        Place place = new Place();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("id")) {
                place.setId(readText(parser));
            } else if (name.equals("name")) {
                place.setName(readText(parser));
            } else if (name.equals("vicinity")) {
                place.setVicinity(readText(parser));
            }  else if (name.equals("formatted_address")) {
                place.setFormattedAddress(readText(parser));
            } else if (name.equals("type")) {
                if (place.getTypes() == null) {
                    place.setTypes(new ArrayList<String>());
                }
                place.getTypes().add(readText(parser));
            } else if (name.equals("geometry")) {
                place.setGeometry(geometryXmlParser.parse(parser,"geometry"));
            } else if (name.equals("rating")) {
                place.setRating(readFloat(parser));
            } else if (name.equals("icon")) {
                place.setIcon(readText(parser));
            } else if (name.equals("reference")) {
                place.setReference(readText(parser));
            } else if (name.equals("opening_hours")) {
                place.setOpeningHours(openingHoursXmlParser.parse(parser,"opening_hours"));
            } else {
                skip(parser);
            }
        }
        return place;
    }
}
