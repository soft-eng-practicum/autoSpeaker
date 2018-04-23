package gluka.autospeakerphone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by Developer on 2/13/18.
 */

public class AutoSpeakerListener extends BroadcastReceiver {

    AudioManager audioManager =null; //(AudioManager)context.getSystemService(Context.AUDIO_SERVICE);

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Log.i("Progress","AutoSpeakerListener Class : Receiver Initialized");
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);

            // Listens to incoming call
            if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                Log.i("Progress", "Incoming call, state = " + state);
                Log.i("Progress", "Incoming call, TelephoneManager = " + TelephonyManager.EXTRA_STATE_RINGING);
            }

            // Answered call
            if ((state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))){

                audioManager.setMode(AudioManager.MODE_IN_CALL);
                audioManager.setSpeakerphoneOn(true);

                Log.i("Progress", "Call Answered, state = " + state);
                Log.i("Progress", "Incoming call, TelephoneManager = " + TelephonyManager.EXTRA_STATE_OFFHOOK);
            }

            // Call ended
            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)){

                audioManager.setMode(AudioManager.MODE_NORMAL);
                audioManager.setSpeakerphoneOn(false);

                Log.i("Progress", "Call ended state = " + state);
                Log.i("Progress", "Incoming call, TelephoneManager = " + TelephonyManager.EXTRA_STATE_IDLE);

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
