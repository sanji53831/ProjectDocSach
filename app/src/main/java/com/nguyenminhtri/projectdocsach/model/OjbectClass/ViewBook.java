package com.nguyenminhtri.projectdocsach.model.OjbectClass;

import java.util.ArrayList;

public class ViewBook {
    private int totalNumberSach;
    private ArrayList<Sach> listSach;

    public ViewBook() {
    }

    public ViewBook(int totalNumberSach, ArrayList<Sach> listSach) {
        this.totalNumberSach = totalNumberSach;
        this.listSach = listSach;
    }

    public int getTotalNumberSach() {
        return totalNumberSach;
    }

    public void setTotalNumberSach(int totalNumberSach) {
        this.totalNumberSach = totalNumberSach;
    }

    public ArrayList<Sach> getListSach() {
        return listSach;
    }

    public void setListSach(ArrayList<Sach> listSach) {
        this.listSach = listSach;
    }
}
