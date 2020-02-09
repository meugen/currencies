package meugeninua.android.currencies.model.db;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurrenciesOpenHelper extends SQLiteOpenHelper {

    private static final String NAME = "currencies";
    private static final int VERSION = 1;

    private final AssetManager assets;

    public CurrenciesOpenHelper(Context context) {
        super(context, NAME, null, VERSION);
        this.assets = context.getAssets();
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        onUpgrade(db, 0, VERSION);
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int version = oldVersion + 1; version <= newVersion; version++) {
            upgrade(db, version);
        }
    }

    private void upgrade(SQLiteDatabase db, int version) {
        final CharSequence content = fetchSql(version);
        final Matcher matcher = Pattern.compile("[^;]+;").matcher(content);
        while (matcher.find()) {
            db.execSQL(matcher.group());
        }
    }

    private CharSequence fetchSql(int version) {
        try {
            Reader reader = null;
            try {
                final String sqlName = String.format(Locale.ENGLISH,
                        "db/%s/%d.sql", NAME, version);
                reader = new BufferedReader(new InputStreamReader(assets.open(sqlName)));

                final char[] buf = new char[256];
                final StringBuilder builder = new StringBuilder();
                while (true) {
                    final int count = reader.read(buf);
                    if (count < 0) {
                        break;
                    }
                    builder.append(buf, 0, count);
                }
                return builder;
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
