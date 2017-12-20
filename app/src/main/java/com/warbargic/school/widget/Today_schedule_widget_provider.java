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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by kippe_000 on 2017-07-04.
 */

public class Today_schedule_widget_provider extends AppWidgetProvider {
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
        views = new RemoteViews(context.getPackageName(), R.layout.today_schedule_widget);

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        // wifi 또는 모바일 네트워크 어느 하나라도 연결이 되어있다면,
        if (wifi.isConnected() || mobile.isConnected()) {
            Log.i("연결됨" , "연결이 되었습니다.");
            // 오늘의 급식 가져오기
            Get_schedule get_schedule = new Get_schedule();
            get_schedule.execute();

        } else {
            Log.i("연결 안 됨" , "연결이 다시 한번 확인해주세요");


        }

        // 학사일정
        int year = Integer.parseInt(new java.text.SimpleDateFormat("yyyy").format(new java.util.Date()));
        int month = Integer.parseInt(new java.text.SimpleDateFormat("MM").format(new java.util.Date()));
        int day = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));
        Calendar oCalendar = Calendar.getInstance( );
        String[] week = { "일", "월", "화", "수", "목", "금", "토" };

        views.setTextViewText(R.id.title, "학사일정 "+ month+"/"+day+" "+week[oCalendar.get(Calendar.DAY_OF_WEEK) -1]+"요일");

        SharedPreferences schedule_pre = context.getSharedPreferences("schedule", Context.MODE_PRIVATE);
        String json_data = schedule_pre.getString("data","");
        Log.d("json_data", json_data+":");
        try {
            JSONObject jsonObject = new JSONObject(json_data);
            int size = jsonObject.length();
            Log.d("size", size+"");
            Iterator i = jsonObject.keys();
            String date = null;
            String data = null;
            int data_size = 0;
            while(i.hasNext()) {
                date = i.next().toString();
                data = jsonObject.getString(date);

                Log.d("date", date+"");
                Log.d("data", data+"");

                if(date.equals(month+"/"+day))
                    break;
                else
                    data_size++;

            }
            if(data_size == size){
                views.setTextViewText(R.id.data_T, "일정이 없습니다");
            }else{
                views.setTextViewText(R.id.data_T, data);
            }


        } catch (JSONException e) {
            e.printStackTrace();
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





    class Get_schedule extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            int year = Integer.parseInt(new java.text.SimpleDateFormat("yyyy").format(new java.util.Date()));
            int month = Integer.parseInt(new java.text.SimpleDateFormat("MM").format(new java.util.Date()));

            String param = "year="+year+"&month="+month;

            URL url = null;
            HttpURLConnection httpURLConnection = null;

            try{
                url = new URL("http://tlsdndql27.vps.phps.kr/taereung_school/get_schedule.php");
                httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(param.getBytes("UTF-8"));

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String buffer = "";


                String mon = String.format("%02d",month);
                String data = "{";
                int i = 0;

                while((buffer = bufferedReader.readLine()) != null){
//                    Log.d("abc", buffer);
                    String[] result = buffer.split("@@!@!@");
//                    Log.d("abb",result[0]);
//                    Log.d("log", mon +"/"+result[0]+":::"+result[1]);

                    data += "\""+mon +"/"+result[0]+"\":\""+result[1]+"\",";
                    i++;
                }
                data = data.substring(0, data.length()-1);
                data += "}";


                SharedPreferences sharedPreferences = context.getSharedPreferences("schedule", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("mon",mon);
                editor.putString("data", data);
                editor.commit();




            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

}
