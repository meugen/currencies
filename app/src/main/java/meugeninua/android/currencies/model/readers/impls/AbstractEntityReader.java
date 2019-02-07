package meugeninua.android.currencies.model.readers.impls;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import meugeninua.android.currencies.model.readers.EntityReader;

abstract class AbstractEntityReader<T> implements EntityReader<T> {

    private final String tagName;

    AbstractEntityReader(final String tagName) {
        this.tagName = tagName;
    }

    protected abstract T readOne(final XmlPullParser parser) throws XmlPullParserException, IOException;

    protected abstract boolean isValidStart(final XmlPullParser parser) throws XmlPullParserException;

    @Override
    public T readOne(final Reader reader) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(reader);
        return readOne(parser);
    }

    @Override
    public List<T> readList(final Reader reader) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(reader);

        final List<T> result = new ArrayList<>();
        while (true) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.END_DOCUMENT) {
                break;
            }
            if (eventType == XmlPullParser.START_TAG && tagName.equals(parser.getName())) {
                result.add(readOne(parser));
            }
        }
        return result;
    }
}
