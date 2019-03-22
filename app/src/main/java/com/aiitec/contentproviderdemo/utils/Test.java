package com.aiitec.contentproviderdemo.utils;

import android.app.Activity;

/**
 * @Author: $user
 * @Time: ${date} ${time}
 * @Description: 模板注释
 * @Email: ailibin@qq.com
 */
public class Test {

    private PermissionsUtils permissionsUtils;
    private Activity context;
    private static final int REQUEST_PERMISSION = 0x2220;

    public void setTest(Activity context) {
        permissionsUtils = new PermissionsUtils(context);
        permissionsUtils.requestPermissions(REQUEST_PERMISSION,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE});
    }
}
