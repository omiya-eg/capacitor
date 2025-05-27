package com.getcapacitor;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Logger {

    public static final String LOG_TAG_CORE = "Capacitor";
    public static CapConfig config;

    private static final List<String> history = new ArrayList<>();
    private static final int historyLimit = 3000;

    private static Logger instance;

    private static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public static void init(CapConfig config) {
        Logger.getInstance().loadConfig(config);
    }

    private void loadConfig(CapConfig config) {
        Logger.config = config;
    }

    public static String tags(String... subtags) {
        if (subtags != null && subtags.length > 0) {
            return LOG_TAG_CORE + "/" + TextUtils.join("/", subtags);
        }

        return LOG_TAG_CORE;
    }

    public static void verbose(String message) {
        verbose(LOG_TAG_CORE, message);
    }

    public static void verbose(String tag, String message) {
        if (!shouldLog()) return;
        Log.v(tag, message);
        addToHistory("[V][" + tag + "] " + message);
    }

    public static void debug(String message) {
        debug(LOG_TAG_CORE, message);
    }

    public static void debug(String tag, String message) {
        if (!shouldLog()) return;
        Log.d(tag, message);
        addToHistory("[D][" + tag + "] " + message);
    }

    public static void info(String message) {
        info(LOG_TAG_CORE, message);
    }

    public static void info(String tag, String message) {
        if (!shouldLog()) return;
        Log.i(tag, message);
        addToHistory("[I][" + tag + "] " + message);
    }

    public static void warn(String message) {
        warn(LOG_TAG_CORE, message);
    }

    public static void warn(String tag, String message) {
        if (!shouldLog()) return;
        Log.w(tag, message);
        addToHistory("[W][" + tag + "] " + message);
    }

    public static void error(String message) {
        error(LOG_TAG_CORE, message, null);
    }

    public static void error(String message, Throwable e) {
        error(LOG_TAG_CORE, message, e);
    }

    public static void error(String tag, String message, Throwable e) {
        if (!shouldLog()) return;
        Log.e(tag, message, e);
        addToHistory("[E][" + tag + "] " + message);
    }

    public static boolean shouldLog() {
        return config == null || config.isLoggingEnabled();
    }

    // 🆕 以下：履歴管理用メソッド

    private static void addToHistory(String message) {
        synchronized (history) {
            String line = message.length() > 4068 ? message.substring(0, 4068) : message;
            history.add(line);
            if (history.size() > historyLimit) {
                history.subList(0, history.size() - historyLimit).clear();
            }
        }
    }

    public static List<String> getHistory(int max) {
        synchronized (history) {
            if (max > 0 && max < history.size()) {
                return new ArrayList<>(history.subList(history.size() - max, history.size()));
            } else {
                return new ArrayList<>(history);
            }
        }
    }

    public static void clearHistory() {
        synchronized (history) {
            history.clear();
        }
    }
}

