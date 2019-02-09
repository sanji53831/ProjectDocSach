package com.nguyenminhtri.projectdocsach.viewmodel.thongtintaikhoan;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.nguyenminhtri.projectdocsach.model.OjbectClass.KhachHang;

public class ViewModelThongTinTaiKhoan extends ViewModel {

    Application application;

    MutableLiveData<KhachHang> khachhang;

    public ViewModelThongTinTaiKhoan(Application application) {
        this.application = application;
    }

    public MutableLiveData<KhachHang> getKhachhang() {
        if (khachhang == null) {
            khachhang = new MutableLiveData<>();
            khachhang.setValue(KhachHang.getInstance(application));
        }
        return khachhang;
    }


}
