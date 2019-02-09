package com.nguyenminhtri.projectdocsach.view.thongtinsach;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;

import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.nguyenminhtri.projectdocsach.R;
import com.nguyenminhtri.projectdocsach.connectinternet.ConnectInternet;
import com.nguyenminhtri.projectdocsach.database.Database;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.ChiTietSachYeuThich;

import com.nguyenminhtri.projectdocsach.model.OjbectClass.KhachHang;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.ViewDanhGia;
import com.nguyenminhtri.projectdocsach.savevariables.SaveVariables;
import com.nguyenminhtri.projectdocsach.view.dangnhap.DangNhap;
import com.nguyenminhtri.projectdocsach.view.loadsach.LoadSach;
import com.nguyenminhtri.projectdocsach.view.main.adapter.CustomAdapterRecyelerView_Main;
import com.nguyenminhtri.projectdocsach.view.tatcadanhgia.TatCaDanhGia;
import com.nguyenminhtri.projectdocsach.view.thongtinsach.adapter.CustomAdapterRecyelerView_DanhGia_ThongTinSach;
import com.nguyenminhtri.projectdocsach.view.vietdanhgia.VietDanhGia;
import com.nguyenminhtri.projectdocsach.viewmodel.thongtinsach.ViewModelThongTinSach;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.Sach;
import com.nguyenminhtri.projectdocsach.presenter.thongtinsach.PresenterThongTinSach;
import com.nguyenminhtri.projectdocsach.view.napdiemtichluy.NapDiemTichLuy;

import java.text.NumberFormat;

