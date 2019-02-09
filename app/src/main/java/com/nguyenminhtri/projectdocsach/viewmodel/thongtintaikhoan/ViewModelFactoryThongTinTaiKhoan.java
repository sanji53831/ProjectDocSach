package com.nguyenminhtri.projectdocsach.viewmodel.thongtintaikhoan;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.nguyenminhtri.projectdocsach.viewmodel.thongtintaikhoan.ViewModelThongTinTaiKhoan;


public class ViewModelFactoryThongTinTaiKhoan implements ViewModelProvider.Factory {

    Application application;

    public ViewModelFactoryThongTinTaiKhoan(Application application) {
        this.application = application;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ViewModelThongTinTaiKhoan.class)) {
            return (T) new ViewModelThongTinTaiKhoan(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
