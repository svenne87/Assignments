package se.svenne.assignment2a;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BroadCastReceiverAlarm extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //receive the id from intent
        int id = intent.getIntExtra("id", -1);
        Intent service = new Intent(context, MyAlarmService.class);
        //set id to intent
        service.putExtra("id", id);
        context.startService(service);
    }
}
