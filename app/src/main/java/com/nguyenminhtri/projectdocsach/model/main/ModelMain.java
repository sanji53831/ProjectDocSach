package com.nguyenminhtri.projectdocsach.model.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.nguyenminhtri.projectdocsach.connectinternet.ContactService;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.ViewBook;
import com.nguyenminhtri.projectdocsach.savevariables.SaveVariables;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.Sach;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class ModelMain {
    AccessToken accessToken;
    AccessTokenTracker accessTokenTracker;

    public AccessToken getAccessTokenFacebook() {

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                accessToken = currentAccessToken;
            }
        };

        accessToken = AccessToken.getCurrentAccessToken();
        return accessToken;
    }

    public GoogleApiClient getGoogleApiClient(Context context, GoogleApiClient.OnConnectionFailedListener failedListener) {
        GoogleApiClient googleApiClient;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage((AppCompatActivity) context, failedListener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        return googleApiClient;
    }

    public GoogleSignInResult getDataLoginGoogle(GoogleApiClient client) {
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(client);
        if (opr.isDone()) {
            return opr.get();
        }
        return null;
    }

    public ArrayList<Sach> getListSachByCatelogy(String matheloai, int limit) {
        ArrayList<Sach> listSach = new ArrayList<>();
        String data = "";
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("tenham", "GetBookByCategory");
        hashMap.put("MaTheLoai", matheloai);
        hashMap.put("Limit", String.valueOf(limit));

        ContactService contactService = new ContactService(SaveVariables.URL, hashMap);
        contactService.execute();

        try {
            data = contactService.get();
            JSONArray jsonArray = new JSONArray(data);
            int length = jsonArray.length();
            for (int i = 1; i < length; i += 2) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String maSach = jsonObject.getString("MaSach");
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
                Sach sach = new Sach(maSach, tenSach, duongDanHinhAnh, duongDanNoiDung, moTa, ngayXuatBan, tenTheLoai, tenNhaXuatBan,
                        tenTacGia, gia, trangThai, maTheLoai, maNhaXuatBan, maTacGia, tongSoTrang);
                listSach.add(sach);
            }
        } catch (
                JSONException e)

        {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return listSach;
    }

    public ViewBook getListSachByCatelogy1(String matheloai, int limit) {
        ArrayList<Sach> listSach = new ArrayList<>();
        String data = "";
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("tenham", "GetBookByCategory");
        hashMap.put("MaTheLoai", matheloai);
        hashMap.put("Limit", String.valueOf(limit));

        ContactService contactService = new ContactService(SaveVariables.URL, hashMap);
        contactService.execute();

        try {
            data = contactService.get();
            JSONArray jsonArray = new JSONArray(data);
            int length = jsonArray.length();
            for (int i = 1; i < length; i += 2) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String maSach = jsonObject.getString("MaSach");
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
                Sach sach = new Sach(maSach, tenSach, duongDanHinhAnh, duongDanNoiDung, moTa, ngayXuatBan, tenTheLoai, tenNhaXuatBan,
                        tenTacGia, gia, trangThai, maTheLoai, maNhaXuatBan, maTacGia, tongSoTrang);
                listSach.add(sach);
            }
            JSONObject object = jsonArray.getJSONObject(0);
            int total = Integer.parseInt(object.getString("count"));
            ViewBook viewBook = new ViewBook(total, listSach);
            return viewBook;
        } catch (
                JSONException e)

        {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteCacheDangNhap(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("infouser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void saveThoiGianDocSach(String userId, String thoigian) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("tenham", "SaveThoiGianDocSach");
        hashMap.put("UserId", userId);
        hashMap.put("ThoiGianDocSach", thoigian);

        ContactService contactService = new ContactService(SaveVariables.URL, hashMap);
        contactService.execute();

        try {
            String data = contactService.get();
            if (!data.equals("success")) {
                Log.d("AAA", data);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
