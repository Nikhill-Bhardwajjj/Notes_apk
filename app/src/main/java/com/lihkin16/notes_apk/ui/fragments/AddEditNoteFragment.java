package com.lihkin16.notes_apk.ui.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lihkin16.notes_apk.R;
import com.lihkin16.notes_apk.data.model.Note;
import com.lihkin16.notes_apk.data.model.NoteSavedListener;
import com.lihkin16.notes_apk.ui.adapters.NoteAdapter;
import com.lihkin16.notes_apk.utils.NoteDataSource;

public class AddEditNoteFragment extends Fragment {

    private OnNoteSavedListener onNoteSavedListener;

    private EditText inputTitle;
    private EditText inputContent;

    private Note currentNote;
    private Button btnSaveNote;
    private String userIdentifier;
    private NoteDataSource noteDataSource;
    private NoteAdapter noteAdapter;

    public void setNoteDataSource(NoteDataSource noteDataSource) {
        this.noteDataSource = noteDataSource;
    }

    public void setNoteAdapter(NoteAdapter noteAdapter) {
        this.noteAdapter = noteAdapter;
    }

    public void setOnNoteSavedListener(NoteSavedListener onNoteSavedListener) {
        this.onNoteSavedListener = (OnNoteSavedListener) onNoteSavedListener;
    }

    public interface OnNoteSavedListener {
        void onNoteSaved(NoteDataSource noteDataSource, NoteAdapter noteAdapter);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add__notes, container, false);

        inputTitle = view.findViewById(R.id.inputTitle);
        inputContent = view.findViewById(R.id.inputContent);
        btnSaveNote = view.findViewById(R.id.btnSaveNote);


        if (getArguments() != null) {
            userIdentifier = getArguments().getString("userIdentifier");
            if (userIdentifier != null) {
                noteDataSource = new NoteDataSource(requireContext(), userIdentifier);
            } else {
                Toast.makeText(view.getContext(), "User not found", Toast.LENGTH_SHORT).show();
            }

            currentNote = getArguments().getParcelable("note");
            if (currentNote != null) {
                inputTitle.setText(currentNote.getTitle());
                inputContent.setText(currentNote.getContent());
            }
        }



        noteDataSource = new NoteDataSource(requireContext(), userIdentifier);
        noteDataSource.open();
        btnSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                saveNote();
            }
        });

        return view;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnNoteSavedListener) {
            onNoteSavedListener = (OnNoteSavedListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnNoteSavedListener");
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    private void saveNote() {
        String title = inputTitle.getText().toString();
        String content = inputContent.getText().toString();

        if (currentNote == null) {

            long noteId = noteDataSource.insertNote(title, content);
            if (noteId != -1) {
                Note newNote = new Note(noteId, title, content);
                // Notify listener or update UI
                if (onNoteSavedListener != null) {
                    onNoteSavedListener.onNoteSaved(noteDataSource, noteAdapter);
                }
            }
        } else {
            // Editing an existing note
            currentNote.setTitle(title);
            currentNote.setContent(content);
            noteDataSource.updateNote(currentNote.getId(), title, content);
            // Notify listener or update UI
            if (onNoteSavedListener != null) {
                onNoteSavedListener.onNoteSaved(noteDataSource, noteAdapter);
            }
        }
    }

    public void setOnNoteSavedListener(OnNoteSavedListener onNoteSavedListener) {
        this.onNoteSavedListener = onNoteSavedListener;
    }



}
