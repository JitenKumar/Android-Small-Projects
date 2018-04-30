package com.example.jiten.alertdemo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView textView ;
    SharedPreferences sharedPreferences;
    public void setLanguage(String language){
        //sharedPreferences = this.getSharedPreferences("com.example.jiten.alertdemo", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("Language",language).apply();
        textView.setText(language);
    }
    /*public void dialogShow(){
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are you sure")
                .setMessage("Choose the Home Language")
                .setPositiveButton("Spanish", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(MainActivity.this,"Thank You It's Done",Toast.LENGTH_LONG).show();
                        setLanguage("Spanish");
                    }
                })
                .setNegativeButton("English", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setLanguage("English");
                    }
                })
                .show();
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() ==  R.id.english){
            setLanguage("English");
        }else if(item.getItemId() == R.id.spanish){
            setLanguage("Spanish");
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        sharedPreferences = this.getSharedPreferences("com.example.jiten.alertdemo",Context.MODE_PRIVATE);
        textView = findViewById(R.id.textView);
        String langugae = sharedPreferences.getString("Language","");
        if(langugae == ""){
            new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Are you sure")
                    .setMessage("Choose the Home Language")
                    .setPositiveButton("Spanish", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(MainActivity.this,"Thank You It's Done",Toast.LENGTH_LONG).show();
                            setLanguage("Spanish");
                        }
                    })
                    .setNegativeButton("English", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setLanguage("English");
                        }
                    })
                    .show();
        }
        else{
            textView.setText(langugae);
        }

    }
}
