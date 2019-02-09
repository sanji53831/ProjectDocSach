package com.nguyenminhtri.projectdocsach.presenter.timkim;

import com.nguyenminhtri.projectdocsach.model.OjbectClass.Sach;
import com.nguyenminhtri.projectdocsach.model.timkim.ModelTimKim;
import com.nguyenminhtri.projectdocsach.savevariables.SaveVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PresenterTimKim implements InterfacePresenterTimKim {

    ModelTimKim modelTimKim;

    public PresenterTimKim() {
        modelTimKim = new ModelTimKim();
    }

    @Override
    public ArrayList<Sach> getListSachTimKim(String tenSach) {
        ArrayList<Sach> listSach = new ArrayList<>();
        String data = modelTimKim.getSachByTenSach(tenSach);
        if (data != null) {
            try {
                JSONArray jsonArray = new JSONArray(data);
                int lenght = jsonArray.length();
                for (int i = 0; i < lenght; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String maSach = jsonObject.getString("MaSach");
                    String tenSach1 = jsonObject.getString("TenSach");
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
                    Sach sach = new Sach(maSach, tenSach1, duongDanHinhAnh, duongDanNoiDung, moTa, ngayXuatBan, tenTheLoai, tenNhaXuatBan,
                            tenTacGia, gia, trangThai, maTheLoai, maNhaXuatBan, maTacGia, tongSoTrang);
                    listSach.add(sach);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return listSach;
    }
}
