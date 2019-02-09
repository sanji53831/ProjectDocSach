package com.nguyenminhtri.projectdocsach.presenter.tusach;


import android.util.Log;

import com.nguyenminhtri.projectdocsach.model.OjbectClass.ChiTietSachDaDoc;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.ChiTietSachYeuThich;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.Sach;
import com.nguyenminhtri.projectdocsach.model.tusach.ModelTuSach;
import com.nguyenminhtri.projectdocsach.savevariables.SaveVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PresenterTuSach implements InterfacePresenterTuSach {

    ModelTuSach modelTuSach;

    public PresenterTuSach() {
        this.modelTuSach = new ModelTuSach();
    }

    @Override
    public ArrayList<ChiTietSachDaDoc> getListSachDaDocFromServer(String userId) {
        ArrayList<ChiTietSachDaDoc> list = new ArrayList<>();
        String data = modelTuSach.getJSONSachDaDocByUser(userId);
        try {
            JSONArray jsonArray = new JSONArray(data);
            int lenght = jsonArray.length();
            if (lenght > 0) {
                for (int i = 0; i < lenght; i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String IdUser = object.getString("UserId");
                    String maSach = object.getString("MaSach");
                    ChiTietSachDaDoc chiTietSachDaDoc = new ChiTietSachDaDoc(maSach, IdUser);
                    list.add(chiTietSachDaDoc);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public ArrayList<ChiTietSachYeuThich> getListSachYeuThichFromServer(String userId) {
        ArrayList<ChiTietSachYeuThich> list = new ArrayList<>();
        String data = modelTuSach.getJSONSachYeuThichByUser(userId);
        try {
            JSONArray jsonArray = new JSONArray(data);
            int lenght = jsonArray.length();
            if (lenght > 0) {
                for (int i = 0; i < lenght; i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String IdUser = object.getString("UserId");
                    String maSach = object.getString("MaSach");
                    ChiTietSachYeuThich chiTietSachYeuThich = new ChiTietSachYeuThich(maSach, IdUser);
                    list.add(chiTietSachYeuThich);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public Sach getSachByMaSach(String maSach) {
        Sach sach = null;
        String data = modelTuSach.getJSONSachByMaSach(maSach);
        try {
            JSONArray jsonArray = new JSONArray(data);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String tenSach = jsonObject.getString("TenSach");
            String gia = jsonObject.getString("Gia");
            String duongDanHinhAnh = SaveVariables.BeforeURL + jsonObject.getString("DuongDanHinhAnh");
            String duongDanNoiDung = SaveVariables.BeforeURL + jsonObject.getString("DuongDanNoiDung");
            String moTa = jsonObject.getString("MoTa");
            String tenTheLoai = jsonObject.getString("TenTheLoai");
            String tenNhaXuatBan = jsonObject.getString("TenNhaXuatBan");
            String tenTacGia = jsonObject.getString("TenTacGia");
            String ngayXuatBan = jsonObject.getString("NgayXuatBan");
            String trangThai = jsonObject.getString("TrangThai");
            String maTheLoai = jsonObject.getString("MaTheLoai");
            String maNhaXuatBan = jsonObject.getString("MaNhaXuatBan");
            String maTacGia = jsonObject.getString("MaTacGia");
            String tongSoTrang = jsonObject.getString("TongSoTrang");
            sach = new Sach(maSach, tenSach, duongDanHinhAnh, duongDanNoiDung, moTa, ngayXuatBan, tenTheLoai, tenNhaXuatBan,
                    tenTacGia, gia, trangThai, maTheLoai, maNhaXuatBan, maTacGia, tongSoTrang);
            return sach;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sach;
    }
}
