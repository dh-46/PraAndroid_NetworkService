package tw.dh46.pranetworkservice;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private ImageView img;

    private UIHandler uiHandler;

    private NetworkService1 service1;

    private boolean isBind;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
           NetworkService1.LocalBinder localBinder = (NetworkService1.LocalBinder)binder;
           service1 = localBinder.getService();
           service1.setLocalHandler(uiHandler);
            isBind = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBind = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.main_text);
        img = findViewById(R.id.main_img);


        uiHandler = new UIHandler();
    }

    public void test1(View view) {
        //  第一招: Java_URL(抓取網頁原始碼用Log顯示)
        //  呼叫service去做
        service1.test1();
    }

    public void test2(View view) {
        //  PM2_接收遠端圖片
        //  1.  下載回來再呈現(SDcard權限, 寫完發出通知)
        //  2.  邊下邊呈現(test2用這個)
        //  一樣Java招: URL
        service1.test2();
    }

    public void test3(View view) {
        service1.test3();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    public void test4(View view) {
    }


    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, NetworkService1.class);
        bindService(intent,serviceConnection,BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!isBind){
            unbindService(serviceConnection);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    class UIHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:
                    String text = msg.getData().getString("test1line", "No data");
                    textView.setText(text);
                    break;
                case 2:
                    Bitmap bitmap = msg.getData().getParcelable("img");
                    img.setImageBitmap(bitmap);
                    break;
            }


        }
    }
}
