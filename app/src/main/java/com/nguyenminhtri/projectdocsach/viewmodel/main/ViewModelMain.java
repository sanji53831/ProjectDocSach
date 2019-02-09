package com.nguyenminhtri.projectdocsach.viewmodel.main;


import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;


import com.nguyenminhtri.projectdocsach.model.OjbectClass.ViewBook;
import com.nguyenminhtri.projectdocsach.savevariables.SaveVariables;


import com.nguyenminhtri.projectdocsach.model.main.ModelMain;
import com.nguyenminhtri.projectdocsach.view.main.MainActivity;


public class ViewModelMain extends ViewModel {

    MutableLiveData<ViewBook> viewListTacPhamKinhDien;
    MutableLiveData<ViewBook> viewListKyNangNgheThuatSong;
    MutableLiveData<ViewBook> viewListVanHoaNgheThuat;
    MutableLiveData<ViewBook> viewListKinhDoanh;
    MutableLiveData<Boolean> checkInternet;

    ModelMain modelMain = new ModelMain();

    public MutableLiveData<Boolean> getCheckInternet() {
        if (checkInternet == null) {
            checkInternet = new MutableLiveData<>();
            checkInternet.setValue(MainActivity.stateInternet);
        }
        return checkInternet;
    }

    public MutableLiveData<ViewBook> getViewListTacPhamKinhDien() {
        if (viewListTacPhamKinhDien == null) {
            viewListTacPhamKinhDien = new MutableLiveData<>();
            viewListTacPhamKinhDien.postValue(modelMain.getListSachByCatelogy1(SaveVariables.maTacPhamKinhDien, 0));
        }
        return viewListTacPhamKinhDien;
    }

    public MutableLiveData<ViewBook> getViewListKyNangNgheThuatSong() {
        if (viewListKyNangNgheThuatSong == null) {
            viewListKyNangNgheThuatSong = new MutableLiveData<>();
            viewListKyNangNgheThuatSong.postValue(modelMain.getListSachByCatelogy1(SaveVariables.maKyNangNgheThuatSong, 0));
        }
        return viewListKyNangNgheThuatSong;
    }

    public MutableLiveData<ViewBook> getViewListVanHoaNgheThuat() {
        if (viewListVanHoaNgheThuat == null) {
            viewListVanHoaNgheThuat = new MutableLiveData<>();
            viewListVanHoaNgheThuat.postValue(modelMain.getListSachByCatelogy1(SaveVariables.maVanHoaNgheThuat, 0));
        }
        return viewListVanHoaNgheThuat;
    }

    public MutableLiveData<ViewBook> getViewListKinhDoanh() {
        if (viewListKinhDoanh == null) {
            viewListKinhDoanh = new MutableLiveData<>();
            viewListKinhDoanh.postValue(modelMain.getListSachByCatelogy1(SaveVariables.maKinhDoanh, 0));
        }
        return viewListKinhDoanh;
    }

}
