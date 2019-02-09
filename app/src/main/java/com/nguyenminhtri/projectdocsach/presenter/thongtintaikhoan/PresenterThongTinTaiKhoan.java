package com.nguyenminhtri.projectdocsach.presenter.thongtintaikhoan;

import android.content.Context;

import com.nguyenminhtri.projectdocsach.model.OjbectClass.KhachHang;
import com.nguyenminhtri.projectdocsach.model.thongtintaikhoan.ModelThongTinTaiKhoan;

public class PresenterThongTinTaiKhoan implements InterfacePresenterThongTinTaiKhoan {

    ModelThongTinTaiKhoan modelThongTinTaiKhoan;

    public PresenterThongTinTaiKhoan() {
        modelThongTinTaiKhoan = new ModelThongTinTaiKhoan();
    }

    @Override
    public Boolean capNhatThongTinTaiKhoan(String userId, String hoten, String ngaysinh, String diachi, int gioitinh) {
        if (modelThongTinTaiKhoan.capNhatThongTinTaiKhoan(userId, hoten, ngaysinh, diachi, gioitinh).equals("success")) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean thayDoiMatKhau(String userId, String matKhau) {
        if (modelThongTinTaiKhoan.thayDoiMatKhau(userId, matKhau).equals("success")) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean thayDoiSoDienThoai(String userId, String soDienThoai) {
        if (modelThongTinTaiKhoan.thayDoiSoDienThoai(userId, soDienThoai).equals("success")) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean thayDoiEmail(String userId, String email) {
        if (modelThongTinTaiKhoan.thayDoiEmail(userId, email).equals("success")) {
            return true;
        }
        return false;
    }

    @Override
    public void capNhatShareferenceUser(Context context, KhachHang khachHang) {
        modelThongTinTaiKhoan.updateSharePreference(context, khachHang);
    }

    @Override
    public void deleteShareferenceUser(Context context) {
        modelThongTinTaiKhoan.deleteSharePreference(context);
    }
}
