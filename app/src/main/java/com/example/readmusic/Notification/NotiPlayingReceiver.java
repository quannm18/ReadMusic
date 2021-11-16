package com.example.readmusic.Notification;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotiPlayingReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String actionName = intent.getAction();
        Intent serviceIntent = new Intent();
        if (actionName!=null){
            switch (actionName){
                case "hsc":

            }
        }
    }
}
