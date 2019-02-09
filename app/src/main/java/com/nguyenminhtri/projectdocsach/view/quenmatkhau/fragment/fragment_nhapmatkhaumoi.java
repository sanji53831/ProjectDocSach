package com.nguyenminhtri.projectdocsach.view.quenmatkhau.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nguyenminhtri.projectdocsach.R;
import com.nguyenminhtri.projectdocsach.connectinternet.ConnectInternet;
import com.nguyenminhtri.projectdocsach.customview.PassWordEditText;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.KhachHang;
import com.nguyenminhtri.projectdocsach.presenter.quenmatkhau.PresenterQuenMatKhau;
import com.nguyenminhtri.projectdocsach.view.quenmatkhau.InterfaceViewQuenMatKhau;

public class fragment_nhapmatkhaumoi extends Fragment {
    TextInputLayout inputLayout;
    PassWordEditText edNhapMatKhauMoi;
    Button btnXacNhan;
    ProgressBar progressBar;
    PresenterQuenMatKhau presenterQuenMatKhau;
    InterfaceViewQuenMatKhau interfaceViewQuenMatKhau;
    ConnectInternet connectInternet;
    String email;
    Handler myHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nhapmatkhaumoi, container, false);
        presenterQuenMatKhau = new PresenterQuenMatKhau();
        interfaceViewQuenMatKhau = (InterfaceViewQuenMatKhau) getActivity();
        connectInternet = new ConnectInternet(getActivity());
        myHandler = new Handler();
        Bundle bundle = getArguments();
        if (bundle != null) {
            email = bundle.getString("Email");
        }
        addViews(view);
        addEvents();
        return view;
    }

    private void addEvents() {
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!connectInternet.checkInternetConnection()) {
                    Toast.makeText(getActivity(), "Bạn không có kết nối internet.", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                btnXacNhan.setEnabled(false);
                final String mkMoi = edNhapMatKhauMoi.getText().toString().trim();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Boolean b = presenterQuenMatKhau.thayDoiMatKhau(email, mkMoi);
                        myHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (b) {
                                    interfaceViewQuenMatKhau.thayDoiThanhCong();
                                } else {
                                    interfaceViewQuenMatKhau.thayDoiThatBai();
                                }
                                getActivity().finish();
                                getActivity().overridePendingTransition(R.anim.from_left_in, R.anim.from_left_out);
                            }
                        }, 0);
                    }
                }).start();
            }
        });
    }

    private void addViews(View view) {
        inputLayout = view.findViewById(R.id.input_edMatKhauMoi);
        edNhapMatKhauMoi = view.findViewById(R.id.edMatKhauMoi);
        btnXacNhan = view.findViewById(R.id.btnXacNhan);
        progressBar = view.findViewById(R.id.progress_bar);
    }

}
