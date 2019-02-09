package com.nguyenminhtri.projectdocsach.model.vietdanhgia;

import com.nguyenminhtri.projectdocsach.connectinternet.ContactService;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.DanhGia;
import com.nguyenminhtri.projectdocsach.savevariables.SaveVariables;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class ModelVietDanhGia {

    public String sendDanhGiaToServer(DanhGia danhGia) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("tenham", "SaveDanhGia");
        hashMap.put("MaDanhGia", danhGia.getMaDanhGia());
        hashMap.put("MaSach", danhGia.getMaSach());
        hashMap.put("TenUser", danhGia.getTenUser());
        hashMap.put("TieuDe", danhGia.getTieuDe());
        hashMap.put("NoiDung", danhGia.getNoiDung());
        hashMap.put("SoSao", danhGia.getSoSao());

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
