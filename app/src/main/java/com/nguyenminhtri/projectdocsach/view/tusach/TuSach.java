package com.nguyenminhtri.projectdocsach.view.tusach;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;


import com.nguyenminhtri.projectdocsach.R;
import com.nguyenminhtri.projectdocsach.connectinternet.ConnectInternet;
import com.nguyenminhtri.projectdocsach.database.Database;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.ChiTietSachDaDoc;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.ChiTietSachYeuThich;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.KhachHang;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.Sach;
import com.nguyenminhtri.projectdocsach.presenter.tusach.PresenterTuSach;
import com.nguyenminhtri.projectdocsach.savevariables.SaveVariables;
import com.nguyenminhtri.projectdocsach.view.tusach.adapter.CustomAdapter_ViewPager_TuSach;
import com.nguyenminhtri.projectdocsach.view.tusach.fragment.fragment_sachdadoc;
import com.nguyenminhtri.projectdocsach.view.tusach.fragment.fragment_sachdamua;
import com.nguyenminhtri.projectdocsach.view.tusach.fragment.fragment_sachyeuthich;
import com.nguyenminhtri.projectdocsach.viewmodel.tusach.ViewModelTuSach;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TuSach extends AppCompatActivity {
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    ViewPager viewPager;
    CustomAdapter_ViewPager_TuSach adapter;
    ProgressBar progressBar;

    KhachHang kh;
    Database database;
    PresenterTuSach presenterTuSach;
    ThreadPoolExecutor threadPoolExecutor;

    CountDownLatch countDownLatch;
    ViewModelTuSach viewModelTuSach;
    Handler handler;
    ConnectInternet connectInternet;

    final int REQUEST_CODE_THONGTINSACH = 200;
    final int REQUEST_CODE_LOADSACH = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tu_sach);
        kh = KhachHang.getInstance(this);
        database = Database.getDatabase(getApplication());
        connectInternet = new ConnectInternet(this);
        handler = new Handler();
        addViews();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (connectInternet.checkInternetConnection()) {
                    ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(20);
                    threadPoolExecutor = new ThreadPoolExecutor(5, 5, 5, TimeUnit.SECONDS, queue);
                    countDownLatch = new CountDownLatch(2);

                    viewModelTuSach = ViewModelProviders.of(TuSach.this).get(ViewModelTuSach.class);
                    presenterTuSach = new PresenterTuSach();


                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                threadPoolExecutor.execute(threadGetListSachDaDocFromServer);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Thread.currentThread().interrupt();
                                Log.d("AAA", "Thread threadGetListSachDaDocFromServer TuSach loi");
                            }

                            try {
                                threadPoolExecutor.execute(threadGetListSachYeuThichFromServer);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Thread.currentThread().interrupt();
                                Log.d("AAA", "Thread threadGetListSachYeuThichFromServer TuSach loi");
                            }

                            try {
                                countDownLatch.await();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    initViewpager();
                                }
                            }, 0);
                        }
                    }).start();


                } else {
                    initViewpager();
                }
            }
        }, 1000);

        initActionBar();
        addEvents();
    }

    private void addEvents() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.sachdadoc:
                        viewPager.setCurrentItem(0);
                        bottomNavigationView.getMenu().getItem(0).setChecked(true);
                        break;
                    case R.id.sachyeuthich:
                        viewPager.setCurrentItem(1);
                        bottomNavigationView.getMenu().getItem(1).setChecked(true);
                        break;
                    case R.id.sachdamua:
                        viewPager.setCurrentItem(2);
                        bottomNavigationView.getMenu().getItem(2).setChecked(true);
                        break;
                }
                return false;
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                bottomNavigationView.getMenu().getItem(i).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    private void initActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.from_right_in, R.anim.from_right_out);
            }
        });
    }

    private void addViews() {
        toolbar = findViewById(R.id.toolBarTuSach);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        viewPager = findViewById(R.id.viewPagerTuSach);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void initViewpager() {
        adapter = new CustomAdapter_ViewPager_TuSach(getSupportFragmentManager());
        adapter.addFragment(new fragment_sachdadoc(), "Sách Đã Đọc");
        adapter.addFragment(new fragment_sachyeuthich(), "Sách Yêu Thích");
        adapter.addFragment(new fragment_sachdamua(), "Sách Đã Mua");
        progressBar.setVisibility(View.GONE);
        viewPager.setAdapter(adapter);
        database.close();
    }


    Runnable threadGetListSachDaDocFromServer = new Runnable() {
        @Override
        public void run() {

            ArrayList<ChiTietSachDaDoc> listCTSDDFromSqlite = new ArrayList<>();
            listCTSDDFromSqlite.addAll(database.getListChiTietSachDaDoc(kh.getId()));

            int sizeSqlite = listCTSDDFromSqlite.size();
            int sizeMysql = kh.getTotalSachDaDoc();
            if (sizeSqlite < sizeMysql) {
                ArrayList<ChiTietSachDaDoc> listCTSDDFromServer = new ArrayList<>();

                listCTSDDFromServer.addAll(presenterTuSach.getListSachDaDocFromServer(kh.getId()));

                int totalSachFromSqlite = database.getTotalSach();

                database.deleteAllChiTietSachDaDoc(kh.getId());
                database.addListChiTietSachDaDoc(listCTSDDFromServer);

                if (totalSachFromSqlite < listCTSDDFromServer.size()) {
                    ArrayList<Sach> list = new ArrayList<>();
                    ArrayList<Bitmap> listHinh = new ArrayList<>();
                    for (ChiTietSachDaDoc book : listCTSDDFromServer) {
                        list.add(presenterTuSach.getSachByMaSach(book.getMaSach()));
                    }
                    int lenght = list.size();
                    for (int i = 0; i < lenght; i++) {
                        try {
                            Bitmap bitmap = Picasso.get().load(list.get(i).getHinhAnh()).get();
                            listHinh.add(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    for (int i = 0; i < lenght; i++) {
                        if (!database.checkSach(list.get(i).getId())) {
                            database.addBook(list.get(i), listHinh.get(i));
                        }
                    }
                }
            }
            countDownLatch.countDown();
        }
    };

    Runnable threadGetListSachYeuThichFromServer = new Runnable() {
        @Override
        public void run() {


            ArrayList<ChiTietSachYeuThich> listCTSYTFromSqlite = new ArrayList<>();
            listCTSYTFromSqlite.addAll(database.getListChiTietSachYeuThich(kh.getId()));

            int sizeSqlite = listCTSYTFromSqlite.size();
            int sizeMysql = kh.getTotalSachYeuThich();
            if (sizeSqlite < sizeMysql) {

                ArrayList<ChiTietSachYeuThich> listCTSYTFromServer = new ArrayList<>();
                listCTSYTFromServer.addAll(presenterTuSach.getListSachYeuThichFromServer(kh.getId()));

                int totalSachFromSqlite = database.getTotalSach();

                database.deleteAllChiTietSachYeuThich(kh.getId());
                database.addListChiTietSachYeuThich(listCTSYTFromServer);

                if (totalSachFromSqlite < listCTSYTFromServer.size()) {
                    ArrayList<Sach> list = new ArrayList<>();
                    ArrayList<Bitmap> listHinh = new ArrayList<>();

                    for (ChiTietSachYeuThich book : listCTSYTFromServer) {
                        list.add(presenterTuSach.getSachByMaSach(book.getMaSach()));
                    }
                    int lenght = list.size();
                    for (int i = 0; i < lenght; i++) {
                        try {
                            Bitmap bitmap = Picasso.get().load(list.get(i).getHinhAnh()).get();
                            listHinh.add(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    for (int i = 0; i < lenght; i++) {
                        if (!database.checkSach(list.get(i).getId())) {
                            database.addBook(list.get(i), listHinh.get(i));
                        }
                    }
                }
            }
            countDownLatch.countDown();

        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_LOADSACH && resultCode == RESULT_OK && data != null) {
            int vitri = data.getIntExtra("ViTri", 0);
            String maSach = data.getStringExtra("MaSach");
            int pageCurrentSqlite = database.getPagerCurrentSachDaDoc(kh.getId(), maSach);
            if (vitri > pageCurrentSqlite) {
                database.savePagerCurrentSachDaDoc(kh.getId(), maSach, vitri);
                viewModelTuSach = ViewModelProviders.of(this).get(ViewModelTuSach.class);
                ArrayList<Sach> list = database.getListSachDaDoc(kh.getId());
                viewModelTuSach.setListSachDaDoc(list);
                list = database.getListSachYeuThich(kh.getId());
                viewModelTuSach.setListSachYeuThich(list);
            }
        }

        if (requestCode == REQUEST_CODE_THONGTINSACH && resultCode == RESULT_OK) {
            if (data != null) {
                int vitri = data.getIntExtra("ViTri", 0);
                String maSach = data.getStringExtra("MaSach");
                int pageCurrentSqlite = database.getPagerCurrentSachDaDoc(kh.getId(), maSach);
                if (vitri > pageCurrentSqlite) {
                    database.savePagerCurrentSachDaDoc(kh.getId(), maSach, vitri);
                }
            }
            viewModelTuSach = ViewModelProviders.of(this).get(ViewModelTuSach.class);
            ArrayList<Sach> list = database.getListSachDaDoc(kh.getId());
            viewModelTuSach.setListSachDaDoc(list);
            list = database.getListSachYeuThich(kh.getId());
            viewModelTuSach.setListSachYeuThich(list);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.from_right_in, R.anim.from_right_out);
    }
}

