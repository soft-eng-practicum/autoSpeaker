package gluka.autospeakerphone;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.util.Log;
import android.widget.RemoteViews;

import static android.content.ContentValues.TAG;

/**
 * Implementation of App Widget functionality.
 */
public class PrototypeWidget extends AppWidgetProvider
{
    AudioManager audioManager;
    boolean isSpeakerphoneOn;
    static boolean isAppWidgetOn;
    static boolean adjustSwitch = true;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        appSwitch(context, intent);
        super.onReceive(context, intent);
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int widgetId : appWidgetIds)
        {
            // Create an Intent to launch Activity
            Intent intent = new Intent(context, PrototypeWidget.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.prototype_widget);
            remoteViews.setOnClickPendingIntent(R.id.imageButton, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    // MainActivity Switch uses this method to toggle AppWidget
    public static void updateWidgets(Context context, boolean isSpeakerOn) {
        adjustSwitch = true;
        isAppWidgetOn = isSpeakerOn;
        Log.d("updateWidget: ", isAppWidgetOn + "");
        Intent intent = new Intent(context, PrototypeWidget.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        intent.putExtra("appWidgetIds", AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, PrototypeWidget.class)));
        context.sendBroadcast(intent);
    }

    // Switch method that toggles speakerphone on/off
    private void appSwitch(Context context, Intent intent)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        isSpeakerphoneOn = sharedPreferences.getBoolean("", true);

        // MainActivity switches appwidget ON/OFF
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

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.prototype_widget);
        Log.d(TAG,"Toggle State: " + isSpeakerphoneOn);
        if (isSpeakerphoneOn)
        {
            // AppWidget speaker image on
            remoteViews.setImageViewResource(R.id.imageButton, R.drawable.autospeakeron);
            // Set speakerphone on
            audioManager.setSpeakerphoneOn(isSpeakerphoneOn);
        }
        else
        {
            // AppWidget speaker image on
            remoteViews.setImageViewResource(R.id.imageButton, R.drawable.autospeakeroff);
            // Set speakerphone Off
            audioManager.setSpeakerphoneOn(isSpeakerphoneOn);
        }

        ComponentName componentName = new ComponentName(context, PrototypeWidget.class);
        AppWidgetManager.getInstance(context).updateAppWidget(componentName, remoteViews);
        isSpeakerphoneOn = !isSpeakerphoneOn;
        // Commit changes to SharedPreference
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("", isSpeakerphoneOn);
        editor.commit();
    }
}

