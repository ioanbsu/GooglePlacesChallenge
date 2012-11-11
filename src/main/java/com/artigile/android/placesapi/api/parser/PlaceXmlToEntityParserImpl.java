package com.artigile.android.placesapi.api.parser;

import com.artigile.android.placesapi.api.model.*;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.ArrayList;

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

    @Inject
    private AddressComponentXmToEntityParser addressComponentXmToEntityParser;

    @Inject
    private PlaceEventXmlToEntityParser placeEventXmlToEntityParser;

    @Inject
    private PlaceReviewXmlToEntityParser placeReviewXmlToEntityParser;

    @Override
    protected void parseValue(XmlPullParser parser, PlacesApiResponseEntity placesApiResponseEntity, String name) throws IOException, XmlPullParserException {
        if (name.equals("status")) {
            placesApiResponseEntity.setStatus(readText(parser));
        } else if (name.equals("next_page_token")) {
            placesApiResponseEntity.setNextPageToken(readText(parser));
        } else if (name.equals("html_attribution")) {
            placesApiResponseEntity.setHtmlAttribution(readText(parser));
        } else if (name.equals("result")) {
            if (placesApiResponseEntity.getPlaceList() == null) {
                placesApiResponseEntity.setPlaceList(new ArrayList<Place>());
            }
            placesApiResponseEntity.getPlaceList().add(parseSingleEntity(parser));
        } else {
            skip(parser);
        }
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
            } else if (name.equals("address_component")) {
                if (place.getAddressComponents() == null) {
                    place.setAddressComponents(new ArrayList<AddressComponent>());
                }
                place.getAddressComponents().add(addressComponentXmToEntityParser.parse(parser, "address_component", new AddressComponent()));
            } else if (name.equals("events")) {
                if (place.getPlaceEvents() == null) {
                    place.setPlaceEvents(new ArrayList<PlaceEvent>());
                }
                place.getPlaceEvents().add(placeEventXmlToEntityParser.parse(parser, "events", new PlaceEvent()));
            } else if (name.equals("vicinity")) {
                place.setVicinity(readText(parser));
            } else if (name.equals("formatted_address")) {
                place.setFormattedAddress(readText(parser));
            } else if (name.equals("formatted_phone_number")) {
                place.setFormattedPhoneNumber(readText(parser));
            } else if (name.equals("international_phone_number")) {
                place.setInternationalPhoneNumber(readText(parser));
            } else if (name.equals("type")) {
                if (place.getTypes() == null) {
                    place.setTypes(new ArrayList<String>());
                }
                place.getTypes().add(readText(parser));
            } else if (name.equals("geometry")) {
                place.setGeometry(geometryXmlParser.parse(parser, "geometry", new Geometry()));
            } else if (name.equals("rating")) {
                place.setRating(readFloat(parser));
            } else if (name.equals("icon")) {
                place.setIcon(readText(parser));
            } else if (name.equals("reference")) {
                place.setReference(readText(parser));
            } else if (name.equals("review")) {
                if (place.getPlaceReview() == null) {
                    place.setPlaceReview(new ArrayList<PlaceReview>());
                }
                place.getPlaceReview().add(placeReviewXmlToEntityParser.parse(parser, "review", new PlaceReview()));
            } else if (name.equals("opening_hours")) {
                place.setOpeningHours(openingHoursXmlParser.parse(parser, "opening_hours", new OpeningHours()));
            } else if (name.equals("utc_offset")) {
                place.setUtcOffset(readInteger(parser));
            } else if (name.equals("website")) {
                place.setWebsite(readText(parser));
            } else {
                skip(parser);
            }
        }
        return place;
    }
}
