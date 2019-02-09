package com.nguyenminhtri.projectdocsach.model.OjbectClass;

public class ChiTietSachYeuThich {
    String maSach;
    String maKhachHang;

    public ChiTietSachYeuThich() {
    }

    public ChiTietSachYeuThich( String maSach, String maKhachHang) {
        this.maSach = maSach;
        this.maKhachHang = maKhachHang;
    }

    public String getMaSach() {
        return maSach;
    }

    public void setMaSach(String maSach) {
        this.maSach = maSach;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }
}
