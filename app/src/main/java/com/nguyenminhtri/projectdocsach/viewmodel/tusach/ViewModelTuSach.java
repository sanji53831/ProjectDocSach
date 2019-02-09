package com.nguyenminhtri.projectdocsach.viewmodel.tusach;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.nguyenminhtri.projectdocsach.database.Database;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.ChiTietSachDaDoc;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.KhachHang;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.Sach;
import com.nguyenminhtri.projectdocsach.model.tusach.ModelTuSach;
import com.nguyenminhtri.projectdocsach.presenter.tusach.PresenterTuSach;

import java.util.ArrayList;

public class ViewModelTuSach extends ViewModel {

    MutableLiveData<ArrayList<Sach>> listSachDaDoc;
    MutableLiveData<ArrayList<Sach>> listSachYeuThich;

    public ViewModelTuSach() {
        listSachDaDoc = new MutableLiveData<>();
        listSachYeuThich = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<Sach>> getListSachDaDoc() {
        return listSachDaDoc;
    }

    public void setListSachDaDoc(ArrayList<Sach> listSachDaDoc) {
        this.listSachDaDoc.setValue(listSachDaDoc);
    }

    public MutableLiveData<ArrayList<Sach>> getListSachYeuThich() {
        return listSachYeuThich;
    }

    public void setListSachYeuThich(ArrayList<Sach> listSachYeuThich) {
        this.listSachYeuThich.setValue(listSachYeuThich);
    }

    //    public MutableLiveData<ArrayList<Sach>> getListSachDaDoc() {
//        if (listSachDaDoc == null) {
//            listSachDaDoc = new MutableLiveData<>();
//            listSachDaDoc.postValue(database.getListSachDaDoc(kh.getId()));
//        }
//        return listSachDaDoc;
//    }
//
//    public MutableLiveData<ArrayList<Sach>> getListSachYeuThich() {
//        if (listSachYeuThich == null) {
//            listSachYeuThich = new MutableLiveData<>();
//            listSachYeuThich.postValue(database.getListSachYeuThich(kh.getId()));
//        }
//        return listSachYeuThich;
//    }

}
