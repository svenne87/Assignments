package se.svenne.assignment2a;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class Alarm extends Fragment implements View.OnClickListener{

    private String timeString;
    private final Handler mHandler = new Handler();
    private Timer mTimer;
    private PendingIntent pIntent;
    private Button startAlarm;
    private ListView listView;
    protected static ArrayAdapter<AlarmNotice> alarmAdapter;
    private int alarmID = 0; //the id for the alarm (the unique pending intent)
    private List<AlarmNotice> alarms;
    private AlarmNotice aNoticeToAlter;
    private int timePickerDialogMinute;
    private int timePickerDialogHour;
    private AlarmDatabaseHandler alarmDb;

    TimePicker timePicker;
    TextView timerTextView;
    CheckBox checkBox;

    //handles the message send from TimePickerDialogShow
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message mess){
            //bundle object
            Bundle b = mess.getData();

            timePickerDialogHour = b.getInt("set_hour");
            timePickerDialogMinute = b.getInt("set_minute");

            //update alarm
            updateAlarm(aNoticeToAlter, timePickerDialogHour, timePickerDialogMinute);

            //Displays the new time for the alarm after we changed it
            Toast.makeText(getActivity().getBaseContext(), b.getString("set_time"), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_alarm, container, false);

        startAlarm = (Button) rootView.findViewById(R.id.start_button);
        listView = (ListView) rootView.findViewById(R.id.listView);
        checkBox = (CheckBox) rootView.findViewById(R.id.check_box);
        timerTextView = (TextView) rootView.findViewById(R.id.timer_text);
        timePicker = (TimePicker) rootView.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

        startAlarm.setOnClickListener(this);

        //initiate database
        alarmDb = new AlarmDatabaseHandler(getActivity());

        alarms = AlarmList.getList();  //fetch the list

        alarmAdapter = new ArrayAdapter<AlarmNotice>(getActivity(), android.R.layout.simple_list_item_1, alarms);

        listView.setAdapter(alarmAdapter);
        registerForContextMenu(listView);

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();

        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        setClock();
                    }
                });
            }
        },0,1000);

    }

    @Override
    public void onPause() {
        super.onPause();
        mTimer.cancel();
    }

    @Override

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo aInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;

        //get the AlarmNotice object
        aNoticeToAlter = alarmAdapter.getItem(aInfo.position);

        menu.setHeaderTitle(aNoticeToAlter.getAlarmTimeString());
        menu.add(1, 1, 1, "Change");
        menu.add(1, 2, 2, "Delete");
    }


    // This method is called when user selects an Item in the Context menu
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        //change AlarmNotice object
        if(itemId == 1){

            //create bundle
            Bundle b = new Bundle();

            //pass values
            b.putInt("set_hour", timePickerDialogHour);
            b.putInt("set_minute", timePickerDialogMinute);

            TimePickerDialogShow timePicker = new TimePickerDialogShow(handler);
            //Set the bundle object on timepicker
            timePicker.setArguments(b);

            //fragment manger for this activity
            FragmentManager fm = getActivity().getSupportFragmentManager();
            //fragment transaction
            FragmentTransaction ft = fm.beginTransaction();
            //Add fragment object to fragment transaction
            ft.add(timePicker, "time_picker");

            //open
            ft.commit();

        } else if(itemId == 2){
            //remove alarm
            removeAlarm(aNoticeToAlter);
        }

        return true;

    }



    @Override
    public void onClick(View v) {
        if(v == startAlarm){

            Intent intent = new Intent(getActivity(), BroadCastReceiverAlarm.class);

            //generate new ID for the pending intent
            alarmID = (int)(Math.random() * 9999);

            //check if alarm list is empty
            if(alarms.size() != 0){
                //if the Id is already in the list we generate a new one
                for(AlarmNotice alarm : alarms){
                    if(alarm.getAlarmId() == alarmID){
                        alarmID = (int)(Math.random() * 9999); //generate a new ID
                        break;
                    }
                }
            }

            //"add" the id to the intent, to be received later
            intent.putExtra("id", alarmID);

            pIntent = PendingIntent.getBroadcast(getActivity(), alarmID, intent, 0);

            AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Activity.ALARM_SERVICE);

            Date date = new Date();
            Calendar calendar_now = Calendar.getInstance();
            calendar_now.setTime(date);

            Calendar calendar_alarm = Calendar.getInstance();
            calendar_alarm.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
            calendar_alarm.set(Calendar.MINUTE, timePicker.getCurrentMinute());
            calendar_alarm.set(Calendar.SECOND, 0);

            //if it's in the past increment date
            if(calendar_alarm.before(calendar_now)){
                calendar_alarm.add(Calendar.DATE,1);
            }

            String alarmTimeString = "" + calendar_alarm.getTime();

            //create a new Object
            AlarmNotice alarmNotice = new AlarmNotice();

            //if it should be repeated
            if(checkBox.isChecked()){
                alarmNotice.setValues(alarmID, alarmTimeString, calendar_alarm, true);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar_alarm.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pIntent);
            } else {
                alarmNotice.setValues(alarmID, alarmTimeString, calendar_alarm, false);

                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar_alarm.getTimeInMillis(), pIntent);
            }

            alarmDb.addAlarm(alarmNotice);
            AlarmList.addToList(alarmNotice);

            alarmAdapter.notifyDataSetChanged();  // update adapter

            Toast.makeText(getActivity(), "Alarm is set to: " + alarmTimeString, Toast.LENGTH_LONG).show();
        }
    }

   //method to set the clock
    private void setClock(){
        //get and set time to textview
        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        timeString = fmt.format(date);
        timerTextView.setText(timeString);
    }

    //method to remove alarm from alarm manager and list
    private void removeAlarm(AlarmNotice alarmNoticeIn){

        Intent intent = new Intent(getActivity(), BroadCastReceiverAlarm.class);
        PendingIntent pendingIntent;

        pendingIntent = PendingIntent.getBroadcast(getActivity(), alarmNoticeIn.getAlarmId(), intent, 0);

        //cancel the alarm
        AlarmManager alarmManager = (AlarmManager)getActivity().getBaseContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        Toast.makeText(getActivity(), "Alarm removed!", Toast.LENGTH_LONG).show();

        alarmDb.deleteAlarm(alarmNoticeIn); //remove alarm from database
        AlarmList.removeFromList(alarmNoticeIn); //remove the alarm from list
        alarmAdapter.notifyDataSetChanged();

    }


    //method to update alarm object and change the alarm in alarm manager
    private void updateAlarm(AlarmNotice alarmNoticeIn, int newHourTime, int newMinuteTime){

        Intent intent = new Intent(getActivity(), BroadCastReceiverAlarm.class);
        intent.putExtra("id", alarmNoticeIn.getAlarmId());
        PendingIntent penIntent = PendingIntent.getBroadcast(getActivity(), alarmNoticeIn.getAlarmId(), intent, 0);
        AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(Activity.ALARM_SERVICE);

        //change the calendar
        Date date = new Date();
        Calendar calendar_now = Calendar.getInstance();
        calendar_now.setTime(date);

        Calendar calendar_alarm = Calendar.getInstance();
        //add new time
        calendar_alarm.set(Calendar.HOUR_OF_DAY, newHourTime);
        calendar_alarm.set(Calendar.MINUTE, newMinuteTime);
        calendar_alarm.set(Calendar.SECOND, 0);

        String alarmTimeString = "" + calendar_alarm.getTime();

        //if its in the past increment date
        if(calendar_alarm.before(calendar_now)){
            calendar_alarm.add(Calendar.DATE,1);
        }

        //create the new alarm object with changed values
        AlarmNotice alteredAlarmNotice = new AlarmNotice();
        alteredAlarmNotice.setValues(alarmNoticeIn.getAlarmId(), alarmTimeString ,calendar_alarm, alarmNoticeIn.getRepeat());

        //if it should be repeated
        if(alteredAlarmNotice.getRepeat() == true){
            //remove old alarm
            alarmManager.cancel(penIntent);
            //add new alarm
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar_alarm.getTimeInMillis(),AlarmManager.INTERVAL_DAY, penIntent);
        } else {
            //remove old alarm
            alarmManager.cancel(penIntent);
            //add new alarm
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar_alarm.getTimeInMillis(), penIntent);
        }

        //update old object in the list with the new
        AlarmList.updateList(alarmNoticeIn, alteredAlarmNotice);

        alarmDb.updateAlarm(alarmNoticeIn); //update alarm in database

        //update adapter
        alarmAdapter.notifyDataSetChanged();

    }

}
