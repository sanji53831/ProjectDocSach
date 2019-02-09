package com.nguyenminhtri.projectdocsach.model.OjbectClass;

public class DanhGia {
    String maDanhGia,maSach,tenUser,tieuDe,noiDung,soSao,ngayDanhGia;

    public DanhGia() {
    }

    public DanhGia(String tenUser, String tieuDe, String noiDung, String soSao, String ngayDanhGia) {
        this.tenUser = tenUser;
        this.tieuDe = tieuDe;
        this.noiDung = noiDung;
        this.soSao = soSao;
        this.ngayDanhGia = ngayDanhGia;
    }

    public DanhGia(String maDanhGia, String maSach, String tenUser, String tieuDe, String noiDung, String soSao) {
        this.maDanhGia = maDanhGia;
        this.maSach = maSach;
        this.tenUser = tenUser;
        this.tieuDe = tieuDe;
        this.noiDung = noiDung;
        this.soSao = soSao;
    }

    public DanhGia(String maDanhGia, String maSach, String tenUser, String tieuDe, String noiDung, String soSao,String ngayDanhGia) {
        this.maDanhGia = maDanhGia;
        this.maSach = maSach;
        this.tenUser = tenUser;
        this.tieuDe = tieuDe;
        this.noiDung = noiDung;
        this.soSao = soSao;
        this.ngayDanhGia = ngayDanhGia;
    }

    public String getMaDanhGia() {
        return maDanhGia;
    }

    public void setMaDanhGia(String maDanhGia) {
        this.maDanhGia = maDanhGia;
    }

    public String getMaSach() {
        return maSach;
    }

    public void setMaSach(String maSach) {
        this.maSach = maSach;
    }

    public String getTenUser() {
        return tenUser;
    }

    public void setTenUser(String tenUser) {
        this.tenUser = tenUser;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getSoSao() {
        return soSao;
    }

    public void setSoSao(String soSao) {
        this.soSao = soSao;
    }

    public String getNgayDanhGia() {
        return ngayDanhGia;
    }

    public void setNgayDanhGia(String ngayDanhGia) {
        this.ngayDanhGia = ngayDanhGia;
    }
}
