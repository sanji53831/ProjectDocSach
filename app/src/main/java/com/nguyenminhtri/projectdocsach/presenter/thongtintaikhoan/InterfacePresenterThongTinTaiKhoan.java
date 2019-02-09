package com.nguyenminhtri.projectdocsach.presenter.thongtintaikhoan;

import android.content.Context;

import com.nguyenminhtri.projectdocsach.model.OjbectClass.KhachHang;

public interface InterfacePresenterThongTinTaiKhoan {
    Boolean capNhatThongTinTaiKhoan(String userId,String hoten,String ngaysinh,String diachi,int gioitinh);
    Boolean thayDoiMatKhau(String userId,String matKhau);
    Boolean thayDoiSoDienThoai(String userId,String soDienThoai);
    Boolean thayDoiEmail(String userId,String email);
    void capNhatShareferenceUser(Context context, KhachHang khachHang);
    void deleteShareferenceUser(Context context);
}
