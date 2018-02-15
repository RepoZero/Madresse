package ir.madresse.Messages;


import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;

import static android.content.Context.NOTIFICATION_SERVICE;

public class CNotification {

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void Normal(Intent intent, PendingIntent pending, Context context, String title, String text, int icon){


        Notification notifications = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(text).setSmallIcon(icon)
                .setContentIntent(pending).build();

        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notifications.flags |= Notification.FLAG_AUTO_CANCEL;
        manager.notify(0, notifications);
    }
}
