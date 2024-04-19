package id.ois.gp.database;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class MasterUrlDataSource {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public MasterUrlDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void addUrl(String url) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_URL, url);
        database.insert(DatabaseHelper.TABLE_MASTER_URL, null, values);
    }

    public List<String> getAllUrls() {
        List<String> urls = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_MASTER_URL,
                new String[]{DatabaseHelper.COLUMN_URL},
                null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            urls.add(cursor.getString(0));
            cursor.moveToNext();
        }

        cursor.close();
        return urls;
    }

    @SuppressLint("Range")
    public String getTopUrl() {
        String url = null;
        Cursor cursor = database.query(DatabaseHelper.TABLE_MASTER_URL,
                new String[]{DatabaseHelper.COLUMN_URL},
                null, null, null, null, DatabaseHelper.COLUMN_ID + " ASC", "1");

        if (cursor != null && cursor.moveToFirst() && cursor.getColumnCount() > 0) {
            url = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_URL));
        }

        if (cursor != null) {
            cursor.close();
        }

        return url;
    }


    public void updateUrl(long id, String newUrl) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_URL, newUrl);
        database.update(DatabaseHelper.TABLE_MASTER_URL, values,
                DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void deleteUrl(long id) {
        database.delete(DatabaseHelper.TABLE_MASTER_URL,
                DatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }
}