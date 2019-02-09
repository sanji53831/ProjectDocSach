package com.nguyenminhtri.projectdocsach.view.napdiemtichluy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.nguyenminhtri.projectdocsach.R;

public class NapDiemTichLuy extends AppCompatActivity {
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nap_diem_tich_luy);
        addViews();
        initActionBar();
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
        toolbar = findViewById(R.id.toolBarNapDiem);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.from_right_in, R.anim.from_right_out);
    }
}
