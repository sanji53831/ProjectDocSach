package com.nguyenminhtri.projectdocsach.model.thongtinsach;

import android.content.Context;
import android.os.Environment;

import com.nguyenminhtri.projectdocsach.connectinternet.ContactService;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.DanhGia;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.ViewDanhGia;
import com.nguyenminhtri.projectdocsach.savevariables.SaveVariables;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.ChiTietSachYeuThich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class ModelThongTinSach {

    public Boolean saveSachYeuThich(String maUser, String maSach) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("tenham", "SaveChiTietSachYeuThichByUser");
        hashMap.put("UserId", maUser);
        hashMap.put("MaSach", maSach);

        ContactService contactService = new ContactService(SaveVariables.URL, hashMap);
        contactService.execute();

        try {
            String data = contactService.get();
            if (data.equals("success")) {
                return true;
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean deleteSachYeuThich(String maUser, String maSach) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("tenham", "DeleteChiTietSachYeuThichByUser");
        hashMap.put("UserId", maUser);
        hashMap.put("MaSach", maSach);

        ContactService contactService = new ContactService(SaveVariables.URL, hashMap);
        contactService.execute();
        try {
            String data = contactService.get();
            if (data.equals("success")) {
                return true;
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    public ArrayList<ChiTietSachYeuThich> getListSachYeuThichFromServer(String maUser) {
        ArrayList<ChiTietSachYeuThich> list = new ArrayList<>();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("tenham", "GetChiTietSachYeuThichByUser");
        hashMap.put("UserId", maUser);

        ContactService contactService = new ContactService(SaveVariables.URL, hashMap);
        contactService.execute();
        try {
            String data = contactService.get();
            JSONArray jsonArray = new JSONArray(data);
            int lenght = jsonArray.length();
            for (int i = 0; i < lenght; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String userid = jsonObject.getString("UserId");
                String masach = jsonObject.getString("MaSach");
                ChiTietSachYeuThich chiTietSachYeuThich = new ChiTietSachYeuThich(masach, userid);
                list.add(chiTietSachYeuThich);
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

    public Boolean checkSachYeuThichFromServer(String maUser, String maSach) {
        ArrayList<ChiTietSachYeuThich> list = new ArrayList<>();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("tenham", "KiemTraChiTietSachYeuThichByUser");
        hashMap.put("UserId", maUser);
        hashMap.put("MaSach", maSach);

        ContactService contactService = new ContactService(SaveVariables.URL, hashMap);
        contactService.execute();
        try {
            String data = contactService.get();
            if (data.equals("success")) {
                return true;
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ViewDanhGia getThreeDanhGiaByMaSach(String maSach) {
        ViewDanhGia viewDanhGia = new ViewDanhGia();
        ArrayList<DanhGia> listDanhGia = new ArrayList<>();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("tenham", "GetThreeDanhGiaByMaSach");
        hashMap.put("MaSach", maSach);

        ContactService contactService = new ContactService(SaveVariables.URL, hashMap);
        contactService.execute();

        try {
            String data = contactService.get();
            JSONArray jsonArray = new JSONArray(data);
            int lenght = jsonArray.length();
            if (lenght != 0) {
                for (int i = 1; i < lenght; i += 2) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String tenUser = jsonObject.getString("TenUser");
                    String tieuDe = jsonObject.getString("TieuDe");
                    String noiDung = jsonObject.getString("NoiDung");
                    String soSao = jsonObject.getString("SoSao");
                    String ngayDanhGia = jsonObject.getString("NgayDanhGia");
                    DanhGia danhGia = new DanhGia(tenUser, tieuDe, noiDung, soSao, ngayDanhGia);
                    listDanhGia.add(danhGia);
                }
                JSONObject object = jsonArray.getJSONObject(0);
                String totalDanhGia = object.getString("count");
                String avgSoSao = object.getString("numstart");
                viewDanhGia.setToTalDanhGia(totalDanhGia);
                viewDanhGia.setAvgSoSao(avgSoSao);
                viewDanhGia.setListDanhGia(listDanhGia);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return viewDanhGia;
    }

}
