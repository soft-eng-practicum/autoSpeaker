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
    private static boolean isSpeakerphoneOn;
    boolean intiSpeakerOn = true;
    boolean setChecked = true;
    private Context context;
    private Intent intent;
    private PhoneStateListener phoneStateListener = new PhoneStateListener();
    private Switch switch1;
    private boolean isSpeakerOn = true;
    private SharedPreferences prefs = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button favList = (Button)findViewById(R.id.favList);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        switch1 = (Switch)findViewById(R.id.switch1);
        setChecked = prefs.getBoolean("switchKey",setChecked);
        Log.d(TAG, "onCreate State: " + setChecked);
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

        // Initialize 'switchKey = True' only once
        if(intiSpeakerOn)
        {
            switch1.setChecked(setChecked);
            intiSpeakerOn = false;
        }
        //call speaker method with switch and preferences as params
        speakerphoneSwitch(switch1,prefs);
    }

    /**
     * Set speakerphone status
     * @param isChecked
     */
    protected static void setSpeaker(boolean isChecked)
    {
        audioManager.setSpeakerphoneOn(isChecked);
        isSpeakerphoneOn = isChecked;
    }

    /**
     * Get speakerphone status
     * @return
     */
    protected static boolean getSpeaker()
    {
        return isSpeakerphoneOn;
    }

    /**
     * Speakerphone switch method
     * @param switch1
     * @param prefs
     */
    protected void speakerphoneSwitch(Switch switch1,final SharedPreferences prefs)
    {
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PrototypeWidget.updateWidgets(MainActivity.this, isChecked);
                WidgetUpdateService widgetService = new WidgetUpdateService();
                if(isChecked){

                    Toast.makeText(getApplicationContext(),"On",Toast.LENGTH_SHORT).show();
                    //Set Speakerphone On
                    setSpeaker(isChecked);
                    widgetService.widgetON();
                    //save state onto the phone hdd, not ram
                    prefs.edit().putBoolean("switchKey", true).apply();
                    //prefs.edit().putBoolean("", true).apply();
                    //prefs.edit().apply();
                    //isSpeakerOn = prefs.getBoolean("switchKey",true);
                    // Listens to phone state when switch is turn ON
                    //phoneStateListener.onReceive(context,intent);
                    Log.d(TAG, "MainSwitch State: " + getSpeaker());
                }
                else if(isChecked==false){

                    Toast.makeText(getApplicationContext(),"OFF",Toast.LENGTH_SHORT).show();
                    //Set Speakerphone Off
                    setSpeaker(isChecked);
                    widgetService.widgetOFF();
                    //save state onto the phone hdd, not ram
                    prefs.edit().putBoolean("switchKey", false).apply();
                    //prefs.edit().putBoolean("", false).apply();
                    //prefs.edit().apply();
                    //isSpeakerOn = prefs.getBoolean("switchKey",false);
                    // Listens to phone state when switch is turn OFF
                    //phoneStateListener.onReceive(context,intent);
                    Log.d(TAG, "MainSwitch State: "+ getSpeaker());
                }
            }
        });
    }

    @Override
    protected void onPostResume() {
        boolean betweenSwitch = prefs.getBoolean("switchKey",true);
        super.onPostResume();

        switch1.setChecked(betweenSwitch);
        Log.d(TAG, "onPostResume State: " + betweenSwitch);
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
            Log.d(TAG, "widgetON State: "+ isSpeakerphoneOn);

        }
        public void widgetOFF(){
            //widget.updateWidgets(mainContext);
            Intent intent = new Intent(mainContext, PrototypeWidget.class);
            intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
            mainContext.sendBroadcast(intent);
            Log.d(TAG, "widgetOFF State: "+ isSpeakerphoneOn);
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }
}
