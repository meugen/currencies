package meugeninua.android.currencies.ui.activities.main;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import javax.inject.Inject;

import meugeninua.android.currencies.R;
import meugeninua.android.currencies.ui.activities.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Inject
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainActivity", "database: " + database);
        setContentView(R.layout.activity_main);
    }
}
