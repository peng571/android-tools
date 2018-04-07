package dev.momo.library.view.dialog;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;

/**
 * Created by momo peng on 2018/3/13.
 */
@RunWith(AndroidJUnit4.class)
public class OkDialogTest {

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        //        OKDialog.newInstance(R.string.double_click_to_quit, 6).show(appContext.);

        assertEquals("android.alchema.com.app", appContext.getPackageName());
    }
}
