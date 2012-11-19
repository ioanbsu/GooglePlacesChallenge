package com.artigile.android.aroundme.api.parser;

import com.artigile.android.aroundme.api.model.AspectRating;
import com.artigile.android.aroundme.api.model.PlaceReview;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author IoaN, 11/10/12 2:52 PM
 */
@Singleton
public class PlaceReviewXmlToEntityParser  extends AbstractXmlToEntityParser<PlaceReview>{

    @Inject
    private AspectRatingXmlToEntityParser aspectRatingXmlToEntityParser;

    @Override
    protected void parseValue(XmlPullParser parser, PlaceReview placeReview, String name) throws IOException, XmlPullParserException {
        if (name.equals("aspects")) {
            if(placeReview.getAspectRatings()==null){
                placeReview.setAspectRatings(new ArrayList<AspectRating>());
            }
            placeReview.getAspectRatings().add(aspectRatingXmlToEntityParser.parse(parser,"aspects",new AspectRating() ));
        } else if (name.equals("author_name")) {
            placeReview.setAuthorName(readText(parser));
        } else if (name.equals("author_url")) {
            placeReview.setAuthorUrl(readText(parser));
        } else if (name.equals("text")) {
            placeReview.setText(readText(parser));
        }  else if (name.equals("time")) {
            placeReview.setTime(readText(parser));
        } else {
            skip(parser);
        }
    }
}
