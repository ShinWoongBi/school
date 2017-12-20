package com.warbargic.school.fcm;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.warbargic.school.R;

public class save_token extends Activity {
    static boolean on = true;
    boolean finish = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_token);




//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(on){
//                    try {
//                        Thread.sleep(10);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    if(finish){
//                        break;
//                    }
//                }
//                if(on == false) {
//                    finish();
//                }
//            }
//        }).start();



    }

    @Override
    protected void onStop() {
        super.onStop();
        finish = true;
    }
}
