package gluka.autospeakerphone;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    private static AudioManager audioManager;
    private boolean isSpeakerphoneOn;
    private Context context;
    private Intent intent;
    private PhoneStateListener phoneStateListener = new PhoneStateListener();
    private Switch switch1;
    private boolean isSpeakerOn = true;
    SharedPreferences prefs = null;




    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button favList = (Button)findViewById(R.id.favList);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        switch1 = (Switch)findViewById(R.id.switch1);
        Log.d(TAG, "onCreate State: " + prefs.getBoolean("switchKey",true));
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

    /**
     * Set speakerphone status
     * @param isChecked
     */
    public void setSpeaker(boolean isChecked)
    {
        audioManager.setSpeakerphoneOn(isChecked);
        isSpeakerphoneOn = isChecked;
    }

    /**
     * Get speakerphone status
     * @return
     */
    protected boolean getSpeaker()
    {
        return isSpeakerphoneOn;
    }

    /**
     * Speakerphone switch method
     * @param switch1
     * @param prefs
     */
    public void speakerphoneSwitch(Switch switch1,final SharedPreferences prefs)
    {
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                WidgetUpdateService widgetService = new WidgetUpdateService();


                if(isChecked){
                    Toast.makeText(getApplicationContext(),"On",Toast.LENGTH_SHORT).show();
                    //Set Speakerphone On
                    setSpeaker(isChecked);
                    widgetService.widgetON();
                    //save state onto the phone hdd, not ram

                    prefs.edit().putBoolean("switchKey", true).apply();
                    //isSpeakerOn = prefs.getBoolean("switchKey",true);
                    Log.d(TAG, "MainSwitch State: " + getSpeaker());
                    // Listens to phone state when switch is turn on
                    phoneStateListener.onReceive(context,intent);

                }
                   if(isChecked==false){
                    Toast.makeText(getApplicationContext(),"OFF",Toast.LENGTH_SHORT).show();
                    //Set Speakerphone Off
                    setSpeaker(isChecked);
                    widgetService.widgetOFF();

                    phoneStateListener.onReceive(context,intent);


                    //save state onto the phone hdd, not ram
                     prefs.edit().putBoolean("switchKey", false).apply();
                    //isSpeakerOn = prefs.getBoolean("switchKey",false);
                    Log.d(TAG, "MainSwitch State: "+ getSpeaker());
                }
            }
        });
    }

    @Override
    protected void onPostResume() {
        boolean t = prefs.getBoolean("switchKey",false);
        super.onPostResume();

        switch1.setChecked(t);
        Log.d("app", "onResume: "+ t);

    }


    public class WidgetUpdateService extends Service {
        PrototypeWidget widget = new PrototypeWidget();
        //SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        Context mainContext;

        public WidgetUpdateService(){
            mainContext = MainActivity.this.getApplicationContext();
        }

        public void widgetON(){
            //widget.updateWidgets(mainContext);
            Intent intent = new Intent(mainContext, PrototypeWidget.class);
            intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
            mainContext.sendBroadcast(intent);
            //Log.d("app", "MainSwitch widgetON: "+ isSpeakerphoneOn);
            //Log.d("app", "MainSwitch WIDgetOn: "+ widget.isAppWidgetOn);

        }
        public void widgetOFF(){
            //widget.updateWidgets(mainContext);
            Intent intent = new Intent(mainContext, PrototypeWidget.class);
            intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
            mainContext.sendBroadcast(intent);
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }
}
