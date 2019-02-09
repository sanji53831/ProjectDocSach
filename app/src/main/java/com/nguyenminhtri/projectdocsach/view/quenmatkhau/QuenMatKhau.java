package com.nguyenminhtri.projectdocsach.view.quenmatkhau;

import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nguyenminhtri.projectdocsach.R;
import com.nguyenminhtri.projectdocsach.connectinternet.ConnectInternet;
import com.nguyenminhtri.projectdocsach.model.quenmatkhau.ModelQuenMatKhau;
import com.nguyenminhtri.projectdocsach.presenter.quenmatkhau.PresenterQuenMatKhau;
import com.nguyenminhtri.projectdocsach.view.quenmatkhau.fragment.fragment_nhapmareset;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

public class QuenMatKhau extends AppCompatActivity implements InterfaceViewQuenMatKhau {
    Toolbar toolbar;
    TextInputLayout inputLayout;
    EditText edEmail;
    Button btnXacNhan;
    ModelQuenMatKhau modelQuenMatKhau;
    PresenterQuenMatKhau presenterQuenMatKhau;
    String maReset;
    String email;
    ConnectInternet connectInternet;
    Handler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quen_mat_khau);
        addViews();
        initActionBar();
        myHandler = new Handler();
        modelQuenMatKhau = new ModelQuenMatKhau();
        presenterQuenMatKhau = new PresenterQuenMatKhau();
        connectInternet = new ConnectInternet(this);
        addEvents();
    }

    private void addEvents() {
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!connectInternet.checkInternetConnection()) {
                    Toast.makeText(QuenMatKhau.this, "Bạn không có kết nối internet.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Random random = new Random();
                maReset = String.valueOf(random.nextInt(999999 - 100000) + 100000);
                email = edEmail.getText().toString().trim();
                if (email.isEmpty()) {
                    inputLayout.setErrorEnabled(true);
                    inputLayout.setError("Hãy điền địa chỉ email !");
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    inputLayout.setErrorEnabled(true);
                    inputLayout.setError("Đây không phải địa chỉ email !");
                    return;
                }
                btnXacNhan.setEnabled(false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Boolean b = presenterQuenMatKhau.kiemTraEmail(email);
                        myHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (!b) {
                                    emailChuaDangKy();
                                } else {
                                    btnXacNhan.setEnabled(true);
                                    modelQuenMatKhau.sendEmail(email, "Lấy lại mật khẩu",
                                            maReset + " là mã reset mật khẩu của bạn.");
                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.setCustomAnimations(R.anim.from_left_in, R.anim.from_left_out);
                                    fragment_nhapmareset fragmentNhapmareset = new fragment_nhapmareset();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("MaReset", maReset);
                                    bundle.putString("Email", email);
                                    fragmentNhapmareset.setArguments(bundle);
                                    fragmentTransaction.add(R.id.framReset, fragmentNhapmareset, "Fragment_NhapMaReset");
                                    fragmentTransaction.commit();
                                    edEmail.setText("");
                                }
                            }
                        }, 0);
                    }
                }).start();
            }
        });
    }

    private void initActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                if (fragmentManager.findFragmentByTag("Fragment_NhapMaReset") != null) {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.from_right_in, R.anim.from_right_out);
                    fragmentTransaction.remove(fragmentManager.findFragmentByTag("Fragment_NhapMaReset"));
                    fragmentTransaction.commit();
                    return;
                }
                if (fragmentManager.findFragmentByTag("Fragment_NhapMatKhauMoi") != null) {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.from_right_in, R.anim.from_right_out);
                    fragmentTransaction.remove(fragmentManager.findFragmentByTag("Fragment_NhapMatKhauMoi"));
                    fragmentTransaction.commit();
                    return;
                }
                finish();
                overridePendingTransition(R.anim.from_left_in, R.anim.from_left_out);
            }
        });
    }

    private void addViews() {
        toolbar = findViewById(R.id.toolBarQuenMatKhau);
        inputLayout = findViewById(R.id.input_edEmail);
        edEmail = findViewById(R.id.edEmail);
        btnXacNhan = findViewById(R.id.btnXacNhan);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentByTag("Fragment_NhapMaReset") != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.from_left_in, R.anim.from_left_out);
            fragmentTransaction.remove(fragmentManager.findFragmentByTag("Fragment_NhapMaReset"));
            fragmentTransaction.commit();
            return;
        }
        if (fragmentManager.findFragmentByTag("Fragment_NhapMatKhauMoi") != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.from_left_in, R.anim.from_left_out);
            fragmentTransaction.remove(fragmentManager.findFragmentByTag("Fragment_NhapMatKhauMoi"));
            fragmentTransaction.commit();
            return;
        }
        super.onBackPressed();
        overridePendingTransition(R.anim.from_left_in, R.anim.from_left_out);
    }


    @Override
    public void thayDoiThanhCong() {
        Toast.makeText(this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void thayDoiThatBai() {
        Toast.makeText(this, "Lỗi! Không thể đổi mật khẩu", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void emailChuaDangKy() {
        btnXacNhan.setEnabled(true);
        inputLayout.setErrorEnabled(true);
        inputLayout.setError("Email này chưa được đăng ký!");
    }
}
