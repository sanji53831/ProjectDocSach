package com.nguyenminhtri.projectdocsach.presenter.tusach;

import com.nguyenminhtri.projectdocsach.model.OjbectClass.ChiTietSachDaDoc;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.ChiTietSachYeuThich;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.Sach;

import java.util.ArrayList;

public interface InterfacePresenterTuSach {
    ArrayList<ChiTietSachDaDoc> getListSachDaDocFromServer(String userId);
    ArrayList<ChiTietSachYeuThich> getListSachYeuThichFromServer(String userId);
    Sach getSachByMaSach(String maSach);
}
