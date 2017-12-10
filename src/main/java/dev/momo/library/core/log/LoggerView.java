package dev.momo.library.core.log;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;

import dev.momo.library.core.Helper;

/**
 * Created by Peng on 2016/12/2.
 */
public class LoggerView extends AppCompatTextView {


    private String filterTag = "";
    private int logLevel = Logger.LOG_ALL;

    boolean topToBottom = false; // show log from top to bottom or bottom to top


    public LoggerView(Context context) {
        super(context);
        if (!isInEditMode()) {
            init();
        }

    }

    public LoggerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            init();
        }
    }


    private void init() {
        setMovementMethod(new ScrollingMovementMethod());
    }

    public void addMessage(String TAG, int level, final String message) {
        if (!TAG.contains(filterTag)) return;
        if (logLevel < level) return;

        Helper.mainUIHandler.post(() -> {
            if (isTopToBottom()) {
                // append the new string to bottom
                append(message + "\n");
                // find the amount we need to scroll.  This works by
                // asking the TextView's internal layout for the position
                // of the final line and then subtracting the TextView's height
                final int scrollAmount = getLayout().getLineTop(getLineCount() - 1) - getHeight();
                // if there is no need to scroll, scrollAmount will be <=0
                if (scrollAmount > 0) {
                    scrollTo(0, scrollAmount);
                } else {
                    scrollTo(0, 0);
                }
            } else {
                setText(message + "\n" + getText());
            }
        });
    }


    public void setTagFilter(String tag) {
        this.filterTag = tag;
    }

    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }


    public boolean isTopToBottom() {
        return topToBottom;
    }

    public void setTopToBottom(boolean topToBottom) {
        this.topToBottom = topToBottom;
    }

}
