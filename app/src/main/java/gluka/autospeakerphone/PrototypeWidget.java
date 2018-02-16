package gluka.autospeakerphone;

import android.app.Activity;
import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;

import static android.content.ContentValues.TAG;

/**
 * Implementation of App Widget functionality.
 */
public class PrototypeWidget extends AppWidgetProvider
{
    public static final String IS_SPEAKERPHONEON="MyPref";
    AudioManager audioManager;
    boolean isSpeakerphoneOn;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        switchToggle(context, intent);
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
            remoteViews.setOnClickPendingIntent(R.id.imageView, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }
    // Switch method that toggles speakerphone on/off
    private void switchToggle(Context context, Intent intent)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        isSpeakerphoneOn = sharedPreferences.getBoolean(IS_SPEAKERPHONEON, false);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.prototype_widget);
        Log.d(TAG,"Toggle State: " + isSpeakerphoneOn);
        if (isSpeakerphoneOn)
        {
            // AppWidget speaker image on
            remoteViews.setImageViewResource(R.id.imageView, R.drawable.autospeakerlogo);
            // Set speakerphone on
            audioManager.setSpeakerphoneOn(isSpeakerphoneOn);
        }
        else
        {
            // AppWidget speaker image on
            remoteViews.setImageViewResource(R.id.imageView, R.drawable.autospeakerlogo);
            // Set speakerphone Off
            audioManager.setSpeakerphoneOn(isSpeakerphoneOn);
        }
        ComponentName componentName = new ComponentName(context, PrototypeWidget.class);
        AppWidgetManager.getInstance(context).updateAppWidget(componentName, remoteViews);
        isSpeakerphoneOn = !isSpeakerphoneOn;
        // Commit changes to SharedPreference
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_SPEAKERPHONEON, isSpeakerphoneOn);
        editor.commit();
    }
}

