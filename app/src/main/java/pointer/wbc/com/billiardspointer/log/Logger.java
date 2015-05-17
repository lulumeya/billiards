package pointer.wbc.com.billiardspointer.log;

import android.util.Log;

import java.util.ArrayList;

import pointer.wbc.com.billiardspointer.App;

public class Logger {

    private static final String TAG = "HealingBeauty";
    private static final boolean needLong = true;
    public static final int LOG_SEPARATE_LENGTH = 3000;

    public static ArrayList<String> longInfo(String str) {
        ArrayList<String> response = new ArrayList<String>();
        if (needLong) {
            if (str.length() > LOG_SEPARATE_LENGTH) {
                while (str.length() > LOG_SEPARATE_LENGTH) {
                    response.add(str.substring(0, LOG_SEPARATE_LENGTH));
                    str = str.substring(LOG_SEPARATE_LENGTH);
                }
                response.add(str);
            } else {
                response.add(str);
            }
        } else {
            response.add(str);
        }
        return response;
    }

    public static void e(String tag, Object message) {
        if (App.isDebuggable()) {
            String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
            int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
            ArrayList<String> response = longInfo(className + "." + methodName + "():" + lineNumber + "   " + message);
            for (String s : response) {
                Log.e(tag, s);
            }
        }
    }

    public static void v(String tag, Object message) {
        if (App.isDebuggable()) {
            String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
            int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
            ArrayList<String> response = longInfo(className + "." + methodName + "():" + lineNumber + "   " + message);
            for (String s : response) {
                Log.v(tag, s);
            }
        }
    }

    public static void e(Object message) {
        String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
        int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
        ArrayList<String> response = longInfo(className + "." + methodName + "():" + lineNumber + "   " + message);
        for (String s : response) {
            Log.e(TAG, s);
        }
    }

    public static void a(Object message) {
        if (App.isDebuggable()) {
            String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
            int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
            ArrayList<String> response = longInfo(className + "." + methodName + "():" + lineNumber + "   " + message);
            for (String s : response) {
                Log.println(Log.ASSERT, TAG, s);
            }
        }
    }

    public static void v(Object message) {
        if (App.isDebuggable()) {
            String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
            int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
            ArrayList<String> response = longInfo(className + "." + methodName + "():" + lineNumber + "   " + message);
            for (String s : response) {
                Log.v(TAG, s);
            }
        }
    }

    public static void d(String tag, Object message) {
        if (App.isDebuggable()) {
            String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
            int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
            ArrayList<String> response = longInfo(className + "." + methodName + "():" + lineNumber + "   " + message);
            for (String s : response) {
                Log.d(tag, s);
            }
        }
    }

    public static void d(Object message) {
        if (App.isDebuggable()) {
            String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
            int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
            ArrayList<String> response = longInfo(className + "." + methodName + "():" + lineNumber + "   " + message);
            for (String s : response) {
                Log.d(TAG, s);
            }
        }
    }

    public static void i(Object message) {
        if (App.isDebuggable()) {
            String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
            int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
            ArrayList<String> response = longInfo(className + "." + methodName + "():" + lineNumber + "   " + message);
            for (String s : response) {
                Log.i(TAG, s);
            }
        }
    }

    public static void objs(Object... objects) {
        if (App.isDebuggable()) {
            String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
            int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
            StringBuilder sb = new StringBuilder();
            for (Object object : objects) {
                if (object != null) {
                    sb.append(object.toString());
                    sb.append("  ");
                }
            }
            ArrayList<String> response = longInfo(className + "." + methodName + "():" + lineNumber + "   " + sb.toString());
            for (String s : response) {
                Log.i(TAG, s);
            }
        }
    }

    public static void i(Object... objects) {
        if (App.isDebuggable()) {
            String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
            int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
            StringBuilder sb = new StringBuilder();
            for (Object object : objects) {
                if (object != null) {
                    sb.append(object.toString());
                    sb.append("  ");
                }
            }
            ArrayList<String> response = longInfo(className + "." + methodName + "():" + lineNumber + "   " + sb.toString());
            for (String s : response) {
                Log.i(TAG, s);
            }
        }
    }

    public static void clean(Object message) {
        if (App.isDebuggable() && message != null) {
            ArrayList<String> response = longInfo(message.toString());
            for (String s : response) {
                Log.e(TAG, s);
            }
        }
    }

    @SuppressWarnings("unused")
    private static String toString(Object... objects) {
        StringBuilder sb = new StringBuilder();
        for (Object o : objects) {
            sb.append(o);
        }
        return sb.toString();
    }
}