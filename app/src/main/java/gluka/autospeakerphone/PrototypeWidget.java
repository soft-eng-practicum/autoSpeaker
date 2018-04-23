package gluka.autospeakerphone;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

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



    // Do not modify
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.prototype_widget);
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int widgetId : appWidgetIds)
        {
            // Create an Intent to launch Activity
            Intent intent = new Intent(context, PrototypeWidget.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            //RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.prototype_widget);
            remoteViews.setOnClickPendingIntent(R.id.imageButton, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
        Log.i("prototype", "onUpdate()");
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        appSwitch(context, intent);
        super.onReceive(context, intent);

        Log.i("prototype", "onReceive(), app on home screen");
    }

    protected void appSwitch(Context context, Intent intent)
    {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        //isSpeakerphoneOn = generateSwitch(context);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.prototype_widget);
        //mainSwitchControl();

        if (isSpeakerphoneOn)
        {
            isSwitchOn = true;
            Log.d(TAG,"appSwitch State: isSwitchOn = " + isSwitchOn);

            // AppWidget speaker image on
            remoteViews.setImageViewResource(R.id.imageButton, R.drawable.autospeakeron);

            Log.i("heezy", "appSwitch(), isSpeakerOn = " + isSpeakerphoneOn);

            Log.d(TAG,"appSwitch State: setChecked = " + true);
        }
        else if (isSpeakerphoneOn == false)
        {
            isSwitchOn = false;
            Log.d(TAG,"appSwitch State: isSwitchOn = " + isSwitchOn);

            // AppWidget speaker image on
            remoteViews.setImageViewResource(R.id.imageButton, R.drawable.autospeakeroff);

            Log.i("heezy", "appSwitch(), isSpeakerOn = " + isSpeakerphoneOn);

            Log.d(TAG,"appSwitch State: setChecked = " + false);
        }
        ComponentName componentName = new ComponentName(context, PrototypeWidget.class);
        AppWidgetManager.getInstance(context).updateAppWidget(componentName, remoteViews);
        isSpeakerphoneOn = !isSpeakerphoneOn;

        // Commit changes to SharedPreference
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("", isSpeakerphoneOn);
        editor.putBoolean("switchKey", isSwitchOn);
        editor.apply();
    }

    /*@Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created


        Log.i("widget", "AppWidget, onEnabled() : App dragged to home screen");
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled

        Log.i("widget", "AppWidget, onDisabled() : App dragged to trashcan");
    }

    protected static boolean generateSwitch(Context context)
    {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        isSpeakerphoneOn = prefs.getBoolean("", setChecked);
        Log.d(TAG,"appSwitch State: isSpeakerphoneOn = " + isSpeakerphoneOn);

        Log.i("prototype", "generateSwitch()");

        return isSpeakerphoneOn;
    }*/



    /*private void mainSwitchControl()
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
        Log.i("prototype", "mainSwitchControl()");
    }*/

    /*
    protected static void updateWidgets(Context context, boolean isSpeakerOn)
    {
        //adjustSwitch = true;
        isAppWidgetOn = isSpeakerOn;

        Intent intent = new Intent(context, PrototypeWidget.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        intent.putExtra("appWidgetIds", AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, PrototypeWidget.class)));
        context.sendBroadcast(intent);

        Log.d(TAG, "updateWidgets State: " + isAppWidgetOn);
        Log.i("heezy", "updateWidgets() isAppWidgetOn = " + isAppWidgetOn);

    }
    */

   /* protected void appSwitch(Context context, Intent intent)
    {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        isSpeakerphoneOn = generateSwitch(context);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.prototype_widget);
        mainSwitchControl();

        if (isSpeakerphoneOn)
        {
            isSwitchOn = true;
            Log.d(TAG,"appSwitch State: isSwitchOn = " + isSwitchOn);

            // AppWidget speaker image on
            remoteViews.setImageViewResource(R.id.imageButton, R.drawable.autospeakeron);

            Log.i("prototype", "appSwitch(), isSwitchOn() = " + isSwitchOn);

            Log.d(TAG,"appSwitch State: setChecked = " + true);
        }
        else if (isSpeakerphoneOn == false)
        {
            isSwitchOn = false;
            Log.d(TAG,"appSwitch State: isSwitchOn = " + isSwitchOn);

            // AppWidget speaker image on
            remoteViews.setImageViewResource(R.id.imageButton, R.drawable.autospeakeroff);

            Log.i("prototype", "appSwitch(), isSwitchOFF() = " + isSwitchOn);
            Log.d(TAG,"appSwitch State: setChecked = " + false);
        }
        ComponentName componentName = new ComponentName(context, PrototypeWidget.class);
        AppWidgetManager.getInstance(context).updateAppWidget(componentName, remoteViews);
        isSpeakerphoneOn = !isSpeakerphoneOn;

        // Commit changes to SharedPreference
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("", isSpeakerphoneOn);
        editor.putBoolean("switchKey", isSwitchOn);
        editor.apply();
    }*/
}
