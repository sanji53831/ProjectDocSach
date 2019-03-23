package com.nguyenminhtri.projectdocsach.view.main;

import android.arch.lifecycle.Observer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;

import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nguyenminhtri.projectdocsach.R;
import com.nguyenminhtri.projectdocsach.connectinternet.ConnectInternet;
import com.nguyenminhtri.projectdocsach.database.Database;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.KhachHang;

import com.nguyenminhtri.projectdocsach.presenter.main.PresenterMain;
import com.nguyenminhtri.projectdocsach.view.lienhe.LienHe;
import com.nguyenminhtri.projectdocsach.view.timkim.TimKim;
import com.nguyenminhtri.projectdocsach.view.napdiemtichluy.NapDiemTichLuy;
import com.nguyenminhtri.projectdocsach.view.thongtintaikhoan.ThongTinTaiKhoan;
import com.nguyenminhtri.projectdocsach.view.tusach.TuSach;
import com.nguyenminhtri.projectdocsach.view.vidiemtichluy.ViDiemTichLuy;
import com.nguyenminhtri.projectdocsach.view.dangnhap.DangNhap;
import com.nguyenminhtri.projectdocsach.view.gopy.GopY;
import com.nguyenminhtri.projectdocsach.view.main.adapter.CustomAdapterViewPager;
import com.nguyenminhtri.projectdocsach.view.main.fragment.fragment_kynang_nghethuatsong;
import com.nguyenminhtri.projectdocsach.view.main.fragment.fragment_kinhdoanh;
import com.nguyenminhtri.projectdocsach.view.main.fragment.fragment_tacpham_kinhdien;
import com.nguyenminhtri.projectdocsach.view.main.fragment.fragment_vanhoa_nghethuat;
import com.nguyenminhtri.projectdocsach.viewmodel.main.ViewModelMain;


public class MainActivity extends AppCompatActivity {

    KhachHang user;
    String displayName;

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    NavigationView navigationView;


    AccessToken accessToken;
    GoogleApiClient googleApiClient;
    GoogleSignInResult googleSignInResult;

    PresenterMain presenterMain;

    Database database;
    ConnectInternet connectInternet;

    final int REQUEST_THONGTINSACH = 1;
    final int REQUEST_THONGTIN_TAIKHOAN = 10;

