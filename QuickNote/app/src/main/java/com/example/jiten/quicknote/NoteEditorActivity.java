package com.example.jiten.quicknote;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashSet;

public class NoteEditorActivity extends AppCompatActivity {

    EditText editText ;
    int noteId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);
        editText = findViewById(R.id.editText);
        Intent intent = getIntent();
        noteId = intent.getIntExtra("NoteId",-1);
        if(noteId != -1){
            editText.setText(MainActivity.arrayList.get(noteId));
        }else{
            MainActivity.arrayList.add("");
            noteId = MainActivity.arrayList.size()-1;
            MainActivity.arrayAdapter.notifyDataSetChanged();
        }
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MainActivity.arrayList.set(noteId,String.valueOf(s));
                MainActivity.arrayAdapter.notifyDataSetChanged();
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.jiten.quicknote", Context.MODE_PRIVATE);
                HashSet<String> set = new HashSet<>(MainActivity.arrayList);
                sharedPreferences.edit().putStringSet("Notes",set).apply();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}
