package com.nguyenminhtri.projectdocsach.presenter.tatcadanhgia;

import com.nguyenminhtri.projectdocsach.model.OjbectClass.DanhGia;

import java.util.ArrayList;

public interface InterfacePresenterTatCaDanhGia {
    public ArrayList<DanhGia> getListDanhGiaByMaSach(String maSach,int limit);
}
