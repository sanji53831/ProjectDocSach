package com.nguyenminhtri.projectdocsach.presenter.dangnhap;


import android.content.Context;
import android.util.Log;

import com.nguyenminhtri.projectdocsach.model.dangnhap.ModelDangNhap;
import com.nguyenminhtri.projectdocsach.view.dangnhap.InterfaceViewDangNhap;

import java.util.Date;

public class PresenterDangNhap implements InterfacePresenterDangNhap {

    ModelDangNhap modelDangNhap;
    InterfaceViewDangNhap interfaceViewDangNhap;

    public PresenterDangNhap(InterfaceViewDangNhap interfaceViewDangNhap) {
        this.interfaceViewDangNhap = interfaceViewDangNhap;
        modelDangNhap = new ModelDangNhap();
    }

    @Override
    public Boolean xuLyDangNhap(Context context, String username, String password) {
        if (modelDangNhap.kiemTraDangNhap(username, password).equals("success")) {
            modelDangNhap.saveUserByUserName(context, username);
            return true;
        } else
            return false;
    }

    @Override
    public Boolean xuLyThemUser(Context context, String email, String hoten) {
        if (!modelDangNhap.kiemTraEmail(email)) {
            String id = "U" + String.valueOf(new Date().getTime());
            String username = email.substring(0, email.indexOf("@"));
            String password = "!@#$%^&";
            if (modelDangNhap.themKhacHang(id, username, password, email, hoten).equals("success")) {
                modelDangNhap.saveUserByEmail(context, id, hoten, email, username, password);
                return true;
            } else return false;
        } else {
            modelDangNhap.saveUserByEmailFromServer(context, email);
            return true;
        }
    }

}
