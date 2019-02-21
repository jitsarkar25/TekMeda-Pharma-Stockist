package stockist.tekmeda.com.tekmedastockist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SensorRestarterBroadcastReceiver extends BroadcastReceiver {
    FirebaseUser user;
    @Override
    public void onReceive(Context context, Intent intent) {
        user= FirebaseAuth.getInstance().getCurrentUser();
        Intent intent1 = new Intent(context, NotificationssService.class);
       // intent1.putExtra("userid",user.getUid());
        context.startService(intent1);;
    }
}
