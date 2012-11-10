package com.artigile.android.placesapi.parser;

import com.artigile.android.placesapi.model.Geometry;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * User: ioanbsu
 * Date: 11/9/12
 * Time: 2:30 PM
 */
public interface XmlToEntityParser<T> {

    T parse(XmlPullParser parser,String requiredTag) throws IOException, XmlPullParserException;

    String readText(XmlPullParser parser) throws IOException, XmlPullParserException;

    Float readFloat(XmlPullParser parser) throws IOException, XmlPullParserException;

    Boolean readBoolean(XmlPullParser parser) throws IOException, XmlPullParserException;
}
