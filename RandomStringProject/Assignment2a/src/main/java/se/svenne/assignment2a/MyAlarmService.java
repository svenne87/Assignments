package se.svenne.assignment2a;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyAlarmService extends Service {

    @Override
    public void onCreate() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        // read id
        int id = intent.getIntExtra("id", -1);

        Intent dialog = new Intent(this, AlarmDialog.class);
        dialog.putExtra("id", id);
        dialog.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(dialog);

    }

    @Override
    public boolean onUnbind(Intent intent)
    {
        return super.onUnbind(intent);
    }



}

