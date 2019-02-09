package com.nguyenminhtri.projectdocsach.presenter.vietdanhgia;

import com.nguyenminhtri.projectdocsach.model.OjbectClass.DanhGia;
import com.nguyenminhtri.projectdocsach.model.vietdanhgia.ModelVietDanhGia;
import com.nguyenminhtri.projectdocsach.presenter.vietdanhgia.InterfacePresenterVietDanhGia;
import com.nguyenminhtri.projectdocsach.view.vietdanhgia.InterfaceViewVietDanhGia;

public class PresenterVietDanhGia implements InterfacePresenterVietDanhGia {

    ModelVietDanhGia modelVietDanhGia;

    public PresenterVietDanhGia() {
        modelVietDanhGia = new ModelVietDanhGia();
    }


    @Override
    public Boolean xuLyVietDanhGia(DanhGia danhGia) {
        if (modelVietDanhGia.sendDanhGiaToServer(danhGia).equals("success")) {
            return true;
        } else {
            return false;
        }
    }
}
