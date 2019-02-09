package com.nguyenminhtri.projectdocsach.presenter.tatcadanhgia;

import com.nguyenminhtri.projectdocsach.model.OjbectClass.DanhGia;
import com.nguyenminhtri.projectdocsach.model.tatcadanhgia.ModelTatCaDanhGia;
import com.nguyenminhtri.projectdocsach.model.vietdanhgia.ModelVietDanhGia;
import com.nguyenminhtri.projectdocsach.presenter.vietdanhgia.InterfacePresenterVietDanhGia;

import java.util.ArrayList;

public class PresenterTatCaDanhGia implements InterfacePresenterTatCaDanhGia {

    ModelTatCaDanhGia modelTatCaDanhGia;

    public PresenterTatCaDanhGia() {
        modelTatCaDanhGia = new ModelTatCaDanhGia();
    }



    @Override
    public ArrayList<DanhGia> getListDanhGiaByMaSach(String maSach, int limit) {
        return modelTatCaDanhGia.getDanhSachDanhGiaByMaSach(maSach,limit);
    }
}
