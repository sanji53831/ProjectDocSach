package com.nguyenminhtri.projectdocsach.presenter.dangky;

import android.content.Context;

public interface InterfacePresenterDangKy {

    public Boolean xuLyDangKy(String username, String password, String email,String hoten);
    public Boolean xuLyTrungUserName(String username);
    public Boolean xuLyTrungEmail(String email);
    public Boolean kiemTraDieuKienDangKy(String username, String password, String repassword, String email);
}
