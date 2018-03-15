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

/**
 * Implementation of App Widget functionality.
 */
public class PrototypeWidget extends AppWidgetProvider {
    private AudioManager audioManager;
    SharedPreferences sharedPreferences;
    private boolean isSpeakerphoneOn;
    private boolean isAppWidgetOn;
    private RemoteViews remoteViews;


    @Override
    public void onReceive(Context context, Intent intent) {
        appSwitch(context);

        super.onReceive(context, intent);
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        remoteViews = new RemoteViews(context.getPackageName(), R.layout.prototype_widget);

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int widgetId : appWidgetIds) {
            // Create an Intent to launch Activity
            Intent intent = new Intent(context, PrototypeWidget.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            remoteViews.setOnClickPendingIntent(R.id.imageButton, pendingIntent);
            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(widgetId, remoteViews);

        }

    }

    /**
     * MainActivity updates appwidget
     */
    public void updateWidgets(Context context) {

        //Log.d("updateWidget: ", isAppWidgetOn + "");
        //setContextwidget(c);
        Intent intent = new Intent(context, PrototypeWidget.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        context.sendBroadcast(intent);
    }


    /**
     * Switch method that toggles appwidget speakerphone on/off
     *
     * @param context
     * @param
     */
    public void appSwitch(Context context) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.prototype_widget);

        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        Log.d("app", "appSwitch: before" +isSpeakerphoneOn);
        isSpeakerphoneOn = sharedPreferences.getBoolean("", false);//generateSwitch();
        isAppWidgetOn = isSpeakerphoneOn;

        Log.d("app", "appSwitch: after:::" +isSpeakerphoneOn);
       // isAppWidgetOn = sharedPreferences.getBoolean("switchKey", false);//generateSwitch();
        //isAppWidgetOn = isSpeakerphoneOn;
        Log.d("app print", "appSwitch before if: " + isSpeakerphoneOn);


        if (isSpeakerphoneOn) {
            // AppWidget speaker image on
            remoteViews.setImageViewResource(R.id.imageButton, R.drawable.autospeakeron);
            //SharedPreferences.Editor editor = sharedPreferences.edit();
           //sharedPreferences.edit().putBoolean("switchKey", isSpeakerphoneOn).apply();
            //isAppWidgetOn = true;



            Log.d("app print", "appSwitch true: " + isSpeakerphoneOn);
            // Set speakerphone on
            audioManager.setSpeakerphoneOn(isSpeakerphoneOn);
        }
        else if (!isSpeakerphoneOn) {

            // AppWidget speaker image on
            remoteViews.setImageViewResource(R.id.imageButton, R.drawable.autospeakeroff);
            Log.d("app print", "appSwitch false: " + isSpeakerphoneOn);


            // Set speakerphone Off
            audioManager.setSpeakerphoneOn(isSpeakerphoneOn);
        }
        ComponentName componentName = new ComponentName(context, PrototypeWidget.class);
        AppWidgetManager.getInstance(context).updateAppWidget(componentName, remoteViews);
        isSpeakerphoneOn = !isSpeakerphoneOn;
        //sharedPreferences.edit().putBoolean("switchKey", isAppWidgetOn).apply();

        // Commit changes to SharedPreference
        sharedPreferences.edit().putBoolean("", isSpeakerphoneOn).apply();
        sharedPreferences.edit().putBoolean("switchKey", isAppWidgetOn).apply();

    }




}