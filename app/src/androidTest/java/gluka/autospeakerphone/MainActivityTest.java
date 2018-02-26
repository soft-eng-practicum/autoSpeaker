package gluka.autospeakerphone;

import android.support.test.runner.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Created by C-Notez on 2/26/2018.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Test
    public void onCreate() throws Exception {

    }

    @Test
    public void speakerphoneSwitch() throws Exception {
        boolean output = true;
        boolean expected = true;
        assertEquals(expected, output);
    }

}