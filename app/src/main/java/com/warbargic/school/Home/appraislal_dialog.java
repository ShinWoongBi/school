package com.warbargic.school.Home;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.warbargic.school.R;

/**
 * Created by kippe_000 on 2017-04-21.
 */

public class appraislal_dialog extends Dialog {
    RatingBar ratingBar;
    Button button;
    EditText editText;

    boolean star = false;
    boolean text = false;

    public appraislal_dialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);
        setContentView(R.layout.appraisal_dialog);


        editText = (EditText)findViewById(R.id.edit);
        button = (Button)findViewById(R.id.btn);
        ratingBar = (RatingBar)findViewById(R.id.star);
        ratingBar.setNumStars(5);
        ratingBar.setStepSize(1);


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                System.out.println(editText.getText().toString()+":");
                if(!editText.getText().toString().equals("")){
                    text = true;
                }else{
                    text = false;
                }

                Check();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        final TextView result_T = (TextView)findViewById(R.id.result);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                String[] result_s = {"매우 나쁨","나쁨","보통","좋음","매우 좋음"};
                String[] color_s = {"#FF403B","#FF7F79","#FFDA7B","#7CFF86","#32FF41"};


                int ratin_i = (int)rating - 1;
//                Log.d("ratin", ratin_i+"");

                if(ratin_i != -1) {
                    result_T.setText(result_s[ratin_i]);
                    result_T.setTextColor(Color.parseColor(color_s[ratin_i]));
                    star = true;
                }else{
                    result_T.setText("--");
                    result_T.setTextColor(Color.parseColor("#000000"));
                    star = false;
                }

                Check();
            }
        });
    }

    void Check(){

//        Log.d("star",star+"");
//        Log.d("text", text+"");
        if(star == true && text == true){
            button.setClickable(true);
            button.setBackgroundColor(Color.parseColor("#FFE551"));

        }else{
            button.setClickable(false);
            button.setBackgroundColor(Color.parseColor("#fff09e"));

        }
    }
}
