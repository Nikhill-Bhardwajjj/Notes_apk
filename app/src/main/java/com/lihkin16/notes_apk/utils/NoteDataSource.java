package com.lihkin16.notes_apk.utils;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.lihkin16.notes_apk.data.model.Note;
import com.lihkin16.notes_apk.utils.NoteDatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class NoteDataSource {

    private SQLiteDatabase database;
    private NoteDatabaseHelper dbHelper;

    private String userIdentifier;

    public NoteDataSource(Context context , String userIdentifier) {
        dbHelper = new NoteDatabaseHelper(context , userIdentifier);
        this.userIdentifier = userIdentifier;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // Insert a new note
    public long insertNote(String title, String content) {
        ContentValues values = new ContentValues();
        values.put(NoteDatabaseHelper.COLUMN_TITLE, title);
        values.put(NoteDatabaseHelper.COLUMN_CONTENT, content);

        return database.insert(NoteDatabaseHelper.TABLE_NOTES, null, values);
    }

    // Update a note
    public int updateNote(long noteId, String title, String content) {
        ContentValues values = new ContentValues();
        values.put(NoteDatabaseHelper.COLUMN_TITLE, title);
        values.put(NoteDatabaseHelper.COLUMN_CONTENT, content);

        return database.update(
                NoteDatabaseHelper.TABLE_NOTES,
                values,
                NoteDatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(noteId)});
    }

    // Delete a note
    public int deleteNote(long noteId) {
        return database.delete(
                NoteDatabaseHelper.TABLE_NOTES,
                NoteDatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(noteId)});
    }

    // Get all notes
    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        Cursor cursor = database.query(
                NoteDatabaseHelper.TABLE_NOTES,
                null,
                null,
                null,
                null,
                null,
                null);

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    do {
                        long noteId = cursor.getLong(cursor.getColumnIndex(NoteDatabaseHelper.COLUMN_ID));
                        String title = cursor.getString(cursor.getColumnIndex(NoteDatabaseHelper.COLUMN_TITLE));
                        String content = cursor.getString(cursor.getColumnIndex(NoteDatabaseHelper.COLUMN_CONTENT));

                        Note note = new Note(noteId, title, content);
                        notes.add(note);
                    } while (cursor.moveToNext());
                }
            } finally {
                cursor.close();
            }
        }
        return notes;
    }
}
