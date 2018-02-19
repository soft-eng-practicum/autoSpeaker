package gluka.autospeakerphone;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    AudioManager audioManager;
    boolean isSpeakerphoneOn;

    Context context;
    Intent intent;
    //boolean switch1 = false;
    PhoneStateListener phoneStateListener = new PhoneStateListener();
    Switch switch1;
    //bundle used to ssave state of switch
    Bundle switchBundle = new Bundle();
    boolean isSpeakerOn =true;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button favList = (Button)findViewById(R.id.favList);
        switch1 = (Switch)findViewById(R.id.switch1);
        Log.d("states", "onCreate: switch " + isSpeakerOn);
        //Required to set Speakerphone On/Off
        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        if (savedInstanceState != null) {
            // Restore value of members from saved state
            boolean saved = savedInstanceState.getBoolean("switchKey");
            isSpeakerOn = saved;
            Log.d("states", "onCreate: " + saved + " " +isSpeakerOn);

        }

        // call the service with all the telephony stuff
        startService(new Intent(this,TelephonyService.class));

        //testing without sim card in my phone -- it works
        if(TelephonyManager.SIM_STATE_ABSENT ==1 )
           Toast.makeText(getApplicationContext(),"No Sim",Toast.LENGTH_SHORT).show();

        favList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent favorite = new Intent(getApplicationContext(), FavoriteList.class);
                startActivity(favorite);
            }
        });

        //messing with the switch
        switch1.setChecked(isSpeakerOn);
        speakerphoneSwitch(switch1);
        /**
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isSpeakerphoneOn = isChecked;
                if(isChecked){
                    Toast.makeText(getApplicationContext(),"On",Toast.LENGTH_SHORT).show();

                    //Set Speakerphone On
                    audioManager.setSpeakerphoneOn(true);
                    isSpeakerOn = true;
                    // Listens to phone state when switch is turn on
                    phoneStateListener.onReceive(context,intent);
                    //savedInstanceState.putBoolean("switchKey", true);

                    Log.d("states", "switch ON  "+isSpeakerOn);


                }
                else if(isChecked==false){
                    Toast.makeText(getApplicationContext(),"OFF",Toast.LENGTH_SHORT).show();
                    //Set Speakerphone Off
                    audioManager.setSpeakerphoneOn(false);
                    phoneStateListener.onReceive(context,intent);
                    //savedInstanceState.putBoolean("switchKey", false);

                    Log.d("states", "switch OFF "+isSpeakerOn);

                }
            }
        });
         */
    }

    @Override
    protected void onSaveInstanceState(Bundle saveInstanceState) {

        saveInstanceState.putBoolean("switchKey", switch1.isChecked());
       Log.d("states", "onSaveInstanceState: " + saveInstanceState.get("switchKey"));
      // switchBundle.putBoolean("switchKey1", switch1.isChecked());
       super.onSaveInstanceState(saveInstanceState);
    }

    // Speakerphone switch method
    public void speakerphoneSwitch(Switch switch1) {

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isSpeakerphoneOn = isChecked;
                PrototypeWidget.updateWidgets(MainActivity.this, isChecked);
                if(isChecked){
                    Toast.makeText(getApplicationContext(),"On",Toast.LENGTH_SHORT).show();

                    //Set Speakerphone On
                    audioManager.setSpeakerphoneOn(true);
                    isSpeakerOn = true;
                    // Listens to phone state when switch is turn on
                    phoneStateListener.onReceive(context,intent);
                    //savedInstanceState.putBoolean("switchKey", true);
                    Log.d("states", "switch ON  "+isSpeakerOn);
                }
                else if(isChecked==false){
                    Toast.makeText(getApplicationContext(),"OFF",Toast.LENGTH_SHORT).show();
                    //Set Speakerphone Off
                    audioManager.setSpeakerphoneOn(false);
                    phoneStateListener.onReceive(context,intent);
                    //savedInstanceState.putBoolean("switchKey", false);
                    Log.d("states", "switch OFF "+isSpeakerOn);
                }
            }
        });
    }
}
