package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NotesActivity extends AppCompat {
    LanguageManger lang;
    TextView email_text ;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef =  database.getReference();
    ArrayList<Note> notesList = new ArrayList<>();
    RecyclerView recyclerView;
    Adapter adapter;
    FloatingActionButton newNoteBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        lang = new LanguageManger(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        //Header , display user name
        email_text = findViewById(R.id.email_text);
        String userName = user.getEmail().split("@")[0];
        email_text.setText(getString(R.string.hey) + " " +  userName);

        //NavBar, Set the material BottomAppBar to be the default bar
        BottomAppBar bottomAppBar = findViewById(R.id.bottom_app_bar);
        setSupportActionBar(bottomAppBar);

        //ListView of notes
        recyclerView = findViewById(R.id.notes_list);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this,notesList);
        recyclerView.setAdapter(adapter);

        newNoteBtn = findViewById(R.id.button_new_note);
        newNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newNoteIntent = new Intent(getApplicationContext(),NewNoteActivity.class);
                startActivity(newNoteIntent);
            }
        });

    }
    //bottom_app_bar_menu Connection
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.bottom_app_bar_menu,menu);
        return true;
    }

    //Option Selector for click on item in the Menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.app_bar_logout: {
                FirebaseAuth.getInstance().signOut();
                this.finish();
            }
            case R.id.ChangeLanguage: {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(NotesActivity.this,R.style.Theme_AppCompat_Dialog);
                builder.setIcon(R.drawable.ic_baseline_language_24);
                builder.setTitle(R.string.choose_lang_msg);
                builder.setNegativeButton("English", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        lang.updateResource("us");
                        recreate();

                    }
                });
                builder.setPositiveButton("עברית", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        lang.updateResource("iw");
                        recreate();

                    }
                });

                builder.show();
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

        @Override
    protected void onStart() {
        super.onStart();

      myRef.addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull  DataSnapshot snapshot) {
             if(  !snapshot.hasChild(user.getUid())){
                 database.getReference(user.getUid() + "/email").setValue(user.getEmail());

             }else{
                 if(snapshot.child(user.getUid()).hasChild("notes")){
                     notesList.clear();
                   for(DataSnapshot snap  : snapshot.child(user.getUid()).child("notes").getChildren()){

                        Note note = snap.getValue(Note.class);
                       assert note != null;
                       note.setNoteId(snap.getKey());
                       notesList.add(note);
                     }
                    adapter.notifyDataSetChanged();
                 }


             }
          }
          @Override
          public void onCancelled(@NonNull DatabaseError error) {

          }
      });

    adapter.setOnItemClickListener(new Adapter.ClickListener() {
        @Override
        public void onItemClick(int position, View v) {
            Log.i("key", notesList.get(position).getNoteId() );
            Intent intent = new Intent(getApplicationContext(),NoteActivity.class);
            intent.putExtra("noteId",notesList.get(position).getNoteId());
            startActivity(intent);

        }
    });


    }
}
