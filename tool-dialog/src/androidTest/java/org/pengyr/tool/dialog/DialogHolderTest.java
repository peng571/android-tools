package dev.momo.library.view.dialog;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import dev.momo.library.view.R;

import static dev.momo.library.view.dialog.DialogConstants.TAG_OK;

/**
 * Created by momo peng on 2018/3/13.
 */

@RunWith(AndroidJUnit4.class)
public class DialogHolderTest extends ActivityInstrumentationTestCase2<DialogHolderActivity> {

    DialogHolderActivity activity;

    public DialogHolderTest(Class<DialogHolderActivity> activityClass) {
        super(activityClass);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();

        // Injecting the Instrumentation instance is required
        // for your test to run with AndroidJUnitRunner.
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        activity = getActivity();
    }


    @Test
    public void okDialogTest() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        OKDialog.newInstance(R.string.double_click_to_quit, 6).show(activity.getFragmentManager(), TAG_OK);


        assertEquals("android.alchema.com.app", appContext.getPackageName());
    }

}

