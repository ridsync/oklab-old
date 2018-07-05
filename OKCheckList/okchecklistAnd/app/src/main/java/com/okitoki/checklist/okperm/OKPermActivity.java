package com.okitoki.checklist.okperm;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.WindowManager;

import com.okitoki.checklist.R;
import com.okitoki.checklist.utils.Logger;
import com.okitoki.checklist.utils.PreferUtil;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by andman on 2016-04-19.
 */
@TargetApi(Build.VERSION_CODES.M)
public class OKPermActivity extends AppCompatActivity {

    public static final int REQ_CODE_PERMISSION_REQUEST = 112;
    public static final int REQ_CODE_REQUEST_SETTING = 119;

    public static final String EXTRA_RETRY_REQUEST = "retry_request";
    public static final String EXTRA_PERMISSIONS = "permissions";
    public static final String EXTRA_DENY_DIALOG_TITLE = "deny_dialog_title";
    public static final String EXTRA_DENY_DIALOG_MESSAGE = "deny_dialog_message";
    public static final String EXTRA_PACKAGE_NAME = "package_name";
    public static final String EXTRA_DENIED_DIALOG_CLOSE_TEXT = "denied_dialog_close_text";

    String denyDialogTitle;
    String denyDialogMessage;
    String[] permissions;
    String packageName;
    String deniedCloseButtonText;
    boolean isRetryRequest;

    String permForAnalytics = "none";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        setupFromSavedInstanceState(savedInstanceState);

        checkPermissions(false);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissionDeniedHasCheck(ArrayList<String> deniedPermissions) {
        // 다시보지않기 체크한상태 거부이력이 하나라도 있다면 권한설정 화면으로 이동 하는 팝업으로 변경함
        for (int i = 0; i < deniedPermissions.size(); i++) {
            String permission = deniedPermissions.get(i);
            boolean isreqPermRationale = shouldShowRequestPermissionRationale(permission);
            boolean isPermissionDenied = PreferUtil.getPreferenceBoolean(permission,getApplicationContext());
            if( !isreqPermRationale
                    && PackageManager.PERMISSION_DENIED == ContextCompat.checkSelfPermission(this, permissions[0])
                    && isPermissionDenied ){
                isRetryRequest = false;
            }
        }
    }

    private void setupFromSavedInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            permissions = savedInstanceState.getStringArray(EXTRA_PERMISSIONS);
            denyDialogTitle = savedInstanceState.getString(EXTRA_DENY_DIALOG_TITLE);
            denyDialogMessage = savedInstanceState.getString(EXTRA_DENY_DIALOG_MESSAGE);
            packageName = savedInstanceState.getString(EXTRA_PACKAGE_NAME);
            deniedCloseButtonText = savedInstanceState.getString(EXTRA_DENIED_DIALOG_CLOSE_TEXT);
            isRetryRequest = savedInstanceState.getBoolean(EXTRA_RETRY_REQUEST);
        } else {

            Intent intent = getIntent();
            permissions = intent.getStringArrayExtra(EXTRA_PERMISSIONS);
            denyDialogTitle = intent.getStringExtra(EXTRA_DENY_DIALOG_TITLE);
            denyDialogMessage = intent.getStringExtra(EXTRA_DENY_DIALOG_MESSAGE);
            packageName = intent.getStringExtra(EXTRA_PACKAGE_NAME);
            deniedCloseButtonText = intent.getStringExtra(EXTRA_DENIED_DIALOG_CLOSE_TEXT);
            isRetryRequest = intent.getBooleanExtra(EXTRA_RETRY_REQUEST,false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putStringArray(EXTRA_PERMISSIONS, permissions);
        outState.putString(EXTRA_DENY_DIALOG_TITLE, denyDialogTitle);
        outState.putString(EXTRA_DENY_DIALOG_MESSAGE, denyDialogMessage);
        outState.putString(EXTRA_PACKAGE_NAME, packageName);
        outState.putString(EXTRA_DENIED_DIALOG_CLOSE_TEXT, deniedCloseButtonText);
        outState.putBoolean(EXTRA_RETRY_REQUEST,isRetryRequest);

        super.onSaveInstanceState(outState);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArrayList<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                deniedPermissions.add(permission);
                PreferUtil.setSharedPreference(permission, true, getApplicationContext()); // 거부한이력 저장
            }
        }
        if (deniedPermissions.isEmpty()) {
            permissionGranted(permissions);
        } else {
            checkPermissionDeniedHasCheck(deniedPermissions);
            showPermissionDenyDialog(deniedPermissions);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.d("OKPerm","okPerm onActivityResult");
        switch (requestCode) {
            case REQ_CODE_REQUEST_SETTING:
                checkPermissions(true);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }

    }

    /**
     * 모든 권한이 허용됬을때만 불린다.
     * @param grantedPermission
     */
    private void permissionGranted(String[] grantedPermission) {
        EventBusProvider.getInstance().post(new OKPermEvent(true, new ArrayList<>(Arrays.asList(grantedPermission)) ));
        finish();
        overridePendingTransition(0, 0);
    }

    private void permissionDenied(ArrayList<String> deniedpermissions) {
        EventBusProvider.getInstance().post(new OKPermEvent(false, deniedpermissions));
        finish();
        overridePendingTransition(0, 0);
    }

    private void checkPermissions(boolean fromOnActivityResult) {
        ArrayList<String> needPermissions = new ArrayList<>();

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                needPermissions.add(permission);
            }

        }

        if (needPermissions.isEmpty()) {
            permissionGranted(permissions);
        }
        //From Setting Activity
        else if (fromOnActivityResult) {
            permissionDenied(needPermissions);
        }
        //Need Request Permissions
        else {
            requestPermissions(needPermissions);
        }
    }


    public void showPermissionDenyDialog(final ArrayList<String> deniedPermissions) {

        if (TextUtils.isEmpty(denyDialogMessage)) {
            // denyDialogMessage 설정 안함
            permissionDenied(deniedPermissions);
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.MaterialAlertDialog);
        builder.setIcon(R.mipmap.ic_launcher)
                .setMessage(denyDialogMessage)
                .setCancelable(false)
                .setNegativeButton(deniedCloseButtonText == null ? getString(R.string.menu_close) : deniedCloseButtonText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        permissionDenied(deniedPermissions);
                    }
                });

        if(denyDialogTitle!=null){
            builder.setTitle(denyDialogTitle);
        }
        if(isRetryRequest){
            builder.setPositiveButton(getString(R.string.btn_confirm), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    checkPermissions(false);
                }
            });
        } else {
            builder.setPositiveButton(getString(R.string.btn_perm_request_setting), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    try {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                .setData(Uri.parse("package:" + packageName));
                        startActivityForResult(intent, REQ_CODE_REQUEST_SETTING);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                        Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                        startActivityForResult(intent, REQ_CODE_REQUEST_SETTING);
                    }

                }
            });
        }

        builder.show();

    }

    public void requestPermissions(ArrayList<String> needPermissions) {
        ActivityCompat.requestPermissions(this, needPermissions.toArray(new String[needPermissions.size()]), REQ_CODE_PERMISSION_REQUEST);

    }
}

