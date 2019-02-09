package com.nguyenminhtri.projectdocsach.model.OjbectClass;

import java.util.ArrayList;

public class ViewDanhGia {
    String toTalDanhGia,avgSoSao;
    ArrayList<DanhGia> listDanhGia;

    public ViewDanhGia() {
        toTalDanhGia = "0";
        avgSoSao = "0";
        listDanhGia = new ArrayList<>();
    }

    public ViewDanhGia(String toTalDanhGia, String avgSoSao, ArrayList<DanhGia> listDanhGia) {
        this.toTalDanhGia = toTalDanhGia;
        this.avgSoSao = avgSoSao;
        this.listDanhGia = listDanhGia;
    }

    public String getToTalDanhGia() {
        return toTalDanhGia;
    }

    public void setToTalDanhGia(String toTalDanhGia) {
        this.toTalDanhGia = toTalDanhGia;
    }

    public String getAvgSoSao() {
        return avgSoSao;
    }

    public void setAvgSoSao(String avgSoSao) {
        this.avgSoSao = avgSoSao;
    }

    public ArrayList<DanhGia> getListDanhGia() {
        return listDanhGia;
    }

    public void setListDanhGia(ArrayList<DanhGia> listDanhGia) {
        this.listDanhGia = listDanhGia;
    }
}
