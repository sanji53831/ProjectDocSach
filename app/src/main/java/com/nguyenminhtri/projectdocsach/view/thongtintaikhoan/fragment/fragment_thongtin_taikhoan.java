package com.nguyenminhtri.projectdocsach.view.thongtintaikhoan.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.nguyenminhtri.projectdocsach.R;
import com.nguyenminhtri.projectdocsach.connectinternet.ConnectInternet;
import com.nguyenminhtri.projectdocsach.customview.PassWordEditText;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.KhachHang;
import com.nguyenminhtri.projectdocsach.presenter.thongtintaikhoan.PresenterThongTinTaiKhoan;
import com.nguyenminhtri.projectdocsach.viewmodel.thongtintaikhoan.ViewModelFactoryThongTinTaiKhoan;
import com.nguyenminhtri.projectdocsach.viewmodel.thongtintaikhoan.ViewModelThongTinTaiKhoan;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class fragment_thongtin_taikhoan extends Fragment implements View.OnClickListener {

    KhachHang kh;
    EditText edTttk, edMk, edSdt, edEmail;

    final String REGEX_SDT = "\\d{10,11}";
    final String REGEX_MATKHAU = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,50})";
    Pattern pattern;
    Matcher matcher;

    ViewModelThongTinTaiKhoan viewModelThongTinTaiKhoan;
    PresenterThongTinTaiKhoan presenterThongTinTaiKhoan;

    ConnectInternet connectInternet;
    ThreadPoolExecutor threadPoolExecutor;
    Handler myHanler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thongtin_taikhoan, container, false);
        kh = KhachHang.getInstance(getContext());
        presenterThongTinTaiKhoan = new PresenterThongTinTaiKhoan();
        viewModelThongTinTaiKhoan = ViewModelProviders.of(getActivity(), new ViewModelFactoryThongTinTaiKhoan(getActivity()
                .getApplication())).get(ViewModelThongTinTaiKhoan.class);

        connectInternet = new ConnectInternet(getActivity());
        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(20);
        threadPoolExecutor = new ThreadPoolExecutor(5, 5, 5, TimeUnit.SECONDS, queue);
        myHanler = new Handler();

        addViews(view);
        addEvents();
        initContentViews();
        return view;
    }

    private void initContentViews() {
        edEmail.setText(kh.getEmail());
        if (!kh.getSodienthoai().equals("")) {
            String basocuoi = kh.getSodienthoai().substring(kh.getSodienthoai().length() - 3, kh.getSodienthoai().length());
            String cacsodau = "";
            for (int i = 0; i < kh.getSodienthoai().length() - 3; i++) {
                cacsodau += "*";
            }
            String sdt = cacsodau + basocuoi;
            edSdt.setText(sdt);
        }
    }

    private void addEvents() {
        edTttk.setOnClickListener(this);
        edMk.setOnClickListener(this);
        edEmail.setOnClickListener(this);
        edSdt.setOnClickListener(this);
    }

    private void addViews(View view) {
        edTttk = view.findViewById(R.id.edTttkThongTinTaiKhoan);
        edMk = view.findViewById(R.id.edMkThongTinTaiKhoan);
        edSdt = view.findViewById(R.id.edSdtThongTinTaiKhoan);
        edEmail = view.findViewById(R.id.edEmailThongTinTaiKhoan);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edTttkThongTinTaiKhoan:
                showDialogThongTinCaNhan();
                break;
            case R.id.edMkThongTinTaiKhoan:
                showDialogDoiMatKhau();
                break;
            case R.id.edSdtThongTinTaiKhoan:
                showDialogSoDienThoai();
                break;
            case R.id.edEmailThongTinTaiKhoan:
                showDialogEmail();
                break;
        }
    }

    private void showDialogThongTinCaNhan() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_thongtin_taikhoan, null);
        builder.setView(view);
        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();
        ImageButton imgClose = view.findViewById(R.id.imgButtonCloseDialogTttk);
        final EditText edNgaySinh = view.findViewById(R.id.edNgaySinh);
        final EditText edHoTen = view.findViewById(R.id.edHoten);
        final EditText edDiaChi = view.findViewById(R.id.edDiaChi);
        Button btnCapNhat = view.findViewById(R.id.btnCapNhat);
        final RadioButton rdNam = view.findViewById(R.id.rdNam);
        final RadioButton rdNu = view.findViewById(R.id.rdNu);

        if (!kh.getHoten().equals("")) {
            edHoTen.setText(kh.getHoten());
        } else {
            edHoTen.setText(kh.getUsername());
        }
        if (kh.getGioitinh() == 1) {
            rdNam.setChecked(true);
        } else if (kh.getGioitinh() == 0) {
            rdNu.setChecked(true);
        }
        if (!kh.getNgaysinh().equals("")) {
            edNgaySinh.setText(kh.getNgaysinh());
        } else {
            edNgaySinh.setText("1/1/1990");
        }
        if (!kh.getDiachi().equals("")) {
            edDiaChi.setText(kh.getDiachi());
        }

        edNgaySinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(edNgaySinh);
            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ten = edHoTen.getText().toString().trim();
                if (ten.equals("")) {
                    Toast.makeText(getActivity(), "Bạn chưa nhập họ tên", Toast.LENGTH_SHORT).show();
                    return;
                }
                String ngaysinh = edNgaySinh.getText().toString().trim();
                String diachi = edDiaChi.getText().toString().trim();
                int gioitinh = 1;
                if (rdNam.isChecked()) {
                    gioitinh = 1;
                } else if (rdNu.isChecked()) {
                    gioitinh = 0;
                }
                if (connectInternet.checkInternetConnection()) {
                    capNhatThongTinCaNhan(ten, ngaysinh, gioitinh, diachi, dialog);
                } else {
                    Toast.makeText(getActivity(), "Bạn không có kết nối internet", Toast.LENGTH_SHORT).show();
                }

            }
        });
        dialog.show();
    }

    private void showDatePicker(final EditText edNgaysinh) {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH);
        final int day = now.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog pickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                edNgaysinh.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, year, month, day);
        pickerDialog.show();
    }

    private void showDialogDoiMatKhau() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_doimatkhau, null);
        builder.setView(view);
        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();

        ImageButton imgClose = view.findViewById(R.id.imgButtonCloseDialogDoiMatKhau);
        final PassWordEditText edMatKhauCu = view.findViewById(R.id.edMatKhauCu);
        final PassWordEditText edMatKhauMoi = view.findViewById(R.id.edMatKhauMoi);
        final PassWordEditText edNhapLaiMatKhauMoi = view.findViewById(R.id.edNhapLaiMatKhauMoi);
        Button btnCapNhap = view.findViewById(R.id.btnCapNhat);

        edMatKhauMoi.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    TextInputLayout textInputLayout = (TextInputLayout) edMatKhauMoi.getParent().getParent();
                    textInputLayout.setErrorEnabled(false);
                    textInputLayout.setError("");
                }
            }
        });

        btnCapNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mkCu = edMatKhauCu.getText().toString().trim();
                String mkMoi = edMatKhauMoi.getText().toString().trim();
                String nhapLaiMkMoi = edNhapLaiMatKhauMoi.getText().toString().trim();
                if (mkCu.equals("") || mkMoi.equals("") || nhapLaiMkMoi.equals("")) {
                    Toast.makeText(getActivity(), "Hãy nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!mkCu.equals(kh.getPassword())) {
                    Toast.makeText(getActivity(), "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
                    return;
                }
                pattern = Pattern.compile(REGEX_MATKHAU);
                matcher = pattern.matcher(mkMoi);
                if (!matcher.matches()) {
                    TextInputLayout textInputLayout = (TextInputLayout) edMatKhauMoi.getParent().getParent();
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError("Mật khẩu phải từ 6 kí tự,có chữ số, chữ thường và chữ hoa.");
                    return;
                }
                if (mkCu.equals(mkMoi)) {
                    Toast.makeText(getActivity(), "Mật khẩu mới không được trùng với mật khẩu cũ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!mkMoi.equals(nhapLaiMkMoi)) {
                    Toast.makeText(getActivity(), "Nhập lại mật khẩu mới không trùng khớp", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (connectInternet.checkInternetConnection()) {
                    thayDoiMatKhau(mkMoi, dialog);
                } else {
                    Toast.makeText(getActivity(), "Bạn không có kết nối internet", Toast.LENGTH_SHORT).show();
                }
            }
        });


        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showDialogSoDienThoai() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_sodienthoai, null);
        builder.setView(view);
        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();

        final EditText edSoDienThoai = view.findViewById(R.id.edSoDienThoai);
        ImageButton imgClose = view.findViewById(R.id.imgButtonCloseDialogSoDienThoai);
        Button btnCapNhat = view.findViewById(R.id.btnCapNhat);

        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sdt = edSoDienThoai.getText().toString().trim();
                pattern = Pattern.compile(REGEX_SDT);
                matcher = pattern.matcher(sdt);
                if (!matcher.matches()) {
                    Toast.makeText(getActivity(), "Đây không phải là số điện thoại", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (connectInternet.checkInternetConnection()) {
                    thayDoiSoDienThoai(sdt, dialog);
                } else {
                    Toast.makeText(getActivity(), "Bạn không có kết nối internet", Toast.LENGTH_SHORT).show();
                }

            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showDialogEmail() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_email, null);
        builder.setView(view);
        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();

        ImageButton imgClose = view.findViewById(R.id.imgButtonCloseDialogEmail);
        final EditText edEmail = view.findViewById(R.id.edEmail);
        Button btnCapNhat = view.findViewById(R.id.btnCapNhat);

        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edEmail.getText().toString().trim();

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(getActivity(), "Đây không phải là Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (connectInternet.checkInternetConnection()) {
                    thayDoiEmail(email, dialog);
                } else {
                    Toast.makeText(getActivity(), "Bạn không có kết nối internet", Toast.LENGTH_SHORT).show();
                }

            }
        });
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void capNhatThongTinCaNhan(final String hoTen, final String ngaySinh, final int gioiTinh, final String diaChi, final AlertDialog dialog) {
        try {
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    final Boolean b = presenterThongTinTaiKhoan.capNhatThongTinTaiKhoan(kh.getId(), hoTen, ngaySinh, diaChi, gioiTinh);
                    myHanler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (b) {
                                kh.setHoten(hoTen);
                                kh.setNgaysinh(ngaySinh);
                                kh.setGioitinh(gioiTinh);
                                kh.setDiachi(diaChi);
                                getActivity().setResult(Activity.RESULT_OK);
                                dialog.dismiss();
                                capNhatThanhCong();
                            } else {
                                capNhatThatBai();
                            }
                        }
                    }, 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
            Log.d("AAA", "Thread capNhatThongTinCaNhan Loi");
        }

    }

    private void thayDoiMatKhau(final String matKhau, final AlertDialog dialog) {
        try {
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    final Boolean b = presenterThongTinTaiKhoan.thayDoiMatKhau(kh.getId(), matKhau);
                    myHanler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (b) {
                                kh.setPassword(matKhau);
                                dialog.dismiss();
                                capNhatThanhCong();
                            } else {
                                capNhatThatBai();
                            }
                        }
                    }, 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
            Log.d("AAA", "Thread thayDoiMatKhau loi");
        }

    }

    private void thayDoiSoDienThoai(final String sdt, final AlertDialog dialog) {
        try {
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    final Boolean b = presenterThongTinTaiKhoan.thayDoiSoDienThoai(kh.getId(), sdt);
                    myHanler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (b) {
                                kh.setSodienthoai(sdt);
                                initContentViews();
                                dialog.dismiss();
                                capNhatThanhCong();
                            } else {
                                capNhatThatBai();
                            }
                        }
                    }, 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
            Log.d("AAA", "Thread thayDoiSoDienThoai loi");
        }

    }

    private void thayDoiEmail(final String email, final AlertDialog dialog) {
        try {
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    final Boolean b = presenterThongTinTaiKhoan.thayDoiEmail(kh.getId(), email);
                    myHanler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (b) {
                                kh.setEmail(email);
                                initContentViews();
                                dialog.dismiss();
                                capNhatThanhCong();
                            } else {
                                capNhatThatBai();
                            }
                        }
                    }, 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
            Log.d("AAA", "Thread thayDoiEmail loi");
        }

    }

    private void capNhatThanhCong() {
        presenterThongTinTaiKhoan.capNhatShareferenceUser(getActivity(), kh);
        viewModelThongTinTaiKhoan.getKhachhang().setValue(kh);
        Toast.makeText(getActivity(), "Cập nhật thông tin tài khoản thành công", Toast.LENGTH_SHORT).show();
    }

    private void capNhatThatBai() {
        Toast.makeText(getActivity(), "Lỗi! Không thể cập nhật", Toast.LENGTH_SHORT).show();
    }
}
