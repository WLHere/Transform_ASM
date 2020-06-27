package com.example.transform_asm.toast;

import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.transform_asm.util.CaughtCallback;
import com.example.transform_asm.util.CaughtRunnable;
import com.example.transform_asm.util.Reflection;

public class ShadowToast {
    private static final String TAG = "ShadowToast";

    public static void show(final Toast toast) {
        if (Build.VERSION.SDK_INT == 25) {
            workaround(toast).show();
        } else {
            toast.show();
        }
    }

    private static Toast workaround(final Toast toast) {
        final Object tn = Reflection.getField(Toast.class, "mTN");
        if (tn == null) {
            return toast;
        }
        final Object handler = Reflection.getFieldValue(tn, "mHandler");
        if (handler instanceof Handler) {
            if (Reflection.setFieldValue(handler, "mCallback", new CaughtCallback((Handler) handler))) {
                return toast;
            }
        }

        final Object show = Reflection.getFieldValue(tn, "mShow");
        if (show instanceof Runnable) {
            if (Reflection.setFieldValue(tn, "mShow", new CaughtRunnable((Runnable) show))) {
                return toast;
            }
        }

        Log.w(TAG, "Neither field mHandler nor mShow of " + tn + " is accessible");
        return toast;
    }
}
