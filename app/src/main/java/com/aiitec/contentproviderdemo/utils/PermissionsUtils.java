package com.aiitec.contentproviderdemo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;


/**
 * Android 6.0权限管理
 *
 * @author ailibin
 * @version 1.0
 * createTime 2019/3/21.
 */
public class PermissionsUtils {


    private Activity context;

    public PermissionsUtils(Activity context) {
        this.context = context;
    }

    public void requestPermissions(int permissionsRequestCode, String... permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (onPermissionsListener != null) {
                onPermissionsListener.onPermissionsSuccess(permissionsRequestCode);
            }
            return;
        }
        if (ContextCompat.checkSelfPermission(context, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context, permissions, permissionsRequestCode);
        } else {
            if (onPermissionsListener != null) {
                onPermissionsListener.onPermissionsSuccess(permissionsRequestCode);
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (onPermissionsListener != null) {
                onPermissionsListener.onPermissionsSuccess(requestCode);
            }
        } else {
            // Permission Denied
            Toast.makeText(context, "权限申请失败", Toast.LENGTH_SHORT).show();
            if (onPermissionsListener != null) {
                onPermissionsListener.onPermissionsFailure(requestCode);
            }
        }
    }

    public static boolean checkSelfPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED;
    }

    private OnPermissionsListener onPermissionsListener;

    public void setOnPermissionsListener(OnPermissionsListener onPermissionsListener) {
        this.onPermissionsListener = onPermissionsListener;
    }

    public interface OnPermissionsListener {
        void onPermissionsSuccess(int requestCode);

        void onPermissionsFailure(int requestCode);
    }
}
