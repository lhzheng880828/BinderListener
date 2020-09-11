# 1.简介
这是一个Binder接口回调实现。
通过位运算方式监听不同的回调事件，Binder Listener中可以定义多种类型事件，通过或运算(|)订阅多种类型的事件

# 2.使用

在Activity生命周期onCreate方法订阅所关心的事件， 在onDestory方法取消订阅，解除绑定

```java

public class MainActivity extends AppCompatActivity {

private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //这里做1s延迟，避免MyApp中MyService尚未启动完成
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
                //创建会议，触发会议状态回调
                ConfCtlManager.getInstance().createConf();
            }
        });

        Button endConfBtn = findViewById(R.id.endConfBtn);
        endConfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建会议，触发会议状态回调
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

```

# 3.BinderListenerManager

该类管理注册的BinderListenr, add方法加入一个BinderListener， remove方法删除一个BinderListener， notifyForAllSubsriber方法用于通知指定事件的监听回调。