package edu.cmu.pocketsphinx.demo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;


import ipc.RootShellCmd;


/**
 * Created by hozdanny on 15/11/16.
 */
public class TouchController extends Service {
    public static final String TAG = "TouchController";
    RootShellCmd rootShellCmd = new RootShellCmd();


    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "TouchController Service Created", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Service: onCreate()");
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "TouchController Service Destroy", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Service: onDestroy()");
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service: onStartCommand()");
        handleIntent(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    //handle the intent with action
    private void handleIntent(Intent intent) {
        if (intent == null || intent.getAction() == null) return;
        // get the action specified in the intent. The actions are given in Constants.
        String action = intent.getAction();
        if (intent.getAction().equalsIgnoreCase(Constant.ACTION_NOTIFICATION)) {
            notification();
        } else if (intent.getAction().equalsIgnoreCase(Constant.ACTION_VOLUME_UP)) {
            volumeUp();
        } else if (intent.getAction().equalsIgnoreCase(Constant.ACTION_VOLUME_DOWN)) {
            volumeDown();
        } else if (intent.getAction().equalsIgnoreCase(Constant.ACTION_OPEN_BROWSER)) {
            open_Brower();
        } else if (intent.getAction().equalsIgnoreCase(Constant.ACTION_CLOSE_BROWSER)) {
            close_Brower();
        } else if (intent.getAction().equalsIgnoreCase(Constant.ACTION_OPEN_CALCULATOR)) {
            open_Calculator();
        } else if (intent.getAction().equalsIgnoreCase(Constant.ACTION_CLOSE_CALCULATOR)) {
            close_Calculator();
        } else if (intent.getAction().equalsIgnoreCase(Constant.ACTION_GO_Home)) {
            go_home();
        } else if (intent.getAction().equalsIgnoreCase(Constant.ACTION_GO_BACK)) {
            go_back();
        }  else if (intent.getAction().equalsIgnoreCase(Constant.ACTION_OPEN_CAMERA)) {
            open_Camera();
        } else if (intent.getAction().equalsIgnoreCase(Constant.ACTION_SHOOT)) {
            tap();
//            rootShellCmd.simulateKey(KeyEvent.KEYCODE_FOCUS);
//            rootShellCmd.simulateKey(KeyEvent.KEYCODE_CAMERA);
        } else if (intent.getAction().equalsIgnoreCase(Constant.ACTION_OPEN_GAME)) {
            open_Game();
        }

    }

    public void go_home(){
        rootShellCmd.simulateKey(KeyEvent.KEYCODE_HOME);
    }

    public void go_back(){
        rootShellCmd.simulateKey(KeyEvent.KEYCODE_BACK);
    }


    public void volumeUp() {
        //RootShellCmd rootShellCmd = new RootShellCmd();
        rootShellCmd.simulateKey(KeyEvent.KEYCODE_VOLUME_UP);
    }

    public void volumeDown() {
        rootShellCmd.simulateKey(KeyEvent.KEYCODE_VOLUME_DOWN);
    }


    public void notification() {
        //rootShellCmd.simulateKey(KeyEvent.KEYCODE_MOVE_HOME);
//        Toast.makeText(this, "notification", Toast.LENGTH_SHORT).show();
        DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
        int y = displaymetrics.heightPixels;
        int x = displaymetrics.widthPixels;
        String temp = x / 2 + " 1 " + x / 2 + " " + y;
        rootShellCmd.simulateSwip(temp);
    }

    public void swipe_down(){
        DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
        int y = displaymetrics.heightPixels;
        int x = displaymetrics.widthPixels;
        String temp = x / 2 +" "+ y/4 +" "+ x / 2 + " " + 3*y/4;
        rootShellCmd.simulateSwip(temp);
    }

    public void swipe_up(){
        DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
        int y = displaymetrics.heightPixels;
        int x = displaymetrics.widthPixels;
        String temp =  x / 2 + " " + 3*y/4+" "+x / 2 +" "+ y/4 ;
        rootShellCmd.simulateSwip(temp);
    }

    public void swipe_left(){
        DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
        int y = displaymetrics.heightPixels;
        int x = displaymetrics.widthPixels;
        String temp =  x / 4 + " " + y/2+" "+3*x/4 +" "+ y/2;
        rootShellCmd.simulateSwip(temp);
    }

    public void swipe_right(){
        DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
        int y = displaymetrics.heightPixels;
        int x = displaymetrics.widthPixels;
        String temp =  3*x / 4 + " " + y/2+" "+ x/4 +" "+ y/2;
        rootShellCmd.simulateSwip(temp);
    }

    public void tap(){
        DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
        int y = displaymetrics.heightPixels;
        int x = displaymetrics.widthPixels;
        String temp =  x / 2 + " " + y/2;
        rootShellCmd.simulateTap(temp);
    }




    public void open_Brower(){
        //Toast.makeText(this, "open_Brower", Toast.LENGTH_SHORT).show();
        rootShellCmd.exec("am start -a android.intent.action.VIEW -d http://www.google.com --ez create_new_tab true");
    }

    public void close_Brower(){
        //Toast.makeText(this, "close_Brower", Toast.LENGTH_SHORT).show();
        rootShellCmd.exec("am force-stop com.android.chrome");
        rootShellCmd.exec("am force-stop com.android.browser");
    }


    public void open_Calculator(){
        rootShellCmd.exec("am start -n com.android.calculator2/com.android.calculator2.Calculator");
    }

    public void close_Calculator(){
        rootShellCmd.exec("am force-stop com.android.calculator2");
    }

    public void open_Camera(){
        rootShellCmd.exec("am start -n com.android.camera/com.android.camera.Camera");
    }

    public void close_Camera(){
        //go_home();
//        rootShellCmd.exec("am force-stop com.android.camera");
    }

    public void open_Game(){
        rootShellCmd.exec("monkey -p com.dotgears.flappybird -c android.intent.category.LAUNCHER 1");

    }

}
