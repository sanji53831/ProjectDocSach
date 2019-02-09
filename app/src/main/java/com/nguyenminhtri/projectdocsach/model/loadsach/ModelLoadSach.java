package com.nguyenminhtri.projectdocsach.model.loadsach;

import com.nguyenminhtri.projectdocsach.connectinternet.ContactService;
import com.nguyenminhtri.projectdocsach.savevariables.SaveVariables;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class ModelLoadSach {

    public String saveSachDaDoc(String maUser, String maSach) {
        String data = null;
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("tenham", "SaveChiTietSachDaDocByUser");
        hashMap.put("UserId", maUser);
        hashMap.put("MaSach", maSach);

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
}
