package com.nguyenminhtri.projectdocsach.presenter.dangnhap;


import android.content.Context;

public interface InterfacePresenterDangNhap {
    Boolean xuLyDangNhap(Context context,String username, String password);
    Boolean xuLyThemUser(Context context,String email, String hoten);
}
