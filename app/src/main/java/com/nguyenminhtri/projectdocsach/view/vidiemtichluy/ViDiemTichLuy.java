package com.nguyenminhtri.projectdocsach.view.vidiemtichluy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.nguyenminhtri.projectdocsach.R;
import com.nguyenminhtri.projectdocsach.database.Database;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.KhachHang;

public class ViDiemTichLuy extends AppCompatActivity {
    Toolbar toolbar;
    TextView tvTongDiem, tvSoSach, tvThoiGianDoc, tvDiemTichLuyDocSach;

    Database database;
    KhachHang kh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vi_diem_tich_luy);

        database = Database.getDatabase(getApplication());
        kh = KhachHang.getInstance(this);

        addViews();
        initActionBar();
        initContentViews();
    }

    private void initContentViews() {
        tvTongDiem.setText("Tổng tích lũy hiện tại: " + kh.getTongdiemtichluy() + " điểm");
        tvSoSach.setText(database.getListChiTietSachDaDoc(kh.getId()).size() + " cuốn");
        tvThoiGianDoc.setText(database.getThoiGianDocSach(kh.getId()) + " phút");
        tvDiemTichLuyDocSach.setText(kh.getDiemtichluytheogio() + "");

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
        toolbar = findViewById(R.id.toolBarViDiem);
        tvTongDiem = findViewById(R.id.tvTongDiemTichLuy);
        tvSoSach = findViewById(R.id.tvSoSachDaDoc);
        tvThoiGianDoc = findViewById(R.id.tvThoiGianDocSach);
        tvDiemTichLuyDocSach = findViewById(R.id.tvDiemTichLuyDocSach);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.from_right_in, R.anim.from_right_out);
    }
}
