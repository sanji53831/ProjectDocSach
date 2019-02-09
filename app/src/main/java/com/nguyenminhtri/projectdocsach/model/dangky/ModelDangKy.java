package com.nguyenminhtri.projectdocsach.model.dangky;

import com.nguyenminhtri.projectdocsach.connectinternet.ContactService;
import com.nguyenminhtri.projectdocsach.savevariables.SaveVariables;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class ModelDangKy {

    public String themKhacHang(String username, String password, String email, String hoten) {
        String id = "U" + String.valueOf(new Date().getTime());
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

    public String kiemTraUserName(String username) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("tenham", "KiemTraUserName");
        hashMap.put("UserName", username);

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

    public String kiemTraEmail(String email) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("tenham", "KiemTraEmail");
        hashMap.put("Email", email);

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


}
