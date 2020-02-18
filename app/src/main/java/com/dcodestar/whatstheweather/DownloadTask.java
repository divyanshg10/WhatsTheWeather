package com.dcodestar.whatstheweather;

import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class DownloadTask extends AsyncTask<String,Void,String> {
    interface OnTaskDownloaded{
        void onTaskDownloaded(String s);
    }

    private static final String TAG = "DownloadTask";
    OnTaskDownloaded callback;

    public DownloadTask(OnTaskDownloaded callback) {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... strings) {
        try{
            Log.d(TAG, "doInBackground: downloading content from url:"+strings[0]);
            URL url=new URL(strings[0]);
            HttpsURLConnection httpsURLConnection=(HttpsURLConnection)url.openConnection();
            InputStream inputStream=httpsURLConnection.getInputStream();
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
            int c=inputStreamReader.read();
            String s="";
            while(c!=-1){
                s+=(char)c;
                c=inputStreamReader.read();
            }
            return s;
        }catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "doInBackground: "+e.getMessage() );
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d(TAG, "onPostExecute:"+s);
        if(callback!=null){
            callback.onTaskDownloaded(s);
        }
    }
}
