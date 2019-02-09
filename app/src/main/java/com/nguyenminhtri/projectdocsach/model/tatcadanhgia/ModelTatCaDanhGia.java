package com.nguyenminhtri.projectdocsach.model.tatcadanhgia;

import com.nguyenminhtri.projectdocsach.connectinternet.ContactService;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.DanhGia;
import com.nguyenminhtri.projectdocsach.savevariables.SaveVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class ModelTatCaDanhGia {

    public ArrayList<DanhGia> getDanhSachDanhGiaByMaSach(String maSach, int limit) {
        ArrayList<DanhGia> list = new ArrayList<>();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("tenham", "GetDanhSachDanhGiaByMaSach");
        hashMap.put("MaSach", maSach);
        hashMap.put("Limit", String.valueOf(limit));

        ContactService contactService = new ContactService(SaveVariables.URL, hashMap);
        contactService.execute();

        try {
            String data = contactService.get();
            JSONArray jsonArray = new JSONArray(data);
            int lenght = jsonArray.length();
            for (int i = 0; i < lenght; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String maDanhGia = jsonObject.getString("MaDanhGia");
                String maSachJson = jsonObject.getString("MaSach");
                String tenUser = jsonObject.getString("TenUser");
                String tieuDe = jsonObject.getString("TieuDe");
                String noiDung = jsonObject.getString("NoiDung");
                String soSao = jsonObject.getString("SoSao");
                String ngayDanhGia = jsonObject.getString("NgayDanhGia");
                DanhGia danhGia = new DanhGia(maDanhGia, maSachJson, tenUser, tieuDe, noiDung, soSao, ngayDanhGia);
                list.add(danhGia);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
