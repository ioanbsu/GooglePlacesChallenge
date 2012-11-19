package com.artigile.android.aroundme.api.parser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * User: ioanbsu
 * Date: 11/9/12
 * Time: 2:30 PM
 */
public interface XmlToEntityParser<T> {

    T parse(XmlPullParser parser,String requiredTag,T objectInstance) throws IOException, XmlPullParserException;

    String readText(XmlPullParser parser) throws IOException, XmlPullParserException;

    Float readFloat(XmlPullParser parser) throws IOException, XmlPullParserException;

    Integer readInteger(XmlPullParser parser) throws IOException, XmlPullParserException;

    Boolean readBoolean(XmlPullParser parser) throws IOException, XmlPullParserException;
}
