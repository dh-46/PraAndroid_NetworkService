package tw.dh46.pranetworkservice;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NetworkService1 extends Service {

    private MainActivity.UIHandler handler;

    private LocalBinder localBinder = new LocalBinder();

    public NetworkService1() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return localBinder;
    }

    class LocalBinder extends Binder {

        NetworkService1 getService(){
            return NetworkService1.this;
        }
    }

    public void setLocalHandler(MainActivity.UIHandler uiHandler){
        this.handler = uiHandler;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //  現在是透過Connection 沒你的事
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void test1(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    URL url = new URL("https://tw.yahoo.com/");
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    connection.connect();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    String line;
                    StringBuffer sb = new StringBuffer();
                    while((line = reader.readLine()) != null) {
                        Log.d("dh46", line);
                        sb.append(line);
                    }
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    message.what = 1;
                    bundle.putString("test1line", sb.toString());
                    message.setData(bundle);
                    handler.sendMessage(message);

                    reader.close();
                    Log.d("dh46","Test1_Connection OK");
                } catch (Exception e) {
                    Log.d("dh46",e.toString());
                }
            }
        }.start();
    }

    public void test2() {
        Log.d("dh46", "test2_Start");
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    URL url = new URL("https://s.yimg.com/ny/api/res/1.2/mxdGVXTvduNWY4qyj36MQQ--~A/YXBwaWQ9aGlnaGxhbmRlcjtzbT0xO3c9NzkwO2g9NDQ5/http://media.zenfs.com/zh-Hant-TW/homerun/ftvn.com.tw/66f78fc05cb6460cd351115a1fedc40a");
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    connection.connect();
                    Bitmap bitmap = BitmapFactory.decodeStream(connection.getInputStream());

                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("img", bitmap);
                    message.setData(bundle);
                    message.what = 2;
                    handler.sendMessage(message);
                    Log.d("dh46", "Test2_OK");
                } catch (java.io.IOException e) {
                    Log.d("dh46", e.toString());
                }
            }
        }.start();
    }

    public void test3() {

    }



}