import java.util.Locale;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThongTinSach extends AppCompatActivity implements InterfaceViewThongTinSach {

    FrameLayout layoutMain;
    AppBarLayout appBarLayout;
    ProgressBar progressBar;
    Toolbar toolbar;
    RatingBar ratingBar;
    ImageView imgSach;
    Button btnDocSach;
    TextView tvTotalDanhGia, tvTenSach, tvTenTacGia, tvTenTheLoai, tvTenNhaXuatBan, tvNgayXuatBan, tvVietDanhGia,
            tvXemTatCaDanhGia, tvThuLai;
    ImageView imgLike;
    ExpandableTextView expandableTextView;
    RecyclerView recyclerViewDanhGia, recyclerViewXemThem;

    CustomAdapterRecyelerView_DanhGia_ThongTinSach adapterDanhGia;
    CustomAdapterRecyelerView_Main adapterXemThemSach;

    PresenterThongTinSach presenterThongTinSach;
    ViewModelThongTinSach viewModelThongTinSach;
    Database database;
    Sach sach;
    byte[] b;
    KhachHang kh;
    Bitmap hinhSach;

    final int REQUEST_CODE_VIETDANHGIA = 10;
    final int REQUEST_CODE_DANGNHAP = 0;
    final int REQUEST_CODE_XEMTATCADANHGIA = 20;
    final int REQUEST_CODE_LOADSACH = 30;

    int totalDanhGia;
    ConnectInternet connectInternet;

    ThreadPoolExecutor threadPoolExecutor;
    Handler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(50);
        threadPoolExecutor = new ThreadPoolExecutor(5, 10, 10, TimeUnit.SECONDS, queue);
        database = Database.getDatabase(getApplication());
        myHandler = new Handler();

        connectInternet = new ConnectInternet(this);
        setContentView(R.layout.activity_thong_tin_sach);
        layoutMain = findViewById(R.id.layoutMainThongTinSach);
        if (!connectInternet.checkInternetConnection()) {
            final View view = LayoutInflater.from(this).inflate(R.layout.layout_nointernet, null, false);
            tvThuLai = view.findViewById(R.id.tvThuLai);
            layoutMain.addView(view);
            tvThuLai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvThuLai.setVisibility(View.GONE);
                    if (!connectInternet.checkInternetConnection()) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                tvThuLai.setVisibility(View.VISIBLE);
                            }
                        }, 1000);
                    } else {
                        layoutMain.removeView(view);
                        View view2 = LayoutInflater.from(ThongTinSach.this).inflate(R.layout.layout_activity_thongtinsach, null, false);
                        viewModelThongTinSach = ViewModelProviders.of(ThongTinSach.this).get(ViewModelThongTinSach.class);
                        layoutMain.addView(view2);
                        addViews(view2);
                        init();
                    }
                }
            });
        } else {
            View view2 = LayoutInflater.from(this).inflate(R.layout.layout_activity_thongtinsach, null, false);
            viewModelThongTinSach = ViewModelProviders.of(this).get(ViewModelThongTinSach.class);
            layoutMain.addView(view2);
            addViews(view2);
            init();
        }

    }

    private void init() {
        kh = KhachHang.getInstance(this);
        getInfoSach();
        presenterThongTinSach = new PresenterThongTinSach(this);
        if (connectInternet.checkInternetConnection()) {
            try {
                threadPoolExecutor.execute(threadGetDanhSachDanhGia);

            } catch (Exception e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
                Log.d("AAA", "Thread threadGetDanhSachDanhGia ThongTinSach Loi");
            }

            checkSachYeuThich();

            try {
                threadPoolExecutor.execute(threadGetDanhSachSachGoiY);
            } catch (Exception e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
                Log.d("AAA", "Thread threadGetDanhSachSachGoiY ThongTinSach Loi");
            }
        }

        initActionBar();
        addEvents();
        initContenViews();


    }

    private void checkSachYeuThich() {
        if (kh != null) {
            try {
                threadPoolExecutor.execute(threadCheckSachYeuThich);
            } catch (Exception e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
                Log.d("AAA", "Thread checkSachYeuThich ThongTinSach Loi");
            }
        } else {
            progressBar.setVisibility(View.GONE);
            imgLike.setImageResource(R.drawable.heartwhite24dp);
        }
    }


    private void getInfoSach() {
        Intent intent = getIntent();
        sach = intent.getParcelableExtra("Sach");
        b = intent.getByteArrayExtra("Hinh");
        hinhSach = BitmapFactory.decodeByteArray(b, 0, b.length);
    }

    private void addEvents() {

        btnDocSach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kh != null) {
                    Intent intent;
                    if (sach.getTrangthai().equals("0")) {
                        intent = new Intent(ThongTinSach.this, LoadSach.class);
                        intent.putExtra("Sach", sach);
                        intent.putExtra("HinhAnh", b);
                        startActivityForResult(intent, REQUEST_CODE_LOADSACH);
                    } else {
                        intent = new Intent(ThongTinSach.this, NapDiemTichLuy.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.from_right_in, R.anim.from_right_out);
                    }
                } else {
                    startActivityForResult(new Intent(ThongTinSach.this, DangNhap.class), REQUEST_CODE_DANGNHAP);
                    overridePendingTransition(R.anim.from_left_in, R.anim.from_left_out);
                }


            }
        });

        imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!connectInternet.checkInternetConnection()) {
                    Toast.makeText(ThongTinSach.this, "Bạn không có kết nối internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (kh != null) {
                    try {
                        if (viewModelThongTinSach.getValueCheckSachYeuThich(kh.getId(), sach.getId()).getValue()) {
                            showDialogDeleteSachYeuThich(new ChiTietSachYeuThich(sach.getId(), kh.getId()));
                        } else {
                            showDialogThemSachYeuThich(new ChiTietSachYeuThich(sach.getId(), kh.getId()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("AAA", "Thread click imgLike Loi");
                    }

                } else {
                    startActivityForResult(new Intent(ThongTinSach.this, DangNhap.class), REQUEST_CODE_DANGNHAP);
                    overridePendingTransition(R.anim.from_left_in, R.anim.from_left_out);
                }
            }
        });

        tvVietDanhGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!connectInternet.checkInternetConnection()) {
                    Toast.makeText(ThongTinSach.this, "Bạn không có kết nối internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (kh != null) {
                    Intent intent = new Intent(ThongTinSach.this, VietDanhGia.class);
                    intent.putExtra("MaSach", sach.getId());
                    startActivityForResult(intent, REQUEST_CODE_VIETDANHGIA);
                } else {
                    startActivityForResult(new Intent(ThongTinSach.this, DangNhap.class), REQUEST_CODE_DANGNHAP);
                    overridePendingTransition(R.anim.from_left_in, R.anim.from_left_out);
                }

            }
        });

        tvXemTatCaDanhGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!connectInternet.checkInternetConnection()) {
                    Toast.makeText(ThongTinSach.this, "Bạn không có kết nối internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(ThongTinSach.this, TatCaDanhGia.class);
                Bundle bundle = new Bundle();
                bundle.putString("MaSach", sach.getId());
                bundle.putInt("ToTalDanhGia", totalDanhGia);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_CODE_XEMTATCADANHGIA);
                overridePendingTransition(R.anim.from_right_in, R.anim.from_right_out);
            }
        });

        viewModelThongTinSach.getViewDanhGiaByMaSach(sach.getId()).observe(this, new Observer<ViewDanhGia>() {
            @Override
            public void onChanged(@Nullable ViewDanhGia viewDanhGia) {
                float soSao = Float.parseFloat(viewDanhGia.getAvgSoSao());
                ratingBar.setRating(soSao);
                tvTotalDanhGia.setText(viewDanhGia.getToTalDanhGia() + " users đánh giá");
                if (viewDanhGia.getToTalDanhGia().equals("1")) {
                    tvXemTatCaDanhGia.setText("Xem tất cả đánh giá");
                    tvXemTatCaDanhGia.setClickable(true);
                }
                adapterDanhGia = new CustomAdapterRecyelerView_DanhGia_ThongTinSach(ThongTinSach.this,
                        viewDanhGia.getListDanhGia());
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ThongTinSach.this,
                        LinearLayoutManager.VERTICAL, false);
                recyclerViewDanhGia.setLayoutManager(layoutManager);
                recyclerViewDanhGia.setAdapter(adapterDanhGia);
            }
        });

    }

    private void showDialogThemSachYeuThich(final ChiTietSachYeuThich chiTietSachYeuThich) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thêm");
        builder.setMessage("Bạn muốn thêm quyển sách này vào tủ sách yêu thích của mình ?");
        builder.setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (connectInternet.checkInternetConnection()) {
                    showImgLikeRed();
                    setResult(RESULT_OK);
                    threadPoolExecutor.execute(threadAddSachYeuThichToSqlite);
                    try {
                        threadPoolExecutor.execute(threadSaveSachYeuThich);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                        Log.d("AAA", "Thread threadSaveSachYeuThich ThongTinSach Loi");
                    }
                } else {
                    Toast.makeText(ThongTinSach.this, "Bạn không có kết nối internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void showDialogDeleteSachYeuThich(final ChiTietSachYeuThich chiTietSachYeuThich) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hủy");
        builder.setMessage("Bạn muốn bỏ yêu thích sách này ?");
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (connectInternet.checkInternetConnection()) {
                    showImgLikeWhite();
                    setResult(RESULT_OK);
                    threadPoolExecutor.execute(threadDeleteSachYeuThichFromSqlite);
                    try {
                        threadPoolExecutor.execute(threadDeleteSachYeuThich);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                        Log.d("AAA", "Thread threadSaveSachYeuThich ThongTinSach Loi");
                    }

                } else {
                    Toast.makeText(ThongTinSach.this, "Bạn không có kết nối internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    private void initContenViews() {
        Drawable drawable = new BitmapDrawable(getResources(), hinhSach);
        appBarLayout.setBackground(drawable);
        appBarLayout.getBackground().setAlpha(100);
        toolbar.setTitle(sach.getTenSach());
        imgSach.setImageBitmap(hinhSach);
        tvTenSach.setText(sach.getTenSach());
        expandableTextView.setText(sach.getMoTa());
        tvTenTacGia.setText(sach.getTenTacGia());
        tvTenTheLoai.setText(sach.getTenTheLoai());
        tvTenNhaXuatBan.setText(sach.getTenNhaXuatBan());
        tvNgayXuatBan.setText(sach.getNgayXuatBan());
        presenterThongTinSach.InitButtonDocSach(sach);


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

    private void addViews(View view) {
        appBarLayout = view.findViewById(R.id.appBarLayout);
        toolbar = view.findViewById(R.id.toolBarThongTinSach);
        progressBar = view.findViewById(R.id.progress_bar);
        ratingBar = view.findViewById(R.id.ratingBar);
        tvTotalDanhGia = view.findViewById(R.id.tvTotalDanhGia);
        tvTenSach = view.findViewById(R.id.tvTenSachThongTinSach);
        imgSach = view.findViewById(R.id.imgSachThongTinSach);
        btnDocSach = view.findViewById(R.id.btnDocSachThongTinSach);
        expandableTextView = view.findViewById(R.id.tvMoTaSachThongTinSach);
        tvTenTacGia = view.findViewById(R.id.tvTacGiaThongTinSach);
        tvTenTheLoai = view.findViewById(R.id.tvTheLoaiThongTinSach);
        tvTenNhaXuatBan = view.findViewById(R.id.tvNhaXuatBanThongTinSach);
        tvNgayXuatBan = view.findViewById(R.id.tvNgayXuatBanThongTinSach);
        tvVietDanhGia = view.findViewById(R.id.tvVietDanhGiaThongTinSach);
        imgLike = view.findViewById(R.id.imgLike);
        recyclerViewDanhGia = view.findViewById(R.id.recyclerViewDanhGiaThongTinSach);
        tvXemTatCaDanhGia = view.findViewById(R.id.tvXemTatCaNhanXetThongTinSach);
        recyclerViewXemThem = view.findViewById(R.id.recyclerViewXemThemSach);
    }

    private void getDanhSachDanhGia(int totalDanhGia) {
        if (totalDanhGia > 0) {
            float soSao = Float.parseFloat(viewModelThongTinSach
                    .getViewDanhGiaByMaSach(sach.getId()).getValue().getAvgSoSao());
            ratingBar.setRating(soSao);
            adapterDanhGia = new CustomAdapterRecyelerView_DanhGia_ThongTinSach(this,
                    viewModelThongTinSach.getViewDanhGiaByMaSach(sach.getId()).getValue().getListDanhGia());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL, false);

            recyclerViewDanhGia.setLayoutManager(layoutManager);
            recyclerViewDanhGia.setAdapter(adapterDanhGia);
        } else {
            tvXemTatCaDanhGia.setText("Chưa có đánh giá nào");
            tvXemTatCaDanhGia.setClickable(false);
        }

    }

    @Override
    public void showImgLikeRed() {
        progressBar.setVisibility(View.GONE);
        imgLike.setImageResource(R.drawable.heartred);
    }

    @Override
    public void showImgLikeWhite() {
        progressBar.setVisibility(View.GONE);
        imgLike.setImageResource(R.drawable.heartwhite24dp);
    }

    @Override
    public void showButtonDocSachMienPhi() {
        btnDocSach.setText("Đọc Sách Miễn Phí");
    }

    @Override
    public void showButtonMuaSach() {
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat vn = NumberFormat.getInstance(localeVN);
        btnDocSach.setText("Mua Sách " + vn.format(Double.parseDouble(sach.getGia())) + " Point");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_DANGNHAP && resultCode == RESULT_OK) {
            kh = KhachHang.getInstance(this);
            initContenViews();
            if (connectInternet.checkInternetConnection()) {
                try {
                    threadPoolExecutor.execute(threadCheckSachYeuThich);
                    setResult(RESULT_OK);
                } catch (Exception e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                    Log.d("AAA", "Thread threadCheckSachYeuThich in onActivityResult ThongTinSach loi");
                }
            }
        }
        if ((requestCode == REQUEST_CODE_VIETDANHGIA || requestCode == REQUEST_CODE_XEMTATCADANHGIA) && resultCode == RESULT_OK) {
            if (connectInternet.checkInternetConnection()) {
                try {
                    threadPoolExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            viewModelThongTinSach.getViewDanhGiaByMaSach(sach.getId())
                                    .postValue(presenterThongTinSach.getThreeDanhGiaByMaSach(sach.getId()));
                        }
                    });
                    setResult(RESULT_OK);
                } catch (Exception e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                    Log.d("AAA", "Thread getThreeDanhGiaByMaSach in onActivityResult ThongTinSach loi");
                }

            }
        }

        if (requestCode == REQUEST_CODE_LOADSACH && resultCode == RESULT_OK && data != null) {
            setResult(RESULT_OK, data);
        }
    }


    Runnable threadAddSachYeuThichToSqlite = new Runnable() {
        @Override
        public void run() {
            database.addBookYeuThich(kh.getId(), sach.getId());
            if (!database.checkSach(sach.getId())) {
                database.addBook(sach, hinhSach);
            }
        }
    };

    Runnable threadDeleteSachYeuThichFromSqlite = new Runnable() {
        @Override
        public void run() {
            database.deleteBookYeuThich(kh.getId(), sach.getId());
        }
    };

    Runnable threadGetDanhSachDanhGia = new Runnable() {
        @Override
        public void run() {
            viewModelThongTinSach.getViewDanhGiaByMaSach(sach.getId());
            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (viewModelThongTinSach.getViewDanhGiaByMaSach(sach.getId()).getValue() != null) {
                        tvTotalDanhGia.setText(viewModelThongTinSach
                                .getViewDanhGiaByMaSach(sach.getId()).getValue().getToTalDanhGia() + " users đánh giá");
                        totalDanhGia = Integer.parseInt(viewModelThongTinSach
                                .getViewDanhGiaByMaSach(sach.getId()).getValue().getToTalDanhGia());
                    }
                    getDanhSachDanhGia(totalDanhGia);
                }
            }, 500);
        }
    };

    Runnable threadCheckSachYeuThich = new Runnable() {
        @Override
        public void run() {
            viewModelThongTinSach.getValueCheckSachYeuThich(kh.getId(), sach.getId());
            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (viewModelThongTinSach.getValueCheckSachYeuThich(kh.getId(), sach.getId()).getValue()) {
                        showImgLikeRed();
                    } else {
                        showImgLikeWhite();
                    }
                }
            }, 500);
        }
    };

    Runnable threadGetDanhSachSachGoiY = new Runnable() {
        @Override
        public void run() {
            if (sach.getMaTheLoai().equals(SaveVariables.maTacPhamKinhDien)) {
                viewModelThongTinSach.getListTacPhamKinhDien();
            } else if (sach.getMaTheLoai().equals(SaveVariables.maKyNangNgheThuatSong)) {
                viewModelThongTinSach.getListKyNangNgheThuatSong();
            } else if (sach.getMaTheLoai().equals(SaveVariables.maVanHoaNgheThuat)) {
                viewModelThongTinSach.getListVanHoaNgheThuat();
            } else {
                viewModelThongTinSach.getListKinhDoanh();
            }
            myHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (sach.getMaTheLoai().equals(SaveVariables.maTacPhamKinhDien)) {
                        adapterXemThemSach = new CustomAdapterRecyelerView_Main(ThongTinSach.this,
                                R.layout.custom_row_recyclerview_xemthemsach_thongtinsach,
                                viewModelThongTinSach.getListTacPhamKinhDien().getValue());
                    } else if (sach.getMaTheLoai().equals(SaveVariables.maKyNangNgheThuatSong)) {
                        adapterXemThemSach = new CustomAdapterRecyelerView_Main(ThongTinSach.this,
                                R.layout.custom_row_recyclerview_xemthemsach_thongtinsach,
                                viewModelThongTinSach.getListKyNangNgheThuatSong().getValue());
                    } else if (sach.getMaTheLoai().equals(SaveVariables.maVanHoaNgheThuat)) {
                        adapterXemThemSach = new CustomAdapterRecyelerView_Main(ThongTinSach.this,
                                R.layout.custom_row_recyclerview_xemthemsach_thongtinsach,
                                viewModelThongTinSach.getListVanHoaNgheThuat().getValue());
                    } else {
                        adapterXemThemSach = new CustomAdapterRecyelerView_Main(ThongTinSach.this,
                                R.layout.custom_row_recyclerview_xemthemsach_thongtinsach,
                                viewModelThongTinSach.getListKinhDoanh().getValue());
                    }
                    if (adapterXemThemSach.getItemCount() > 0) {

                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ThongTinSach.this, LinearLayoutManager.HORIZONTAL, false);
                        recyclerViewXemThem.setLayoutManager(layoutManager);
                        recyclerViewXemThem.setAdapter(adapterXemThemSach);
                    }
                }
            }, 500);
        }
    };

    Runnable threadSaveSachYeuThich = new Runnable() {
        @Override
        public void run() {
            viewModelThongTinSach.getValueCheckSachYeuThich(kh.getId(), sach.getId()).postValue(true);
            presenterThongTinSach.saveSachYeuThich(kh.getId(), sach.getId());
        }
    };

    Runnable threadDeleteSachYeuThich = new Runnable() {
        @Override
        public void run() {
            viewModelThongTinSach.getValueCheckSachYeuThich(kh.getId(), sach.getId()).postValue(false);
            presenterThongTinSach.deleteSachYeuThich(kh.getId(), sach.getId());
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.from_left_in, R.anim.from_left_out);
    }
}
