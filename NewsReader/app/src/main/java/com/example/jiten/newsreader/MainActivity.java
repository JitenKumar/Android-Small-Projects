package com.example.jiten.newsreader;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> newsList = new ArrayList<>();
    ArrayList<String> contentList = new ArrayList<>();
    ArrayAdapter<String> newsAdapter;
    SQLiteDatabase articleDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // top stories url

        articleDB = this.openOrCreateDatabase("Articles",MODE_PRIVATE,null);
        // IF NO TABLE EXISTS
        articleDB.execSQL("CREATE TABLE IF NOT EXISTS articles (id INTEGER PRIMARY KEY,articleId INTEGER, title VARCHAR, content VARCHAR)");
        updateListView();

        listView = findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),SecondActivity.class);
                intent.putExtra("content",contentList.get(position));
                startActivity(intent);
            }
        });
        newsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, newsList);
        listView.setAdapter(newsAdapter);
        DownloadNews downloadNews = new DownloadNews();
        try {
            downloadNews.execute("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class DownloadNews extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection connection = null;
            try {
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();

                }
                // JSON ARRAY OF IDS
                JSONArray newsIdArray = new JSONArray(result);
                int noOfItems = 20;
                if (newsIdArray.length() < 20) {
                    noOfItems = newsIdArray.length();
                }
                // clear table before adding the data again and again
                articleDB.execSQL("DELETE FROM articles");
                for (int i = 0; i < noOfItems; i++) {
                    String arcticleId = newsIdArray.getString(i);
                    url = new URL("https://hacker-news.firebaseio.com/v0/item/" + arcticleId + ".json?print=pretty");
                    connection = (HttpURLConnection) url.openConnection();
                    in = connection.getInputStream();
                    reader = new InputStreamReader(in);
                    data = reader.read();
                    String articleInfo = "";
                    while (data != -1) {
                        char current = (char) data;
                        articleInfo += current;
                        data = reader.read();
                    }
                    JSONObject newsObject = new JSONObject(articleInfo);
                    if(!newsObject.isNull("title")&& !newsObject.isNull("url")){
                        String articleTitle = newsObject.getString("title");
                        String articleUrl = newsObject.getString("url");
                        url = new URL(articleUrl);
                        connection = (HttpURLConnection) url.openConnection();
                        in = connection.getInputStream();
                        reader = new InputStreamReader(in);
                        data = reader.read();
                        String articleContent="";
                        while (data != -1) {
                            char current = (char) data;
                            articleContent += current;
                            data = reader.read();

                        }
                        Log.i("Article Content",articleContent);
                        String query ="INSERT INTO articles(articleId,title,content) values(?,?,?)";
                        SQLiteStatement statement = articleDB.compileStatement(query);
                        statement.bindString(1,arcticleId);
                        statement.bindString(2,articleTitle);
                        statement.bindString(3,articleContent);
                        statement.execute();

                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            updateListView();
        }
    }
    public void updateListView(){
        Cursor c = articleDB.rawQuery("SELECT * FROM articles",null);
        int contentIndex = c.getColumnIndex("content");
        //int articleIdIndex = c.getColumnIndex("articleId");
        int articleTitleIndex = c.getColumnIndex("title");
        if(c.moveToFirst()){
            newsList.clear();
            contentList.clear();
            do{
                newsList.add(c.getString(articleTitleIndex));
                contentList.add(c.getString(contentIndex));
            }while(c.moveToNext());
            newsAdapter.notifyDataSetChanged();
        }
    }
}