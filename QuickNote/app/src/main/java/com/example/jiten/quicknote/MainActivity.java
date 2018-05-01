package com.example.jiten.quicknote;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    static ArrayList<String> arrayList;
    static ArrayAdapter<String> arrayAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
     super.onOptionsItemSelected(item);
     if(item.getItemId()== R.id.addnote){
         Intent intent = new Intent(getApplicationContext(),NoteEditorActivity.class);
         startActivity(intent);
         return true;
     }
     return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.jiten.quicknote", Context.MODE_PRIVATE);
        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notes",null);
        if(set == null){
            arrayList.add("Example Note");
        }
        else{
            arrayList = new ArrayList(set);
        }

        arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),NoteEditorActivity.class);
                intent.putExtra("NoteId",position);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you Sure")
                        .setMessage("Do you want to remove the Current Note")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.arrayList.remove(position);
                                MainActivity.arrayAdapter.notifyDataSetChanged();
                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.jiten.quicknote", Context.MODE_PRIVATE);
                                HashSet<String> set = new HashSet<>(MainActivity.arrayList);
                                sharedPreferences.edit().putStringSet("Notes",set).apply();
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
                return true;
            }
        });

    }
}
