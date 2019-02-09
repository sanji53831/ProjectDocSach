package com.nguyenminhtri.projectdocsach.presenter.main;

import android.content.Context;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

public interface InterfacePresenterMain {
    AccessToken getTokenFacebook();
    GoogleApiClient getGoogleApiClient(Context context, GoogleApiClient.OnConnectionFailedListener failedListener);
    GoogleSignInResult getDataLoginGoogle(GoogleApiClient client);
    void deleteCacheDangNhap(Context context);
    void saveThoiGianDocSach(String userId, String thoigian);
}
