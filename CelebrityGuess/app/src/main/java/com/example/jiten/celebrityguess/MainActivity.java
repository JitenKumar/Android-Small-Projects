package com.example.jiten.celebrityguess;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RadialGradient;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> celebUrls = new ArrayList<>();
    ArrayList<String> celebNames = new ArrayList<>();
    int chosenCeleb = 0;
    ImageView imageView;
    int locationOfCorrectAnswer;
    String[] answers = new String[4];
    Button button0;
    Button button1;
    Button button2;
    Button button3;

    // celebrity chosen
    public void celebChosen(View view) {
        if (view.getTag().toString().equals(Integer.toString(locationOfCorrectAnswer))) {
            Toast.makeText(getApplicationContext(),"Correct",Toast.LENGTH_LONG).show();
        }
      else
    {   Toast.makeText(getApplicationContext(),"Wrong It was",Toast.LENGTH_LONG).show();
    }
        createNewQuestion();
}

    public void createNewQuestion(){
        Random random = new Random();
        chosenCeleb = random.nextInt(celebUrls.size());
        DonwloadImages imageTask = new DonwloadImages();
        Bitmap celebImage;
        try {
            celebImage = imageTask.execute(celebUrls.get(chosenCeleb)).get();
            imageView.setImageBitmap(celebImage);
            locationOfCorrectAnswer = random.nextInt(4);
            int incorrect=0;
            for (int i =0;i< answers.length;i++){
                if(locationOfCorrectAnswer == i){
                    answers[i] = celebNames.get(chosenCeleb);
                }else{
                    incorrect = random.nextInt(celebUrls.size());
                    while(incorrect == chosenCeleb){
                        incorrect = random.nextInt(celebUrls.size());
                    }
                    answers[i] = celebNames.get(incorrect);
                }
            }

            button0.setText(answers[0]);
            button1.setText(answers[1]);
            button2.setText(answers[2]);
            button3.setText(answers[3]);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
    // for downloading the Images
    public class DonwloadImages extends AsyncTask<String,Void,Bitmap>{

        @Override
        protected Bitmap doInBackground(String... urls) {
            try{
                URL url = new URL(urls[0]);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream in = urlConnection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class DonwloadTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url ;
            HttpsURLConnection urlConnection;
            try{

                url = new URL(urls[0]);
                urlConnection = (HttpsURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while(data != -1){
                    char current  = (char) data;
                    result += current;

                }
                return result;
            }catch (Exception e){
                e.printStackTrace();
            }
            // remove after all
            return "Done" ;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        button0 = findViewById(R.id.button0);
        button1= findViewById(R.id.button1);
        button2= findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        DonwloadTask donwloadTask = new DonwloadTask();
        String result = "";
        try{
            result = donwloadTask.execute("http://www.posh24.com/celebrities").get();
            String[] splitResults = result.split("<div class=\"sidebarContainer\">");
            // for url checking
            Pattern p = Pattern.compile("<img src=\"(.*?)\"");
            Matcher m = p.matcher(splitResults[0]);
            while(m.find()){
                celebUrls.add(m.group(1));
            }
            // for name of the celebrities
            Pattern p1 = Pattern.compile("alt=\"(.*?)\"");
            Matcher m1 = p1.matcher(splitResults[0]);
            while(m1.find()){
                celebNames.add(m1.group(1));
            }
            createNewQuestion();

        }catch (InterruptedException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
