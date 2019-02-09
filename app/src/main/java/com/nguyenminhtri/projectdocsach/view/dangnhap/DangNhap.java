package com.nguyenminhtri.projectdocsach.view.dangnhap;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;

import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.nguyenminhtri.projectdocsach.connectinternet.ConnectInternet;
import com.nguyenminhtri.projectdocsach.customview.ClearEditText;
import com.nguyenminhtri.projectdocsach.customview.PassWordEditText;
import com.nguyenminhtri.projectdocsach.R;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.KhachHang;
import com.nguyenminhtri.projectdocsach.model.dangnhap.ModelDangNhap;
import com.nguyenminhtri.projectdocsach.presenter.dangnhap.PresenterDangNhap;
import com.nguyenminhtri.projectdocsach.view.dangky.DangKy;
import com.nguyenminhtri.projectdocsach.view.main.MainActivity;
import com.nguyenminhtri.projectdocsach.view.quenmatkhau.QuenMatKhau;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DangNhap extends AppCompatActivity implements InterfaceViewDangNhap {
    ConnectInternet connectInternet;
    ComponentName callingComponent;

    ProgressBar progressBar;

    Toolbar toolbar;
    Button btnDangNhap, btnDangKy, btnQuenMatKhau, btnDangNhapFacebook, btnDangNhapGoogle;
    ClearEditText edTenDangNhap;
    PassWordEditText edMatKhau;
    TextInputLayout input_edTenDangNhap, input_edMatKhau;
    CallbackManager callbackManager;
    GoogleSignInClient mGoogleSignInClient;
    ProgressDialog progressDialog;
    final int RC_SIGN_IN = 1;
    PresenterDangNhap presenterDangNhap;
    ModelDangNhap modelDangNhap;

    ThreadPoolExecutor threadPoolExecutor;
    String personName = "";
    String email = "";

    Handler handler;
    String username = "";
    String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectInternet = new ConnectInternet(this);
        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(10);
        threadPoolExecutor = new ThreadPoolExecutor(5, 5, 5, TimeUnit.SECONDS, queue);
        initFacebook();
        initGoogle();
        setContentView(R.layout.activity_dang_nhap);
        addViews();
        initActionBar();
        addEvents();
        callingComponent = getCallingActivity();
    }

    private void initGoogle() {
        modelDangNhap = new ModelDangNhap();
        mGoogleSignInClient = modelDangNhap.getGoogleSignInClient(this);

    }

    private void initFacebook() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            personName = object.getString("name");
                            email = object.getString("email");
                            if (presenterDangNhap.xuLyThemUser(getApplicationContext(), email, personName)) {
                                helloUser(personName);
                            }
                            returnActivityCall();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Thread.currentThread().interrupt();
                        }
                    }
                });
                Bundle parameter = new Bundle();
                parameter.putString("fields", "name,email");
                graphRequest.setParameters(parameter);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.d("kiemtra", "thoat");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("kiemtra", error.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            progressDialog.cancel();
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                personName = result.getSignInAccount().getDisplayName();
                email = result.getSignInAccount().getEmail();
                try {
                    if (presenterDangNhap.xuLyThemUser(getApplicationContext(), email, personName)) {
                        helloUser(personName);
                    }
                    returnActivityCall();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("AAA", "Thread Dang Nhap Xu Ly User Loi");
                }

            }
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            final Boolean b = presenterDangNhap.xuLyDangNhap(getApplicationContext(), username, password);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (b) {
                        DangNhapThanhCong();
                    } else {
                        DangNhapThatBai();
                    }
                }
            }, 0);
        }
    };

    private void addEvents() {
        presenterDangNhap = new PresenterDangNhap(this);
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!connectInternet.checkInternetConnection()) {
                    Toast.makeText(DangNhap.this, "Bạn chưa kết nối internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                username = edTenDangNhap.getText().toString().trim();
                password = edMatKhau.getText().toString().trim();

                if (username.equals("") || password.equals("")) {
                    Toast.makeText(DangNhap.this, "Hãy điền thông tin để đăng nhập", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                btnDangNhap.setEnabled(false);
                handler = new Handler();
                try {
                    threadPoolExecutor.execute(runnable);
                } catch (Exception e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                    Log.d("AAA", "Thread Button Dang Nhap Loi");
                }


            }
        });

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DangNhap.this, DangKy.class));
                overridePendingTransition(R.anim.from_right_in, R.anim.from_right_out);
            }
        });

        btnQuenMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DangNhap.this, QuenMatKhau.class));
                overridePendingTransition(R.anim.from_right_in, R.anim.from_right_out);
            }
        });

        btnDangNhapFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!connectInternet.checkInternetConnection()) {
                    Toast.makeText(DangNhap.this, "Bạn chưa kết nối internet", Toast.LENGTH_SHORT).show();
                    return;
                }

                LoginManager.getInstance().logInWithReadPermissions(DangNhap.this,
                        Arrays.asList("public_profile", "email"));
            }
        });

        btnDangNhapGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!connectInternet.checkInternetConnection()) {
                    Toast.makeText(DangNhap.this, "Bạn chưa kết nối internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                showProgressDiaglog();
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
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

    private void showProgressDiaglog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    private void addViews() {
        progressBar = findViewById(R.id.progress_bar);
        toolbar = findViewById(R.id.toolBarDangNhap);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        btnDangKy = findViewById(R.id.btnDangKy);
        btnQuenMatKhau = findViewById(R.id.btnQuenMatKhau);
        btnDangNhapFacebook = findViewById(R.id.btnDangNhapFaceBook);
        btnDangNhapGoogle = findViewById(R.id.btnDangNhapGoogle);
        edTenDangNhap = findViewById(R.id.edUserNameDangNhap);
        edMatKhau = findViewById(R.id.edPassWordDangNhap);
        input_edTenDangNhap = findViewById(R.id.input_edTenDangNhapDangNhap);
        input_edMatKhau = findViewById(R.id.input_edMatKhauDangNhap);
    }


    @Override
    public void DangNhapThanhCong() {
        btnDangNhap.setEnabled(true);
        progressBar.setVisibility(View.GONE);
        KhachHang kh = KhachHang.getInstance(getApplicationContext());
        if (!kh.getHoten().equals("")) {
            helloUser(kh.getHoten());
        } else {
            helloUser(kh.getUsername());
        }
        returnActivityCall();
    }


    @Override
    public void DangNhapThatBai() {
        btnDangNhap.setEnabled(true);
        progressBar.setVisibility(View.GONE);
        Toast.makeText(DangNhap.this, "Tài khoản hoặc mật khẩu không đúng", Toast.LENGTH_LONG).show();
    }

    @Override
    public void helloUser(String username) {
        Toast.makeText(this, "Chào mừng bạn, " + username, Toast.LENGTH_LONG).show();
    }

    private void returnActivityCall() {
        if (callingComponent == null) {
            finish();
            startActivity(new Intent(DangNhap.this, MainActivity.class));
            overridePendingTransition(R.anim.from_right_in, R.anim.from_right_out);
        } else {
            setResult(RESULT_OK);
            finish();
            overridePendingTransition(R.anim.from_right_in, R.anim.from_right_out);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.from_right_in, R.anim.from_right_out);
    }
}
