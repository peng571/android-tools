package org.pengyr.tool.core.util;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;

/**
 * Created by momo peng on 2018/3/21.
 */

public class HtmlUtil {

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_COMPACT);
        } else {
            return Html.fromHtml(source);
        }
    }

    //    @SuppressWarnings("deprecation")
    //    public static Spanned fromHtml(@StringRes int sourceRes) {
    //        return fromHtml(ResourceHelper.getString(sourceRes));
    //    }
}
