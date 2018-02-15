package gluka.autospeakerphone;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import org.w3c.dom.Text;

/**
 * Created by Gluka on 2/8/18.
 */

public class TelephonyService extends Service {

    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
    final Text text = null;
    Intent i = new Intent(this,MainActivity.class);

    // Create a new PhoneStateListener
    PhoneStateListener listener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            String stateString = "N/A";
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    stateString = "Idle";
                    Toast.makeText(getApplicationContext(),"idle",Toast.LENGTH_SHORT).show();
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    stateString = "Off Hook";
                    Toast.makeText(getApplicationContext(),"OFF",Toast.LENGTH_SHORT).show();
                    Log.d("Progress", "Telephony class called, call answered");
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    stateString = "Ringing";
                    Toast.makeText(getApplicationContext(),"Ring",Toast.LENGTH_SHORT).show();
                    break;
            }
            if(TelephonyManager.NETWORK_TYPE_UNKNOWN==0){
                Toast.makeText(getApplicationContext(),"wrong",Toast.LENGTH_SHORT).show();
            }
            if(TelephonyManager.SIM_STATE_ABSENT==1){
                Toast.makeText(getApplicationContext(),"no sim",Toast.LENGTH_SHORT).show();

            }

        }

    };


    @Override
    public IBinder onBind(Intent intent) {
        intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        return null;
    }

}
