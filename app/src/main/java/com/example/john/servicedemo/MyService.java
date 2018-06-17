package com.example.john.servicedemo;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import static com.example.john.servicedemo.MainActivity.CHANNEL_ID;
//-By default, a service runs in the same process as the main thread of the application.
//-Services which run in the process of the application are sometimes called local services

//Platform service and custom service
//-Platform service
//   The Android platform provides & runs predefined system services & every Android app can use them
//-Custom serivce
//   predefined by the user-Custom services are started from other Android components,
//   i.e., activities, broadcast receivers and other services

//By default started services are background, meaning that their process won't be given
//   foreground CPU scheduling (unless something else in that process is foreground) and,
//   if the system needs to kill them to reclaim more memory they be killed without too much harm.

//-Foreground Service-A service that keeps running even when the activity it started in terminates
//    A foreground service is a service that should have the same priority as an active activity
//    and therefore should not be killed by the Android system, even if the system is low on memory
//    -foreground service must provide a notification for the status bar
//    -you have to close a foreground service yourself or else it won't stop-stopSelf()
public class MyService extends Service {
    private MediaPlayer mMediaPlayer;

    //Called when we first create our service-called once, onStartCommand() however is called
    //    everytime we call startService()-startService() can be called many times
    @Override
    public void onCreate() {
        super.onCreate();
        //u can put code in here instead of onStartCommand() if u want the code to only run once
        // when service starts
        Toast.makeText(getApplicationContext(),"Service started!",Toast.LENGTH_SHORT).show();
        mMediaPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
        //
    }

    //When client calls startService()-the onStartCommand() is called after
    // onStartCommand() runs in background but it runs in MainThread
    // -triggered when we start our service() -A.K.A startService()
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        //** NOT IMPORTANT- All you need to know is this code creates a notification object
        String input = "Notification contextText Here";
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Example Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_android)
                .setContentIntent(pendingIntent)
                .build();

        //start the service in the foreground
        //   -so this means the started service is now a foreground service
        //   -if you don't make your service a foreground service-the system will kill the service
        //     when it needs to free up memory/or kill the service after 1 min ish
        //   -pass in an id(bigger than 0)-id identify the notification if u later want 2 update it
        startForeground(1, notification);
        //-You use startForeground(int, Notification) if killing your service would be disruptive
        // to the user, such as if your service is performing background music playback,
        // so the user would notice if their music stopped playing.

        //By default - services are run in the main UI thread - so any heavy work will freeze the UI
        //   -you should handle heavy work by running a seperate thread
        //   -or starting a IntentService

        //stopSelf()-stops the entire service-the next time it starts again-it calls onCreate()
        //   -use stopSelf() whenever the service is done it's task

        return START_NOT_STICKY;
        //-Service.START_STICKY-Service is restarted if it gets terminated. Intent data passed
        // to the onStartCommand method is null. Used for services which manages their
        // own state and do not depend on the Intent data. Service only gets restarted when
        // there is enough extra memory not being used by any other app
        //-Service.START_NOT_STICKY-Service is not restarted. Used for services which
        // are periodically triggered anyway. The service is only restarted if the runtime
        // has pending startService() calls since the service termination.
        //-Service.START_REDELIVER_INTENT-Similar to Service.START_STICKY but the last intent
        // that was passed is re-delivered to the onStartCommand method.
    }

    //Bounded services will need onBind to bind to the component that started it
    //    -The service can communicate back and forth with the bounded component(like an activity)
    //Unbounded service - sometimes called started service - doesn't need to implement this method
    //    -Unbound service cannot communicate with other components
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //binds a service to an activity-if you don't want to bind your service
        //   to any activity, return null
        return null;
    }

    //onDestroy() is triggered when stopService() is called
    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediaPlayer.stop();
        Toast.makeText(getApplicationContext(),"onDestroy()!",Toast.LENGTH_SHORT).show();

    }
}
