package com.artigile.android.placesapi;

import android.util.JsonReader;
import android.util.JsonToken;
import com.google.inject.Singleton;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * User: ioanbsu
 * Date: 11/9/12
 * Time: 12:37 PM
 */
@Singleton
public class IoaNJsonReader {

    public List readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));

        try {
            return readMessagesArray(reader);
        } catch (Exception e) {
            System.out.println("ddfgdfg");
        } finally {
            reader.close();
        }
        return new ArrayList();
    }


    public List readMessagesArray(JsonReader reader) throws IOException {
        List messages = new ArrayList();

        reader.beginObject();
        while (reader.hasNext()) {
            messages.add(readMessage(reader));
        }
        reader.endArray();
        return messages;
    }

    public String readMessage(JsonReader reader) throws IOException {
        long id = -1;
        String text = null;
        List geo = null;

        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id")) {
                id = reader.nextLong();
            } else if (name.equals("text")) {
                text = reader.nextString();
            } else if (name.equals("geo") && reader.peek() != JsonToken.NULL) {
                geo = readDoublesArray(reader);
            } else if (name.equals("user")) {
                //  user = readUser(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return "asdfasdfasd";
    }

    public List readDoublesArray(JsonReader reader) throws IOException {
        List doubles = new ArrayList();

        reader.beginArray();
        while (reader.hasNext()) {
            doubles.add(reader.nextDouble());
        }
        reader.endArray();
        return doubles;
    }

    public String readUser(JsonReader reader) throws IOException {
        String username = null;
        int followersCount = -1;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("name")) {
                username = reader.nextString();
            } else if (name.equals("followers_count")) {
                followersCount = reader.nextInt();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return "sdfgsdfg";
    }
}


