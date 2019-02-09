package com.nguyenminhtri.projectdocsach.connectinternet;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DowloadPDFToExternalStorge extends AsyncTask<String, Void, Void> {
    final int MEGABYTE = 1024 * 1024;
    Context context;

    public DowloadPDFToExternalStorge(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... strings) {

        try {
            URL url = new URL(strings[0]);
            String fileName = strings[0].substring(strings[0].lastIndexOf("/") + 1, strings[0].length());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();
            File foder;
            File file;
            if (isExternalStorageReadable()) {
                foder = new File(Environment.getExternalStorageDirectory(), "/appdocsach");
                if (!foder.exists())
                    foder.mkdir();
                file = new File(foder, fileName);
                if (!file.exists())
                    file.createNewFile();
            } else {
                foder = new File("/data/data/" + context.getPackageName() + "/appdocsach");
                if (!foder.exists())
                    foder.mkdir();
                file = new File(foder, fileName);
                if (!file.exists())
                    file.createNewFile();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] buffer = new byte[MEGABYTE];
            int bufferLength = 0;
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, bufferLength);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            inputStream.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