    public static Boolean stateInternet;
    ViewModelMain viewModelMain;
    NetworkChangeReceiver changeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //
        //
        super.onCreate(savedInstanceState);
        presenterMain = new PresenterMain();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_main);
        changeReceiver = new NetworkChangeReceiver();
        IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(changeReceiver, filter);
        connectInternet = new ConnectInternet(this);
        viewModelMain = new ViewModelMain();
        stateInternet = connectInternet.checkInternetConnection();
        addViews();
        initActionBar();
        initViewPager();

        getTokenFaceBook();
        getTokenGoogle();
        getUserByUserName();

        initSqlite();
        updateTimeDocSach();
        initNavigation();

        viewModelMain.getCheckInternet().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (user == null) {
                    initNavigationNoUser();
                } else {
                    if (aBoolean) {
                        initNavigationHaveUser();
                    } else {
                        initNavigationNoInternet();
                    }
                }
            }
        });
    }

    private void updateTimeDocSach() {
        if (user != null && !user.getId().equals("")) {
            int timeInMysql = user.getThoigiandocsach();
            final int timeInSqlite = Integer.parseInt(database.getThoiGianDocSach(user.getId()));
            if (timeInMysql != timeInSqlite) {
                SharedPreferences sharedPreferences = getSharedPreferences("infouser", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("ThoiGianDocSach", String.valueOf(timeInSqlite));
                editor.apply();
                try {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            presenterMain.saveThoiGianDocSach(user.getId(), String.valueOf(timeInSqlite));
                        }
                    }).start();
                } catch (Exception e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                    Log.d("AAA", "Thread saveThoiGianDocSach MainActivity Loi!");
                }


            }
        }
    }

    private void initSqlite() {
        database = Database.getDatabase(getApplication());
        if (user != null) {
            if (!database.checkUser(user.getId())) {
                database.addUser(user.getId(), String.valueOf(user.getThoigiandocsach()));
            }
        }
    }

    private void getUserByUserName() {
        if (user == null) {
            user = KhachHang.getInstance(this);
            if (user != null) {
                if (!user.getHoten().equals("")) {
                    displayName = user.getHoten();
                } else displayName = user.getUsername();
                if (!connectInternet.checkInternetConnection()) {
                    initNavigationNoInternet();
                } else initNavigationHaveUser();
                MenuItem item = navigationView.getMenu().findItem(R.id.helloUser);
                SpannableString s = new SpannableString("Xin chào, " + displayName);
                s.setSpan(new ForegroundColorSpan(Color.BLUE), 0, s.length(), 0);
                item.setTitle(s);
            }
        }
    }

    private void getTokenFaceBook() {

        accessToken = presenterMain.getTokenFacebook();
    }

    private void getTokenGoogle() {
        googleApiClient = presenterMain.getGoogleApiClient(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

            }
        });
        googleSignInResult = presenterMain.getDataLoginGoogle(googleApiClient);
    }

    private void initActionBar() {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_navigation, R.string.close_navigation);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initNavigation() {
        if (user == null) {
            initNavigationNoUser();
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.dangNhap:
                        startActivity(new Intent(MainActivity.this, DangNhap.class));
                        overridePendingTransition(R.anim.from_left_in, R.anim.from_left_out);
                        break;
                    case R.id.thongTinTaiKhoan:
                        startActivityForResult(new Intent(MainActivity.this, ThongTinTaiKhoan.class), REQUEST_THONGTIN_TAIKHOAN);
                        overridePendingTransition(R.anim.from_left_in, R.anim.from_left_out);
                        break;
                    case R.id.viDiemTichLuy:
                        startActivity(new Intent(MainActivity.this, ViDiemTichLuy.class));
                        overridePendingTransition(R.anim.from_left_in, R.anim.from_left_out);
                        break;
                    case R.id.napDiemTichLuy:
                        startActivity(new Intent(MainActivity.this, NapDiemTichLuy.class));
                        overridePendingTransition(R.anim.from_left_in, R.anim.from_left_out);
                        break;
                    case R.id.tuSach:
                        startActivity(new Intent(MainActivity.this, TuSach.class));
                        overridePendingTransition(R.anim.from_left_in, R.anim.from_left_out);
                        break;
                    case R.id.gopY:
                        startActivity(new Intent(MainActivity.this, GopY.class));
                        overridePendingTransition(R.anim.from_left_in, R.anim.from_left_out);
                        break;
                    case R.id.lienHe:
                        startActivity(new Intent(MainActivity.this, LienHe.class));
                        overridePendingTransition(R.anim.from_left_in, R.anim.from_left_out);
                        break;
                    case R.id.thoatTaiKhoan:
                        showDialogLogOut();
                        break;
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void showDialogLogOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Bạn muốn đăng xuất ?");
        builder.setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (accessToken != null) {
                    LoginManager.getInstance().logOut();
                }
                if (googleSignInResult != null) {
                    Auth.GoogleSignInApi.signOut(googleApiClient);
                }
                user = null;
                KhachHang.deleteKhachHang();
                initNavigationNoUser();
                presenterMain.deleteCacheDangNhap(getApplicationContext());
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

    private void initViewPager() {
        CustomAdapterViewPager adapter = new CustomAdapterViewPager(getSupportFragmentManager());
        adapter.addFragment(new fragment_tacpham_kinhdien(), "Tác Phẩm Kinh Điển");
        adapter.addFragment(new fragment_kynang_nghethuatsong(), "Kỹ Năng - Nghệ Thuật Sống");
        adapter.addFragment(new fragment_vanhoa_nghethuat(), "Văn Hóa - Nghệ Thuật");
        adapter.addFragment(new fragment_kinhdoanh(), "Kinh Doanh");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mainactivity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                startActivity(new Intent(MainActivity.this, TimKim.class));
                overridePendingTransition(R.anim.from_right_in, R.anim.from_right_out);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addViews() {
        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolBarMain);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        navigationView = findViewById(R.id.navigationView);
    }

    private void initNavigationNoUser() {
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.menu_navigation);
        navigationView.getMenu().findItem(R.id.helloUser).setVisible(false);
        navigationView.getMenu().findItem(R.id.tuSach).setVisible(false);
        navigationView.getMenu().findItem(R.id.thongTinTaiKhoan).setVisible(false);
        navigationView.getMenu().findItem(R.id.viDiemTichLuy).setVisible(false);
        navigationView.getMenu().findItem(R.id.napDiemTichLuy).setVisible(false);
        navigationView.getMenu().findItem(R.id.gopY).setVisible(false);
        navigationView.getMenu().findItem(R.id.thoatTaiKhoan).setVisible(false);
    }

    private void initNavigationHaveUser() {
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.menu_navigation);
        MenuItem item = navigationView.getMenu().findItem(R.id.helloUser);
        SpannableString s = new SpannableString("Xin chào, " + displayName);
        s.setSpan(new ForegroundColorSpan(Color.BLUE), 0, s.length(), 0);
        item.setTitle(s);
        navigationView.getMenu().findItem(R.id.dangNhap).setVisible(false);
    }

    private void initNavigationNoInternet() {
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.menu_navigation);
        MenuItem item = navigationView.getMenu().findItem(R.id.helloUser);
        SpannableString s = new SpannableString("Xin chào, " + displayName);
        s.setSpan(new ForegroundColorSpan(Color.BLUE), 0, s.length(), 0);
        item.setTitle(s);
        navigationView.getMenu().findItem(R.id.dangNhap).setVisible(false);
        navigationView.getMenu().findItem(R.id.thongTinTaiKhoan).setVisible(false);
        navigationView.getMenu().findItem(R.id.viDiemTichLuy).setVisible(false);
        navigationView.getMenu().findItem(R.id.napDiemTichLuy).setVisible(false);
        navigationView.getMenu().findItem(R.id.gopY).setVisible(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(changeReceiver);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawers();
        } else if (viewPager.getCurrentItem() > 0) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        } else moveTaskToBack(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_THONGTINSACH && resultCode == RESULT_OK) {
            getUserByUserName();
            initSqlite();
            updateTimeDocSach();
            initNavigation();
        }
        if (requestCode == REQUEST_THONGTIN_TAIKHOAN && resultCode == RESULT_OK) {
            SharedPreferences sharedPreferences = getSharedPreferences("infouser", MODE_PRIVATE);
            if (sharedPreferences.contains("Id")) {
                user = KhachHang.getInstance(this);
                MenuItem item = navigationView.getMenu().findItem(R.id.helloUser);
                SpannableString s = new SpannableString("Xin chào, " + user.getHoten());
                s.setSpan(new ForegroundColorSpan(Color.MAGENTA), 0, s.length(), 0);
                item.setTitle(s);
            } else {
                if (accessToken != null) {
                    LoginManager.getInstance().logOut();
                }
                if (googleSignInResult != null) {
                    googleApiClient.disconnect();
                }

                user = null;
                KhachHang.deleteKhachHang();
                initNavigationNoUser();
            }

        }
    }

    class NetworkChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectInternet connectInternet = new ConnectInternet(context);
            if (connectInternet.checkInternetConnection()) {
                viewModelMain.getCheckInternet().setValue(true);
            } else {
                viewModelMain.getCheckInternet().setValue(false);
            }
        }

    }
}
