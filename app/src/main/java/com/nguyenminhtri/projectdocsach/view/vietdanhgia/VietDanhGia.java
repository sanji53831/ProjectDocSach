package com.nguyenminhtri.projectdocsach.view.vietdanhgia;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.nguyenminhtri.projectdocsach.R;
import com.nguyenminhtri.projectdocsach.connectinternet.ConnectInternet;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.DanhGia;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.KhachHang;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.ViewDanhGia;
import com.nguyenminhtri.projectdocsach.presenter.vietdanhgia.PresenterVietDanhGia;

public class VietDanhGia extends AppCompatActivity implements InterfaceViewVietDanhGia, View.OnFocusChangeListener {

    TextInputLayout input_edTieuDe, input_edNoiDung;
    EditText edTieuDe, edNoiDung;
    RatingBar ratingBar;
    Button btnDongY;

    KhachHang kh;
    DanhGia danhGia;
    PresenterVietDanhGia presenterVietDanhGia;
    Handler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viet_danh_gia);
        addViews();
        kh = KhachHang.getInstance(this);
        presenterVietDanhGia = new PresenterVietDanhGia();
        addEvents();

    }

    private void addEvents() {
        btnDongY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectInternet connectInternet = new ConnectInternet(getApplicationContext());
                if (!connectInternet.checkInternetConnection()) {
                    Toast.makeText(VietDanhGia.this, "Bạn không có kết nối internet", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = getIntent();
                String maSach = intent.getStringExtra("MaSach");
                if (maSach.equals("")) {
                    return;
                }

                String tieuDe = edTieuDe.getText().toString().trim();
                String noiDung = edNoiDung.getText().toString().trim();

                if (tieuDe.length() <= 0) {
                    input_edTieuDe.setErrorEnabled(true);
                    input_edTieuDe.setError("Bạn chưa nhập tiêu đề");
                    return;
                }

                if (noiDung.length() <= 0) {
                    input_edNoiDung.setErrorEnabled(true);
                    input_edNoiDung.setError("Bạn chưa nhập nội dung");
                    return;
                }

                btnDongY.setEnabled(false);

                String soSao = String.valueOf(ratingBar.getRating());
                String maDanhGia = kh.getId();
                String tenUser = kh.getHoten();
                if (tenUser.equals("")) {
                    tenUser = kh.getUsername();
                }
                danhGia = new DanhGia(maDanhGia, maSach, tenUser, tieuDe, noiDung, soSao);

                myHandler = new Handler();
                try {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final Boolean b = presenterVietDanhGia.xuLyVietDanhGia(danhGia);
                            myHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (b) {
                                        danhGiaThanhCong();
                                    } else {
                                        danhGiaThatBai();
                                    }
                                }
                            }, 0);
                        }
                    }).start();
                } catch (Exception e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                    Log.d("AAA", "Thread VietDanhGia loi");
                }

            }
        });

        edTieuDe.setOnFocusChangeListener(this);
        edNoiDung.setOnFocusChangeListener(this);
    }

    private void addViews() {
        input_edTieuDe = findViewById(R.id.input_edTieuDeVietDanhGia);
        input_edNoiDung = findViewById(R.id.input_edNoiDungVietDanhGia);
        edTieuDe = findViewById(R.id.edTieuDeVietDanhGia);
        edNoiDung = findViewById(R.id.edNoiDungVietDanhGia);
        ratingBar = findViewById(R.id.rbVietDanhGia);
        btnDongY = findViewById(R.id.btnDongYVietDanhGia);
    }

    @Override
    public void danhGiaThanhCong() {
        btnDongY.setEnabled(true);
        Toast.makeText(this, "Bạn đã đánh giá thành công quyển sách này", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void danhGiaThatBai() {
        btnDongY.setEnabled(true);
        Toast.makeText(this, "Bạn đã đánh giá quyển sách này rồi", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.edTieuDeVietDanhGia:
                if (!hasFocus) {
                    String tieuDe = edTieuDe.getText().toString().trim();
                    if (tieuDe.length() > 0) {
                        input_edTieuDe.setErrorEnabled(false);
                        input_edTieuDe.setError("");
                    }
                }
                break;
            case R.id.edNoiDungVietDanhGia:
                if (!hasFocus) {
                    String noiDung = edNoiDung.getText().toString().trim();
                    if (noiDung.length() > 0) {
                        input_edNoiDung.setErrorEnabled(false);
                        input_edNoiDung.setError("");
                    }
                }
                break;
        }
    }
}
