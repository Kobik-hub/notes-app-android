package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class NoteActivity extends AppCompat {
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference myRef;

    TextView noteTitleEdit ;
    TextView noteContextEdit ;
    TextView noteTimeStamp;
    String noteId;
    Note note;
    Button saveNoteBtn;
    ImageView deleteNoteBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        noteId = getIntent().getStringExtra("noteId");

        myRef =  database.getReference().child(user.getUid()).child("notes").child(noteId);
        noteTitleEdit = findViewById(R.id.noteTitleEdit);
        noteContextEdit = findViewById(R.id.noteContextEdit);
        noteTimeStamp = findViewById(R.id.noteTimeStamp);
        saveNoteBtn = findViewById(R.id.saveNoteBtn);
        deleteNoteBtn = findViewById(R.id.deleteNoteBtn);
        saveNoteBtn.setActivated(false);
        saveNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child("title").setValue(noteTitleEdit.getText().toString());
                myRef.child("context").setValue(noteContextEdit.getText().toString());
                DatabaseReference kk = database.getReference().child(user.getUid()).child("notes").push();
                finish();

            }
        });

        deleteNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(NoteActivity.this,R.style.Theme_AppCompat_Dialog)
                        .setTitle(R.string.delete_note)
                        .setMessage(R.string.msg_delete_note)

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                myRef.removeValue();



                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)

                        .show()
                        .getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);


                ;
            }
        });

        //change the button mode for the selector
        noteContextEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean textChanged = Math.abs(count - before) == 1;
                if (textChanged) {
                    if (s.toString().length() > 4 && noteContextEdit.getText().toString().length()> 4){

                        saveNoteBtn.setActivated(true);
                    }else{
                        saveNoteBtn.setActivated(false);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        noteTitleEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean textChanged = Math.abs(count - before) == 1;
                if (textChanged) {
                    if (s.toString().length() > 4 && noteContextEdit.getText().toString().length()> 4){

                        saveNoteBtn.setActivated(true);
                    }else{
                        saveNoteBtn.setActivated(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
              note = snapshot.getValue(Note.class);
                note.setNoteId(snapshot.getKey());

                noteTitleEdit.setText( note.getTitle());
                noteContextEdit.setText(note.getContext());
                noteTimeStamp.setText(getString(R.string.created_in) + " " +  String.valueOf(note.getDate()));
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });


    }


}