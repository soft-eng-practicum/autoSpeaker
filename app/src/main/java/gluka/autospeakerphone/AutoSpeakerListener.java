package gluka.autospeakerphone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import static gluka.autospeakerphone.PrototypeWidget.getSpeaker;

/**
 * Created by Developer on 2/13/18.
 */

public class AutoSpeakerListener extends BroadcastReceiver {

    AudioManager audioManager =null; //(AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
     //telephonyManager = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Log.i("Progress","Receiver initialized");
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
            /**
            // Listens to incoming call
            if(state.equals(TelephonyManager.EXTRA_STATE_RINGING))
            {
                Log.i("Progress", "Incoming call");
            }
            */
            // Answered call - AppWidget
            if((state.equals(telephonyManager.EXTRA_STATE_OFFHOOK)) && (getSpeaker()))
            {
                audioManager.setMode(AudioManager.MODE_IN_CALL);
                audioManager.setSpeakerphoneOn(true);
                Log.i("Progress", "Call Answered");
                Log.d("TAG", "Phone State: getSpeaker() = " + getSpeaker());
            }
            else if((state.equals(telephonyManager.EXTRA_STATE_OFFHOOK)) && (getSpeaker() == false))
            {
                audioManager.setMode(AudioManager.MODE_IN_CALL);
                audioManager.setSpeakerphoneOn(false);
                Log.i("Progress", "Call Answered");
                Log.d("TAG", "Phone State: getSpeaker() = " + getSpeaker());
            }
            // Answered call - MainActivity Switch
            else if ((state.equals(telephonyManager.EXTRA_STATE_OFFHOOK)) && MainActivity.getSpeaker())
            {
                audioManager.setMode(AudioManager.MODE_IN_CALL);
                audioManager.setSpeakerphoneOn(true);
                Log.i("Progress", "Call Answered");
                Log.d("TAG", "Phone State: MainActivity.getSpeaker() = " + MainActivity.getSpeaker());
            }
            else if((state.equals(telephonyManager.EXTRA_STATE_OFFHOOK)) && MainActivity.getSpeaker() == false)
            {
                audioManager.setMode(AudioManager.MODE_IN_CALL);
                audioManager.setSpeakerphoneOn(false);
                Log.i("Progress", "Call Answered");
                Log.d("TAG", "Phone State: MainActivity.getSpeaker() = " + MainActivity.getSpeaker());
            }

            if (state.equals(telephonyManager.EXTRA_STATE_IDLE)){
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
}
