package gluka.autospeakerphone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import android.media.AudioManager;

public class MainActivity extends AppCompatActivity {
    Intent serviceIntent;
    AudioManager audioManager;
    boolean isSpeakerphoneOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Switch tb = (Switch)findViewById(R.id.switch1);
        final Button favList = (Button)findViewById(R.id.favList);

        //Required to set Speakerphone On/Off
        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        // call the service with all the telephony stuff
        startService(new Intent(this,TelephonyService.class));

        //testing without sim card in my phone -- it works
        if(TelephonyManager.SIM_STATE_ABSENT ==1 )
           Toast.makeText(getApplicationContext(),"no sim",Toast.LENGTH_SHORT).show();

        favList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent favorite = new Intent(getApplicationContext(), FavoriteList.class);
                startActivity(favorite);
            }
        });
        //messing with the switch
        tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isSpeakerphoneOn = isChecked;
                if(isChecked){
                    Toast.makeText(getApplicationContext(),"On",Toast.LENGTH_SHORT).show();
                    //Set Speakerphone On
                    audioManager.setSpeakerphoneOn(isChecked);
                    Log.d("Switch in progress","true");
                }
                else if(isChecked==false){
                    Toast.makeText(getApplicationContext(),"OFF",Toast.LENGTH_SHORT).show();
                    //Set Speakerphone Off
                    audioManager.setSpeakerphoneOn(isChecked);
                    Log.d("Switch in progress","false");
                }
            }
        });
    }
}
