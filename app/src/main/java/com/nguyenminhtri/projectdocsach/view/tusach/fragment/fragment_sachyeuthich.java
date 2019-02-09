package com.nguyenminhtri.projectdocsach.view.tusach.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nguyenminhtri.projectdocsach.R;
import com.nguyenminhtri.projectdocsach.database.Database;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.KhachHang;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.Sach;
import com.nguyenminhtri.projectdocsach.view.tusach.adapter.CustomAdapter_Recycler_TuSach;
import com.nguyenminhtri.projectdocsach.viewmodel.tusach.ViewModelTuSach;

import java.util.ArrayList;

public class fragment_sachyeuthich extends Fragment {

    Database database;
    KhachHang kh;
    CustomAdapter_Recycler_TuSach adapter;
    ViewModelTuSach viewModelTuSach;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sachyeuthich, null, false);
        addViews(view);
        database = Database.getDatabase(getActivity().getApplication());
        kh = KhachHang.getInstance(getContext());
        viewModelTuSach = ViewModelProviders.of(getActivity()).get(ViewModelTuSach.class);
        if (adapter == null) {
            initRecyclerView();
        }
        viewModelTuSach.getListSachYeuThich().observe(getActivity(), new Observer<ArrayList<Sach>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Sach> saches) {
                try {
                    adapter = new CustomAdapter_Recycler_TuSach(kh, database, getActivity(), saches);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        return view;
    }

    private void addViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewSachYeuThich);
    }


    private void initRecyclerView() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                ArrayList<Sach> list = database.getListSachYeuThich(kh.getId());
                viewModelTuSach.getListSachYeuThich().postValue(list);
                return null;
            }
        }.execute();
    }
}
