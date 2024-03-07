package com.lihkin16.notes_apk.ui.fragments;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.lihkin16.notes_apk.R;
import com.lihkin16.notes_apk.data.model.Note;
import com.lihkin16.notes_apk.data.model.NoteSavedListener;
import com.lihkin16.notes_apk.ui.adapters.NoteAdapter;
import com.lihkin16.notes_apk.utils.NoteDataSource;
import com.lihkin16.notes_apk.utils.NoteDatabaseHelper;

import java.util.List;

public class NoteListFragment extends Fragment implements NoteSavedListener {

    private TextView textView;
    private GoogleSignInClient googleSignInClient ;

    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private NoteDataSource noteDataSource;
    String userIdentifier;

    private NoteAdapter.OnNoteItemClickListener onNoteItemClickListener = new NoteAdapter.OnNoteItemClickListener() {
        @Override
        public void onNoteItemClick(Note note) {
            showAddNoteDialog(note);


        }
        public void onEditButtonClick(Note note) {
            showAddNoteDialog(note);
        }

        public void onDeleteButtonClick(Note note) {

            showDeleteConfirmationDialog(note);
        }



        private void showDeleteConfirmationDialog(Note note) {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Delete Note");
            builder.setMessage("Are you sure you want to delete this note?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteNoteFromDatabase(note);
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.show();
        }

        private void deleteNoteFromDatabase(Note note) {
            noteDataSource.deleteNote(note.getId());
            noteAdapter.setNotes(noteDataSource.getAllNotes());
        }





        private void updateNoteInDatabase(Note note) {

            noteDataSource.updateNote(note.getId(), note.getTitle(), note.getContent());
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes_list, container, false);


        textView  =view.findViewById(R.id.name) ;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(view.getContext());

        if(acct!=null)
        {
            String name = acct.getDisplayName();
            if (name != null) {
                String upperCaseName = name.toUpperCase();
                textView.setText(upperCaseName);
            }
        }


         userIdentifier = acct.getId();
        NoteDatabaseHelper noteDatabaseHelper = new NoteDatabaseHelper(requireContext(), userIdentifier);



        recyclerView = view.findViewById(R.id.recyclerViewNotes);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        noteDataSource = new NoteDataSource(requireContext() , userIdentifier);
        noteDataSource.open();

        List<Note> notes = noteDataSource.getAllNotes();
        noteAdapter = new NoteAdapter(requireContext(), notes , onNoteItemClickListener );
        recyclerView.setAdapter(noteAdapter);

        Button buttonAddNote = view.findViewById(R.id.btnAddNote);
        buttonAddNote.setOnClickListener(v -> {
            showAddNoteDialog(null);
        });

        return view;
    }

    private void showAddNoteDialog(@Nullable Note note) {
        AddEditNoteFragment addEditNoteFragment = new AddEditNoteFragment();

        addEditNoteFragment.setNoteDataSource(noteDataSource);
        addEditNoteFragment.setNoteAdapter(noteAdapter);

        Bundle bundle = new Bundle();
        bundle.putString("userIdentifier", userIdentifier);
        if (note != null) {
            bundle.putParcelable("note", (Parcelable) note);
        }
        addEditNoteFragment.setArguments(bundle);

        // Use FragmentTransaction to replace the current fragment with AddEditNoteFragment
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, addEditNoteFragment)
                .addToBackStack(null) // If you want to add to the back stack
                .commit();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        noteDataSource.close();
    }


    @Override
    public void onNoteSaved(NoteDataSource noteDataSource, NoteAdapter noteAdapter) {
        List<Note> updatedNotes = noteDataSource.getAllNotes();
        noteAdapter.setNotes(updatedNotes);
        noteAdapter.notifyDataSetChanged();
    }
}
