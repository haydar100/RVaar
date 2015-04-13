package hu.rijkswaterstaat.rvaar.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Haydar on 13-04-15.
 * http://stackoverflow.com/questions/9109438/how-to-use-an-existing-database-with-an-android-application
 * http://stackoverflow.com/questions/6540906/simple-export-and-import-of-a-sqlite-database-on-android
 */
public class SQLiteConnector extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "rVaar.db";
    private static final int DATABASE_VERSION = 1;


    public SQLiteConnector(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        /// methode aanroepen die een database aanmaakt
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        // als je de database upgrade dan kan je dus operaties uitvoeren via deze methode
    }
}
