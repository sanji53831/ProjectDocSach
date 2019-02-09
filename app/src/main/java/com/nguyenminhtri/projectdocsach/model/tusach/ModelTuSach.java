package com.nguyenminhtri.projectdocsach.model.tusach;

import android.util.Log;

import com.nguyenminhtri.projectdocsach.connectinternet.ContactService;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.ChiTietSachDaDoc;
import com.nguyenminhtri.projectdocsach.savevariables.SaveVariables;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;


public class ModelTuSach {

    public String getJSONSachDaDocByUser(String userId){
        String data = null;

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("tenham","GetChiTietSachDaDocByUser");
        hashMap.put("UserId",userId);

        ContactService contactService = new ContactService(SaveVariables.URL,hashMap);
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

    public String getJSONSachYeuThichByUser(String userId){
        String data = null;

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("tenham","GetChiTietSachYeuThichByUser");
        hashMap.put("UserId",userId);

        ContactService contactService = new ContactService(SaveVariables.URL,hashMap);
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

    public String getJSONSachByMaSach(String maSach){
        String data = null;

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("tenham","GetBookByMaSach");
        hashMap.put("MaSach",maSach);

        ContactService contactService = new ContactService(SaveVariables.URL,hashMap);
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
}
