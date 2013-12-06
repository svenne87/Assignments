package se.svenne.assignment2a;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Calendar;

public class AlarmDialog extends Activity {

    private Uri alertSound;
    private Ringtone ringtone;
    private AlarmDatabaseHandler alarmDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the alarm ID from the intent extra data
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        int alarmID = 0;

        if (extras != null) {
            alarmID = extras.getInt("id", -1);
        } else {
            alarmID = -1;
        }


        AlarmNotice alarmNotice = new AlarmNotice();
        alarmDb = new AlarmDatabaseHandler(this);

        //get the correct alarm object from list
        for(AlarmNotice alarm : AlarmList.getList()){
            if(alarm.getAlarmId() == alarmID){
                alarmNotice = alarm;
                break;
            }
        }

        alertSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        //if alarm sound not found
        if(alertSound == null){
            alertSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        startAlarmSound(); //start alarm sound

        //For Notification
        alertDialog.setTitle("Alarm");
        alertDialog.setMessage("WAKE UP!");
        final AlarmNotice finalAlarmNotice = alarmNotice;
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Shutdown Alarm", new DialogInterface.OnClickListener() {
            //if shutdown alarm is clicked
            public void onClick(DialogInterface dialog, int which) {
                if(finalAlarmNotice != null){
                    removeAlarm(finalAlarmNotice, false); //remove the alarm
                }
                ringtone.stop(); //stop alarm sound
                finish();  //this activity is finished
            }
        });

        final int finalAlarmID = alarmID;
        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "Snooze", new DialogInterface.OnClickListener() {
            //if snooze is clicked
            public void onClick(DialogInterface dialog, int which) {
                if(finalAlarmNotice != null){
                    removeAlarm(finalAlarmNotice, true); //remove the alarm
                }
                ringtone.stop(); //stop alarm sound
                snoozeFunction(finalAlarmID); //snooze
                finish();  //this activity is finished
            }
        });

        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialog.show();

    }


    //Method to play alarm sound
    private void startAlarmSound(){
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), alertSound);
        ringtone.play();
    }

    //Method for the snooze function
    private void snoozeFunction(int alarmIDIn){

        Intent intentI = new Intent(this, BroadCastReceiverAlarm.class);
        intentI.putExtra("id", alarmIDIn);
        PendingIntent penIntent = PendingIntent.getBroadcast(this, alarmIDIn, intentI, 0);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        //Snooze 5 min = 360 seconds
        calendar.add(Calendar.SECOND, 360);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), penIntent);
    }

    //method to remove alarm from alarm manager and list
    private void removeAlarm(AlarmNotice alarmNoticeIn, boolean snooze){

        Intent intent = new Intent(this, BroadCastReceiverAlarm.class);
        PendingIntent pendingIntent;

        pendingIntent = PendingIntent.getBroadcast(this, alarmNoticeIn.getAlarmId(), intent, 0);

        //cancel the alarm
        AlarmManager alarmManager = (AlarmManager)this.getBaseContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        //if we choose to snooze
        if(snooze != true){
            Toast.makeText(this, "Alarm removed!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Snooze active!", Toast.LENGTH_LONG).show();
        }

        alarmDb.deleteAlarm(alarmNoticeIn); //remove alarm from database
        AlarmList.removeFromList(alarmNoticeIn); //remove the alarm from list
        Alarm.alarmAdapter.notifyDataSetChanged();

    }

}

