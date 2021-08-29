package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class NewNoteActivity extends AppCompat {

    FirebaseUser user;

    FirebaseDatabase database;
    DatabaseReference myRef;
    TextView noteTitleEdit ;
    TextView noteContextEdit ;
    Button saveNewNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef =  database.getReference().child(user.getUid()).child("notes");
        noteTitleEdit = findViewById(R.id.newNoteTitleEdit);
        noteContextEdit = findViewById(R.id.newNoteContextEdit);
        saveNewNote = findViewById(R.id.newNoteSaveBtn);



        saveNewNote.setActivated(false);
        saveNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(noteTitleEdit.getText().length()<4 && noteContextEdit.getText().length()<4){
                    Toast.makeText(getApplicationContext(), R.string.title_and_body_require_msg,Toast.LENGTH_SHORT).show();

                }
                else if(noteTitleEdit.getText().length()<4){
                    Toast.makeText(getApplicationContext(), R.string.title_is_require_msg,Toast.LENGTH_SHORT).show();
                }
                else if(noteContextEdit.getText().length()<4){
                    Toast.makeText(getApplicationContext(), R.string.context_is_required_msg,Toast.LENGTH_SHORT).show();
                }
                else{
                    String title = noteTitleEdit.getText().toString();
                    String context = noteContextEdit.getText().toString();
                    Note newNote = new Note(title,context);
                    myRef.push().setValue(newNote);
                    Toast.makeText(getApplicationContext(), R.string.new_note_created_msg,Toast.LENGTH_SHORT).show();
                    finish();

                }
            }
        });

        //Text Changed Listener for require from the user title and body of the text
        noteContextEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 4 && noteTitleEdit.getText().toString().length()> 4){

                    saveNewNote.setActivated(true);
                }else{
                    saveNewNote.setActivated(false);
                }

            }
        });
        noteTitleEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 4 && noteTitleEdit.getText().toString().length()> 4){

                    saveNewNote.setActivated(true);
                }else{
                    saveNewNote.setActivated(false);
                }

            }
        });

    }
}