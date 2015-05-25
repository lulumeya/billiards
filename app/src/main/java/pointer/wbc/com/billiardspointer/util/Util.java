package pointer.wbc.com.billiardspointer.util;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pointer.wbc.com.billiardspointer.App;
import pointer.wbc.com.billiardspointer.BaseActivity;
import pointer.wbc.com.billiardspointer.log.Logger;

public class Util {

    private static final String CONFIG = "config";
    private static final String KEY1 = "android:viewHierarchyState";
    private static final String KEY2 = "android:support:fragments";
    private static final String ACCOUNT_ID = "ACCOUNT_ID";
    private static final String ACCOUNT_PW = "ACCOUNT_PW";

    private static Toast toast;

    public static void share(BaseActivity activity, View v) {
        try {
            ShareCompat.IntentBuilder builder = ShareCompat.IntentBuilder.from(activity);
            v.setDrawingCacheEnabled(true);
            v.buildDrawingCache(true);
            Bitmap bitmap = v.getDrawingCache();
            File f = new File(Environment.getExternalStorageDirectory(), "healingpaper_share.png");
            if (!f.exists()) {
                f.createNewFile();
            }
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(f));
            Uri uri = Uri.fromFile(f);
            builder.setChooserTitle("공유할 어플 선택");
            builder.setSubject("게임결과 공유");
            builder.setText("게임결과 공유");
            builder.setStream(uri);
            builder.setType("image/png");
            builder.startChooser();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void toast(String message) {
        if (App.getContext() != null) {
            if (isMainThread()) {
                if (toast == null) {
                    toast = Toast.makeText(App.getContext(), message, App.isDebuggable() ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
                } else {
                    toast.setText(message);
                }
                toast.show();
            } else {
                App.handler.post(() -> {
                    if (toast == null) {
                        toast = Toast.makeText(App.getContext(), message, App.isDebuggable() ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
                    } else {
                        toast.setText(message);
                    }
                    toast.show();
                });
            }
        }
    }

    public static void toast(int messageResource) {
        if (App.getContext() != null) {
            try {
                toast(App.getContext().getString(messageResource));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isEmailValid(String email) {
        String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@" + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|" + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches())
            return true;
        else
            return false;
    }

    public static int getActionBarHeight(Context context) {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    public static File getOriginalPhoto() {
        String path = Environment.getExternalStorageDirectory() + File.separator + "oligomonPic.jpg";
        File f = new File(path);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return f;
    }

    public static String getPath(Context context, Uri uri) {
        String path = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            path = cursor.getString(column_index);
            cursor.close();
        }
        return path;
    }

    public static File getShareFile() {
        String path = Environment.getExternalStorageDirectory() + File.separator + "oligomonShare.jpg";
        File f = new File(path);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return f;
    }

    public static File getFramePhoto() {
        String path = Environment.getExternalStorageDirectory() + File.separator + "oligomonFrame.jpg";
        File f = new File(path);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return f;
    }

    public static File getPreviewPhoto() {
        String path = Environment.getExternalStorageDirectory() + File.separator + "oligomonPreview.jpg";
        File f = new File(path);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return f;
    }

//    public static Reachability isReachableForInternetConnection(Context context) {
//        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//        if (mobile == null) {
//            if (wifi.isAvailable()) {
//                return Reachability.kReachableWifi;
//            } else {
//                return Reachability.kNotReachable;
//            }
//        }
//        if (wifi.isAvailable() && mobile.isAvailable()) {
//            return Reachability.kReachableALL;
//        } else if (wifi.isAvailable() && !mobile.isAvailable()) {
//            return Reachability.kReachableWifi;
//        } else if (!wifi.isAvailable() && mobile.isAvailable()) {
//            return Reachability.kReachable3G;
//        } else {
//            return Reachability.kNotReachable;
//        }
//    }

    public static void sendMail(Context context, String sender, String subject, String content) {

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "admin@healingpaper.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, sender);
        emailIntent.putExtra(Intent.EXTRA_TEXT, content);
        context.startActivity(Intent.createChooser(emailIntent, "메일보내기"));
    }

    public static boolean laterSDK(int SDKInt) {
        Logger.v("", "Build.VERSION.SDK_INT " + Build.VERSION.SDK_INT + " : " + SDKInt);
        return Build.VERSION.SDK_INT >= SDKInt;
    }

    public static int getScreenHeightWithoutStatusbar(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels - (int) Math.ceil(25 * dm.density);
    }

    /*
    private static Bitmap loadBitmapFromUrl(Context context, String src) throws MalformedURLException, IOException {
        Logger.e("loadBitmapFromUrl " + src);
        long currentTimeMillis = System.currentTimeMillis();
        try {
            URL url = new URL(src);
            InputStream input = getInputStream(url);

            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(input, null, opts);

            input = getInputStream(url);

            int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
            if (opts.outWidth != -1) {
                int sampleRate = Math.round((float) opts.outWidth / (float) widthPixels);
                Logger.e("~~ sampleRate " + sampleRate);
                if (sampleRate > 1) {
                    opts.inJustDecodeBounds = false;
                    opts.inSampleSize = sampleRate;
                    Bitmap myBitmap = BitmapFactory.decodeStream(input, null, opts);
                    saveCache(myBitmap, src, context);
                    return myBitmap;
                } else {
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);
                    saveCache(myBitmap, src, context);
                    return myBitmap;
                }
            } else {
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                saveCache(myBitmap, src, context);
                return myBitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } catch (Error e) {
            e.printStackTrace();
            return null;
        } finally {
            Logger.e("~~ loadBitmapFromUrl " + (currentTimeMillis - System.currentTimeMillis()));
        }
    }
    */

    public static InputStream getInputStream(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.connect();
        InputStream input = connection.getInputStream();
        return input;
    }

    public static boolean isCached(Context context, String src) {
        String filename = get_md5_hash(src);
        File f = context.getFileStreamPath(filename);
        Logger.e("isCached " + src + " f.exist " + f.exists());
        return f != null && f.exists() && f.isFile();
    }

    public static String get_md5_hash(String url) {
        String hash = "";

        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(url.getBytes());
            byte messageDigest[] = algorithm.digest();

            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < messageDigest.length; i++) {
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }

            hash = "CACHE_" + hexString.toString();
        } catch (Exception e) {

        }

        return hash;
    }

    /*
    public static void loadImage(final Context context, final String src, final ImageListener listener) throws MalformedURLException, IOException {
        final Bitmap b = loadBitmapFromUrl(context, src);
        if (listener != null) {
            Logger.e("finish " + b);
            if (context instanceof Activity) {
                ((Activity) context).runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        listener.onLoad(false, b);
                    }
                });
            }
        }
    }

    private static ImageSize imageSize;
    private static ImageSize size;

    public static void getImageFile(final Context context, String src, final DataPairListener<File, Bitmap> listener) {
        if (!src.startsWith("http")) {
            src = API.baseUrl + src;
        }
        ImageLoader.getInstance().loadImage(src, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String imageUri, View view) {
                Logger.e(imageUri + " started");
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                Logger.e(imageUri + " onLoadingFailed");
                listener.onFinish(null, null);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                File findInCache = DiscCacheUtil.findInCache(imageUri, ImageLoader.getInstance().getDiscCache());
                Logger.e(imageUri + " onLoadingComplete " + findInCache);
                listener.onFinish(findInCache, loadedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                Logger.e(imageUri + " onLoadingCancelled ");
            }
        });
    }

    public static void getImage(final Context context, final String url, final ImageListener listener, View v) {
        final String src;
        if (url != null && url.startsWith("/")) {
            src = API.baseUrl + url;
        } else {
            src = url;
        }
        Logger.e("~~ getImage " + src);

        if (imageSize == null) {
            int width = (int) (context.getResources().getDisplayMetrics().widthPixels / 3f);
            int height = (int) (width / 2f);
            imageSize = new ImageSize(width, height);
        }

        size = null;
        if (v != null && v.getWidth() != 0 && v.getWidth() < imageSize.getHeight()) {
            size = new ImageSize(v.getWidth(), v.getHeight());
        } else {
            size = imageSize;
        }

        if (Util.isMainThread()) {
            startLoading(src, listener, size);
        } else {
            Runnable runnable = new Runnable() {

                public void run() {
                    startLoading(src, listener, size);
                }
            };
            if (context instanceof Activity) {
                ((Activity) context).runOnUiThread(runnable);
            } else if (v != null) {
                v.post(runnable);
            }
        }
    }

    public static void getImage(final Context context, final String url, final ImageListener listener, int width, int height) {
        final String src;
        if (url != null && url.startsWith("/")) {
            src = API.baseUrl + url;
        } else {
            src = url;
        }
        Logger.e("~~ getImage " + src);

        size = new ImageSize(width, height);

        if (Util.isMainThread()) {
            startLoading(src, listener, size);
        } else {
            Runnable runnable = new Runnable() {

                public void run() {
                    startLoading(src, listener, size);
                }
            };
            if (context instanceof Activity) {
                ((Activity) context).runOnUiThread(runnable);
            }
        }
    }

    private static void startLoading(final String src, final ImageListener listener, ImageSize size) {
        ImageLoader.getInstance().loadImage(src, size, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String imageUri, View view) {
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                listener.onLoad(false, loadedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
            }
        });
    }

    public static void saveCache(Bitmap bitmap, String url, Context context) {
        if (bitmap != null) {
            String filename = get_md5_hash(url);
            Logger.e("save_cache " + url + " filename " + filename);
            if (filename != "") {
                try {
                    FileOutputStream fOut = context.openFileOutput(filename, Context.MODE_PRIVATE);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Bitmap loadCache(String url, Context context) {
        String filename = get_md5_hash(url);

        if (filename != "") {
            try {
                long currentTimeMillis = System.currentTimeMillis();
                FileInputStream stream = context.openFileInput(filename);
                Bitmap bitmap = BitmapFactory.decodeStream(stream);
                stream.close();

                Logger.e("~~ loadCache " + (currentTimeMillis - System.currentTimeMillis()));

                return bitmap;
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
                e.printStackTrace();
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }

        return null;
    }
    */

    public static int getStatusbarHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return (int) Math.ceil(25 * dm.density);
    }

    public static boolean isTablet(Context context) {
        return ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE);
    }

    /*
    public static void popBackStack(Context context) {
        BaseActivity baseActivity = (BaseActivity) context;
        FragmentManager fm = baseActivity.getFragmentManager();
        int backStackEntryCount = fm.getBackStackEntryCount();
        if (backStackEntryCount > 0) {
            fm.popBackStack();
        } else {
            baseActivity.finish();
        }
    }

    public static View addFragment(Context context, int layout) {
        return addFragment(context, layout, android.R.id.content);
    }

    public static void addActivity(Context context, int layout) {
        Intent intent = new Intent(context, NavigationActivity.class);
        intent.putExtra(EXTRA_LAYOUT_ID, layout);
        context.startActivity(intent);
    }

    public static void addActivity(Context context, String fragName) {
        Intent intent = new Intent(context, NavigationActivity.class);
        intent.putExtra(EXTRA_FRAGMENT_NAME, fragName);
        context.startActivity(intent);
    }

    public static long getTime(String time) {
        return Timestamp.valueOf(time).getTime();
    }

    public static View addFragment(Context context, int layout, int id) {
        View view = LayoutInflater.from(context).inflate(layout, null);
        BaseFragment fragment = NavigationFragment.getInstance(view);
        FragmentManager fm = ((BaseActivity) context).getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(id, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
        return view;
    }

    public static Fragment addFragment(Context context, Fragment fragment) {
        FragmentManager fm = ((BaseActivity) context).getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(android.R.id.content, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
        return fragment;
    }

    public static View replaceFragment(Context context, int layout, int id) {
        return replaceFragment(context, layout, id, true);
    }

    public static View replaceFragment(Context context, int layout, int id, boolean retainInstance) {
        View view = LayoutInflater.from(context).inflate(layout, null);
        BaseFragment fragment = NavigationFragment.getInstance(view);
        fragment.setRetainInstance(retainInstance);
        FragmentManager fm = ((BaseActivity) context).getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(id, fragment, (System.currentTimeMillis() + "_" + layout));
        ft.commit();
        return view;
    }

    public static View replaceFragment(Context context, View layout, int id) {
        BaseFragment fragment = NavigationFragment.getInstance(layout);
        fragment.setRetainInstance(true);
        FragmentManager fm = ((BaseActivity) context).getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(id, fragment, (System.currentTimeMillis() + "_" + layout));
        ft.commit();
        return layout;
    }

    public static View replaceFragment(Context context, View layout, int id, int enter, int exit) {
        BaseFragment fragment = NavigationFragment.getInstance(layout);
        FragmentManager fm = ((BaseActivity) context).getFragmentManager();
        fragment.setRetainInstance(true);
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(enter, exit);
        ft.replace(id, fragment, (System.currentTimeMillis() + "_" + layout));
        ft.commit();
        return layout;
    }

    public static Fragment replaceFragment(Context context, Fragment fragment) {
        fragment.setRetainInstance(true);
        FragmentManager fm = ((BaseActivity) context).getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(android.R.id.content, fragment, (System.currentTimeMillis() + "_" + fragment.getClass().getSimpleName()));
        ft.commit();
        return fragment;
    }
    */

    public static boolean appInstalledOrNot(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public static float pixelsToSp(Context context, Float px) {
        Logger.e("context.getResources().getDisplayMetrics().scaledDensity " + context.getResources().getDisplayMetrics().scaledDensity);
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px / scaledDensity;
    }

    public static int pixelsToSp(Context context, int px) {
        Logger.e("context.getResources().getDisplayMetrics().scaledDensity " + context.getResources().getDisplayMetrics().scaledDensity);
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (px / scaledDensity);
    }

    public static int dpToPx(int dp) {
        if (App.getContext() != null) {
            return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, App.getContext().getResources().getDisplayMetrics()));
        } else {
            return dp * 2;
        }
    }

