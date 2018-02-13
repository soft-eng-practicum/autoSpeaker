package gluka.autospeakerphone;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class PrototypeWidget extends AppWidgetProvider {

    ImageView speaker;
    MainActivity mainActivity = new MainActivity();

    @Override
    public void onUpdate(final Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // There may be multiple widgets active, so update all of them
        /**
         for (int appWidgetId : appWidgetIds) {
         updateAppWidget(context, appWidgetManager, appWidgetId);
         }
         */

        final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, PrototypeWidget.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.prototype_widget);
            views.setOnClickPendingIntent(R.id.imageView, pendingIntent);

            if(mainActivity.isSpeakerphoneOn) {
                //mainActivity.audioManager.setSpeakerphoneOn(true);
                Log.d("AppWidget in progress","true");
            }
            else if(mainActivity.isSpeakerphoneOn == false) {
                //mainActivity.audioManager.setSpeakerphoneOn(false);
                Log.d("AppWidget in progress","false");
            }

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    static void updateAppWidgets(Context context) {
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

