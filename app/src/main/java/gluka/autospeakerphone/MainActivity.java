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

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    private static AudioManager audioManager;
    protected static boolean isSpeakerphoneOn;
    protected static boolean setChecked = true;
    private Context context;
    private Intent intent;
    private AutoSpeakerListener autoSpeakerListener;
    protected static Switch switch1;
    private static final String PREFS_NAME = "prefs";
    private static final String PREF_DARK_THEME = "dark_theme";
    Switch blindswitch;
    protected static SharedPreferences prefs = null;
    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {

        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);

        if(useDarkTheme) {
            setTheme(R.style.AppCompatBlind);

        }
        super.onCreate(savedInstanceState);
       //
       // int theme = loadTheme();
       // this.setTheme(theme);
        //getWindow().getContext().setTheme(loadTheme());
        setContentView(R.layout.activity_main);
        final Button about = (Button) findViewById(R.id.aboutBtn);
        final Button favList = (Button)findViewById(R.id.favList);
        final Button feedback = (Button) findViewById(R.id.feedbackBtn);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        switch1 = (Switch)findViewById(R.id.switch1);
        Switch blindswitch = (Switch)findViewById(R.id.swBlind);

        autoSpeakerListener = new AutoSpeakerListener();
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

        about.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent aboutPage = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(aboutPage);
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent feedbackPage = new Intent(getApplicationContext(), FeedbackSystem.class);
                startActivity(feedbackPage);
            }
        });

        // Initialize 'switchKey = True' only once
        switch1.setChecked(setChecked);
        //call speaker method with switch and preferences as params
        speakerphoneSwitch(switch1,prefs);

        blindswitch.setChecked(useDarkTheme);
        blindswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked) {
                    toggleTheme(isChecked);
                }
                else if(isChecked==false) {
                    toggleTheme(isChecked);
                }
            }
        });


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
    protected void speakerphoneSwitch(final Switch switch1, final SharedPreferences prefs)
    {
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {

                if(switch1.isPressed())
                {
                    PrototypeWidget.updateWidgets(MainActivity.this, isChecked);
                    Log.d("TAG", "MainSwitch State: isPressed() = " + switch1.isPressed());
                }
                else {
                    Log.d("TAG", "MainSwitch State: isPressed() = " + switch1.isPressed());
                }
                if(isChecked)
                {
                    Toast.makeText(getApplicationContext(),"ON",Toast.LENGTH_SHORT).show();
                    //Set Speakerphone On
                    setSpeaker(isChecked);
                    //save state onto the phone hdd, not ram
                    prefs.edit().putBoolean("switchKey", true).apply();

                    /**
                    // Listens to phone state when switch is turn ON
                    PackageManager packageManager = getPackageManager();
                    ComponentName componentName = new ComponentName(getApplicationContext(),AutoSpeakerListener.class);

                    packageManager.setComponentEnabledSetting(componentName,
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP);
                    autoSpeakerListener.onReceive(context,intent);
                     */
                    Log.d(TAG, "MainSwitch State: " + getSpeaker());


                }
                else if(isChecked==false)
                {
                    Toast.makeText(getApplicationContext(),"OFF",Toast.LENGTH_SHORT).show();
                    //Set Speakerphone Off
                    setSpeaker(isChecked);
                    //save state onto the phone hdd, not ram
                    prefs.edit().putBoolean("switchKey", false).apply();

                    /**
                    // Listens to phone state when switch is turn OFF
                    PackageManager packageManager = getPackageManager();
                    ComponentName componentName = new ComponentName(getApplicationContext(),AutoSpeakerListener.class);

                    packageManager.setComponentEnabledSetting(componentName,
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP);
                    autoSpeakerListener.onReceive(context,intent);
                     */
                    Log.d(TAG, "MainSwitch State: "+ getSpeaker());

                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        boolean betweenSwitch = prefs.getBoolean("switchKey",true);
        switch1.setChecked(betweenSwitch);
        Log.d(TAG, "onResume State: " + betweenSwitch);
    }

    public int loadTheme(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //Load theme color
        int theme = sharedPreferences.getInt("Theme", android.R.style.Theme_Holo_Light); //RED is default color, when nothing is saved yet

        return theme;
    }
    public void saveTheme(int theme) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("Theme",theme);
        //editor.apply();
    }
    private void toggleTheme(boolean darkTheme) {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(PREF_DARK_THEME, darkTheme);
        editor.apply();

        Intent intent = getIntent();
        finish();

        startActivity(intent);
    }



}
