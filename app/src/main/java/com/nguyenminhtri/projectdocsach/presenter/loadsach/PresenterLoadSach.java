package com.nguyenminhtri.projectdocsach.presenter.loadsach;

import com.nguyenminhtri.projectdocsach.model.loadsach.ModelLoadSach;

public class PresenterLoadSach implements InterfacePresenterLoadSach {

    ModelLoadSach modelLoadSach;

    public PresenterLoadSach() {
        modelLoadSach = new ModelLoadSach();
    }

    @Override
    public Boolean saveSachDocDocToServer(String userId, String maSach) {
        if (modelLoadSach.saveSachDaDoc(userId, maSach).equals("success")) {
            return true;
        }
        return false;
    }
}
