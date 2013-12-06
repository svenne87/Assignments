package se.svenne.assignment2a;

import android.app.TimePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

public class TimePickerDialogShow extends DialogFragment {
    Handler handler ;
    int hour;
    int minute;

    public TimePickerDialogShow(Handler h){
        //reference to the message handler
        handler = h;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        //bundle object to get currently set time
        Bundle b = getArguments();

        hour = b.getInt("set_hour");  //get hour
        minute = b.getInt("set_minute");  //get minute

        android.app.TimePickerDialog.OnTimeSetListener listener = new android.app.TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourIn, int minuteIn) {

                hour = hourIn;
                minute = minuteIn;

                //create bundle to pass set time
                Bundle b = new Bundle();
                b.putInt("set_hour", hour);
                b.putInt("set_minute", minute);

                //to string
                b.putString("set_time", "Alarm changed to: " + Integer.toString(hour) + " : " + Integer.toString(minute));

                //create message
                Message mess = new Message();
                mess.setData(b);

                //Message is sending using the message handler
                handler.sendMessage(mess);
            }
        };

       return new TimePickerDialog(getActivity(), listener, hour, minute, true);
    }

}



