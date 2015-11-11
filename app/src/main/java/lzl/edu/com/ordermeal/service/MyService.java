package lzl.edu.com.ordermeal.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import lzl.edu.com.ordermeal.ui.FindCollectActivity;

public class MyService extends Service {
    private Intent mIntent;
    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mIntent = new Intent(this, FindCollectActivity.class);
        startActivity(mIntent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
