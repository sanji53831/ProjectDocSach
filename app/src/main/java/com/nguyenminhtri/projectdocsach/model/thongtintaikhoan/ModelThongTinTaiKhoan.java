package com.nguyenminhtri.projectdocsach.model.thongtintaikhoan;

import android.content.Context;
import android.content.SharedPreferences;

import com.nguyenminhtri.projectdocsach.connectinternet.ContactService;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.KhachHang;
import com.nguyenminhtri.projectdocsach.savevariables.SaveVariables;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class ModelThongTinTaiKhoan {

    public String capNhatThongTinTaiKhoan(String userId, String hoten, String ngaysinh, String diachi, int gioitinh) {
        String data = null;

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("tenham", "CapNhatThongTinTaiKhoan");
        hashMap.put("Id", userId);
        hashMap.put("HoTen", hoten);
        hashMap.put("NgaySinh", ngaysinh);
        hashMap.put("DiaChi", diachi);
        hashMap.put("GioiTinh", String.valueOf(gioitinh));

        ContactService contactService = new ContactService(SaveVariables.URL, hashMap);
        contactService.execute();

        try {
            data = contactService.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return data;
    }

    public String thayDoiMatKhau(String userId, String matKhau) {
        String data = null;

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("tenham", "DoiMatKhau");
        hashMap.put("Id", userId);
        hashMap.put("NewPassWord", matKhau);

        ContactService contactService = new ContactService(SaveVariables.URL, hashMap);
        contactService.execute();

        try {
            data = contactService.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return data;
    }

    public String thayDoiSoDienThoai(String userId, String soDienThoai) {
        String data = null;

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("tenham", "DoiSoDienThoai");
        hashMap.put("Id", userId);
        hashMap.put("SoDienThoai", soDienThoai);

        ContactService contactService = new ContactService(SaveVariables.URL, hashMap);
        contactService.execute();

        try {
            data = contactService.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return data;
    }

    public String thayDoiEmail(String userId, String email) {
        String data = null;

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("tenham", "DoiEmail");
        hashMap.put("Id", userId);
        hashMap.put("Email", email);

        ContactService contactService = new ContactService(SaveVariables.URL, hashMap);
        contactService.execute();

        try {
            data = contactService.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return data;
    }

    public void updateSharePreference(Context context, KhachHang khachHang) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("infouser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Id", khachHang.getId());
        editor.putString("UserName", khachHang.getUsername());
        editor.putString("PassWord", khachHang.getPassword());
        editor.putString("Email", khachHang.getEmail());
        editor.putString("HoTen", khachHang.getHoten());
        editor.putString("GioiTinh", String.valueOf(khachHang.getGioitinh()));
        editor.putString("NgaySinh", khachHang.getNgaysinh());
        editor.putString("DiaChi", khachHang.getDiachi());
        editor.putString("SoDienThoai", khachHang.getSodienthoai());
        editor.putString("TongDiemTichLuy", String.valueOf(khachHang.getTongdiemtichluy()));
        editor.putString("DiemTichLuyTheoGio", String.valueOf(khachHang.getDiemtichluytheogio()));
        editor.putString("ThoiGianDocSach", String.valueOf(khachHang.getThoigiandocsach()));
        editor.putString("CountSachDaDoc", String.valueOf(khachHang.getTotalSachDaDoc()));
        editor.putString("CountSachYeuThich", String.valueOf(khachHang.getTotalSachYeuThich()));
        editor.apply();
    }

    public void deleteSharePreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("infouser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
