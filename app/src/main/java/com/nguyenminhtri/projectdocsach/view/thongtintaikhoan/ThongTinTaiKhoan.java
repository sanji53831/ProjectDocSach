package com.nguyenminhtri.projectdocsach.view.thongtintaikhoan;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.nguyenminhtri.projectdocsach.R;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.KhachHang;
import com.nguyenminhtri.projectdocsach.presenter.thongtintaikhoan.PresenterThongTinTaiKhoan;
import com.nguyenminhtri.projectdocsach.view.main.adapter.CustomAdapterViewPager;
import com.nguyenminhtri.projectdocsach.view.napdiemtichluy.NapDiemTichLuy;
import com.nguyenminhtri.projectdocsach.view.thongtintaikhoan.fragment.fragment_lichsu_giaodich;
import com.nguyenminhtri.projectdocsach.view.thongtintaikhoan.fragment.fragment_thongtin_taikhoan;
import com.nguyenminhtri.projectdocsach.viewmodel.thongtintaikhoan.ViewModelFactoryThongTinTaiKhoan;
import com.nguyenminhtri.projectdocsach.viewmodel.thongtintaikhoan.ViewModelThongTinTaiKhoan;

public class ThongTinTaiKhoan extends AppCompatActivity {
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;

    TextView tvDiemTichLuy, tvUserName;
    Button btnDangXuat, btnNapDiem;

    KhachHang kh;
    ViewModelThongTinTaiKhoan viewModelThongTinTaiKhoan;
    PresenterThongTinTaiKhoan presenterThongTinTaiKhoan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_tai_khoan);
        kh = KhachHang.getInstance(this);
        viewModelThongTinTaiKhoan = ViewModelProviders.of(this, new ViewModelFactoryThongTinTaiKhoan(getApplication())).
                get(ViewModelThongTinTaiKhoan.class);
        presenterThongTinTaiKhoan = new PresenterThongTinTaiKhoan();

        viewModelThongTinTaiKhoan.getKhachhang().observe(this, new Observer<KhachHang>() {
            @Override
            public void onChanged(@Nullable KhachHang khachHang) {
                kh = KhachHang.getInstance(ThongTinTaiKhoan.this);
                initContentView();
            }
        });

        addViews();
        initActionBar();
        initTabLayOut();
        initContentView();
        addEvent();
    }

    private void addEvent() {
        btnDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogLogOut();
            }
        });

        btnNapDiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ThongTinTaiKhoan.this, NapDiemTichLuy.class));
                overridePendingTransition(R.anim.from_left_in, R.anim.from_left_out);
            }
        });
    }

    private void showDialogLogOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn muốn đăng xuất ?");
        builder.setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenterThongTinTaiKhoan.deleteShareferenceUser(getApplicationContext());
                setResult(RESULT_OK);
                finish();
                overridePendingTransition(R.anim.from_right_in, R.anim.from_right_out);
            }
        });
        builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void initContentView() {
        if (kh.getHoten().equals("")) {
            tvUserName.setText(kh.getUsername());
        } else {
            tvUserName.setText(kh.getHoten());
        }
        tvDiemTichLuy.setText(String.valueOf(kh.getTongdiemtichluy()));

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

    private void initTabLayOut() {
        CustomAdapterViewPager adapterViewPager = new CustomAdapterViewPager(getSupportFragmentManager());
        adapterViewPager.addFragment(new fragment_thongtin_taikhoan(), "TÀI KHOẢN");
        adapterViewPager.addFragment(new fragment_lichsu_giaodich(), "LỊCH SỬ NẠP ĐIỂM");
        viewPager.setAdapter(adapterViewPager);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void addViews() {
        toolbar = findViewById(R.id.toolBarThongTinTaiKhoan);
        tabLayout = findViewById(R.id.tabLayoutInTttk);
        viewPager = findViewById(R.id.viewPagerInTttk);
        tvUserName = findViewById(R.id.tvUserName);
        tvDiemTichLuy = findViewById(R.id.tvDiemTichLuy);
        btnNapDiem = findViewById(R.id.btnNapDiem);
        btnDangXuat = findViewById(R.id.btnDangXuat);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.from_right_in, R.anim.from_right_out);
    }
}
