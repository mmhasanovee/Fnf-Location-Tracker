package xyz.mmhasanovee.fnflocationtracker.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

import xyz.mmhasanovee.fnflocationtracker.Model.User;
import xyz.mmhasanovee.fnflocationtracker.R;
import xyz.mmhasanovee.fnflocationtracker.Utils.Commonx;
import xyz.mmhasanovee.fnflocationtracker.Utils.NotificationHelper;

public class MyFCMService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(remoteMessage.getData()!=null){

            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
                sendNotificationWithChannel(remoteMessage);

            else
                sendNotification(remoteMessage);



            //addRequestToUserInformation(remoteMessage.getData());

        }


    }

    private void addRequestToUserInformation(Map<String, String> data) {

        DatabaseReference fr = FirebaseDatabase.getInstance().getReference(Commonx.USER_INFORMATION).child(data.get(Commonx.TO_UID)).child(Commonx.FRIEND_REQUEST);
        User userx = new User();
        userx.setUid(data.get(Commonx.FROM_UID));
        userx.setEmail(data.get(Commonx.FROM_NAME));

        fr.child(userx.getUid()).setValue(userx);

    }


    private void sendNotification(RemoteMessage remoteMessage) {

        Map<String, String> data = remoteMessage.getData();
        String title = "Friend Requests";
        String content = "New friend request from "+data.get(Commonx.FROM_NAME);

        Uri defaultsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(content)
                .setSound(defaultsound)
                .setAutoCancel(false);

        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        manager.notify(new Random().nextInt(),builder.build());

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendNotificationWithChannel(RemoteMessage remoteMessage) {

        Map<String, String> data = remoteMessage.getData();
        String title = "Friend Request";
        String content = "New friend request from "+data.get(Commonx.FROM_NAME);


        NotificationHelper helper;
        Notification.Builder builder;

        Uri defaultsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        helper = new NotificationHelper(this);
        builder = helper.getFnfLocationTrackerNotification(title,content,defaultsound);

        helper.getManager().notify(new Random().nextInt(),builder.build());
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){

            final DatabaseReference tokens= FirebaseDatabase.getInstance()
                    .getReference(Commonx.TOKENS);
            tokens.child(user.getUid()).setValue(s);
        }
    }
}
