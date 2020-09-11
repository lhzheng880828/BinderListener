package com.calvin.android.binderlistener;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.calvin.android.binderlistener.fwlib.ConfCtlManager;
import com.calvin.android.binderlistener.fwlib.ConfStateListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //添加会议状态/参会人员状态监听， 使用位运算监听多种类型事件
                ConfCtlManager.getInstance().listen(listener,ConfStateListener.LISTEN_CONF_STATE|
                        ConfStateListener.LISTEN_SUBSCRIBER_STATE);
            }
        }, 1000);

        setContentView(R.layout.activity_main);

        Button createConfBtn = findViewById(R.id.createConfBtn);
        createConfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfCtlManager.getInstance().createConf();
            }
        });

        Button endConfBtn = findViewById(R.id.endConfBtn);
        endConfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfCtlManager.getInstance().endConf();

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消状态监听
        ConfCtlManager.getInstance().listen(listener, ConfStateListener.LISTEN_NONE);
    }

    private ConfStateListener listener = new ConfStateListener(){

        @Override
        protected void onConfChanged(int state, ConfInfo confInfo) {
            Log.d(TAG, "state = "+state+", confInfo = "+confInfo);
        }

        @Override
        protected void onSubscriberChanged(SubscriberInfo subInfo) {
            Log.d(TAG, "subInfo = "+subInfo);
        }
    };
}