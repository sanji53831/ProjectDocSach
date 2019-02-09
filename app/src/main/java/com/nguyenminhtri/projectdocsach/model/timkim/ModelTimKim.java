package com.nguyenminhtri.projectdocsach.model.timkim;

import com.nguyenminhtri.projectdocsach.connectinternet.ContactService;
import com.nguyenminhtri.projectdocsach.savevariables.SaveVariables;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class ModelTimKim {
    public String getSachByTenSach(String tenSach) {
        String data = null;

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("tenham", "TimSach");
        hashMap.put("TenSach", tenSach);

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

    ;
}
