package com.nguyenminhtri.projectdocsach.view.tusach.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nguyenminhtri.projectdocsach.R;
import com.nguyenminhtri.projectdocsach.database.Database;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.KhachHang;

public class fragment_sachdamua extends Fragment {

    Database database;
    KhachHang kh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sachdamua, null, false);


        return view;
    }
}
