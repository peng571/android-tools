package dev.momo.library.core.log;

import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.IllegalFormatConversionException;
import java.util.Locale;

/**
 * Created by momopeng on 11/25/15.
 */
public class Logger {

    private static final String TAG = Logger.class.getSimpleName();

    public static final int LOG_ALL = 10;
    public static final int LOG_V = 4;
    public static final int LOG_D = 3;
    public static final int LOG_W = 2;
    public static final int LOG_E = 1;
    public static final int LOG_NONE = 0;

    private static int logLevel = LOG_ALL;
    private static String APP_TAG = "MOMO"; // DEFAULT APP_LOG

    private static final String TAG_FORMAT = "%s_%s";
    private static final String SYSTEM_LOG_FORMAT = "%s(%d) :%s";


    private static String getLogType(int logType) {
        switch (logType) {
            default:
            case LOG_NONE:
                return "N";
            case LOG_ALL:
                return "A";
            case LOG_D:
                return "D";
            case LOG_E:
                return "E";
            case LOG_V:
                return "V";
        }
    }

    /**
     * LoggerView setting
     * <p>
     * see @LoggerView.class to know more.
     * remember to removeLogView when exist logger view parent activtity or fragment.
     */
    private static LogView loggerView;

    public static void setLogView(LogView view) {
        loggerView = view;
    }

    public static void removeLogView() {
        loggerView = null;
    }

    public static void setLogLevel(int level) {
        logLevel = level;
    }


    /**
     * TAG setting
     * @param APP_TAG
     */
    public static void setTAG(String APP_TAG) {
        Logger.APP_TAG = APP_TAG;
    }

    private static String logFilePath;

    /**
     * Info Method
     */
    public static void I(String tag, String log) {
        String fullTag = String.format(Locale.getDefault(), TAG_FORMAT, APP_TAG, tag);
        if (logLevel >= LOG_V) {
            if (log == null) log = "";
            printLogcat(fullTag, LOG_V, log, null);
            printSystemLog(fullTag, LOG_V, log, null);
            appendLog(fullTag, LOG_V, log);

        }

        if (loggerView != null) {
            loggerView.addMessage(fullTag, LOG_V, log);
        }
    }

    public static void I(String tag, String log, Object... args) {
        String format = log;
        try {
            format = String.format(log, args);
        } catch (IllegalFormatConversionException e) {
            Logger.E(TAG, e);
        }
        I(tag, format);
    }


    /**
     * Debug Method
     */
    public static void D(String tag, String log) {
        String fullTag = String.format(Locale.getDefault(), TAG_FORMAT, APP_TAG, tag);
        if (logLevel >= LOG_D) {
            if (log == null) log = "";
            printLogcat(fullTag, LOG_D, log, null);
            printSystemLog(fullTag, LOG_D, log, null);
            appendLog(fullTag, LOG_D, log);

        }

        if (loggerView != null) {
            loggerView.addMessage(fullTag, LOG_D, log);
        }
    }

    public static void D(String tag, String log, Object... args) {
        String format = log;
        try {
            format = String.format(log, args);
        } catch (IllegalFormatConversionException e) {
            Logger.E(String.format(Locale.getDefault(), TAG_FORMAT, APP_TAG, tag), e);
        }
        D(tag, format);
    }


    /**
     * Warming Method
     */
    public static void W(String tag, String log, Throwable error) {
        String fullTag = String.format(Locale.getDefault(), TAG_FORMAT, APP_TAG, tag);
        if (logLevel >= LOG_W) {
            if (log == null) log = "";
            if (error == null) {
                Log.w(fullTag, log);
            } else {
                Log.w(fullTag, log, error);
            }
            printLogcat(fullTag, LOG_W, log, error);
            printSystemLog(fullTag, LOG_W, log, error);
            appendLog(fullTag, LOG_W, log, error);

        }

        if (loggerView != null) {
            loggerView.addMessage(fullTag, LOG_W, log);
            if (error != null) {
                loggerView.addMessage(fullTag, LOG_W, error.getMessage());
            }
        }
    }

    public static void WS(String tag, String log) {
        String fullTag = String.format(Locale.getDefault(), TAG_FORMAT, APP_TAG, tag);
        if (logLevel >= LOG_W) {
            if (log == null) log = "";
            printLogcat(fullTag, LOG_W, log, null);
            printSystemLog(fullTag, LOG_W, log, null);
            appendLog(fullTag, LOG_W, log);
        }

        if (loggerView != null) {
            loggerView.addMessage(fullTag, LOG_W, log);
        }
    }

    public static void WS(String tag, String log, Object... args) {
        if (log == null) return;
        String format = log;
        try {
            format = String.format(Locale.getDefault(), log, args);
        } catch (IllegalFormatConversionException e) {
            Logger.E(TAG, e);
        }
        Logger.WS(tag, format);
    }


