
package com.example.scribble;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NotesActivity extends AppCompatActivity {

    private EditText inputNote, searchNote;
    private Button addNoteButton;
    private RecyclerView recyclerView;
    private NotesAdapter notesAdapter;
    private ArrayList<String> notesList;
    private NotesDatabaseHelper dbHelper;
    private TextView welcomeText;
    private ImageView tickImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_activity);

        inputNote = findViewById(R.id.inputNote);
        searchNote = findViewById(R.id.searchNote);
        addNoteButton = findViewById(R.id.addNoteButton);
        recyclerView = findViewById(R.id.recyclerView);
        welcomeText = findViewById(R.id.welcomeText);
        tickImageView = findViewById(R.id.tickImageView); // Initialize tick ImageView


        notesList = new ArrayList<>();
        dbHelper = new NotesDatabaseHelper(this);

        loadNotesFromDatabase();

        notesAdapter = new NotesAdapter(notesList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(notesAdapter);

        // Display welcome message
        String username = getIntent().getStringExtra("username");
        if (username != null) {
            welcomeText.setText("Welcome, " + username + "!");
        } else {
            welcomeText.setText("Welcome, Guest!");
        }

        // Search functionality
        searchNote.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterNotes(s.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        // Add note functionality
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String note = inputNote.getText().toString();
                if (!TextUtils.isEmpty(note)) {
                    saveNoteToDatabase(note);
                    notesList.add(note);
                    notesAdapter.notifyItemInserted(notesList.size() - 1);
                    inputNote.setText("");

                    showTickAnimation();

                }
            }
        });
    }

    private void showTickAnimation() {
        // Make the tick image visible
        tickImageView.setVisibility(View.VISIBLE);

        // Add a fade-out animation for the tick image
        tickImageView.animate()
                .alpha(0f)
                .setDuration(9000) // Adjust the duration of the animation
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        // Hide the tick image after the animation completes
                        tickImageView.setVisibility(View.GONE);
                        tickImageView.setAlpha(1f); // Reset the alpha value
                    }
                });
    }

    private void loadNotesFromDatabase() {
        notesList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("notes", new String[]{"note"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            notesList.add(cursor.getString(0));
        }
        cursor.close();
    }

    private void saveNoteToDatabase(String note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("note", note);
        db.insert("notes", null, values);
    }

    private void deleteNoteFromDatabase(String note) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("notes", "note = ?", new String[]{note});
    }

    private void filterNotes(String query) {
        ArrayList<String> filteredList = new ArrayList<>();
        for (String note : notesList) {
            if (note.toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(note);
            }
        }
        notesAdapter.updateList(filteredList);
    }

    // Inner class for RecyclerView Adapter
    private class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

        private ArrayList<String> notesList;

        public NotesAdapter(ArrayList<String> notesList) {
            this.notesList = new ArrayList<>(notesList); // Make a copy to maintain original list
        }

        public void updateList(ArrayList<String> newList) {
            this.notesList.clear();
            this.notesList.addAll(newList);
            notifyDataSetChanged();
        }

        @Override
        public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item_activity, parent, false);
            return new NoteViewHolder(view);
        }

        @Override
        public void onBindViewHolder(NoteViewHolder holder, int position) {
            String note = notesList.get(position);
            holder.noteText.setText(note);
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteNoteFromDatabase(note);
                    notesList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, notesList.size());
                }
            });
        }

        @Override
        public int getItemCount() {
            return notesList.size();
        }

        // Inner class for ViewHolder
        class NoteViewHolder extends RecyclerView.ViewHolder {
            TextView noteText;
            Button deleteButton;

            public NoteViewHolder(View itemView) {
                super(itemView);
                noteText = itemView.findViewById(R.id.noteText);
                deleteButton = itemView.findViewById(R.id.deleteButton);
            }
        }
    }

    // Inner class for SQLite Helper
    private static class NotesDatabaseHelper extends android.database.sqlite.SQLiteOpenHelper {

        private static final String DATABASE_NAME = "notes.db";
        private static final int DATABASE_VERSION = 1;

        public NotesDatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE notes (id INTEGER PRIMARY KEY AUTOINCREMENT, note TEXT NOT NULL)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }
    }
}

