package com.warbargic.school.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

import com.warbargic.school.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by kippe_000 on 2017-07-03.
 */

public class Today_meal_widget_provider extends AppWidgetProvider {
    RemoteViews views;
    Context context;

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        this.context = context;
        views = new RemoteViews(context.getPackageName(), R.layout.today_meal_widget);

        // 급식
        int year = Integer.parseInt(new java.text.SimpleDateFormat("yyyy").format(new java.util.Date()));
        int month = Integer.parseInt(new java.text.SimpleDateFormat("MM").format(new java.util.Date()));
        int day = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
        Calendar oCalendar = Calendar.getInstance( );
        String[] week = { "일", "월", "화", "수", "목", "금", "토" };

        views.setTextViewText(R.id.title, "급식 "+month+"/"+day+" "+week[oCalendar.get(Calendar.DAY_OF_WEEK) - 1]+"요일");


        SharedPreferences meal_pre = context.getSharedPreferences("meal",Context.MODE_PRIVATE);
        String date = meal_pre.getString("date","");
        Log.d("date", year+"/"+month+"/"+day);
        if(date.equals(year+"/"+month+"/"+day)){
            String s = meal_pre.getString("data","");
            Log.d("data", s+":");
            views.setTextViewText(R.id.data_T, s);
        }else{
            String s = "급식 데이터가 없습니다";
            views.setTextViewText(R.id.data_T, s);
        }


        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        // wifi 또는 모바일 네트워크 어느 하나라도 연결이 되어있다면,
        if (wifi.isConnected() || mobile.isConnected()) {
            Log.i("연결됨" , "연결이 되었습니다.");
            // 오늘의 급식 가져오기
            Get_meal get_meal = new Get_meal();
            get_meal.execute();

        } else {
            Log.i("연결 안 됨" , "연결이 다시 한번 확인해주세요");


        }

        appWidgetManager.updateAppWidget(appWidgetIds, views);
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

    }
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }





    class Get_meal extends AsyncTask<String, String, String> {
        String data = null, data2 = null;


        @Override
        protected String doInBackground(String... params) {

            URL url = null;
            HttpURLConnection httpURLConnection = null;

            try{
                url = new URL("http://tlsdndql27.vps.phps.kr/taereung_school/get_meal_today.php");
                httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);


                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
//                String buffer = bufferedReader.readLine();
                String buffer = "";

                if((buffer = bufferedReader.readLine()) != null){
                    Log.d("석식", buffer);
                    data2 = buffer;
                }


                if((buffer = bufferedReader.readLine()) != null){
                    Log.d("중식", buffer);
                    data = buffer;
                }
                Log.d("data", data+"");
                Log.d("data2", data2+"");


            }catch (Exception e){
                e.printStackTrace();
            }


            return data;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // 최종적으로 출력될 데이터
            String real_data = "";

            if (data == null && data2 == null){
                real_data = "급식 데이터가 없습니다";
            }else {
                real_data += "※중식※\n"+data2;
                if(data != null){
                    real_data += "\n\n※석식※\n"+data;
                }
            }

            int year = Integer.parseInt(new java.text.SimpleDateFormat("yyyy").format(new java.util.Date()));
            int month = Integer.parseInt(new java.text.SimpleDateFormat("MM").format(new java.util.Date()));
            int day = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));

            SharedPreferences sharedPreferences = context.getSharedPreferences("meal", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Log.d("date",year+"/"+month+"/"+day);
            editor.putString("date", year+"/"+month+"/"+day);
            editor.putString("data",real_data);
            editor.commit();


            Calendar oCalendar = Calendar.getInstance( );
            String[] week = { "일", "월", "화", "수", "목", "금", "토" };
            views.setTextViewText(R.id.data_T, real_data);
        }
    }
}
