package pointer.wbc.com.billiardspointer.preference;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import pointer.wbc.com.billiardspointer.App;

/**
 * Created by Dalton on 2014. 9. 21..
 */
public class Pref {
    private static final String PREF = Pref.class.getName();
    public static final String EVENT_ALARM = "eventAlarm";
    public static final String BID_ALARM = "bidAlarm";
    public static final String DISTRICT = "district";
    public static final String NEED_PRIVATE_ALERT = "needPrivateAlert";
    public static final String HOSPITAL_AUTH_TOKEN = "hospitalAuthToken";
    private static SharedPreferences pref;

    static {
        Pref.pref = App.getContext().getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    public static void setContext(Context context) {
        Pref.pref = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    public static String getString(String key, String defaultValue) {
        assertContext();
        return pref.getString(key, defaultValue);
    }

    private static void assertContext() {
        assert (App.getContext() != null);
    }

    public static void putString(String key, String value) {
        assertContext();
        pref.edit().putString(key, value).commit();
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        assertContext();
        return pref.getBoolean(key, defaultValue);
    }

    public static void putBoolean(String key, boolean value) {
        assertContext();
        pref.edit().putBoolean(key, value).commit();
    }

    public static void remove(String key) {
        assertContext();
        pref.edit().remove(key).commit();
    }

    public static List<Long> getLongArray(String key) {
        assertContext();
        final List<Long> result = new ArrayList<>();
        try {
            final JSONArray array = new JSONArray(pref.getString(key, "[]"));
            for (int i = 0; i < array.length(); i++) {
                result.add(array.getLong(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void putLongArray(String key, long value, int maxCount) {
        assertContext();
        try {
            final JSONArray array = new JSONArray(pref.getString(key, "[]"));
            final JSONArray result = new JSONArray();
            List<Long> tempList = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                tempList.add(array.getLong(i));
            }
            tempList.add(value);
            if (tempList.size() > maxCount) {
                final int diff = tempList.size() - maxCount;
                tempList = tempList.subList(diff, tempList.size());
            }
            for (Long val : tempList) {
                result.put(val);
            }
            pref.edit().putString(key, result.toString()).commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}