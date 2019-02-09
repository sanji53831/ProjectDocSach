package com.nguyenminhtri.projectdocsach.view.main.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.nguyenminhtri.projectdocsach.R;
import com.nguyenminhtri.projectdocsach.connectinternet.ConnectInternet;

public class fragment_kinhdoanh extends Fragment {

    TextView tvKetNoiInterNet;
    FrameLayout layoutMain;
    ConnectInternet connectInternet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vanhoa_nghethuat, container, false);
        layoutMain = view.findViewById(R.id.layoutMainVanHoaNgheThuat);


        connectInternet = new ConnectInternet(getActivity());
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
                    }
                }
            });
            layoutMain.addView(viewNoInternet);
        }
        return view;

    }
}
