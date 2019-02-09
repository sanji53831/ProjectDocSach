package com.nguyenminhtri.projectdocsach.model.dangnhap;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.nguyenminhtri.projectdocsach.connectinternet.ContactService;
import com.nguyenminhtri.projectdocsach.savevariables.SaveVariables;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;


public class ModelDangNhap {

    public GoogleSignInClient getGoogleSignInClient(Context context) {
        GoogleSignInClient mGoogleSignInClient;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
        return mGoogleSignInClient;
    }


    public String kiemTraDangNhap(String username, String password) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("tenham", "KiemTraDangNhap");
        hashMap.put("UserName", username);
        hashMap.put("PassWord", password);

        ContactService contactService = new ContactService(SaveVariables.URL, hashMap);
        contactService.execute();

        try {
            String data = contactService.get();
            return data;

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String themKhacHang(String id, String username, String password, String email, String hoten) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("tenham", "DangKy");
        hashMap.put("Id", id);
        hashMap.put("UserName", username);
        hashMap.put("PassWord", password);
        hashMap.put("Email", email);
        hashMap.put("HoTen", hoten);
        ContactService contactService = new ContactService(SaveVariables.URL, hashMap);
        contactService.execute();
        try {
            String data = contactService.get();
            return data;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveUserByEmail(Context context, String id, String hoten, String email, String username, String password) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("infouser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Id", id);
        editor.putString("UserName", username);
        editor.putString("PassWord", password);
        editor.putString("Email", email);
        editor.putString("HoTen", hoten);
        editor.putString("GioiTinh", "0");
        editor.putString("NgaySinh", "");
        editor.putString("DiaChi", "");
        editor.putString("SoDienThoai", "");
        editor.putString("TongDiemTichLuy", "0");
        editor.putString("DiemTichLuyTheoGio", "0");
        editor.putString("ThoiGianDocSach", "0");
        editor.putString("CountSachDaDoc", "0");
        editor.putString("CountSachYeuThich", "0");
        editor.apply();
    }

    public void saveUserByEmailFromServer(Context context,String email) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("tenham", "GetUserByEmail");
        hashMap.put("Email", email);
        ContactService contactService = new ContactService(SaveVariables.URL, hashMap);
        contactService.execute();

        try {
            String data = contactService.get();
            updateCacheDangNhap(context, data);

        } catch (ExecutionException e) {

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void saveUserByUserName(Context context, String username) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("tenham", "GetUserByUserName");
        hashMap.put("UserName", username);

        ContactService contactService = new ContactService(SaveVariables.URL, hashMap);
        contactService.execute();

        try {
            String data = contactService.get();
            updateCacheDangNhap(context, data);

        } catch (ExecutionException e) {

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void updateCacheDangNhap(Context context, String data) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("infouser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            JSONArray jsonArray = new JSONArray(data);
            JSONObject object0 = jsonArray.getJSONObject(0);
            JSONObject object = jsonArray.getJSONObject(1);
            String id = object.getString("Id");
            String userName = object.getString("UserName");
            String passWord = object.getString("PassWord");
            String email = object.getString("Email");
            String hoten = object.getString("HoTen");
            String gioitinh = object.getString("GioiTinh");
            String ngaysinh = object.getString("NgaySinh");
            String diachi = object.getString("DiaChi");
            String sodienthoai = object.getString("SoDienThoai");
            String tongdiemtichluy = object.getString("TongDiemTichLuy");
            String diemtichluytheogio = object.getString("DiemTichLuyTheoGio");
            String thoigiandocsach = object.getString("ThoiGianDocSach");

            String totalsachdadoc = object0.getString("countsachdadoc");
            String totalsachyeuthich = object0.getString("countsachyeuthich");

            editor.putString("Id", id);
            editor.putString("UserName", userName);
            editor.putString("PassWord", passWord);
            editor.putString("Email", email);
            editor.putString("HoTen", hoten);
            editor.putString("GioiTinh", gioitinh);
            editor.putString("NgaySinh", ngaysinh);
            editor.putString("DiaChi", diachi);
            editor.putString("SoDienThoai", sodienthoai);
            editor.putString("TongDiemTichLuy", tongdiemtichluy);
            editor.putString("DiemTichLuyTheoGio", diemtichluytheogio);
            editor.putString("ThoiGianDocSach", thoigiandocsach);
            editor.putString("CountSachDaDoc", totalsachdadoc);
            editor.putString("CountSachYeuThich", totalsachyeuthich);

            editor.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Boolean kiemTraEmail(String email) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("tenham", "KiemTraEmail");
        hashMap.put("Email", email);

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
}
