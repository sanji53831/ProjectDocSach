package com.nguyenminhtri.projectdocsach.view.viewbook;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;


import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import com.nguyenminhtri.projectdocsach.connectinternet.ConnectInternet;
import com.nguyenminhtri.projectdocsach.R;
import com.nguyenminhtri.projectdocsach.connectinternet.DowloadPDFToExternalStorge;
import com.nguyenminhtri.projectdocsach.database.Database;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.KhachHang;
import com.nguyenminhtri.projectdocsach.model.readbook.ModelReadBook;
import com.nguyenminhtri.projectdocsach.savevariables.SaveVariables;
import com.nguyenminhtri.projectdocsach.view.tusach.TuSach;


import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class ReadBook extends AppCompatActivity {
    KhachHang kh;
    Database database;
    PDFView pdfView;

    int second = 0;
    int minute = 0;
    Handler handler;
    Handler handlerCheckPage;
    ModelReadBook modelReadBook;

    ThreadPoolExecutor threadPoolExecutor;
    int numberPage = 0;
    String tenSach;
    String maSach;
    int numberPageSqlite = 0;
    int checkPage = numberPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_book_offline);
        if (savedInstanceState != null) {
            numberPage = savedInstanceState.getInt("page");
            checkPage = numberPage;
            second = savedInstanceState.getInt("second");
            minute = savedInstanceState.getInt("minute");
        }

        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(20);
        threadPoolExecutor = new ThreadPoolExecutor(5, 5, 5, TimeUnit.SECONDS, queue);


        kh = KhachHang.getInstance(this);
        database = Database.getDatabase(getApplication());
        modelReadBook = new ModelReadBook();
        pdfView = findViewById(R.id.pdfViewerOffline);

        Intent intent = getIntent();
        tenSach = intent.getStringExtra("TenSach");
        maSach = intent.getStringExtra("MaSach");
        File file;
        file = new File(Environment.getExternalStorageDirectory() +
                "/appdocsach/" + tenSach);
        if (file.exists()) {
            displayFromFile(file);
        } else {
            file = new File("/data/data/" + getPackageName() + "/appdocsach/" + tenSach);
            if (file.exists()) {
                displayFromFile(file);
            }
        }
        handler = new Handler();
        handlerCheckPage = new Handler();
        handler.postDelayed(countTime, 0);
        handlerCheckPage.postDelayed(checkTrangSach, 0);
    }

    private void displayFromFile(File assetFileName) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            pdfView.setPivotX(width / 2);
            pdfView.setScaleX(2.5f);
            pdfView.setScaleY(2f);
        }
        numberPageSqlite = database.getPagerCurrentSachDaDoc(kh.getId(), maSach);
        numberPage = numberPageSqlite;
        pdfView.fromFile(assetFileName).defaultPage(numberPage).load();

    }

    @Override
    public void finish() {
        Intent it = new Intent();
        it.putExtra("ViTri", pdfView.getCurrentPage());
        it.putExtra("MaSach", maSach);
        setResult(RESULT_OK, it);
        super.finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("page", pdfView.getCurrentPage());
        outState.putInt("second", second);
        outState.putInt("minute", minute);
    }

    @Override
    protected void onStop() {
        handler.removeCallbacks(countTime);
        handlerCheckPage.removeCallbacks(checkTrangSach);
        int timeInSqlite = Integer.parseInt(database.getThoiGianDocSach(kh.getId()));
        final int time = (timeInSqlite + minute);
        Log.d("AAA", "ket thuc: " + time);
        updateTimeDocSach(time);
        if (pdfView.getCurrentPage() > numberPageSqlite) {
            savePagerCurrentSachDaDoc();
        }
        ConnectInternet connectInternet = new ConnectInternet(this);
        if (connectInternet.checkInternetConnection()) {
            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        modelReadBook.saveThoiGianDocSach(kh.getId(), String.valueOf(time));
                    }
                }).start();
            } catch (Exception e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
                Log.d("AAA", "Thread saveThoiGianDocSach ReadBook loi");
            }
        }
        super.onStop();

    }

    private void updateTimeDocSach(final int time) {
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                database.updateThoiGianDocSach(kh.getId(), String.valueOf(time));
            }
        });
    }

    private void savePagerCurrentSachDaDoc() {
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                database.savePagerCurrentSachDaDoc(kh.getId(), maSach, pdfView.getCurrentPage());
            }
        });
    }


    Runnable countTime = new Runnable() {
        @Override
        public void run() {
            second++;
            if (second == 60) {
                second = 0;
                minute++;
                Log.d("AAA", "tich luy duoc: " + minute);
            }
            handler.postDelayed(this, 1000);
        }
    };

    Runnable checkTrangSach = new Runnable() {
        @Override
        public void run() {
            if (checkPage == pdfView.getCurrentPage()) {
                if (minute > 5) {
                    int timeInSqlite = Integer.parseInt(database.getThoiGianDocSach(kh.getId())) - 4;
                    int time = (timeInSqlite + minute);
                    Log.d("AAA", "treo may: " + minute + " phut");
                    Log.d("AAA", "treo may: " + timeInSqlite + " phut trong sqlite");
                    Log.d("AAA", "duoc luu vao sqlite: " + time + " phut");
                    database.updateThoiGianDocSach(kh.getId(), String.valueOf(time));
                }
                second = 0;
                minute = 0;
                Log.d("AAA", "da reset thanh" + minute + "");
            } else {
                checkPage = pdfView.getCurrentPage();
            }
            handlerCheckPage.postDelayed(this, 300000);
        }
    };
}
