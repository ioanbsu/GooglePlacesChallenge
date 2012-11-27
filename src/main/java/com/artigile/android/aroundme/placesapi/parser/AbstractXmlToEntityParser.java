package com.artigile.android.aroundme.placesapi.parser;

import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * User: ioanbsu
 * Date: 11/9/12
 * Time: 2:32 PM
 */
public abstract class AbstractXmlToEntityParser<T> implements XmlToEntityParser<T> {

    protected abstract  void parseValue(XmlPullParser parser, T object, String name) throws IOException, XmlPullParserException;

    @Override
    final public T parse(XmlPullParser parser,String requiredTag,T  object) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, "",requiredTag);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            parseValue(parser, object, name);
        }
        return object;
    }

    @Override
    public String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    @Override
    public Float readFloat(XmlPullParser parser) throws IOException, XmlPullParserException {
        Float result = null;
        if (parser.next() == XmlPullParser.TEXT) {
            try {
                result = Float.valueOf(parser.getText());
            } catch (NumberFormatException e) {
                Log.d("DEBUG_TAG", "Failed to parse Float value from XMl response");

            }
            parser.nextTag();
        }
        return result;
    }

    @Override
    public Integer readInteger(XmlPullParser parser) throws IOException, XmlPullParserException {
        Integer result = null;
        if (parser.next() == XmlPullParser.TEXT) {
            try {
                result = Integer.valueOf(parser.getText());
            } catch (NumberFormatException e) {
                Log.d("DEBUG_TAG", "Failed to parse Float value from XMl response");

            }
            parser.nextTag();
        }
        return result;
    }

    @Override
    public Boolean readBoolean(XmlPullParser parser) throws IOException, XmlPullParserException {
        Boolean result = null;
        if (parser.next() == XmlPullParser.TEXT) {
            result = Boolean.parseBoolean(parser.getText());
            parser.nextTag();
        }
        return result;
    }

    protected void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            int nextToken = parser.next();
            if (nextToken == XmlPullParser.TEXT) {
                Log.w("SKIPPED: ", parser.getText());
            }
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

}
