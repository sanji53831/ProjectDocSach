package com.nguyenminhtri.projectdocsach.view.loadsach;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nguyenminhtri.projectdocsach.R;
import com.nguyenminhtri.projectdocsach.connectinternet.ConnectInternet;
import com.nguyenminhtri.projectdocsach.connectinternet.DowloadPDFToExternalStorge;
import com.nguyenminhtri.projectdocsach.database.Database;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.KhachHang;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.Sach;
import com.nguyenminhtri.projectdocsach.model.readbook.ModelReadBook;
import com.nguyenminhtri.projectdocsach.presenter.loadsach.PresenterLoadSach;
import com.nguyenminhtri.projectdocsach.view.viewbook.ReadBook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class LoadSach extends AppCompatActivity {
    ConnectInternet connectInternet;
    ModelReadBook modelReadBook;
    KhachHang khachHang;
    String url;
    String tenSach;
    Sach sach;
    byte[] b;
    Bitmap bitmap;
    TextView tvMoSach;
    ProgressBar progressBar;
    Database database;
    PresenterLoadSach presenterLoadSach;

    ThreadPoolExecutor threadPoolExecutor;
    Handler myHanler;

    Intent it;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectInternet = new ConnectInternet(this);
        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(20);
        threadPoolExecutor = new ThreadPoolExecutor(5, 5, 5, TimeUnit.SECONDS, queue);
        myHanler = new Handler();
        presenterLoadSach = new PresenterLoadSach();

        setContentView(R.layout.activity_load_sach);
        database = Database.getDatabase(getApplication());
        khachHang = KhachHang.getInstance(this);
        tvMoSach = findViewById(R.id.tvMoSach);
        progressBar = findViewById(R.id.progress_bar);

        modelReadBook = new ModelReadBook();
        Intent intent = getIntent();
        sach = intent.getParcelableExtra("Sach");
        b = intent.getByteArrayExtra("HinhAnh");
        bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);

        url = sach.getNoiDung();
        tenSach = url.substring(url.lastIndexOf("/") + 1, url.length());

        it = new Intent(this, ReadBook.class);
        it.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
        it.putExtra("MaSach", sach.getId());
        it.putExtra("TenSach", tenSach);


        myHanler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!modelReadBook.checkSachStorge(getApplicationContext(), tenSach)) {
                    if (connectInternet.checkInternetConnection()) {
                        try {
                            new DowloadPDFToExternalStorge().execute(url);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                            Log.d("AAA", "Thread DowLoadPDF In LoadSach Loi");
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        tvMoSach.setText("Lỗi! Không thể mở sách bạn hãy kết nối internet để tiến hành mở sách!");
                    }
                } else {
                    threadPoolExecutor.execute(threadAddSachToSqlite);
                    startActivity(it);
                    finish();
                }
            }
        }, 2000);

    }

    class DowloadPDFToExternalStorge extends AsyncTask<String, Void, Void> {
        final int MEGABYTE = 1024 * 1024;

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            threadPoolExecutor.execute(threadAddSachToSqlite);
            if (connectInternet.checkInternetConnection()) {
                try {
                    threadPoolExecutor.execute(threadAddSachDaDocToServer);
                } catch (Exception e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                    Log.d("AAA", "Thread threadAddSachDaDocToServer in LoadSach Loi");
                }

            }
            startActivity(it);
            finish();
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
                    foder = new File("/data/data/" + getApplicationContext().getPackageName() + "/appdocsach");
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

    Runnable threadAddSachToSqlite = new Runnable() {
        @Override
        public void run() {
            if (!database.checkSach(sach.getId())) {
                database.addBook(sach, bitmap);
            }
            if (!database.checkSachDaDoc(khachHang.getId(), sach.getId())) {
                database.addBookDaDoc(khachHang.getId(), sach.getId());
            }

        }
    };

    Runnable threadAddSachDaDocToServer = new Runnable() {
        @Override
        public void run() {
            presenterLoadSach.saveSachDocDocToServer(khachHang.getId(), sach.getId());
        }
    };

}
