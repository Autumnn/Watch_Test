package com.example.wangqs.watch_test;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.WearableListenerService;

public class DataLayerListenerService extends WearableListenerService{
    private static final String LOG_TAG = "WearableListener";
    public static final String HEARTBEAT = "HEARTBEAT";

    private static Handler handler;
    private static int currentValue=0;
    private static int Y=0;
    private static int Z=0;

    public static Handler getHandler() {
        return handler;
    }

    public static void setHandler(Handler handler) {
        DataLayerListenerService.handler = handler;
        // send current value as initial value.

        Bundle b = new Bundle();
        b.putInt("Y", Y);
        b.putInt("Z", Z);
        Message msg = handler.obtainMessage();

        if(handler!=null){
            msg.setData(b);
            msg.sendToTarget();
//            handler.sendEmptyMessage(currentValue);
        }

    }

    @Override
    public void onPeerConnected(Node peer) {
        super.onPeerConnected(peer);

        String id = peer.getId();
        String name = peer.getDisplayName();

        Log.d(LOG_TAG, "Connected peer name & ID: " + name + "|" + id);
    }
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        Log.d(LOG_TAG, "received a message from wear: " + messageEvent.getPath());
        // save the new heartbeat value
        String Value = messageEvent.getPath();
        String s[] = Value.split("-");
        Y = Integer.parseInt(s[0]);
        Z = Integer.parseInt(s[1]);
//        currentValue = Integer.parseInt(messageEvent.getPath());

        Bundle b = new Bundle();
        b.putInt("Y", Y);
        b.putInt("Z", Z);
        Message msg = handler.obtainMessage();

        if(handler!=null) {
            // if a handler is registered, send the value as new message
            //handler.sendEmptyMessage(currentValue);
            msg.setData(b);
            msg.sendToTarget();
        }
    }
}
