package com.dcodestar.whatstheweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements DownloadTask.OnTaskDownloaded {
    final String APIKey=getResources().getString(R.string.your_api_key);
    String query="https://api.openweathermap.org/data/2.5/weather?q=%s&appid="+APIKey;
    private static final String TAG = "MainActivity";
    EditText cityEditText;
    Button cityButton;
    TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityButton=findViewById(R.id.cityButton);
        cityEditText=findViewById(R.id.cityEditText);
        resultTextView=findViewById(R.id.resultTextView);
        resultTextView.setText("");
    }

    @Override
    public void onTaskDownloaded(String s) {
        if(s==null){
            resultTextView.setText("Invalid city");
        }else {
            String result=null;
            try {
                Log.d(TAG, "onTaskDownloaded: "+s);
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray=jsonObject.getJSONArray("weather");
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject mainObj=jsonArray.getJSONObject(i);
                    result=mainObj.getString("main");
                    result=result+":"+mainObj.getString("description")+"\n";
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "onTaskDownloaded: "+e.getMessage());
                result="Some Error Occurred";
            }
            resultTextView.setText(result);
        }
    }

    public void cityButtonPressed(View view){
        InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(cityEditText.getApplicationWindowToken(),0);

        String city=cityEditText.getText().toString();
        city.replaceAll("\\s","");
        DownloadTask downloadTask=new DownloadTask(this);
        downloadTask.execute(String.format(query,city));
    }
}
