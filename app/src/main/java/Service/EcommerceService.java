package Service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.stores.R;

import Activities.CartActivity;

public class EcommerceService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String data = intent.getStringExtra("data");

        sendNotification(data);

        return START_NOT_STICKY;
    }

    @SuppressLint("ForegroundServiceType")
    private void sendNotification(String data) {

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.logo_ecommerce);
        Intent intent = new Intent(this, CartActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, ConFigApplication.CHANNEL_ID)
                .setContentTitle("Ecommerce")
                .setContentText(data)
                .setLargeIcon(bitmap)
                .setColor(Color.BLUE)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigLargeIcon((Bitmap) null)
                        .bigPicture(bitmap))
                .setSmallIcon(R.drawable.bell)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        Notification notification = builder.build();
        startForeground(1, notification);

    }
}
