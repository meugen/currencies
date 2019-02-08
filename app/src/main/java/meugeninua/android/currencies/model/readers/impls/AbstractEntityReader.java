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

    @Override
    public T readOne(final Reader reader) throws XmlPullParserException, IOException {
        StoreEntityReadListenerImpl<T> listener = new StoreEntityReadListenerImpl<>();
        readOne(reader, listener);
        return listener.result.isEmpty() ? null : listener.result.get(0);
    }

    @Override
    public List<T> readList(final Reader reader) throws XmlPullParserException, IOException {
        StoreEntityReadListenerImpl<T> listener = new StoreEntityReadListenerImpl<>();
        readList(reader, listener);
        return listener.result;
    }

    @Override
    public void readOne(Reader reader, OnEntityReadListener<T> listener) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(reader);
        listener.onEntityRead(readOne(parser));
    }

    @Override
    public void readList(Reader reader, OnEntityReadListener<T> listener) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(reader);

        boolean isContinue = true;
        while (isContinue) {
            int eventType = parser.next();
            if (eventType == XmlPullParser.END_DOCUMENT) {
                break;
            }
            if (eventType == XmlPullParser.START_TAG && tagName.equals(parser.getName())) {
                isContinue = listener.onEntityRead(readOne(parser));
            }
        }
    }

    private static class StoreEntityReadListenerImpl<T> implements OnEntityReadListener<T> {

        final List<T> result = new ArrayList<>();

        @Override
        public boolean onEntityRead(T entity) {
            result.add(entity);
            return true;
        }
    }
}
