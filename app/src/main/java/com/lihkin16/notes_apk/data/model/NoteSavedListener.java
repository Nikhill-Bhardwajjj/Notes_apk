package com.lihkin16.notes_apk.data.model;

import com.lihkin16.notes_apk.ui.adapters.NoteAdapter;
import com.lihkin16.notes_apk.utils.NoteDataSource;

public interface NoteSavedListener {
    void onNoteSaved(NoteDataSource noteDataSource, NoteAdapter noteAdapter);
}