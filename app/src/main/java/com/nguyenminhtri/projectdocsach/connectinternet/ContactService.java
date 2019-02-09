package com.nguyenminhtri.projectdocsach.connectinternet;


import android.net.Uri;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ContactService extends AsyncTask<String, Void, String> {

    String đuongan;
    HashMap<String, String> hashMap;
    Boolean flag;

    public ContactService(String url) {
        this.đuongan = url;
        flag = true;
    }

    public ContactService(String url, HashMap<String, String> hashMap) {
        this.đuongan = url;
        this.hashMap = hashMap;
        flag = false;
    }

    @Override
    protected String doInBackground(String... strings) {
        String data = "";
        try {
            URL url = new URL(đuongan);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            if(flag){
               data = methodGet(httpURLConnection);
            }else {
               data = methodPost(httpURLConnection);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    private String methodGet(HttpURLConnection httpURLConnection) {
        String data = "";
        StringBuilder stringBuilder = new StringBuilder();
        try {
            httpURLConnection.connect();
            InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            bufferedReader.close();
            inputStreamReader.close();
            data = stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    private String methodPost(HttpURLConnection httpURLConnection) {
        String data = "";
        StringBuilder stringBuilder = new StringBuilder();
        try {
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder();
            for(Map.Entry<String,String> values : hashMap.entrySet()){
                String key = values.getKey();
                String value = values.getValue();
                builder.appendQueryParameter(key,value);
            }

            String query = builder.build().getEncodedQuery();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpURLConnection.getOutputStream());
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
            bufferedWriter.write(query);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStreamWriter.close();
            httpURLConnection.connect();

            InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            bufferedReader.close();
            inputStreamReader.close();
            data = stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}

