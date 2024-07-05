package Service;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import com.example.stores.R;

public class ConFigApplication extends Application {
    public static final String CHANNEL_ID = "WEAR SHOES";

    @Override
    public void onCreate() {
        super.onCreate();
        createChannel();
    }
    private void createChannel(){
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
           String channelName = getString(R.string.channel_name);
           String channelDescription = getString(R.string.channel_description);
           int channelImportance = NotificationManager.IMPORTANCE_DEFAULT;

           Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
           AudioAttributes audioAttributes = new AudioAttributes.Builder()
                   .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                   .build();

           NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, channelImportance);
           channel.setDescription(channelDescription);
           channel.setSound(uri, audioAttributes);

           NotificationManager notificationManager = getSystemService(NotificationManager.class);
           notificationManager.createNotificationChannel(channel);
       }
    }
}