    public static boolean isFragmentRestoring(Bundle savedInstanceState) {
        return savedInstanceState != null && savedInstanceState.containsKey(KEY1) && savedInstanceState.containsKey(KEY2);
    }

    public static boolean isMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static float distance(double lat_a, double lng_a, double lat_b, double lng_b) {
        return distance((float) lat_a, (float) lng_a, (float) lat_b, (float) lng_b);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void releaseImagesOnly(View v) {
        boolean isImageView = v instanceof ImageView;
        boolean isViewGroup = v instanceof ViewGroup;
        boolean isAdapterView = v instanceof AdapterView;
        Logger.e("releaseImagesOnly " + v.getClass().getSimpleName() + " isImageView:" + isImageView + " isViewGroup:" + isViewGroup + " isAdapterView:" + isAdapterView);
        if (isImageView) {
            if (((ImageView) v).getDrawable() != null) {
                ((ImageView) v).getDrawable().setCallback(null);
            }
            ((ImageView) v).setImageDrawable(null);
        } else {
            if (isViewGroup) {
                if (isAdapterView) {
                    Logger.e("releaseImagesOnly ViewPager child:" + (((ViewPager) v).getChildCount()));
                    ((AdapterView) v).setAdapter(null);
                }
                for (int i = 0; i < ((ViewGroup) v).getChildCount(); i++) {
                    releaseImagesOnly(((ViewGroup) v).getChildAt(i));
                }
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void releaseImages(View v) {
        if (v instanceof ImageView) {
            if (((ImageView) v).getDrawable() != null) {
                ((ImageView) v).getDrawable().setCallback(null);
            }
            ((ImageView) v).setImageDrawable(null);
            removeFromParent(v);
        } else if (v instanceof ViewGroup) {
            if (v instanceof AdapterView) {
                ((AdapterView) v).setAdapter(null);
            }
            for (int i = 0; i < ((ViewGroup) v).getChildCount(); i++) {
                releaseImages(((ViewGroup) v).getChildAt(i));
            }
        } else {
            removeFromParent(v);
        }
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public static void removeFromParent(View v) {
        if (v.getBackground() != null) {
            v.getBackground().setCallback(null);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                v.setBackgroundDrawable(null);
            } else {
                v.setBackground(null);
            }
        }
        if (v.getTag() != null) {
            v.setTag(null);
        }
    }

    @SuppressLint("UseValueOf")
    public static float distance(float lat_a, float lng_a, float lat_b, float lng_b) {
        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat_b - lat_a);
        double lngDiff = Math.toRadians(lng_b - lng_a);
        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) + Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) * Math.sin(lngDiff / 2) * Math.sin(lngDiff / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;
        int meterConversion = 1609;
        return new Float(distance * meterConversion).floatValue();
    }

    @SuppressLint("DefaultLocale")
    public static String getVersionName(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String lv = pi.versionName;
            // 소수점 이하 두자리 까지만 검사
            return String.format("%.2f", Float.valueOf(lv));
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    public static long convertLocalTimeToUTC(long pv_localDateTime) {
        long lv_UTCTime = pv_localDateTime;
        TimeZone z = TimeZone.getDefault();
        int offset = z.getOffset(pv_localDateTime);
        lv_UTCTime = pv_localDateTime - offset;
        return lv_UTCTime;
    }

    public static long convertUTCToLocalTime(long pv_UTCDateTime) {
        long lv_localDateTime = pv_UTCDateTime;
        TimeZone z = TimeZone.getDefault();
        int offset = z.getOffset(pv_UTCDateTime);
        lv_localDateTime = pv_UTCDateTime + offset;
        return lv_localDateTime;
    }

    /**
     * Helper method to get a SharedPreferences instance.
     */
    public static SharedPreferences getSharedPreferences(Context context, String key) {
        return context.getSharedPreferences(key, 0);
    }

    /**
     * Helper method to get a SharedPreferences instance.
     */
    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(CONFIG, 0);
    }

	/*
     * public static void appyTypeface( Context context, View v ) {
	 * if ( !(context instanceof BaseActivity) ) {
	 * throw new IllegalArgumentException( "context is not a instance of BaseActivity" );
	 * }
	 * Typeface tf = (( BaseActivity ) context).getTypeface();
	 * applyTypeface( tf, v );
	 * }
	 * private static void applyTypeface( Typeface tf, View v ) {
	 * if ( v instanceof TextView ) {
	 * (( TextView ) v).setTypeface( tf );
	 * } else if ( v instanceof ViewGroup ) {
	 * for ( int i = 0 ; i < (( ViewGroup ) v).getChildCount() ; i++ ) {
	 * applyTypeface( tf, (( ViewGroup ) v).getChildAt( i ) );
	 * }
	 * }
	 * }
	 */

    public static String getNameById(Resources resources, int id) {
        return resources.getResourceEntryName(id);
    }

    public static void saveAccount(Context context, String id, String pw) {
        Editor editor = getSharedPreferences(context).edit();
        editor.putString(ACCOUNT_ID, id);
        editor.putString(ACCOUNT_PW, pw);
        editor.commit();
    }

    public static void deleteAccount(Context context) {
        Editor editor = getSharedPreferences(context).edit();
        editor.remove(ACCOUNT_ID);
        editor.remove(ACCOUNT_PW);
        editor.commit();
    }

    public static Pair<String, String> getAccount(Context context) {
        SharedPreferences pref = getSharedPreferences(context);
        return new Pair<String, String>(pref.getString(ACCOUNT_ID, null), pref.getString(ACCOUNT_PW, null));
    }

    public static boolean isFirstLaunch(Context context) {
        SharedPreferences pref = getSharedPreferences(context);
        boolean isFirstLaunch = pref.getBoolean("isFirstLaunch", true);
        if (isFirstLaunch) {
            pref.edit().putBoolean("isFirstLaunch", false).commit();
        }
        return isFirstLaunch;
    }

    /**
     * 키보드 숨기기
     *
     * @param context    context
     * @param myEditText EditText 객체 (키보드를 나타나게한..)
     */
    public static void hideKeyboard(Context context, EditText myEditText) {
        Logger.e("context " + context + " myEditText " + myEditText);
        if (context != null && myEditText != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
            }
        }
    }

    /**
     * 키보드 숨기기
     *
     * @param context    context
     * @param myEditText EditText 객체 (키보드를 나타나게한..)
     */
    public static void showKeyboard(Context context, EditText myEditText) {
        Logger.e("context " + context + " myEditText " + myEditText);
        if (context != null && myEditText != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(myEditText, InputMethodManager.SHOW_FORCED);
            }
        }
    }

