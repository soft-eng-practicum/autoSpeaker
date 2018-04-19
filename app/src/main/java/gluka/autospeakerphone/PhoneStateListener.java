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

public class PhoneStateListener extends BroadcastReceiver {

    AudioManager audioManager =null; //(AudioManager)context.getSystemService(Context.AUDIO_SERVICE);

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Log.i("Progress","Receiver initialized");
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
            // Listens to incoming call
            if(state.equals(TelephonyManager.EXTRA_STATE_RINGING))
            {
                Log.i("Progress", "Incoming call");
            }
            // Answered call
            if ((state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) && (MainActivity.getSpeaker() || PrototypeWidget.getSpeaker())){
                Log.d("TAG", "Phone State: getSpeaker() = " + MainActivity.getSpeaker());

                audioManager.setMode(AudioManager.MODE_IN_CALL);
                audioManager.setSpeakerphoneOn(true);
                Log.i("Progress", "Call Answered");
            }
            else if((state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) && ((MainActivity.getSpeaker() == false) || (PrototypeWidget.getSpeaker() == false)))
            {
                Log.d("TAG", "Phone State: getSpeaker() = " + MainActivity.getSpeaker());
                audioManager.setMode(AudioManager.MODE_IN_CALL);
                audioManager.setSpeakerphoneOn(false);
                Log.i("Progress", "Call Answered");
            }
            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                Log.i("Progress", "Call ended");
                Log.e("Progress", "Service idle");
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
