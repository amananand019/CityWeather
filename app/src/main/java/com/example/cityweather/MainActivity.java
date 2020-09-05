package com.example.cityweather;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.inputmethodservice.InputMethodService;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    TextView textView1,textView2;
    EditText editText;

    public class DownloadTask extends AsyncTask <String, Void, String>{
        @Override
        protected String doInBackground(String... urls) {
            URL url;
            HttpsURLConnection connection = null;
            String result = "";
            try {
                url = new URL(urls[0]);
                connection = (HttpsURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                int data = reader.read();
                while (data!=-1) {
                    char currentdata = (char) data;
                    result += currentdata;
                    data = reader.read();
                }
                return result;
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("Weather Info ", s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String WeatherInfo = jsonObject.getString("weather");
                textView1.setText("City: "+ jsonObject.getString("name"));
                JSONArray jsonArray1 = new JSONArray(WeatherInfo);
                for(int i=0; i< jsonArray1.length(); i++){
                    JSONObject  jsonObject1 = jsonArray1.getJSONObject(i);
                    textView2.setText("Info: "+jsonObject1.getString("description"));
                }
                String main = jsonObject.getString("main");
                Log.i("weatherinfo", jsonObject.getString("description"));
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        textView1 = (TextView)findViewById(R.id.textView1);
        textView2 = (TextView)findViewById(R.id.textView2);
        editText = (EditText)findViewById(R.id.editText);
        Button button = (Button)findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadTask task = new DownloadTask();
                task.execute("https://openweathermap.org/data/2.5/weather?q="+editText.getText().toString()+"&appid=439d4b804bc8187953eb36d2a8c26a02");
            }
        });

    }
}