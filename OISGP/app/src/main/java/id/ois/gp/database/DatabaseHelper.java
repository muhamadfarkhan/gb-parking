package id.ois.gp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "master_url_db";
    private static final int DATABASE_VERSION = 1;


    // Data default
    private static final String[] DEFAULT_URLS = {
            "http://api-gp.farkhan.biz.id"
    };


    // Nama tabel dan kolom
    public static final String TABLE_MASTER_URL = "master_url";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_URL = "url";

    // Query pembuatan tabel
    private static final String CREATE_TABLE_MASTER_URL = "CREATE TABLE " +
            TABLE_MASTER_URL + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_URL + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MASTER_URL);

        insertDefaultUrls(db);
    }


    private void insertDefaultUrls(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        for (String url : DEFAULT_URLS) {
            values.put(COLUMN_URL, url);
            db.insert(TABLE_MASTER_URL, null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MASTER_URL);
        onCreate(db);
    }
}