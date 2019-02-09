package com.nguyenminhtri.projectdocsach.view.dangky;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nguyenminhtri.projectdocsach.customview.ClearEditText;
import com.nguyenminhtri.projectdocsach.customview.PassWordEditText;
import com.nguyenminhtri.projectdocsach.R;
import com.nguyenminhtri.projectdocsach.presenter.dangky.PresenterDangKy;
import com.nguyenminhtri.projectdocsach.view.dangnhap.DangNhap;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DangKy extends AppCompatActivity implements InterfaceViewDangKy, View.OnFocusChangeListener {

    ProgressBar progressBar;
    PresenterDangKy presenterDangKy;
    TextInputLayout input_edTenDangNhap, input_edMatKhau, input_edNhapLaiMatKhau, input_edEmail;
    ClearEditText edTenDangNhap, edEmal;
    PassWordEditText edMatKhau, edNhapLaiMatKhau;
    Button btnDangKy;
    TextView tvDaCoTaiKhoan;
    Toolbar toolbar;

    Boolean flagUsername;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        presenterDangKy = new PresenterDangKy(this);
        addViews();
        initActionBar();
        addEvents();
    }

    private void addEvents() {
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String usename = edTenDangNhap.getText().toString().trim();
                final String password = edMatKhau.getText().toString().trim();
                String repassword = edNhapLaiMatKhau.getText().toString().trim();
                final String email = edEmal.getText().toString().trim();

                if (presenterDangKy.kiemTraDieuKienDangKy(usename, password, repassword, email)) {
                    progressBar.setVisibility(View.VISIBLE);
                    btnDangKy.setEnabled(false);
                    flagUsername = false;
                    try {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                final Boolean b = presenterDangKy.xuLyTrungUserName(usename);
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (b) {
                                            trungUserName();
                                        } else {
                                            flagUsername = true;
                                        }
                                    }
                                }, 1000);
                            }
                        }).start();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                final Boolean b = presenterDangKy.xuLyTrungEmail(email);
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (b) {
                                            trungEmail();
                                        } else {
                                            if (flagUsername) {
                                                new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        final Boolean b = presenterDangKy.xuLyDangKy(usename, password, email, "");
                                                        handler.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                if (b) {
                                                                    dangKyThanhCong();
                                                                } else {
                                                                    dangKyThatBai();
                                                                }
                                                            }
                                                        }, 1000);
                                                    }
                                                }).start();
                                            }
                                        }
                                    }
                                }, 1000);
                            }

                        }).start();
                    } catch (Exception e) {
                        e.toString();
                        Thread.currentThread().interrupt();
                        Log.d("AAA", "Thread Dang Ky Loi");
                    }
                }
            }
        });


        tvDaCoTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.from_left_in, R.anim.from_left_out);
            }
        });

        edTenDangNhap.setOnFocusChangeListener(this);
        edNhapLaiMatKhau.setOnFocusChangeListener(this);
        edEmal.setOnFocusChangeListener(this);
    }


    private void initActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.from_left_in, R.anim.from_left_out);
            }
        });
    }

    private void addViews() {
        progressBar = findViewById(R.id.progress_bar);
        toolbar = findViewById(R.id.toolBarDangKy);
        edTenDangNhap = findViewById(R.id.edTenDangNhapDangKy);
        edEmal = findViewById(R.id.edEmailDangKy);
        edMatKhau = findViewById(R.id.edMatKhauDangKy);
        edNhapLaiMatKhau = findViewById(R.id.edNhapLaiMatKhauDangKy);
        btnDangKy = findViewById(R.id.btnDangKyDangKy);
        tvDaCoTaiKhoan = findViewById(R.id.tvDaCoTaiKhoanDangKy);
        input_edTenDangNhap = findViewById(R.id.input_edTenDangNhapDangKy);
        input_edMatKhau = findViewById(R.id.input_edMatKhauDangKy);
        input_edNhapLaiMatKhau = findViewById(R.id.input_edNhapLaiMatKhauDangKy);
        input_edEmail = findViewById(R.id.input_edEmailDangKy);
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.edTenDangNhapDangKy:
                if (!hasFocus) {
                    String tdn = edTenDangNhap.getText().toString().trim();
                    if (!tdn.equals("")) {
                        input_edTenDangNhap.setErrorEnabled(false);
                        input_edTenDangNhap.setError("");
                    }
                }
                break;
            case R.id.edNhapLaiMatKhauDangKy:
                if (!hasFocus) {
                    String nhaplaimatkhau = edNhapLaiMatKhau.getText().toString().trim();
                    if (!nhaplaimatkhau.equals("")) {
                        if (!nhaplaimatkhau.equals(edMatKhau.getText().toString().trim())) {
                            input_edNhapLaiMatKhau.setErrorEnabled(true);
                            input_edNhapLaiMatKhau.setError("Mật khẩu không trùng khớp");
                        } else {
                            input_edNhapLaiMatKhau.setErrorEnabled(false);
                            input_edNhapLaiMatKhau.setError("");
                        }
                    }
                }
                break;
            case R.id.edEmailDangKy:
                if (!hasFocus) {
                    String email = edEmal.getText().toString().trim();
                    if (!email.equals("")) {
                        Boolean kiemTraEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches();
                        if (!kiemTraEmail) {
                            input_edEmail.setErrorEnabled(true);
                            input_edEmail.setError("Đây không phải là địa chỉ Email !");
                        } else {
                            input_edEmail.setErrorEnabled(false);
                            input_edEmail.setError("");
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void dangKyThanhCong() {
        btnDangKy.setEnabled(true);
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_LONG).show();
        finish();
        overridePendingTransition(R.anim.from_left_in, R.anim.from_left_out);
    }

    @Override
    public void dangKyThatBai() {
        btnDangKy.setEnabled(true);
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, "Lỗi ! Không đăng ký được", Toast.LENGTH_LONG).show();
    }

    @Override
    public void trungUserName() {
        progressBar.setVisibility(View.GONE);
        btnDangKy.setEnabled(true);
        input_edTenDangNhap.setErrorEnabled(true);
        input_edTenDangNhap.setError("Tài khoản này đã được đăng ký");
    }

    @Override
    public void trungEmail() {
        btnDangKy.setEnabled(true);
        progressBar.setVisibility(View.GONE);
        input_edEmail.setErrorEnabled(true);
        input_edEmail.setError("Email này đã được đăng ký");
    }


    @Override
    public void annouceUsernameEmpty() {
        input_edTenDangNhap.setErrorEnabled(true);
        input_edTenDangNhap.setError("Bạn chưa điền vào mục này");
    }

    @Override
    public void annouceUsernameShort() {
        input_edTenDangNhap.setErrorEnabled(true);
        input_edTenDangNhap.setError("Tên đăng nhập tối thiểu có 5 ký tự");
    }

    @Override
    public void annoucePasswordEmpty() {
        input_edMatKhau.setErrorEnabled(true);
        input_edMatKhau.setError("Bạn chưa điền vào mục này");
    }

    @Override
    public void annoucePasswordInvalid() {
        input_edMatKhau.setErrorEnabled(true);
        input_edMatKhau.setError("Mật khẩu không hợp lệ");
    }


    @Override
    public void annouceRePasswordEmpty() {
        input_edNhapLaiMatKhau.setErrorEnabled(true);
        input_edNhapLaiMatKhau.setError("Mật khẩu không trùng khớp");
    }

    @Override
    public void annouceEmailEmpty() {
        input_edEmail.setErrorEnabled(true);
        input_edEmail.setError("Bạn chưa điền vào mục này");
    }

    @Override
    public void annouceEmailInvalid() {
        input_edEmail.setErrorEnabled(true);
        input_edEmail.setError("Đây không phải là địa chỉ Email !");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.from_left_in, R.anim.from_left_out);
    }
}
