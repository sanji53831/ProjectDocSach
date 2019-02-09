package com.nguyenminhtri.projectdocsach.presenter.main;


import android.content.Context;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nguyenminhtri.projectdocsach.model.dangnhap.ModelDangNhap;
import com.nguyenminhtri.projectdocsach.model.main.ModelMain;

public class PresenterMain implements InterfacePresenterMain {
    ModelMain modelMain = new ModelMain();

    @Override
    public AccessToken getTokenFacebook() {

        return modelMain.getAccessTokenFacebook();
    }

    @Override
    public GoogleApiClient getGoogleApiClient(Context context, GoogleApiClient.OnConnectionFailedListener failedListener) {
        return modelMain.getGoogleApiClient(context,failedListener);
    }

    @Override
    public GoogleSignInResult getDataLoginGoogle(GoogleApiClient client) {
        return modelMain.getDataLoginGoogle(client);
    }

    @Override
    public void deleteCacheDangNhap(Context context) {
        modelMain.deleteCacheDangNhap(context);
    }

    @Override
    public void saveThoiGianDocSach(String userId, String thoigian) {
        modelMain.saveThoiGianDocSach(userId,thoigian);
    }


}
