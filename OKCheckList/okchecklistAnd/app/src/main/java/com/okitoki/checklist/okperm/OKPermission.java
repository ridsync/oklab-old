package com.okitoki.checklist.okperm;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;

import com.okitoki.checklist.utils.JavaUtil;
import com.okitoki.checklist.utils.Logger;

import java.util.ArrayList;
import java.util.Arrays;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

/**
 * Created by andman on 2016-04-19.
 *  1) 단일 권한 요청
 *  2) 다중 권한 요청 : 모든권한 허용시에만 Granted
 *  ※ 현재 구현상황 : 요청한 모든 권한에 대해 사용자가 모두 허용 했을때만 Granted 호출. 이외 한개라도 거부하면 Denied호출
 * => 각 권한별 권한처리에 대한 결과처리가 필요시엔 각각의 요청한 권한상태리스트를 넘기는걸로 변경해야함.
 */
public class OKPermission {

    public PermissionListener onsPermListener;
    public String[] permissions;
    public String denyMessage;
    public String deniedCloseButtonText;
    public String denyTitle;
    Context context;
    boolean isRetry;

    public interface PermissionListener {

        void onPermissionGranted(ArrayList<String> permissionCode);

        void onPermissionDenied(ArrayList<String> permissionCode);
    }

    public OKPermission(Context context) {
        if(context!=null)
            this.context = context;
    }

    public OKPermission setPermissionListener(PermissionListener listener) {

        onsPermListener = listener;

        return this;
    }

    public PermissionListener getPermissionListener() {
        return onsPermListener;
    }

    public OKPermission setIsRetryPermRequest(boolean isRetry) {
        this.isRetry = isRetry;
        return this;
    }

    public OKPermission setPermissions(String... permissions) {

        this.permissions = permissions;
        return this;
    }

    public OKPermission setAllDeniedMessages(String denyMessage) {

        this.denyMessage = denyMessage;
        return this;
    }

    public OKPermission setAllDeniedMessages(@StringRes int stringRes) {

        if (stringRes <= 0)
            throw new IllegalArgumentException("Invalid value for DeniedMessage");

        this.denyMessage = context.getString(stringRes);
        return this;
    }

    public OKPermission setDeniedCloseButtonText(String deniedCloseButtonText) {

        this.deniedCloseButtonText = deniedCloseButtonText;
        return this;
    }


    public OKPermission setDeniedCloseButtonText(@StringRes int stringRes) {

        if (stringRes <= 0)
            throw new IllegalArgumentException("Invalid value for DeniedCloseButtonText");

        this.deniedCloseButtonText = context.getString(stringRes);

        return this;
    }
    public OKPermission setDeniedTitleText(String denyTitle) {

        this.denyTitle = denyTitle;
        return this;
    }


    public OKPermission setDeniedTitleText(@StringRes int denyTitle) {

        if (denyTitle <= 0)
            throw new IllegalArgumentException("Invalid value for denyTitle ResId");

        this.denyTitle = context.getString(denyTitle);

        return this;
    }


    public void check() {

        if (onsPermListener == null) {
            throw new NullPointerException("You must setPermissionListener() on OKPermission");
        } else if (JavaUtil.isEmpty(permissions)) {
            throw new NullPointerException("You must setPermissions() on OKPermission");
        }

        // 퍼미션 미리 체크 23이하는 무조건 Granted 콜백
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Logger.d("OKPerm","check Build.VERSION.SDK_INT < (permissions);" + permissions);
            this.onsPermListener.onPermissionGranted(new ArrayList<>(Arrays.asList(permissions)));
        } else {
            boolean hasNoPerm = false;
            for (int i = 0; i < permissions.length; i++) {
                if (PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(context, permissions[i])) {
                    hasNoPerm = true;
                }
            }
            if (hasNoPerm) {
                Logger.d("OKPerm", "check checkPermissions start" + permissions);
                checkPermissions();
            } else {
                this.onsPermListener.onPermissionGranted(new ArrayList<>(Arrays.asList(permissions)));
                Logger.d("OKPerm", "check PERMISSION_GRANTED;" + permissions);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void checkPermissions() {
        EventBus.getDefault().register(this);

        Intent intent = new Intent(context, OKPermActivity.class);
        intent.putExtra(OKPermActivity.EXTRA_PERMISSIONS, permissions);

        intent.putExtra(OKPermActivity.EXTRA_RETRY_REQUEST, isRetry);
        intent.putExtra(OKPermActivity.EXTRA_DENY_DIALOG_TITLE, denyTitle);
        intent.putExtra(OKPermActivity.EXTRA_DENY_DIALOG_MESSAGE, denyMessage);
        intent.putExtra(OKPermActivity.EXTRA_PACKAGE_NAME, context.getPackageName());
        intent.putExtra(OKPermActivity.EXTRA_DENIED_DIALOG_CLOSE_TEXT, deniedCloseButtonText);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Subscribe
    public void onEvent(OKPermEvent event){
        Logger.d("OKPerm","okPermission  onEvent(event);" + event.toString());
        if (event.hasPermissionAll()) {
            this.onsPermListener.onPermissionGranted(event.getReturnPermissions());
        } else {
            this.onsPermListener.onPermissionDenied(event.getReturnPermissions());
        }
        EventBus.getDefault().unregister(this);
    }

}

