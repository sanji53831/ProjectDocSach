package com.nguyenminhtri.projectdocsach.presenter.dangky;

import android.util.Patterns;

import com.nguyenminhtri.projectdocsach.model.dangky.ModelDangKy;
import com.nguyenminhtri.projectdocsach.view.dangky.InterfaceViewDangKy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PresenterDangKy implements InterfacePresenterDangKy {

    InterfaceViewDangKy interfaceViewDangKy;
    ModelDangKy modelDangKy;

    public PresenterDangKy(InterfaceViewDangKy interfaceViewDangKy) {
        this.interfaceViewDangKy = interfaceViewDangKy;
        modelDangKy = new ModelDangKy();
    }

    @Override
    public Boolean xuLyDangKy(String username, String password, String email, String hoten) {
        if (modelDangKy.themKhacHang(username, password, email, hoten).equals("success")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean xuLyTrungUserName(String username) {
        if (modelDangKy.kiemTraUserName(username).equals("success")) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public Boolean xuLyTrungEmail(String email) {
        if (modelDangKy.kiemTraEmail(email).equals("success")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean kiemTraDieuKienDangKy(String username, String password, String repassword, String email) {

        if (username.equals("")) {
            interfaceViewDangKy.annouceUsernameEmpty();
            return false;
        }

        if (username.length() < 5) {
            interfaceViewDangKy.annouceUsernameShort();
            return false;
        }

        if (password.equals("")) {
            interfaceViewDangKy.annoucePasswordEmpty();
            return false;
        }

//        String PATTERN_REGEX_PASSWORD = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,50})";
//        Pattern pattern = Pattern.compile(PATTERN_REGEX_PASSWORD);
//        Matcher matcher = pattern.matcher(password);
//        if (!matcher.matches()) {
//            interfaceViewDangKy.annoucePasswordInvalid();
//            return false;
//        }


        if (!password.equals(repassword)) {
            interfaceViewDangKy.annouceRePasswordEmpty();
            return false;
        }

        if (email.equals("")) {
            interfaceViewDangKy.annouceEmailEmpty();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            interfaceViewDangKy.annouceEmailInvalid();
            return false;
        }

        return true;
    }
}
