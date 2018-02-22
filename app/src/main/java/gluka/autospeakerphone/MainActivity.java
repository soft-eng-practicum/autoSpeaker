package gluka.autospeakerphone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
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
    PhoneStateListener phoneStateListener = new PhoneStateListener();
    Switch switch1;
    boolean isSpeakerOn = true;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button favList = (Button)findViewById(R.id.favList);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        switch1 = (Switch)findViewById(R.id.switch1);
        Log.d("states", "onCreate: switch boot pref " + prefs.getBoolean("switchKey",true));
        //Required to set Speakerphone On/Off
        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);


        // call the service with all the telephony stuff
        startService(new Intent(this,TelephonyService.class));


        favList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent favorite = new Intent(getApplicationContext(), FavoriteList.class);
                startActivity(favorite);
            }
        });

        //messing with the switch
        switch1.setChecked(prefs.getBoolean("switchKey",true));
        //call speaker method with switch and preferences as params
        speakerphoneSwitch(switch1,prefs);
    }

    // Speakerphone switch method
    public void speakerphoneSwitch(Switch switch1,final SharedPreferences prefs) {

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
                //save state onto the phone hdd, not ram
                    prefs.edit().putBoolean("switchKey", true).commit();
                    prefs.edit().apply();
                    isSpeakerOn = prefs.getBoolean("switchKey",true);
                    Log.d("states", "switchON pref "+ isSpeakerOn);
                    // Listens to phone state when switch is turn on
                    phoneStateListener.onReceive(context,intent);
                }
                else if(isChecked==false){
                    Toast.makeText(getApplicationContext(),"OFF",Toast.LENGTH_SHORT).show();
                    //Set Speakerphone Off
                    audioManager.setSpeakerphoneOn(false);
                    phoneStateListener.onReceive(context,intent);
                    //save state onto the phone hdd, not ram
                    prefs.edit().putBoolean("switchKey", false).commit();
                    prefs.edit().apply();
                    isSpeakerOn = prefs.getBoolean("switchKey",false);
                    Log.d("states", "switchOFF pref "+ isSpeakerOn);
                }
            }
        });
    }

}
