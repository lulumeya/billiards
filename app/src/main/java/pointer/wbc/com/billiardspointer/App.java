package pointer.wbc.com.billiardspointer;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Handler;

/**
 * Created by Haksu on 2015-05-17.
 */
public class App extends Application {
    private static Context context;
    private static boolean isDebuggable;
    public static final Handler handler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = this;
        isDebuggable = (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        this.context = null;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        App.context = context;
    }

    public static boolean isDebuggable() {
        return isDebuggable;
    }

    public static void setIsDebuggable(boolean isDebuggable) {
        App.isDebuggable = isDebuggable;
    }
}