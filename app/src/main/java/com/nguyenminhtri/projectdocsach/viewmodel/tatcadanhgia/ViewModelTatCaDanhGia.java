package com.nguyenminhtri.projectdocsach.viewmodel.tatcadanhgia;


import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.nguyenminhtri.projectdocsach.model.OjbectClass.DanhGia;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.ViewBook;
import com.nguyenminhtri.projectdocsach.model.main.ModelMain;
import com.nguyenminhtri.projectdocsach.model.tatcadanhgia.ModelTatCaDanhGia;
import com.nguyenminhtri.projectdocsach.savevariables.SaveVariables;

import java.util.ArrayList;


public class ViewModelTatCaDanhGia extends ViewModel {

    ModelTatCaDanhGia modelTatCaDanhGia = new ModelTatCaDanhGia();

    MutableLiveData<ArrayList<DanhGia>> listDanhGiaByMaSach;


    public MutableLiveData<ArrayList<DanhGia>> getListDanhGiaByMaSach(String maSach) {
        if (listDanhGiaByMaSach == null) {
            listDanhGiaByMaSach = new MutableLiveData<>();
            listDanhGiaByMaSach.postValue(modelTatCaDanhGia.getDanhSachDanhGiaByMaSach(maSach, 0));
        }
        return listDanhGiaByMaSach;
    }

}
