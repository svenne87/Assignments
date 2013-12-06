package se.svenne.assignment2a;

import java.util.ArrayList;
import java.util.List;

public class AlarmList {

    private static List<AlarmNotice> alarms = new ArrayList<AlarmNotice>();

    //return list
    protected static List<AlarmNotice> getList(){
        return alarms;
    }

    //set list
    protected static void setList(List<AlarmNotice> listIn){
        alarms = listIn;
    }

    //add to the list
    protected static void addToList(AlarmNotice aNotice){
        alarms.add(aNotice);
    }

    //remove from the list
    protected static void removeFromList(AlarmNotice aNotice){
        alarms.remove(aNotice);
    }

    //update the list
    protected static void updateList(AlarmNotice oldAlarmNotice, AlarmNotice newAlarmNotice){

        //find the position for the old object
        int pos = alarms.indexOf(oldAlarmNotice);

        //update
        alarms.set(pos, newAlarmNotice);
    }

}
