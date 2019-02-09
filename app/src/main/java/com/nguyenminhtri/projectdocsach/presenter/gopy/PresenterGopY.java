package com.nguyenminhtri.projectdocsach.presenter.gopy;

import com.nguyenminhtri.projectdocsach.model.gopy.ModelGopY;
import com.nguyenminhtri.projectdocsach.view.gopy.InterfaceViewGopY;

public class PresenterGopY implements InterfacePresenterGopY {

    ModelGopY modelGopY;

    public PresenterGopY() {
        modelGopY = new ModelGopY();
    }

    @Override
    public Boolean saveGopY(String userId, String hoTen, String email, String noiDung) {
        if (modelGopY.saveGopY(userId, hoTen, email, noiDung).equals("success")) {
            return true;
        } else {
            return false;
        }
    }
}
