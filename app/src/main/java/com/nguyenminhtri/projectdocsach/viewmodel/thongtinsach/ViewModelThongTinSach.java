package com.nguyenminhtri.projectdocsach.viewmodel.thongtinsach;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;


import com.nguyenminhtri.projectdocsach.model.OjbectClass.ChiTietSachYeuThich;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.DanhGia;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.Sach;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.ViewDanhGia;
import com.nguyenminhtri.projectdocsach.model.main.ModelMain;
import com.nguyenminhtri.projectdocsach.model.thongtinsach.ModelThongTinSach;
import com.nguyenminhtri.projectdocsach.savevariables.SaveVariables;

import java.util.ArrayList;

public class ViewModelThongTinSach extends ViewModel {

    MutableLiveData<ArrayList<Sach>> listTacPhamKinhDien;
    MutableLiveData<ArrayList<Sach>> listKyNangNgheThuatSong;
    MutableLiveData<ArrayList<Sach>> listVanHoaNgheThuat;
    MutableLiveData<ArrayList<Sach>> listKinhDoanh;

    MutableLiveData<ArrayList<ChiTietSachYeuThich>> listSachYeuThich;
    MutableLiveData<Boolean> checkSachYeuThich;
    MutableLiveData<ViewDanhGia> viewDanhGiaByMaSach;

    ModelThongTinSach modelThongTinSach = new ModelThongTinSach();
    ModelMain modelMain = new ModelMain();

    public MutableLiveData<ArrayList<ChiTietSachYeuThich>> getListSachYeuThich(String userId) {
        if (listSachYeuThich == null) {
            listSachYeuThich = new MutableLiveData<>();
            listSachYeuThich.postValue(modelThongTinSach.getListSachYeuThichFromServer(userId));
        }
        return listSachYeuThich;
    }

    public void insertSachYeuThich(ChiTietSachYeuThich chiTietSachYeuThich) {
        listSachYeuThich.getValue().add(chiTietSachYeuThich);
    }

    public void deleteSachYeuThich(ChiTietSachYeuThich chiTietSachYeuThich) {
        listSachYeuThich.getValue().remove(chiTietSachYeuThich);
    }

    public MutableLiveData<Boolean> getValueCheckSachYeuThich(String userId,String maSach) {
        if (checkSachYeuThich == null) {
            checkSachYeuThich = new MutableLiveData<>();
            checkSachYeuThich.postValue(modelThongTinSach.checkSachYeuThichFromServer(userId,maSach));
        }
        return checkSachYeuThich;
    }


    public MutableLiveData<ViewDanhGia> getViewDanhGiaByMaSach(String maSach) {
        if (viewDanhGiaByMaSach == null) {
            viewDanhGiaByMaSach = new MutableLiveData<>();
            viewDanhGiaByMaSach.postValue(modelThongTinSach.getThreeDanhGiaByMaSach(maSach));
        }
        return viewDanhGiaByMaSach;
    }

    public MutableLiveData<ArrayList<Sach>> getListTacPhamKinhDien() {
        if (listTacPhamKinhDien == null) {
            listTacPhamKinhDien = new MutableLiveData<>();
            listTacPhamKinhDien.postValue(modelMain.getListSachByCatelogy(SaveVariables.maTacPhamKinhDien, 0));
        }
        return listTacPhamKinhDien;
    }

    public MutableLiveData<ArrayList<Sach>> getListKyNangNgheThuatSong() {
        if (listKyNangNgheThuatSong == null) {
            listKyNangNgheThuatSong = new MutableLiveData<>();
            listKyNangNgheThuatSong.postValue(modelMain.getListSachByCatelogy(SaveVariables.maKyNangNgheThuatSong, 0));
        }
        return listKyNangNgheThuatSong;
    }

    public MutableLiveData<ArrayList<Sach>> getListVanHoaNgheThuat() {
        if (listVanHoaNgheThuat == null) {
            listVanHoaNgheThuat = new MutableLiveData<>();
            listVanHoaNgheThuat.postValue(modelMain.getListSachByCatelogy(SaveVariables.maVanHoaNgheThuat, 0));
        }
        return listVanHoaNgheThuat;
    }

    public MutableLiveData<ArrayList<Sach>> getListKinhDoanh() {
        if (listKinhDoanh == null) {
            listKinhDoanh = new MutableLiveData<>();
            listKinhDoanh.postValue(modelMain.getListSachByCatelogy(SaveVariables.maKinhDoanh, 0));
        }
        return listKinhDoanh;
    }
}
