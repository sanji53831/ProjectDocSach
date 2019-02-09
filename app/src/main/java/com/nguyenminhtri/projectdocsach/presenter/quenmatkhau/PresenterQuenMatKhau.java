package com.nguyenminhtri.projectdocsach.presenter.quenmatkhau;

import com.nguyenminhtri.projectdocsach.model.quenmatkhau.ModelQuenMatKhau;

public class PresenterQuenMatKhau implements InterfacePresenterQuenMatKhau {


    ModelQuenMatKhau modelQuenMatKhau;

    public PresenterQuenMatKhau() {
        modelQuenMatKhau = new ModelQuenMatKhau();
    }

    @Override
    public Boolean thayDoiMatKhau(String email, String matKhauMoi) {
        if (modelQuenMatKhau.thayDoiMatKhau(email, matKhauMoi).equals("success")) {
            return true;
        } else {
            return false;
        }

    }


    @Override
    public Boolean kiemTraEmail(final String email) {
        if (modelQuenMatKhau.kiemTraEmail(email).equals("success")) {
            return true;
        } else return false;
    }
}