    /**
     * Error Method
     */
    public static void E(String tag, String log, @Nullable Throwable error) {
        String fullTag = String.format(Locale.getDefault(), TAG_FORMAT, APP_TAG, tag);
        if (log == null) log = "";
        if (logLevel >= LOG_E) {
            printLogcat(fullTag, LOG_E, log, error);
            printSystemLog(fullTag, LOG_E, log, error);
            appendLog(fullTag, LOG_E, log, error);
        }

        if (loggerView != null) {
            loggerView.addMessage(fullTag, LOG_E, log);
            if (error != null) {
                loggerView.addMessage(fullTag, LOG_E, error.getMessage());
            }
        }
    }

    public static void E(String tag, Throwable error) {
        E(tag, null, error);
    }


    public static void ES(String tag, String log) {
        if (log == null) return;
        Logger.E(tag, log, null);
    }


    public static void ES(String tag, String log, Object... args) {
        if (log == null) return;
        String format = log;
        try {
            format = String.format(Locale.getDefault(), log, args);
        } catch (IllegalFormatConversionException e) {
            Logger.E(TAG, e);
        }
        Logger.ES(tag, format);
    }

    /**
     * print log with android logcat
     */
    // android studio logcat has max limit 1024*4 for each log, so cut log to show all of string
    private final static int LOG_CAT_MAX = 3000;

    private static void printLogcat(String tag, int level, String sb, Throwable e) {
        if (sb.length() < LOG_CAT_MAX) {
            printCutLogcat(tag, level, sb, e);
        } else {
            int chunkCount = sb.length() / LOG_CAT_MAX;     // integer division
            for (int i = 0; i <= chunkCount; i++) {
                int max = LOG_CAT_MAX * (i + 1);
                if (max >= sb.length()) {
                    printCutLogcat(tag + "_" + i, level, sb.substring(LOG_CAT_MAX * i), e);
                } else {
                    printCutLogcat(tag + "_" + i, level, sb.substring(LOG_CAT_MAX * i, max), e);
                }
            }
        }
    }

    private static void printCutLogcat(String tag, int level, String sb, Throwable e) {
        switch (level) {
            case LOG_V:
                Log.v(tag, sb);
                break;
            default:
            case LOG_D:
                Log.d(tag, sb);
                break;
            case LOG_W:
                if (e == null) {
                    Log.w(tag, sb);
                } else {
                    Log.w(tag, sb, e);
                }
                break;
            case LOG_E:
                if (e == null) {
                    Log.e(tag, sb);
                } else {
                    Log.e(tag, sb, e);
                }
                break;
        }
    }


    /**
     * print log with java system (for junit test)
     */
    private static void printSystemLog(String tag, int level, String log, Throwable e) {
        String logMessage = String.format(Locale.getDefault(), SYSTEM_LOG_FORMAT, tag, level, log);
        switch (level) {
            case LOG_E:
            case LOG_W:
                System.err.println(logMessage);
                if (e != null) {
                    System.err.println(String.format(Locale.getDefault(), SYSTEM_LOG_FORMAT, tag, level, e.getMessage()));
                }
                break;
            default:
                System.out.println(logMessage);
        }
    }


    /**
     * log file methods
     */
    public static void setLogFile(String filePath) {
        logFilePath = filePath;
    }

    public static String setLogFile() {
        if (logFilePath == null) return "";
        return logFilePath;
    }

    private final static String LOG_TIME_FORMAT = "MM/dd HH:mm:ss.Z";
    private final static SimpleDateFormat LOG_TIME_FOMATER = new SimpleDateFormat(LOG_TIME_FORMAT, Locale.getDefault());

    private final static String LOG_FORMAT = "%s (%s) [%s] %s\n";
    private final static String LOG_FORMAT_E = "%s (%s) [%s] %s: %s\n";


    private synchronized static void appendLog(String tag, int logType, String text, Throwable ex) {
        if (ex == null) {
            appendLog(tag, logType, text);
            return;
        }
        appendLog(String.format(Locale.getDefault(), LOG_FORMAT_E,
                LOG_TIME_FOMATER.format(System.currentTimeMillis()),
                getLogType(logType),
                tag, text, ex.toString()));
    }

    private synchronized static void appendLog(String tag, int logType, String text) {
        appendLog(String.format(Locale.getDefault(), LOG_FORMAT,
                LOG_TIME_FOMATER.format(System.currentTimeMillis()),
                getLogType(logType),
                tag,
                text));
    }

    private synchronized static void appendLog(String text) {
        if (logFilePath == null || logFilePath.isEmpty()) return;
        File logFile = new File(logFilePath);
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "", e);
            }
        }
        try {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "", e);
        }
    }
}
