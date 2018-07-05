package com.okitoki.checklist.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by okc on 2015-06-21.
 */
public class Notifyer {

    protected void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
