package com.example.abozyigit.homework;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

//İkinci ödevden alınmıştır.
public class Database extends SQLiteOpenHelper {

    private static String DB_NAME = "AppDB";
    private static int DB_VERSION = 1;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String command = "CREATE TABLE MYTABLE( "
                + "ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "SURE INTEGER,"
                + "ZORLUK BOOLEAN)";
        db.execSQL(command);
    }

    public void insert(boolean zorluk, int sure) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("ZORLUK", zorluk);
        contentValues.put("SURE", sure);
        sqLiteDatabase.insert("MYTABLE", null, contentValues);
    }

    public List<String> verileriAl() {
        List<String> skorlar = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from MYTABLE", null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                boolean zorluk = cursor.getString(cursor.getColumnIndex("ZORLUK")).equals("1");
                int sure = cursor.getInt(cursor.getColumnIndex("SURE"));
                skorlar.add((zorluk ? "Zor modda " : "Kolay modda ") + sure + " saniye");
                cursor.moveToNext();
            }
        }
        cursor.close();
        return skorlar;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS MYTABLE");
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}