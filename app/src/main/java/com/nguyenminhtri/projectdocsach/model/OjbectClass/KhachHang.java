package com.nguyenminhtri.projectdocsach.model.OjbectClass;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class KhachHang {
    private int gioitinh, tongdiemtichluy, diemtichluytheogio, thoigiandocsach;
    private String id, hoten, ngaysinh, diachi, sodienthoai, email, username, password;
    private int totalSachDaDoc, totalSachYeuThich;
    private static KhachHang kh;

    private KhachHang() {
    }


    private KhachHang(String id, int gioitinh, int tongdiemtichluy, int diemtichluytheogio, String hoten, String ngaysinh, String diachi,
                      String sodienthoai, String email, String username, String password, int thoigiandocsach) {
        this.id = id;
        this.gioitinh = gioitinh;
        this.tongdiemtichluy = tongdiemtichluy;
        this.diemtichluytheogio = diemtichluytheogio;
        this.hoten = hoten;
        this.ngaysinh = ngaysinh;
        this.diachi = diachi;
        this.sodienthoai = sodienthoai;
        this.email = email;
        this.username = username;
        this.password = password;
        this.thoigiandocsach = thoigiandocsach;
    }

    public static KhachHang getInstance(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("infouser", Context.MODE_PRIVATE);
        if (kh == null && sharedPreferences.contains("Id")) {
            String id = sharedPreferences.getString("Id", "");
            String userName = sharedPreferences.getString("UserName", "");
            String passWord = sharedPreferences.getString("PassWord", "");
            String email = sharedPreferences.getString("Email", "");
            String hoten = sharedPreferences.getString("HoTen", "");
            String ngaysinh = sharedPreferences.getString("NgaySinh", "");
            String diachi = sharedPreferences.getString("DiaChi", "");
            String sodienthoai = sharedPreferences.getString("SoDienThoai", "");
            int gioitinh = Integer.parseInt(sharedPreferences.getString("GioiTinh", "0"));
            int tongdiemtichluy = Integer.parseInt(sharedPreferences.getString("TongDiemTichLuy", "0"));
            int diemtichluytheogio = Integer.parseInt(sharedPreferences.getString("DiemTichLuyTheoGio", "0"));
            int thoigiandocsach = Integer.parseInt(sharedPreferences.getString("ThoiGianDocSach", "0"));
            int totalsachdadoc = Integer.parseInt(sharedPreferences.getString("CountSachDaDoc", "0"));
            int totalsachyeuthich = Integer.parseInt(sharedPreferences.getString("CountSachYeuThich", "0"));
            kh = new KhachHang(id, gioitinh, tongdiemtichluy, diemtichluytheogio,
                    hoten, ngaysinh, diachi, sodienthoai, email, userName, passWord, thoigiandocsach);
            kh.setTotalSachDaDoc(totalsachdadoc);
            kh.setTotalSachYeuThich(totalsachyeuthich);
        }
        return kh;
    }

    public int getTotalSachDaDoc() {
        return totalSachDaDoc;
    }

    public void setTotalSachDaDoc(int totalSachDaDoc) {
        this.totalSachDaDoc = totalSachDaDoc;
    }

    public int getTotalSachYeuThich() {
        return totalSachYeuThich;
    }

    public void setTotalSachYeuThich(int totalSachYeuThich) {
        this.totalSachYeuThich = totalSachYeuThich;
    }

    public static void deleteKhachHang() {
        kh = null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getGioitinh() {
        return gioitinh;
    }

    public void setGioitinh(int gioitinh) {
        this.gioitinh = gioitinh;
    }

    public int getTongdiemtichluy() {
        return tongdiemtichluy;
    }

    public void setTongdiemtichluy(int tongdiemtichluy) {
        this.tongdiemtichluy = tongdiemtichluy;
    }

    public int getDiemtichluytheogio() {
        return diemtichluytheogio;
    }

    public void setDiemtichluytheogio(int diemtichluytheogio) {
        this.diemtichluytheogio = diemtichluytheogio;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public String getNgaysinh() {
        return ngaysinh;
    }

    public void setNgaysinh(String ngaysinh) {
        this.ngaysinh = ngaysinh;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getSodienthoai() {
        return sodienthoai;
    }

    public void setSodienthoai(String sodienthoai) {
        this.sodienthoai = sodienthoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getThoigiandocsach() {
        return thoigiandocsach;
    }

    public void setThoigiandocsach(int thoigiandocsach) {
        this.thoigiandocsach = thoigiandocsach;
    }
}
