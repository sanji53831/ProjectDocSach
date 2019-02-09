package com.nguyenminhtri.projectdocsach.model.readbook;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.nguyenminhtri.projectdocsach.connectinternet.ContactService;
import com.nguyenminhtri.projectdocsach.savevariables.SaveVariables;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class ModelReadBook {

    public Boolean checkSachStorge(Context context, String nameFilePDF) {
        File fileInExternal = new File(Environment.getExternalStorageDirectory().toString() + "/appdocsach", nameFilePDF);
        if (fileInExternal.exists()) {
            return true;
        }
        File fileInInternal = new File("/data/data/" + context.getPackageName() + "/appdocsach", nameFilePDF);
        if (fileInInternal.exists()) {
            return true;
        }
        return false;
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
