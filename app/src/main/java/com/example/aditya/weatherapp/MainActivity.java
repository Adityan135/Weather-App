package com.example.aditya.weatherapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText City;
    TextView resultview;
    public void getWeather(View view){
      try{
        downloader download=new downloader();
        download.execute("http://api.openweathermap.org/data/2.5/weather?q="+City.getText().toString()+",india&APPID=96f99b6f6f79684d2194c2b42a634976");
        InputMethodManager mgr=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(City.getWindowToken(),0);}
        catch (Exception e){
          e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Can't find the weather",Toast.LENGTH_SHORT).show();
        }
    }
    public  class downloader extends AsyncTask<String,Void,String>{


        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection connection;
            String result="";
            try {
                url = new URL(strings[0]);
                connection=(HttpURLConnection) url.openConnection();
                InputStream in=connection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();

                while (data!=-1){
                    char x=(char) data;
                    result+=x;
                    data=reader.read();
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();

                return null;
            }



        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject=new JSONObject(s);
                String weather=jsonObject.getString("weather");
                JSONArray jsonArray=new JSONArray(weather);
                String result="";
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonPart=jsonArray.getJSONObject(i);
                    String main=jsonPart.getString("main");
                    String description=jsonPart.getString("description");
                    if(!main.equals("")&& !description.equals("")){
                        result+=main+":"+description+"\r\n";
                    }
                }
                if (result != "") {
                    resultview.setText(result);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Can't find the weather",Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Can't find the weather",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        City=findViewById(R.id.cityname);
        resultview=findViewById(R.id.weatherdisplay);
    }
}
