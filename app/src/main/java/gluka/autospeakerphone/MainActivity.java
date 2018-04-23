package gluka.autospeakerphone;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
    protected static Switch mainSwitch;
    protected static SharedPreferences sharedPreferences;
    boolean checkedCondition;

    String className = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainSwitch = (Switch)findViewById(R.id.mainSwitch);
        sharedPreferences = getSharedPreferences(getPackageName() + "." + className, MODE_PRIVATE);

        final Button about = (Button) findViewById(R.id.aboutBtn);
        final Button favList = (Button)findViewById(R.id.favList);
        final Button feedback = (Button) findViewById(R.id.feedbackBtn);


        // Favorite List Activity Button
        favList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent favorite = new Intent(getApplicationContext(), FavoriteList.class);
                startActivity(favorite);
            }
        });


        // About The Developers Button
        about.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent aboutPage = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(aboutPage);
            }
        });

        // Feed Back Button, connected to Google Firebase Database
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent feedbackPage = new Intent(getApplicationContext(), FeedbackSystem.class);
                startActivity(feedbackPage);
            }
        });

        // Main Switch onclickListener
        mainSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            // When switch is change on/off
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                checkedCondition = isChecked;

                if(checkedCondition == true) {
                    // PackageManager and ComponentName will activate BroadcastReceiver when switch is onn
                    PackageManager packageManager = getPackageManager();
                    ComponentName componentName = new ComponentName(getApplicationContext(),AutoSpeakerListener.class);
                    packageManager.setComponentEnabledSetting(componentName,
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP);
                    Log.i("Progress", "Broadcast Receiver : Activated");


                    // Saving main switch state when user is turning it on
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("switchValue", checkedCondition);
                    editor.commit();

                    // Updates PrototypeWidget to the on position
                    //PrototypeWidget.updateWidgets(MainActivity.this, checkedCondition);


                    // Displays to users that AutoSpeaker is ON
                    Toast.makeText(getApplicationContext(),"ON",Toast.LENGTH_SHORT).show();

                    Log.i("Progress", "Saved switched value to SharedPreference : Switch Value : " + checkedCondition);
                }

                else if(checkedCondition == false) {

                    // PackageManager and ComponentName will deactivate BroadcastReceiver when switch is off
                    PackageManager packageManager = getPackageManager();
                    ComponentName componentName = new ComponentName(getApplicationContext(),AutoSpeakerListener.class);
                    packageManager.setComponentEnabledSetting(componentName,
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP);
                    Log.i("Progress", "Broadcast Receiver : Deactivated");


                    // Saving main switch state when user is turning it on
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("switchValue", checkedCondition);
                    editor.commit();

                    // Updates PrototypeWidget to the off position
                    //PrototypeWidget.updateWidgets(MainActivity.this, checkedCondition);

                    // Displays to user that AutoSpeaker is OFF
                    Toast.makeText(getApplicationContext(),"OFF",Toast.LENGTH_SHORT).show();

                    Log.i("Progress", "Saved switched value to SharedPreference : Switch Value : " + checkedCondition);


                }
            }
        });

        Log.d("Progress", "onCreate(), switch = " + checkedCondition);
    }

    // Called when app is visible to the user
    @Override
    protected void onStart() {
        super.onStart();

        // Retrieving main switch value when app is loaded
        mainSwitch.setChecked(sharedPreferences.getBoolean("switchValue", checkedCondition));

        Log.i("Progress", "onStart(), switch = " + checkedCondition);
    }

    // App will be restarted when phone crashes?? lol
    @Override
    protected void onRestart() {
        super.onRestart();

        Log.i("Progress", "onRestart(), switch = " + checkedCondition);
    }

    // Called when app is opened again but not killed by task manager
    @Override
    protected void onResume() {
        super.onResume();

        mainSwitch.setChecked(sharedPreferences.getBoolean("switchValue", checkedCondition));
        //PrototypeWidget.updateWidgets(MainActivity.this, sharedPreferences.getBoolean("switchValue",checkedCondition));
        Log.i("Progress", "onResume(), switch = " + checkedCondition);
    }

    // Called when "home" button is pressed or killed by task manager
    @Override
    protected void onStop() {
        super.onStop();

        // Saving main switch value when app is stopped by pressing "home" button
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("switchValue", checkedCondition);
        editor.commit();

        Log.i("Progress", "onStop(), switch = " + checkedCondition);
    }

    // Called when "back" button is pressed
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Saving main switch value when app is destroyed by pressing "back" button
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("switchValue", checkedCondition);
        editor.commit();

        Log.i("Progress", "onDestroy(), switch = " + checkedCondition);
    }



    // Returns the value of the switch at it's last known state
    public boolean getSwitchValue() {

        Log.i("Progress", "getSwitchValue(), switch = " + checkedCondition);

        return sharedPreferences.getBoolean("switchValue", checkedCondition);
    }

}
