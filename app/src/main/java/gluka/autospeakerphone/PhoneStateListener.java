package gluka.autospeakerphone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by Developer on 2/13/18.
 */

public class PhoneStateListener extends BroadcastReceiver {

    AudioManager audioManager =null; //(AudioManager)context.getSystemService(Context.AUDIO_SERVICE);

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Log.i("Progress","Receiver initialized");
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

            // Listens to incoming call
            if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                Log.i("Progress", "Incoming call");
            }
            // Answered call
            if ((state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))){
                audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
                audioManager.setMode(AudioManager.MODE_IN_CALL);
                audioManager.setSpeakerphoneOn(true);

                Log.i("Progress", "Call Answered");
            }
            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                Log.i("Progress", "Call ended");
                audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
                audioManager.setMode(AudioManager.MODE_NORMAL);
                audioManager.setSpeakerphoneOn(false);

                Log.i("Progress", "Call ended");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void disableBroadCastReceiver(Context context, Intent intent) {


        Log.d("Progress", "disableBroadCastReceiver()");
    }
}
