package com.example.john.servicedemo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
//Simple Service demo-when user presses "start" button to startService()-it will then call
// onStartCommand()
public class MainActivity extends AppCompatActivity {
    public static final String CHANNEL_ID = "exampleServiceChannel";
    //Service lifecycle
    //-startService(Intent) is called-if service is not created, then onCreate() method
    //of that service is called.
    //-Once service started-onStartCommand(Intent) method in the service is called-it passes
    //  Intent object from startService(Intent) call
    //-If startService(intent) is called while the service is running, its onStartCommand()
    //  is also called. Therefore your service needs to be prepared that
    //  onStartCommand() can be called several times.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Intent intent = new Intent(getApplicationContext(), MyService.class);
//        intent.putExtra("key1","some value key1");
//        startService(intent);//Context.startService(Intent)

    }
    public void onClick(View view){
        if(view.getId()==R.id.start){
            //starts a service on the main thread-but it runs in background
            // Service can be used in tasks with no UI, but shouldn’t be too long.
//            startService(new Intent(this, MyService.class));

            //startService() can only be called when the app is running
            //   -if you want to start a service while your app is in the background then use
            //startForegroundService()- starts a service while the app is in the background
            //   -with this method - you have a 5 second time window to call startForeground()
            //     in your service class or else the system will kill your service immediately
            //-if you try to call startService() from the background-your app will crash
            ContextCompat.startForegroundService(this, new Intent(this, MyService.class));
            //-Unlike the ordinary startService(Intent), this method can be used at any time,
            // regardless of whether the app hosting the service is in a foreground state.
        }else if(view.getId() == R.id.stop){
            //You must call stopSelf() or stopService() to stop a service
            // once your job is done
            stopService(new Intent(this, MyService.class));
        }
    }
}
//-Only one service can be started & be running at a time. If th
//IntentService class -one time use - once task done, it terminates itself automatically.
//   The IntentService class offers the onHandleIntent() method which will be
//   asynchronously called by the Android system.