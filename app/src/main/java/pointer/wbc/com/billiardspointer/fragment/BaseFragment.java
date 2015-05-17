package pointer.wbc.com.billiardspointer.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

import pointer.wbc.com.billiardspointer.BaseActivity;
import pointer.wbc.com.billiardspointer.util.StringUtil;


/**
 * Created by Dalton on 2014. 8. 9..
 */
public class BaseFragment extends Fragment {
    public Dialog dialog;
    View rootview;
    Context context;
    private ProgressDialog progress;
    protected boolean destroyed = false;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        setRetainInstance(true);
        this.context = activity;
        destroyed = false;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = null;
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).dismiss();
            ((BaseActivity) getActivity()).dismissProgress();
        }
    }

    public void showProgress() {
        showProgress(null);
    }

    public void showProgress(String message) {
        dismissProgress();
        if (context != null) {
            progress = new ProgressDialog(context);
            progress.setCancelable(true);
            progress.setCanceledOnTouchOutside(false);
            if (StringUtil.notEmpty(message)) {
                progress.setMessage(message);
            } else {
                progress.setMessage("로딩중입니다..");
            }
            progress.setIndeterminate(true);
            progress.show();
        }
    }

    public void dismissProgress() {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyed = true;
    }
}