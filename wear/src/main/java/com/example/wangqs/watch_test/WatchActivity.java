package com.example.wangqs.watch_test;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.wearable.view.WatchViewStub;
import android.widget.ImageView;
import android.widget.TextView;

public class WatchActivity extends Activity implements HeartbeatService.OnChangeListener {

    private TextView mTextView;
    private ImageView mImageView;
    boolean jump = true;
    int initial_value = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
                mImageView = (ImageView) stub.findViewById(R.id.imageView);


                bindService(new Intent(WatchActivity.this, HeartbeatService.class), new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName componentName, IBinder binder) {
 //                       Log.d(LOG_TAG, "connected to service.");
                        // set our change listener to get change events
                        ((HeartbeatService.HeartbeatServiceBinder)binder).setChangeListener(WatchActivity.this);
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName componentName) {

                    }
                }, Service.BIND_AUTO_CREATE);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onValueChanged(int newValue_Y, int newValue_Z) {
        // will be called by the service whenever the heartbeat value changes.
        mTextView.setText(Integer.toString(newValue_Z));

        if(((newValue_Y-initial_value)>100)||((initial_value-newValue_Y)>100)) {
            if (jump) {
                mImageView.setImageDrawable(getResources().getDrawable(R.drawable.jump));
                jump = false;
            } else {
                mImageView.setImageDrawable(getResources().getDrawable(R.drawable.baoli));
                jump = true;
            }
            initial_value = newValue_Y;
        }


    }
}
