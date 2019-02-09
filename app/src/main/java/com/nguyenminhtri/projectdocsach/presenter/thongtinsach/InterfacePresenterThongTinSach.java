package com.nguyenminhtri.projectdocsach.presenter.thongtinsach;

import com.nguyenminhtri.projectdocsach.model.OjbectClass.ViewDanhGia;
import com.nguyenminhtri.projectdocsach.viewmodel.thongtinsach.ViewModelThongTinSach;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.Sach;

public interface InterfacePresenterThongTinSach {
     void InitButtonDocSach(Sach sach);
     void deleteSachYeuThich(String userId,String maSach);
     void saveSachYeuThich(String userId,String maSach);
     ViewDanhGia getThreeDanhGiaByMaSach(String maSach);
     Boolean getValueCheckSachYeuThich(String userId,String maSach);
}
