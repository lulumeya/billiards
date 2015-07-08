package pointer.wbc.com.billiardspointer;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Handler;

import com.kakao.AuthType;
import com.kakao.Session;
import com.kakao.UserProfile;
import com.raizlabs.android.dbflow.config.FlowManager;

import pointer.wbc.com.billiardspointer.preference.Pref;

/**
 * Created by Haksu on 2015-05-17.
 */
public class App extends Application {
    private static Context context;
    private static boolean isDebuggable;
    public static final Handler handler = new Handler();
    private static UserProfile user;

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = this;
        user = Pref.getObject(Const.KEY_PROFILE, UserProfile.class);

        isDebuggable = (0 != (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
        Session.initialize(this, AuthType.KAKAO_TALK);

        FlowManager.init(this);
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

    public static void setUser(UserProfile user) {
        App.user = user;
    }

    public static UserProfile getUser() {
        return user;
    }
}