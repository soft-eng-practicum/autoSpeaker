package gluka.autospeakerphone;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Created by C-Notez on 2/26/2018.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void onCreate() throws Exception {

    }

    @Test
    public void getSpeaker() throws Exception {
        boolean output;
        boolean expected = true;
        MainActivity.setSpeaker(expected);
        output = MainActivity.getSpeaker();
        Assert.assertEquals(expected, output);
    }

    @Test
    public void speakerphoneSwitch() throws Exception {
        boolean expected = true;
        Assert.assertEquals(expected, MainActivity.getSpeaker());
        //Click on change button
        onView(withId(R.id.switch1)).perform(click());
        expected = false;
        Assert.assertEquals(expected, MainActivity.getSpeaker());
        //Click on change button
        onView(withId(R.id.switch1)).perform(click());

    }
}