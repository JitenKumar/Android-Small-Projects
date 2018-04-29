package com.example.jiten.jsonprocessing;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JSONDataDownload jsonDataDownload = new JSONDataDownload();
        jsonDataDownload.execute("api.openweathermap.org/data/2.5/weather?q=Chandigarh,India");
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
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                String weatherInfo = jsonObject.getString("weather");
                JSONArray jsonArray = new JSONArray(weatherInfo);
                for (int i = 0;i<jsonArray.length();i++){
                    JSONObject jsonPart =  jsonArray.getJSONObject(i);
                    // LOGGIN THE DATA
                    Log.i("Descrption",jsonPart.getString("description"));
                    Log.i("main",jsonPart.getString("main"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("Result",result);
        }
    }
}
