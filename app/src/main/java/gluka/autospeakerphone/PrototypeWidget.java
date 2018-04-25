package gluka.autospeakerphone;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

/**
 * Implementation of App Widget functionality.
 */
public class PrototypeWidget extends AppWidgetProvider
{
    private static SharedPreferences prefs;
    private static boolean isSpeakerphoneOn = true;
    private static boolean isAppWidgetOn = true;
    private static boolean isSwitchOn;
    private static boolean adjustSwitch = false;
    private RemoteViews remoteViews;
    private static boolean setChecked = true;
    protected ComponentName componentName;
    protected static ImageButton imageButton;
    protected static int widgetNum;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        appSwitch(context, intent);
        super.onReceive(context, intent);
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.prototype_widget);
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int widgetId : appWidgetIds)
        {
            widgetNum = widgetId;
            // Create an Intent to launch Activity
            Intent intent = new Intent(context, PrototypeWidget.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            //RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.prototype_widget);
            remoteViews.setOnClickPendingIntent(R.id.imageButton, pendingIntent);
            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    /**
     * MainActivity updates appwidget
     * @param context
     * @param isSpeakerOn
     */
    protected static void updateWidgets(Context context, boolean isSpeakerOn)
    {
        adjustSwitch = true;
        isAppWidgetOn = isSpeakerOn;
        Log.d(TAG, "updateWidgets State: " + isAppWidgetOn);
        Intent intent = new Intent(context, PrototypeWidget.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        intent.putExtra("appWidgetIds", AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, PrototypeWidget.class)));
        context.sendBroadcast(intent);
    }

    /**
     * Initial start up ON then generates OFF/ON
     * @param context
     * @return
     */
    protected static boolean generateSwitch(Context context)
    {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        isSpeakerphoneOn = prefs.getBoolean("", setChecked);
        Log.d(TAG,"appSwitch State: isSpeakerphoneOn = " + isSpeakerphoneOn);
        return isSpeakerphoneOn;
    }

    /**
     * MainActivity switch controller
     */
    private void mainSwitchControl()
    {
        if(isAppWidgetOn && adjustSwitch)
        {
            isSpeakerphoneOn = isAppWidgetOn;
            adjustSwitch = false;
        }
        else if((isAppWidgetOn == false) && adjustSwitch)
        {
            isSpeakerphoneOn = isAppWidgetOn;
            adjustSwitch = false;
        }
    }

    /**
     * Switch method that toggles appwidget speakerphone on/off
     * @param context
     * @param intent
     */
    protected void appSwitch(Context context, Intent intent)
    {
        AutoSpeakerListener autoSpeakerListener = new AutoSpeakerListener();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.prototype_widget);
        isSpeakerphoneOn = generateSwitch(context);
        mainSwitchControl();
        if (isSpeakerphoneOn)
        {
            isSwitchOn = true;
            Log.d(TAG,"appSwitch State: isSwitchOn = " + isSwitchOn);
            // AppWidget speaker image on
            remoteViews.setImageViewResource(R.id.imageButton, R.drawable.autospeakeron);
            try {
                autoSpeakerListener.onReceive(context, intent);
            }
            catch(NullPointerException e)
            {
                Log.d(TAG, "NullPointerException");
            }
            Toast.makeText(context.getApplicationContext(),"ON",Toast.LENGTH_SHORT).show();
            Log.d(TAG,"appSwitch State: setChecked = " + true);
        }
        else if (isSpeakerphoneOn == false)
        {
            isSwitchOn = false;
            Log.d(TAG,"appSwitch State: isSwitchOn = " + isSwitchOn);
            // AppWidget speaker image on
            remoteViews.setImageViewResource(R.id.imageButton, R.drawable.autospeakeroff);
            try {
                autoSpeakerListener.onReceive(context, intent);
            }
            catch(NullPointerException e)
            {
                Log.d(TAG, "NullPointerException");
            }
            Toast.makeText(context.getApplicationContext(),"OFF",Toast.LENGTH_SHORT).show();
            Log.d(TAG,"appSwitch State: setChecked = " + false);
        }

        componentName = new ComponentName(context, PrototypeWidget.class);
        AppWidgetManager.getInstance(context).updateAppWidget(componentName, remoteViews);
        isSpeakerphoneOn = !isSpeakerphoneOn;
        // Commit changes to SharedPreference
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("", isSpeakerphoneOn);
        editor.putBoolean("switchKey", isSwitchOn);
        editor.apply();
    }

    protected static boolean getSpeaker()
    {
        return isSwitchOn;
    }
}