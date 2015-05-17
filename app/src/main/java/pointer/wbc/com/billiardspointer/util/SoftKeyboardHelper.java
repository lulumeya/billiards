package pointer.wbc.com.billiardspointer.util;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class SoftKeyboardHelper {
	public static void showSoftKeyboard(View view) {
		InputMethodManager mgr = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.showSoftInput(view, InputMethodManager.SHOW_FORCED);
	}

	public static void hideSoftKeyboard(View view) {
		InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromInputMethod(view.getWindowToken(), 0);
	}
	public static void toggleSoftKeyboard(View view) {
		InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			imm.toggleSoftInput(0, 0);
		} else {
			//imm.hideSoftInputFromInputMethod(view.getWindowToken(), 0);
			imm.toggleSoftInputFromWindow(view.getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
		}
	}

	public static boolean isSoftKeyboardActive(View view) {
		InputMethodManager mgr = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		return mgr.isActive(view);
	}
}