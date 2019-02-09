package com.nguyenminhtri.projectdocsach.presenter.thongtinsach;

import com.nguyenminhtri.projectdocsach.model.OjbectClass.ViewDanhGia;
import com.nguyenminhtri.projectdocsach.viewmodel.thongtinsach.ViewModelThongTinSach;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.ChiTietSachYeuThich;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.Sach;
import com.nguyenminhtri.projectdocsach.model.thongtinsach.ModelThongTinSach;
import com.nguyenminhtri.projectdocsach.view.thongtinsach.InterfaceViewThongTinSach;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PresenterThongTinSach implements InterfacePresenterThongTinSach {
    ModelThongTinSach modelThongTinSach;
    InterfaceViewThongTinSach interfaceViewThongTinSach;

    public PresenterThongTinSach(InterfaceViewThongTinSach interfaceViewThongTinSach) {
        modelThongTinSach = new ModelThongTinSach();
        this.interfaceViewThongTinSach = interfaceViewThongTinSach;
    }


    @Override
    public void InitButtonDocSach(Sach sach) {
        if (sach.getTrangthai().equals("0")) {
            interfaceViewThongTinSach.showButtonDocSachMienPhi();
        } else {
            interfaceViewThongTinSach.showButtonMuaSach();
        }
    }

    @Override
    public void deleteSachYeuThich(String userId, String maSach) {
        modelThongTinSach.deleteSachYeuThich(userId, maSach);
    }

    @Override
    public void saveSachYeuThich(String userId, String maSach) {
        modelThongTinSach.saveSachYeuThich(userId, maSach);
    }

    @Override
    public ViewDanhGia getThreeDanhGiaByMaSach(String maSach) {
        return modelThongTinSach.getThreeDanhGiaByMaSach(maSach);
    }

    @Override
    public Boolean getValueCheckSachYeuThich(String userId, String maSach) {
        return modelThongTinSach.checkSachYeuThichFromServer(userId,maSach);
    }

}