    /**
     * 텍스트뷰에 원래 텍스트 크기와 다른 텍스트를 덧붙인다.
     *
     * @param size 1.0 을 기준으로 크거나 작은 float 수치를 전달해서 덧붙일 텍스트 크기 조절
     * @param view 대상 TextView
     * @param text 추가될 텍스트
     */
    public static void appendResizedText(float size, TextView view, String text) {
        String original = view.getText().toString();
        String appended = original + text;
        int index = original.length();
        int length = appended.length();
        Spannable span = new SpannableString(appended);
        span.setSpan(new RelativeSizeSpan(size), index, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        view.setText(span);
    }

    public static File getFilteredImage() {
        String path = Environment.getExternalStorageDirectory() + File.separator + "oligomonFiltered.jpg";
        File f = new File(path);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return f;
    }

    public static String getSavedServerIp(Context context) {
        return getSharedPreferences(context).getString("server", null);
    }

    public static void setSavedServerIp(Context context, String server) {
        getSharedPreferences(context).edit().putString("server", server).commit();
    }

    public static View getDividerV(Context context) {
        View v = new View(context);
        v.setBackgroundColor(0xffdddddd);
        v.setMinimumHeight(1);
        return v;
    }

    public static int getExactly(int size) {
        return View.MeasureSpec.makeMeasureSpec(size, View.MeasureSpec.EXACTLY);
    }

    public static boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
        for (String string : subset) {
            if (!superset.contains(string)) {
                return false;
            }
        }
        return true;
    }

