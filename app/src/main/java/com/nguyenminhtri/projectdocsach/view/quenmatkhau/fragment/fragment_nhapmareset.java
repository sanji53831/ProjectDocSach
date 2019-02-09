package com.nguyenminhtri.projectdocsach.view.quenmatkhau.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nguyenminhtri.projectdocsach.R;
import com.nguyenminhtri.projectdocsach.connectinternet.ConnectInternet;
import com.nguyenminhtri.projectdocsach.model.quenmatkhau.ModelQuenMatKhau;
import com.nguyenminhtri.projectdocsach.view.quenmatkhau.QuenMatKhau;

import java.util.Random;

public class fragment_nhapmareset extends Fragment {
    TextInputLayout inputLayout;
    EditText edNhapMaReset;
    Button btnXacNhan, btnGuiLaiMaReset;
    String email;
    String maReset;
    String maResetTam;
    ModelQuenMatKhau modelQuenMatKhau;
    ConnectInternet connectInternet;
    Handler myHandler;
    int time = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nhapmareset, container, false);
        modelQuenMatKhau = new ModelQuenMatKhau();
        myHandler = new Handler();
        connectInternet = new ConnectInternet(getActivity());
        Bundle bundle = getArguments();
        if (bundle != null) {
            email = bundle.getString("Email");
            maReset = bundle.getString("MaReset");
        }
        addViews(view);
        addEvents();
        countTime();
        return view;
    }

    private void countTime() {
        myHandler.postDelayed(runnable, 0);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            time++;
            if (time >= 600) {
                maResetTam = maReset;
                maReset = null;
                return;
            }
            myHandler.postDelayed(this, 1000);
        }
    };

    private void addEvents() {
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!connectInternet.checkInternetConnection()) {
                    Toast.makeText(getActivity(), "Bạn không có kết nối internet.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String ma = edNhapMaReset.getText().toString().trim();
                if (ma.isEmpty()) {
                    inputLayout.setErrorEnabled(true);
                    inputLayout.setError("Hãy điền mã reset mật khẩu để tiếp tục !");
                    return;
                }
                if (maReset == null && ma.equals(maResetTam)) {
                    inputLayout.setErrorEnabled(true);
                    inputLayout.setError("Mã reset này đã quá thời gian sử dụng !");
                    return;
                }
                if (!ma.equals(maReset)) {
                    inputLayout.setErrorEnabled(true);
                    inputLayout.setError("Mã reset không đúng!");
                } else {
                    myHandler.removeCallbacks(runnable);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.from_left_in, R.anim.from_left_out);
                    fragment_nhapmatkhaumoi fragmentNhapmatkhaumoi = new fragment_nhapmatkhaumoi();
                    Bundle bundle = new Bundle();
                    bundle.putString("Email", email);
                    fragmentNhapmatkhaumoi.setArguments(bundle);
                    fragmentTransaction.replace(R.id.framReset, fragmentNhapmatkhaumoi, "Fragment_NhapMatKhauMoi");
                    fragmentTransaction.commit();
                }
            }
        });

        btnGuiLaiMaReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time = 0;
                if (!connectInternet.checkInternetConnection()) {
                    Toast.makeText(getActivity(), "Bạn không có kết nối internet.", Toast.LENGTH_SHORT).show();
                    return;
                }
                btnXacNhan.setEnabled(false);
                btnGuiLaiMaReset.setEnabled(false);
                Random random = new Random();
                maReset = String.valueOf(random.nextInt(999999 - 100000) + 100000);
                modelQuenMatKhau.sendEmail(email, "Lấy lại mật khẩu",
                        maReset + " là mã reset mật khẩu của bạn.");
                myHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnXacNhan.setEnabled(true);
                        btnGuiLaiMaReset.setEnabled(true);
                    }
                }, 2000);
            }
        });
    }

    private void addViews(View view) {
        inputLayout = view.findViewById(R.id.input_edNhapMaReset);
        edNhapMaReset = view.findViewById(R.id.edMaReset);
        btnXacNhan = view.findViewById(R.id.btnXacNhan);
        btnGuiLaiMaReset = view.findViewById(R.id.btnGuiLaiMaReset);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        myHandler.removeCallbacks(runnable);
    }
}
