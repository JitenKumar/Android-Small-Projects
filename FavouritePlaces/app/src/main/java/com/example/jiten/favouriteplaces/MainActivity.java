package com.example.jiten.favouriteplaces;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> placesList;
    static ArrayList<LatLng> locations;
    static ArrayAdapter placesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = findViewById(R.id.listView);
        placesList = new ArrayList<>();
        locations = new ArrayList<>();
        ArrayList<String> latitudes = new ArrayList<>();
        ArrayList<String> longitudes = new ArrayList<>();

        placesList.clear();
        locations.clear();
        latitudes.clear();
        longitudes.clear();
        
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.jiten.favouriteplaces", Context.MODE_PRIVATE);
        try {
            placesList = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("places",ObjectSerializer.serialize(new ArrayList<String>())));
            latitudes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("latitudes",ObjectSerializer.serialize(new ArrayList<String>())));
            longitudes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("longitudes",ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (placesList.size()>0 && latitudes.size()>0 && longitudes.size()>0){
            if(placesList.size() ==  latitudes.size() && latitudes.size() == longitudes.size()){
                for (int i=0;i<latitudes.size();i++){
                    locations.add(new LatLng(Double.parseDouble(latitudes.get(i)),Double.parseDouble(longitudes.get(i))));
                }
            }
        }
        else {
            placesList.add("Add a new Place Please");
            locations.add(new LatLng(0, 0));
        }

        placesAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,placesList);
        listView.setAdapter(placesAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                intent.putExtra("PlaceNo",position);
                startActivity(intent);

            }
        });
    }
}
