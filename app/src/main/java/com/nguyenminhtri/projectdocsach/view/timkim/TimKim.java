package com.nguyenminhtri.projectdocsach.view.timkim;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nguyenminhtri.projectdocsach.R;
import com.nguyenminhtri.projectdocsach.connectinternet.ConnectInternet;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.Sach;
import com.nguyenminhtri.projectdocsach.presenter.timkim.PresenterTimKim;
import com.nguyenminhtri.projectdocsach.view.main.adapter.CustomAdapterRecyelerView_Main;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TimKim extends AppCompatActivity {
    SearchView searchView;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    TextView tvThongBao;

    PresenterTimKim presenterTimKim;
    ConnectInternet connectInternet;
    CustomAdapterRecyelerView_Main adapter;

    ThreadPoolExecutor threadPoolExecutor;
    Handler myHanler;
    ArrayList<Sach> listSach;
    String tenSach = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tim_kim);
        addViews();
        presenterTimKim = new PresenterTimKim();
        connectInternet = new ConnectInternet(this);
        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(5);
        threadPoolExecutor = new ThreadPoolExecutor(2, 2, 5, TimeUnit.MINUTES, queue);
        myHanler = new Handler();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (!connectInternet.checkInternetConnection()) {
                    Toast.makeText(TimKim.this, "Bạn không có kết nối internet", Toast.LENGTH_SHORT).show();

                } else {
                    if (listSach != null && listSach.size() > 0 && adapter != null) {
                        listSach.clear();
                        adapter.notifyDataSetChanged();
                    }
                    tenSach = s;
                    progressBar.setVisibility(View.VISIBLE);
                    tvThongBao.setVisibility(View.GONE);
                    try {
                        threadPoolExecutor.execute(threadLoadSach);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                        Log.d("AAA", "Thread loadSach In TimKim loi");
                    }

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    private void addViews() {
        searchView = findViewById(R.id.seachrSach);
        recyclerView = findViewById(R.id.recyclerViewTimKim);
        progressBar = findViewById(R.id.progress_bar);
        tvThongBao = findViewById(R.id.tvThongBao);
    }

    Runnable threadLoadSach = new Runnable() {
        @Override
        public void run() {
            listSach = new ArrayList<>();
            listSach.addAll(presenterTimKim.getListSachTimKim(tenSach));
            myHanler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (listSach.size() > 0) {
                        adapter = new CustomAdapterRecyelerView_Main(TimKim.this, R.layout.custom_row_recyclerview_timkim, listSach);
                        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(TimKim.this, 2, LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(layoutManager);
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setAdapter(adapter);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        tvThongBao.setVisibility(View.VISIBLE);
                    }

                }
            }, 1000);

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.from_left_in, R.anim.from_left_out);
    }
}
