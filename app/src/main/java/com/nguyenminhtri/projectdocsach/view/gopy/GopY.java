package com.nguyenminhtri.projectdocsach.view.gopy;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nguyenminhtri.projectdocsach.R;
import com.nguyenminhtri.projectdocsach.connectinternet.ConnectInternet;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.KhachHang;
import com.nguyenminhtri.projectdocsach.presenter.gopy.PresenterGopY;

public class GopY extends AppCompatActivity implements InterfaceViewGopY {
    Toolbar toolbar;
    EditText edNoiDung;
    Button btnHuyBo, btnGui;

    KhachHang khachHang;
    ConnectInternet connectInternet;
    PresenterGopY presenterGopY;
    Handler myHanler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gop_y);
        khachHang = KhachHang.getInstance(this);
        connectInternet = new ConnectInternet(this);
        presenterGopY = new PresenterGopY();
        myHanler = new Handler();
        addViews();
        initActionBar();
        addEvents();
    }

    private void addEvents() {
        btnHuyBo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnGui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!connectInternet.checkInternetConnection()) {
                    Toast.makeText(GopY.this, "Bạn không có kết nối internet", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    btnGui.setEnabled(false);
                    btnHuyBo.setEnabled(false);
                    final String userId = khachHang.getId();
                    final String hoTen;
                    if (!khachHang.getHoten().equals("")) {
                        hoTen = khachHang.getHoten();
                    } else {
                        hoTen = khachHang.getUsername();
                    }
                    final String email = khachHang.getEmail();
                    final String noiDung = edNoiDung.getText().toString().trim();
                    try {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                final Boolean b = presenterGopY.saveGopY(userId, hoTen, email, noiDung);
                                myHanler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (b) {
                                            gopYThanhCong();
                                        } else {
                                            gopYThatBai();
                                        }
                                    }
                                }, 500);
                            }
                        }).start();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                        Log.d("AAA", "Thread Gop Y Loi");
                    }
                }
            }
        });
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
        toolbar = findViewById(R.id.toolBarGopY);
        edNoiDung = findViewById(R.id.edNoiDung);
        btnHuyBo = findViewById(R.id.btnHuyBo);
        btnGui = findViewById(R.id.btnGui);
    }

    @Override
    public void gopYThanhCong() {
        btnGui.setEnabled(true);
        btnHuyBo.setEnabled(true);
        Toast.makeText(this, "Cảm ơn bạn đã gửi góp ý. Chúng tôi sẽ xem xét và cải thiện tốt hơn !", Toast.LENGTH_LONG).show();
        finish();
        overridePendingTransition(R.anim.from_right_in, R.anim.from_right_out);
    }

    @Override
    public void gopYThatBai() {
        btnGui.setEnabled(true);
        btnHuyBo.setEnabled(true);
        Toast.makeText(this, "Lỗi không thể gửi !", Toast.LENGTH_LONG).show();
        finish();
        overridePendingTransition(R.anim.from_right_in, R.anim.from_right_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.from_right_in, R.anim.from_right_out);
    }
}
