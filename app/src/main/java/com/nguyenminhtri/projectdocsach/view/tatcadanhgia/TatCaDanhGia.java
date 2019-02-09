package com.nguyenminhtri.projectdocsach.view.tatcadanhgia;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.nguyenminhtri.projectdocsach.R;
import com.nguyenminhtri.projectdocsach.connectinternet.ConnectInternet;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.InterfaceLoadMore;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.KhachHang;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.LoadMore;
import com.nguyenminhtri.projectdocsach.presenter.tatcadanhgia.PresenterTatCaDanhGia;

import com.nguyenminhtri.projectdocsach.view.dangnhap.DangNhap;
import com.nguyenminhtri.projectdocsach.view.thongtinsach.ThongTinSach;
import com.nguyenminhtri.projectdocsach.view.thongtinsach.adapter.CustomAdapterRecyelerView_DanhGia_ThongTinSach;
import com.nguyenminhtri.projectdocsach.view.vietdanhgia.VietDanhGia;
import com.nguyenminhtri.projectdocsach.viewmodel.tatcadanhgia.ViewModelTatCaDanhGia;
import com.nguyenminhtri.projectdocsach.viewmodel.thongtinsach.ViewModelThongTinSach;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class TatCaDanhGia extends AppCompatActivity implements InterfaceLoadMore {
    KhachHang kh;
    FrameLayout layoutMain;
    Toolbar toolbar;
    RecyclerView recyclerView;
    FloatingActionButton fabComment;
    ProgressBar progressBarHead, progressBar;

    TextView tvThuLai;

    String maSach;
    int totalDanhGia;

    ConnectInternet connectInternet;
    Handler myHandler;

    CustomAdapterRecyelerView_DanhGia_ThongTinSach adapter;

    ViewModelTatCaDanhGia viewModelTatCaDanhGia;

    PresenterTatCaDanhGia presenterTatCaDanhGia;

    final int REQUEST_CODE_VIETDANHGIA = 20;
    final int REQUEST_CODE_DANGNHAP = 30;
    LoadMore loadMore;
    ThreadPoolExecutor threadPoolExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectInternet = new ConnectInternet(this);
        setContentView(R.layout.activity_tat_ca_danh_gia);
        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(10);
        threadPoolExecutor = new ThreadPoolExecutor(2, 2, 5, TimeUnit.SECONDS, queue);
        myHandler = new Handler();

        kh = KhachHang.getInstance(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        maSach = bundle.getString("MaSach");
        totalDanhGia = bundle.getInt("ToTalDanhGia");
        layoutMain = findViewById(R.id.layoutMainTatCaDanhGia);

        if (!connectInternet.checkInternetConnection()) {
            final View view = LayoutInflater.from(this).inflate(R.layout.layout_nointernet, null, false);
            tvThuLai = view.findViewById(R.id.tvThuLai);

            tvThuLai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvThuLai.setVisibility(View.GONE);
                    if (!connectInternet.checkInternetConnection()) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                tvThuLai.setVisibility(View.VISIBLE);
                            }
                        }, 1000);
                    } else {
                        layoutMain.removeView(view);
                        View view2 = LayoutInflater.from(TatCaDanhGia.this).inflate(R.layout.layout_activity_tatcadanhgia, null, false);
                        layoutMain.addView(view2);
                        addViews(view2);
                        init();
                    }
                }
            });
            layoutMain.addView(view);
        } else {
            View view2 = LayoutInflater.from(this).inflate(R.layout.layout_activity_tatcadanhgia, null, false);
            layoutMain.addView(view2);
            addViews(view2);
            init();
        }

    }

    private void init() {
        initActionBar();

        presenterTatCaDanhGia = new PresenterTatCaDanhGia();
        viewModelTatCaDanhGia = ViewModelProviders.of(this).get(ViewModelTatCaDanhGia.class);

        addEvents();

        try {
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    viewModelTatCaDanhGia.getListDanhGiaByMaSach(maSach);
                    myHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (viewModelTatCaDanhGia.getListDanhGiaByMaSach(maSach).getValue().size() > 0) {
                                progressBarHead.setVisibility(View.GONE);
                                adapter = new CustomAdapterRecyelerView_DanhGia_ThongTinSach(TatCaDanhGia.this,
                                        viewModelTatCaDanhGia.getListDanhGiaByMaSach(maSach).getValue(), 0);

                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(TatCaDanhGia.this,
                                        LinearLayoutManager.VERTICAL, false);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.setAdapter(adapter);
                                loadMore = new LoadMore(layoutManager, TatCaDanhGia.this);
                                recyclerView.addOnScrollListener(loadMore);
                            }
                        }
                    }, 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
            Log.d("AAA", "Thread TatCaDanhGia loi");
        }

    }

    private void addEvents() {
        fabComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kh != null) {
                    Intent intent = new Intent(TatCaDanhGia.this, VietDanhGia.class);
                    intent.putExtra("MaSach", maSach);
                    startActivityForResult(intent, REQUEST_CODE_VIETDANHGIA);
                } else {
                    startActivityForResult(new Intent(TatCaDanhGia.this, DangNhap.class), REQUEST_CODE_DANGNHAP);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_VIETDANHGIA && resultCode == RESULT_OK) {
            try {
                viewModelTatCaDanhGia.getListDanhGiaByMaSach(maSach).setValue(presenterTatCaDanhGia.getListDanhGiaByMaSach(maSach, 0));
                adapter = new CustomAdapterRecyelerView_DanhGia_ThongTinSach(this,
                        viewModelTatCaDanhGia.getListDanhGiaByMaSach(maSach).getValue(), 0);
                recyclerView.setAdapter(adapter);
                setResult(RESULT_OK);
            } catch (Exception e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
                Log.d("AAA", "Thread onActivityResult TatCaDanhGia loi");
            }

        }
        if (requestCode == REQUEST_CODE_DANGNHAP && resultCode == RESULT_OK) {
            kh = KhachHang.getInstance(this);
            setResult(RESULT_OK);
        }
    }

    private void initActionBar() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.from_left_in, R.anim.from_left_out);
            }
        });
    }

    private void addViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewTatCaDanhGia);
        toolbar = view.findViewById(R.id.toolBarTatCaDanhGia);
        fabComment = view.findViewById(R.id.fabComment);
        progressBarHead = view.findViewById(R.id.progress_barHead);
        progressBar = view.findViewById(R.id.progress_bar);
    }

    //LoadMore
    int sum = 0;
    int tong = 0;


    @Override
    public void xuLyLoadMore(int totalItem) {
        if (connectInternet.checkInternetConnection()) {
            loadMore.setLoad(true);
            sum = totalItem;
            tong = viewModelTatCaDanhGia.getListDanhGiaByMaSach(maSach).getValue().size();
            if ((tong < totalDanhGia)) {
                progressBar.setVisibility(View.VISIBLE);
                try {
                    threadPoolExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            viewModelTatCaDanhGia.getListDanhGiaByMaSach(maSach).getValue()
                                    .addAll(presenterTatCaDanhGia.getListDanhGiaByMaSach(maSach, sum));
                            myHandler.postDelayed(runnable, 2000);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                    Log.d("AAA", "Thread LoadMore TatCaDanhGia loi");
                }

            }
        }
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            adapter.notifyDataSetChanged();
            if (viewModelTatCaDanhGia.getListDanhGiaByMaSach(maSach).getValue().size() != tong) {
                progressBar.setVisibility(View.GONE);
                if (viewModelTatCaDanhGia.getListDanhGiaByMaSach(maSach).getValue().size() < totalDanhGia) {
                    loadMore.setLoad(false);
                }
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.from_left_in, R.anim.from_left_out);
    }
}
