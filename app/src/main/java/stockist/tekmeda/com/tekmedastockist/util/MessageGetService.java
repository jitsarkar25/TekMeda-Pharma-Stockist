package stockist.tekmeda.com.tekmedastockist.util;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import stockist.tekmeda.com.tekmedastockist.MainActivity;
import stockist.tekmeda.com.tekmedastockist.OrderedMedicineActivity;
import stockist.tekmeda.com.tekmedastockist.R;

public class MessageGetService extends FirebaseMessagingService {

    private static final int CHANNEL_ID = 134;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

      Intent intent = new Intent(this, OrderedMedicineActivity.class);
        String retailerid = remoteMessage.getData().get("retailerid");
        intent.putExtra("retailerId",retailerid);
       //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.tekmeda)
                .setSound(alarmSound)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(1,mBuilder.build());

    }
}
