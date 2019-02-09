package com.nguyenminhtri.projectdocsach.model.gopy;

import com.nguyenminhtri.projectdocsach.connectinternet.ContactService;
import com.nguyenminhtri.projectdocsach.savevariables.SaveVariables;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class ModelGopY {

    public String saveGopY(String userId, String hoTen, String email, String noiDung) {
        String data = null;

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("tenham", "SaveGopY");
        hashMap.put("UserId", userId);
        hashMap.put("HoTen", hoTen);
        hashMap.put("Email", email);
        hashMap.put("NoiDung", noiDung);

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
