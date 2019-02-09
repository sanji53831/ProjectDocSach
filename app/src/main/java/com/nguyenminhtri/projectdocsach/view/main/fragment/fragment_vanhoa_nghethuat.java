package com.nguyenminhtri.projectdocsach.view.main.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nguyenminhtri.projectdocsach.R;
import com.nguyenminhtri.projectdocsach.connectinternet.ConnectInternet;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.InterfaceLoadMore;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.LoadMore;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.Sach;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.ViewBook;
import com.nguyenminhtri.projectdocsach.savevariables.SaveVariables;
import com.nguyenminhtri.projectdocsach.viewmodel.main.ViewModelMain;
import com.nguyenminhtri.projectdocsach.model.main.ModelMain;
import com.nguyenminhtri.projectdocsach.view.main.adapter.CustomAdapterRecyelerView_Main;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class fragment_vanhoa_nghethuat extends Fragment implements InterfaceLoadMore {
    ProgressBar progressBarHead, progressBar;
    RecyclerView recyclerView;
    CustomAdapterRecyelerView_Main adapter;
    ModelMain modelMain;
    ViewModelMain viewModelMain;
    int countSum = 0;

    Handler handler = new Handler();
    Handler handler1 = new Handler();

    TextView tvKetNoiInterNet;
    FrameLayout layoutMain;
    ConnectInternet connectInternet;
    LoadMore loadMore;
    ThreadPoolExecutor threadPoolExecutor;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_vanhoa_nghethuat, container, false);
        layoutMain = view.findViewById(R.id.layoutMainVanHoaNgheThuat);
        connectInternet = new ConnectInternet(getActivity());
        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(10);
        threadPoolExecutor = new ThreadPoolExecutor(5, 5, 5, TimeUnit.SECONDS, queue);

        if (!connectInternet.checkInternetConnection()) {
            final View viewNoInternet = inflater.inflate(R.layout.layout_nointernet, container, false);
            tvKetNoiInterNet = viewNoInternet.findViewById(R.id.tvThuLai);

            tvKetNoiInterNet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvKetNoiInterNet.setVisibility(View.GONE);
                    if (!connectInternet.checkInternetConnection()) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                tvKetNoiInterNet.setVisibility(View.VISIBLE);
                            }
                        }, 1000);
                    } else {
                        layoutMain.removeView(viewNoInternet);
                        View viewHaveInternet = inflater.inflate(R.layout.layout_fragment_vanhoa_nghethuat, container, false);
                        layoutMain.addView(viewHaveInternet);
                        initView(viewHaveInternet);
                    }
                }
            });
            layoutMain.addView(viewNoInternet);
        } else {
            View viewHaveInternet = inflater.inflate(R.layout.layout_fragment_vanhoa_nghethuat, container, false);
            layoutMain.addView(viewHaveInternet);
            initView(viewHaveInternet);
        }
        return view;
    }

    private void initView(View view) {
        modelMain = new ModelMain();
        handler1 = new Handler();
        viewModelMain = ViewModelProviders.of(getActivity()).get(ViewModelMain.class);
        progressBarHead = view.findViewById(R.id.progress_barHead);
        progressBar = view.findViewById(R.id.progress_bar);
        recyclerView = view.findViewById(R.id.recyclerViewVanHoaNgheThuat);

        if (connectInternet.checkInternetConnection()) {
            try {
                threadPoolExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        final ViewBook viewBook;
                        viewBook = modelMain.getListSachByCatelogy1(SaveVariables.maVanHoaNgheThuat, 0);
                        viewModelMain.getViewListVanHoaNgheThuat().postValue(viewBook);
                        handler1.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (viewBook.getListSach().size() > 0) {
                                    progressBarHead.setVisibility(View.GONE);
                                    countSum = viewModelMain.getViewListVanHoaNgheThuat().getValue().getTotalNumberSach();
                                    adapter = new CustomAdapterRecyelerView_Main(getActivity(), R.layout.custom_row_recyclerview_main,
                                            viewModelMain.getViewListVanHoaNgheThuat().getValue().getListSach());
                                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 3, LinearLayoutManager.VERTICAL, false);
                                    recyclerView.setLayoutManager(layoutManager);
                                    recyclerView.setAdapter(adapter);
                                    loadMore = new LoadMore(layoutManager, fragment_vanhoa_nghethuat.this);
                                    recyclerView.addOnScrollListener(loadMore);
                                }
                            }
                        }, 0);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
                Log.d("AAA", "Thread fragment_vanhoa_nghethuat loi!");
            }

        }
    }

    //LoadMore
    int sum = 0;
    int tong = 0;

    @Override
    public void xuLyLoadMore(int totalItem) {
        if (connectInternet.checkInternetConnection()) {
            sum = totalItem;
            tong = viewModelMain.getViewListVanHoaNgheThuat().getValue().getListSach().size();
            if (tong < countSum) {
                loadMore.setLoad(true);
                if (connectInternet.checkInternetConnection()) {
                    progressBar.setVisibility(View.VISIBLE);
                    try {
                        threadPoolExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                viewModelMain.getViewListVanHoaNgheThuat().getValue()
                                        .getListSach()
                                        .addAll(modelMain.getListSachByCatelogy1(SaveVariables.maVanHoaNgheThuat, sum).getListSach());
                                handler.postDelayed(runnable, 1000);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                        Log.d("AAA", "Thread LoadMore fragment_vanhoa_nghethuat loi!");
                    }


                }
            }
        }
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            adapter.notifyDataSetChanged();
            if (viewModelMain.getViewListVanHoaNgheThuat().getValue().getListSach().size() != tong) {
                progressBar.setVisibility(View.GONE);
                if (viewModelMain.getViewListVanHoaNgheThuat().getValue().getListSach().size() < countSum) {
                    loadMore.setLoad(false);
                }
            }
        }

    };
}
