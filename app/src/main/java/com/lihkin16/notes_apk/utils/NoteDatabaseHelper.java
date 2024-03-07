package com.lihkin16.notes_apk.utils;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NoteDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME_PREFIX = "notes.db";
    private static final int DATABASE_VERSION = 1;

    // Table name and column names
    public static final String TABLE_NOTES = "notes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";
    private String userIdentifier;


    // SQL statement to create the notes table
    private static final String CREATE_TABLE_NOTES =
            "CREATE TABLE " + TABLE_NOTES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_CONTENT + " TEXT);";

    public NoteDatabaseHelper(Context context ,String userIdentifier ) {
        super(context, DATABASE_NAME_PREFIX + userIdentifier+".db", null, DATABASE_VERSION);
         this.userIdentifier = userIdentifier;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the notes table
        db.execSQL(CREATE_TABLE_NOTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades if needed
        // For now, we'll just drop and recreate the table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
        onCreate(db);
    }
}
