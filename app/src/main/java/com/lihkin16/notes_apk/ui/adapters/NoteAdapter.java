package com.lihkin16.notes_apk.ui.adapters;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lihkin16.notes_apk.R;
import com.lihkin16.notes_apk.data.model.Note;
import com.lihkin16.notes_apk.utils.NoteDataSource;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private List<Note> notes;
    private Context context;
    private OnNoteItemClickListener onNoteItemClickListener;

    public NoteAdapter(Context context, List<Note> notes, OnNoteItemClickListener onNoteItemClickListener) {
        this.context = context;
        this.notes = notes;
        this.onNoteItemClickListener = onNoteItemClickListener;

    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view, onNoteItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.bind(note);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    public List<Note> getNotes() {
        return notes;
    }


    public void updateData(List<Note> updatedNotes) {
        notes = updatedNotes;
        notifyDataSetChanged();
    }

    // Remove a note from the adapter
    public void removeNote(int position) {
        notes.remove(position);
        notifyItemRemoved(position);
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView contentTextView;
        ImageButton buttonEdit;
        ImageButton buttonDelete;


        public NoteViewHolder(@NonNull View itemView, OnNoteItemClickListener listener) {
            super(itemView);

            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
            titleTextView = itemView.findViewById(R.id.textViewNoteTitle);
            contentTextView = itemView.findViewById(R.id.textViewNoteContent);


            buttonEdit.setOnClickListener(v -> {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onNoteItemClickListener != null) {
                    onNoteItemClickListener.onEditButtonClick(notes.get(position));
                }
            });

            buttonDelete.setOnClickListener(v -> {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onNoteItemClickListener != null) {
                    onNoteItemClickListener.onDeleteButtonClick(notes.get(position));
                }
            });


        }

        public void bind(Note note) {
            titleTextView.setText(note.getTitle());
            contentTextView.setText(note.getContent());


        }
    }







    public interface OnNoteItemClickListener {
        void onNoteItemClick(Note note);
        void onEditButtonClick(Note note);
        void onDeleteButtonClick(Note note);



    }

        public void addNote(Note note) {
            notes.add(note);
            notifyItemInserted(notes.size() - 1);
        }
    }



