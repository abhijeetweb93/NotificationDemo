package com.avalon.abhijeet.notificationdemo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int notificationID = 123123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getIntent() != null && getIntent().hasExtra("NotificationData") && getIntent().hasExtra("NotificationID")) {
            ((TextView) findViewById(R.id.tviHello)).setText(getIntent().getExtras().getString("NotificationData"));

            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(getIntent().getExtras().getInt("NotificationID"));
        }


        findViewById(R.id.btnShow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNotification(notificationID);
            }
        });

    }


    private void createNotification(int notificationID) {

        String text = "Hey, how are the notifications going?";
        String channelID = "my_channel_01";

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        //Prepare Send Data via notification
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("NotificationData", text);
        intent.putExtra("NotificationID", notificationID);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);


        //create notification builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelID);
        builder.setSmallIcon(R.mipmap.ic_launcher);  //set small icon
        builder.setContentTitle("J.Papers");  //set content title
        builder.setContentText(text);  //set content text

        //create notification object and set intent
        Notification notification = builder.setContentIntent(pIntent).build();

//        Notification n = new Notification.Builder(this)
//                .setContentTitle("New mail from " + "test@gmail.com")
//                .setContentText("Subject")
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentIntent(pIntent)
//                .setAutoCancel(true)
//                .addAction(R.mipmap.ic_launcher, "Call", pIntent)
//                .addAction(R.mipmap.ic_launcher, "More", pIntent)
//                .addAction(R.mipmap.ic_launcher, "And more", pIntent).build();


        // if device OS is Oreo
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_LOW;

            NotificationChannel mChannel = new NotificationChannel(channelID, "J.Papers", importance);
            // Configure the notification channel.
            mChannel.setDescription(text);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            mNotificationManager.createNotificationChannel(mChannel);
            mNotificationManager.notify(notificationID, notification);

        } else {//if device os less than Oreo
            mNotificationManager.notify(notificationID, notification);
        }

    }
}
