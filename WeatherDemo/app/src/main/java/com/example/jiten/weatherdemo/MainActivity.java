package com.example.jiten.weatherdemo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;

import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    EditText cityTextView ;
    TextView resultTextView;
    public void findWeather(View view){
        JSONDataDownload jsonDataDownload = new JSONDataDownload();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromInputMethod(cityTextView.getWindowToken(),0);
        jsonDataDownload.execute("api.openweathermap.org/data/2.5/weather?q="+cityTextView.getText().toString());


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityTextView = findViewById(R.id.cityEditText);
        resultTextView = findViewById(R.id.resultTextView);
    }

    public class JSONDataDownload extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {
            String result= "";
            URL url;
            HttpsURLConnection connection = null;
            try{
                url = new URL(urls[0]);
                connection = (HttpsURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while(data !=  -1){
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            } catch (Exception e){
                e.printStackTrace();
                //Toast.makeText(getApplicationContext(),"could not find weather",Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                String message = "";
                JSONObject jsonObject = new JSONObject(result);
                String weatherInfo = jsonObject.getString("weather");
                JSONArray jsonArray = new JSONArray(weatherInfo);
                for (int i = 0;i<jsonArray.length();i++){
                    JSONObject jsonPart =  jsonArray.getJSONObject(i);
                    String main ="";
                    String desc= "";
                    main = jsonPart.getString("main");
                    desc = jsonPart.getString("description");
                    while(main != "" && desc != ""){
                        message = main + " : " + desc;
                    }
                }
                if (message !=""){
                    resultTextView.setText(message);
                }else{
                   // Toast.makeText(getApplicationContext(),"could not find weather",Toast.LENGTH_LONG).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
               // Toast.makeText(getApplicationContext(),"could not find weather",Toast.LENGTH_LONG).show();
            }
            Log.i("Result",result);
        }
    }
}

