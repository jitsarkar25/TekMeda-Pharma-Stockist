package stockist.tekmeda.com.tekmedastockist;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NotificationssService extends Service {

    public String userId;
    public void startNotificationListener(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference().child("PlacedOrders").child("Stockists");
                databaseReference3.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        if(dataSnapshot.getKey().equalsIgnoreCase(userId)) {

                   /* Intent intent= new Intent(getApplicationContext(),ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,PendingIntent.FLAG_ONE_SHOT);
                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext());
                    notificationBuilder.setContentTitle("New Order");
                    notificationBuilder.setContentText("You Have a new Order");
                    notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
                    notificationBuilder.setAutoCancel(true);
                    notificationBuilder.setContentIntent(pendingIntent);
                    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    notificationBuilder.setSound(alarmSound);
                    notificationBuilder.setLights(Color.BLUE, 500, 500);
                    long[] pattern = {500,500,500,500,500,500,500,500,500};
                    notificationBuilder.setVibrate(pattern);
                    notificationBuilder.setStyle(new NotificationCompat.InboxStyle());
                    NotificationManager notificationManager = (NotificationManager) getSystemService((Context.NOTIFICATION_SERVICE));
                    notificationManager.notify(0,notificationBuilder.build());*/
                            showNotification();
                        }
                    }
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        if(dataSnapshot.getKey().equalsIgnoreCase(userId)) {

                            showNotification();
                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }).start();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        Log.e("Service started","started");
    }
    @Override
    public void onDestroy() {

        Intent broadcastIntent = new Intent("uk.ac.shef.oak.ActivityRecognition.RestartSensor");
        sendBroadcast(broadcastIntent);
        Toast.makeText(this, "MyService Completed or Stopped.", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
      userId = intent.getStringExtra("userid");
        startNotificationListener();
        Log.e("Service started 2","started");
        Toast.makeText(this, "MyService Started. "+userId, Toast.LENGTH_SHORT).show();
        //If service is killed while starting, it restarts.





        return START_STICKY;
    }

    public void showNotification(){
        Log.e("Service started 2","notify");
        Intent intent= new Intent(getApplicationContext(),ProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext());
        notificationBuilder.setContentTitle("New Order");
        notificationBuilder.setContentText("You Have a new Order");
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setContentIntent(pendingIntent);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder.setSound(alarmSound);
        notificationBuilder.setLights(Color.BLUE, 500, 500);
        long[] pattern = {500,500,500,500,500,500,500,500,500};
        notificationBuilder.setVibrate(pattern);
        notificationBuilder.setStyle(new NotificationCompat.InboxStyle());
        NotificationManager notificationManager = (NotificationManager) getSystemService((Context.NOTIFICATION_SERVICE));
       notificationManager.notify(0,notificationBuilder.build());
       // startForeground(1,notificationBuilder.build());
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.e("Service","Removed");
        startService(rootIntent);


    }

    @Override
    public void onLowMemory() {
        Log.e("Service","Low Memory");
        super.onLowMemory();
    }
}
