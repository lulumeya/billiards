package pointer.wbc.com.billiardspointer;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;

import pointer.wbc.com.billiardspointer.listener.DataListener;
import pointer.wbc.com.billiardspointer.log.Logger;
import pointer.wbc.com.billiardspointer.preference.Pref;
import pointer.wbc.com.billiardspointer.util.SoftKeyboardHelper;
import pointer.wbc.com.billiardspointer.util.StringUtil;
import pointer.wbc.com.billiardspointer.util.Util;


/**
 * Created by Dalton on 2014. 8. 9..
 */
public class BaseActivity extends AppCompatActivity {
    public Context context;
    public Dialog dialog;
    public ProgressDialog progress;
    private static volatile int stackCount = 0;
    public boolean isActive;
    private DataListener<Boolean> keyboardListener;
    public boolean keyboardVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
        boolean isLight = Pref.getBoolean(Const.THEME, false);
        if (isLight) {
            setTheme(R.style.AppTheme_Light);
            getWindow().setBackgroundDrawableResource(R.drawable.light_bg);
        } else {
            setTheme(R.style.AppTheme);
            getWindow().setBackgroundDrawableResource(R.drawable.dark_bg);
        }
        if (App.getContext() == null) {
            App.setContext(getApplicationContext());
        }

        final View activityRootView = getWindow().getDecorView().findViewById(android.R.id.content);
        if (activityRootView != null) {
            final int height = Util.dpToPx(40);
            activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (activityRootView != null) {
                        int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight() - Util.getStatusbarHeight(context);
                        boolean newValue = heightDiff > height;
                        if (keyboardListener != null) {
                            if (keyboardVisible != newValue) {
                                keyboardListener.onFinish(newValue);
                            }
                        }
                        keyboardVisible = newValue;
                    }
                }
            });
        }

        stackCount++;
    }

    public void setKeyboardListener(DataListener keyboardListener) {
        this.keyboardListener = keyboardListener;
    }

    public void hideKeyboard(View v) {
        if (keyboardVisible) {
            keyboardVisible = false;
            SoftKeyboardHelper.toggleSoftKeyboard(v);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.objs(getClass().getSimpleName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.objs(getClass().getSimpleName());
    }

    @Override
    protected void onStart() {
        super.onStart();
        isActive = true;
        Logger.objs(getClass().getSimpleName());
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActive = false;
        Logger.objs(getClass().getSimpleName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismiss();
        dismissProgress();
        this.context = null;
        this.keyboardListener = null;
        stackCount--;
        Logger.objs(getClass().getSimpleName());
        Logger.i(stackCount + " Activities running...");
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = null;
    }

    public void dismissProgress() {
        if (progress != null) {
            if (progress.isShowing()) {
                progress.dismiss();
            }
            progress = null;
        }
    }

    public void showProgress(String message) {
        dismissProgress();
        progress = new ProgressDialog(this);
        progress.setCancelable(true);
        progress.setCanceledOnTouchOutside(false);
        if (StringUtil.notEmpty(message)) {
            progress.setMessage(message);
        }
        progress.show();
    }
}