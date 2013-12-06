package se.svenne.assignment2a;


import java.util.Calendar;
import java.util.Date;

public class AlarmNotice {
    private int id;
    private int alarmID;
    private Calendar calendar;
    private Long calendarMilliseconds;
    private String alarmTimeString;
    private boolean repeat;

    //constructor
    public AlarmNotice(){

    }

    //constructor
    public AlarmNotice(int idIn, String alarmTimeStringIn, int alarmIDIn, boolean repeatIn, Long calendarMilliseconds){
        id = idIn;
        alarmID = alarmIDIn;

        Calendar calendar_alarm = Calendar.getInstance();
        calendar_alarm.setTimeInMillis(calendarMilliseconds);

        alarmTimeString = alarmTimeStringIn;
        repeat = repeatIn;


    }

    //set all values
    protected void setValues(int alarmIDIn, String alarmTimeStringIn, Calendar calendarIn, boolean repeatIn){
        alarmID = alarmIDIn;
        calendar = calendarIn;
        alarmTimeString = alarmTimeStringIn;
        repeat = repeatIn;
    }

    //returns the id
    protected int getId(){
        return id;
    }


    //set the id
    protected void setId(int idIn){
        id = idIn;
    }

    //returns the alarmID
    protected int getAlarmId(){
        return alarmID;
    }

    //set int alarmId
    protected void setAlarmId(int alarmIdIn){
        alarmID = alarmIdIn;
    }

    //returns the Calendar
    protected Calendar getCalendar(){
        return calendar;
    }

    //returns the Calendar in milliseconds
    protected long getCalendarMilli(){
        calendarMilliseconds = calendar.getTimeInMillis();
        return calendarMilliseconds;
    }

    //set calendar milliseconds
    protected void setCalendarMilliseconds(long calendarMillisecondsIn){
       calendarMilliseconds = calendarMillisecondsIn;
    }

    //returns the alarm time as a string
    protected String getAlarmTimeString(){
        return alarmTimeString;
    }

    //set the alarm time as a string
    protected void setAlarmString(String alarmTimeStringIn){
        alarmTimeString = alarmTimeStringIn;
    }

    //returns true or false if the alarm should be repeated
    protected boolean getRepeat(){
        return repeat;
    }

    //method to set if the alarm should be repeated, true or false
    protected void setRepeat(boolean repeatIn){
        repeat = repeatIn;
    }

    @Override
    public String toString(){

        String isRepeated;

        if(repeat != false){
            isRepeated = "repeteras varje dag.";
        } else {
            isRepeated = "";
        }

        return "Alarm: " + alarmTimeString + " " + isRepeated;
    }
}
