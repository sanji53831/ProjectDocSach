package com.nguyenminhtri.projectdocsach.model.OjbectClass;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Sach implements Parcelable {
    String id,tenSach,noiDung,moTa,hinhAnh,ngayXuatBan,tenTheLoai,tenNhaXuatBan,tenTacGia,gia,trangthai;
    String maTheLoai,maNhaXuatBan,maTacGia,tongsotrang;
    Bitmap bitmap;

    public Sach() {
    }

    public Sach(String id,String tenSach, String hinhAnh, String noiDung, String moTa,
                String ngayXuatBan, String tenTheLoai, String tenNhaXuatBan, String tenTacGia,
                String gia, String trangthai, String maTheLoai,String maNhaXuatBan,String maTacGia,String tongsotrang) {
        this.id = id;
        this.tenSach = tenSach;
        this.hinhAnh = hinhAnh;
        this.noiDung = noiDung;
        this.moTa = moTa;
        this.ngayXuatBan = ngayXuatBan;
        this.tenTheLoai = tenTheLoai;
        this.tenNhaXuatBan = tenNhaXuatBan;
        this.tenTacGia = tenTacGia;
        this.gia = gia;
        this.trangthai = trangthai;
        this.maTheLoai = maTheLoai;
        this.maNhaXuatBan = maNhaXuatBan;
        this.maTacGia = maTacGia;
        this.tongsotrang = tongsotrang;
    }

    public Sach(String id,String tenSach,Bitmap hinhAnh, String noiDung, String moTa,
                String ngayXuatBan, String tenTheLoai, String tenNhaXuatBan, String tenTacGia,
                String gia, String trangthai, String maTheLoai,String maNhaXuatBan,String maTacGia,String tongsotrang) {
        this.id = id;
        this.tenSach = tenSach;
        this.bitmap = hinhAnh;
        this.noiDung = noiDung;
        this.moTa = moTa;
        this.ngayXuatBan = ngayXuatBan;
        this.tenTheLoai = tenTheLoai;
        this.tenNhaXuatBan = tenNhaXuatBan;
        this.tenTacGia = tenTacGia;
        this.gia = gia;
        this.trangthai = trangthai;
        this.maTheLoai = maTheLoai;
        this.maNhaXuatBan = maNhaXuatBan;
        this.maTacGia = maTacGia;
        this.tongsotrang = tongsotrang;
    }

    protected Sach(Parcel in) {
        id = in.readString();
        tenSach = in.readString();
        noiDung = in.readString();
        moTa = in.readString();
        hinhAnh = in.readString();
        ngayXuatBan = in.readString();
        tenTheLoai = in.readString();
        tenNhaXuatBan = in.readString();
        tenTacGia = in.readString();
        gia = in.readString();
        trangthai = in.readString();
        maTheLoai = in.readString();
        maNhaXuatBan = in.readString();
        maTacGia = in.readString();
        tongsotrang = in.readString();
        bitmap = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<Sach> CREATOR = new Creator<Sach>() {
        @Override
        public Sach createFromParcel(Parcel in) {
            return new Sach(in);
        }

        @Override
        public Sach[] newArray(int size) {
            return new Sach[size];
        }
    };

    public String getTongsotrang() {
        return tongsotrang;
    }

    public void setTongsotrang(String tongsotrang) {
        this.tongsotrang = tongsotrang;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getNgayXuatBan() {
        return ngayXuatBan;
    }

    public void setNgayXuatBan(String ngayXuatBan) {
        this.ngayXuatBan = ngayXuatBan;
    }

    public String getTenTheLoai() {
        return tenTheLoai;
    }

    public void setTenTheLoai(String tenTheLoai) {
        this.tenTheLoai = tenTheLoai;
    }

    public String getTenNhaXuatBan() {
        return tenNhaXuatBan;
    }

    public void setTenNhaXuatBan(String tenNhaXuatBan) {
        this.tenNhaXuatBan = tenNhaXuatBan;
    }

    public String getTenTacGia() {
        return tenTacGia;
    }

    public void setTenTacGia(String tenTacGia) {
        this.tenTacGia = tenTacGia;
    }

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }

    public String getTrangthai() {
        return trangthai;
    }

    public void setTrangthai(String trangthai) {
        this.trangthai = trangthai;
    }

    public String getMaTheLoai() {
        return maTheLoai;
    }

    public void setMaTheLoai(String maTheLoai) {
        this.maTheLoai = maTheLoai;
    }

    public String getMaNhaXuatBan() {
        return maNhaXuatBan;
    }

    public void setMaNhaXuatBan(String maNhaXuatBan) {
        this.maNhaXuatBan = maNhaXuatBan;
    }

    public String getMaTacGia() {
        return maTacGia;
    }

    public void setMaTacGia(String maTacGia) {
        this.maTacGia = maTacGia;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(tenSach);
        dest.writeString(noiDung);
        dest.writeString(moTa);
        dest.writeString(hinhAnh);
        dest.writeString(ngayXuatBan);
        dest.writeString(tenTheLoai);
        dest.writeString(tenNhaXuatBan);
        dest.writeString(tenTacGia);
        dest.writeString(gia);
        dest.writeString(trangthai);
        dest.writeString(maTheLoai);
        dest.writeString(maNhaXuatBan);
        dest.writeString(maTacGia);
        dest.writeString(tongsotrang);
        dest.writeParcelable(bitmap, flags);
    }
}