    public static void copyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static boolean openUrl(Context context, String url) {
        try {
            if (url == null)
                return false;
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void goUpdate(Context context) {
        openUrl(context, getMarketUrl(context));
    }

    public static String getMarketUrl(Context context) {
        String id = getAppId(context);
        return "market://details?id=" + id;
    }

    public static String getAppId(Context context) {
        return context.getApplicationInfo().packageName;
    }

    public static float spToPixel(int i) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, i, App.getContext().getResources().getDisplayMetrics());
    }

    public static int spToPixelInt(int i) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, i, App.getContext().getResources().getDisplayMetrics()));
    }

    public static void sendNotification(Context context, String s) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context).setContentTitle("My에버리지")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(s)).setContentText(s).setAutoCancel(true).setLights(0x00ff00, 2000, 3000)
                .setDefaults(Notification.DEFAULT_SOUND).setVibrate(new long[]{100, 100, 100});
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }

    public static String getCacheDir(Context context) {
        return context.getCacheDir() + File.separator + "temp";
    }

    public static void clearCache(Context context) {
        File dir = new File(getCacheDir(context));
        if (dir.exists() && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                new File(dir, children[i]).delete();
            }
        }
    }

    public static void setTextValid(String text, TextView textView) {
        if (StringUtil.notEmpty(text) && textView != null) {
            textView.setText(text);
        }
    }

    public static boolean supportsViewElevation() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
    }

    public static Drawable getDrawable(Resources resources, int drawableResource) {
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= 21) {
            drawable = resources.getDrawable(drawableResource, null);
        } else {
            drawable = resources.getDrawable(drawableResource);
        }
        return drawable;
    }
}