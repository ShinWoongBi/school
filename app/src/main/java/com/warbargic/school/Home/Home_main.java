package com.warbargic.school.Home;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.warbargic.school.Home.fragment.fragment_class;
import com.warbargic.school.Home.fragment.fragment_class2;
import com.warbargic.school.Home.fragment.fragment_class3;
import com.warbargic.school.Home.fragment.fragment_class4;
import com.warbargic.school.Home.fragment.fragment_class5;
import com.warbargic.school.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import static com.warbargic.school.R.id.date;


public class Home_main extends Fragment {
    TextView place_name_T, sky_name_T, tmax_T, tmin_T, tc_T, date_T;
    ListView calender_listview, meal_listview;
    TextView meal_T;
    ScrollView scrollView;
    Calender_adapter calender_adapter;
    ImageView weather_image;
//    ViewPager viewPager;
    int COUNT = 5;
//    Thread_home thread_home;
    boolean stop = false;
    Animation animation;
    SharedPreferences sharedPreferences;
    LocationManager mLocationManager = null;
    ImageButton imageButton = null;
    ProgressBar progressBar;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.home, container, false);

        animation = AnimationUtils.loadAnimation(getContext(),R.anim.rotate);
        place_name_T = (TextView)view.findViewById(R.id.place_name);
        sky_name_T = (TextView)view.findViewById(R.id.sky_name);
        tmax_T = (TextView)view.findViewById(R.id.tmax);
        tmin_T = (TextView)view.findViewById(R.id.tmin);
        tc_T = (TextView)view.findViewById(R.id.tc);
        date_T = (TextView)view.findViewById(R.id.date);
        imageButton = (ImageButton)view.findViewById(R.id.weather_btn);
        progressBar =((ProgressBar)view.findViewById(R.id.progressbar));
        weather_image = (ImageView)view.findViewById(R.id.weather_image);

        calender_listview = (ListView) view.findViewById(R.id.calendar_small);
//        weather_listview = (ListView)view.findViewById(R.id.weather_small);
        meal_listview = (ListView)view.findViewById(R.id.meal_small);
        scrollView = (ScrollView)view.findViewById(R.id.scrollview);
        calender_adapter = new Calender_adapter(getContext());
        meal_T = (TextView)view.findViewById(R.id.meal_text);
//        viewPager = (ViewPager)view.findViewById(R.id.home_viewpager);

//        Set_data();

        sharedPreferences = getActivity().getSharedPreferences("weather", Context.MODE_PRIVATE);
        calender_listview.setAdapter(calender_adapter);
        calender_listview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        final Fragment[] fragments = {new fragment_class(),new fragment_class2(), new fragment_class3(), new fragment_class4(), new fragment_class5()};


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getweather();
                imageButton.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        ((Button)view.findViewById(R.id.appraisal_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appraislal_dialog appraislal_dialog = new appraislal_dialog(getContext());
                appraislal_dialog.show();
            }
        });

        int month = Integer.parseInt(new java.text.SimpleDateFormat("MM").format(new java.util.Date()));
        ((TextView)view.findViewById(R.id.calendar_month)).setText(month+"월");

        ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);


        // 날씨
        String place_name, sky_name, tc, tmax, tmin, weather_date, sky_code;
        place_name = sharedPreferences.getString("place_name","--");
        sky_name = sharedPreferences.getString("sky_name","--");
        tc = sharedPreferences.getString("tc","--");
        tmax = sharedPreferences.getString("tmax", "--");
        tmin = sharedPreferences.getString("tmin", "--");
        weather_date = sharedPreferences.getString("date", "업데이트 없음");
        sky_code = sharedPreferences.getString("code","");

        System.out.println("--------weather sharedpreference data--------");
        System.out.println(place_name+":");
        System.out.println(sky_name +":");
        System.out.println(tc+":");
        System.out.println(tmax+":");
        System.out.println(tmin+":");
        System.out.println(weather_date+":");

        place_name_T.setText(place_name+"");
        sky_name_T.setText(sky_name+"");
        tc_T.setText(tc+"");
        tmax_T.setText(tmax+"");
        tmin_T.setText(tmin+"");
        date_T.setText(weather_date);

        if(sky_code.equals("SKY_A01")){
            weather_image.setImageResource(R.drawable.weather_1);
        }else if(sky_code.equals("SKY_A02")){
            weather_image.setImageResource(R.drawable.weather_2);
        }else if(sky_code.equals("SKY_A03") || sky_code.equals("SKY_A07")){
            weather_image.setImageResource(R.drawable.weather_3);
        }else if(sky_code.equals("SKY_A04") || sky_code.equals("SKY_A08") || sky_code.equals("SKY_A06") || sky_code.equals("SKY_A10")){
            weather_image.setImageResource(R.drawable.weather_4);
        }else if(sky_code.equals("SKY_A05") || sky_code.equals("SKY_A09")){
            weather_image.setImageResource(R.drawable.weather_5);
        }else if(sky_code.equals("SKY_A11")){
            weather_image.setImageResource(R.drawable.weather_11);
        }else{
            weather_image.setImageResource(R.drawable.weather_11);
        }


        // 학사일정
        SharedPreferences schedule_pre = getContext().getSharedPreferences("schedule", Context.MODE_PRIVATE);
        String json_data = schedule_pre.getString("data","");
        Log.d("json_data", json_data+":");
        try {
            JSONObject jsonObject = new JSONObject(json_data);
            int size = jsonObject.length();
            Log.d("size", size+"");
            Iterator i = jsonObject.keys();
            String date;
            String data;
            while(i.hasNext()) {
                date = i.next().toString();
                data = jsonObject.getString(date);

                Log.d("date", date+"");
                Log.d("data", data+"");

                calender_adapter.AddItem(date, data);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        calender_adapter.notifyDataSetChanged();
        int day = Integer.parseInt(new java.text.SimpleDateFormat("dd").format(new java.util.Date()));
        calender_listview.setSelection(day-1);

        // 급식
        int year = Integer.parseInt(new java.text.SimpleDateFormat("yyyy").format(new java.util.Date()));
//        int day = Integer.parseInt(new SimpleDateFormat("dd").format(new Date())); //위에 있음
        SharedPreferences meal_pre = getContext().getSharedPreferences("meal",Context.MODE_PRIVATE);
        String date = meal_pre.getString("date","");
        Log.d("date", year+"/"+month+"/"+day);
        if(date.equals(year+"/"+month+"/"+day)){
            String s = meal_pre.getString("data","");
            Log.d("data", s+":");
            meal_T.setText(s);
        }else{
            String s = "급식 데이터가 없습니다";
            meal_T.setText(s);

        }





        // wifi 또는 모바일 네트워크 어느 하나라도 연결이 되어있다면,
        if (wifi.isConnected() || mobile.isConnected()) {
            Log.i("연결됨" , "연결이 되었습니다.");
            // 학사일정 가져오기
            Get_schedule get_schedule = new Get_schedule();
            get_schedule.execute();

            // 오늘의 급식 가져오기
            Get_meal get_meal = new Get_meal();
            get_meal.execute();

            // 날씨 가져오기
            getweather();
        } else {
            Log.i("연결 안 됨" , "연결이 다시 한번 확인해주세요");


        }



        return view;
    }

    @Override
    public void onStop() {
        super.onStop();

    }


    void getweather(){
        Log.d("**************", "getweather");


        String TAG = "loacation";
        final int LOCATION_INTERVAL = 1000;
        final float LOCATION_DISTANCE = 1;

        Log.e(TAG, "onCreate");
        mLocationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.PASSIVE_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, locationListener);


        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }

        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, locationListener);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    locationListener);


        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }




    }


    void Set_data(){
        calender_adapter.AddItem("04/05","체험학습/1, " +
                "교육여행/2");
        calender_adapter.AddItem("04/06","체험학습/1, " +
                "교육여행/2");
        calender_adapter.AddItem("04/07","체험학습/1, " +
                "교육여행/2, " +
                "학급활동/3");
        calender_adapter.AddItem("04/12","학력평가/3");
        calender_adapter.AddItem("04/13","과학의달행사, " +
                "태릉 휴센터 1기 개강식");
        calender_adapter.AddItem("04/14","동아리활동");
        calender_adapter.AddItem("04/18","교직원연수");
        calender_adapter.AddItem("04/19","1학기 행복캠프조직");
        calender_adapter.AddItem("04/21","장애이해교육");
        calender_adapter.AddItem("04/27","중간고사");
        calender_adapter.AddItem("04/28","중간고사");
    }



    public class Data{
        String date;
        String data;

        Data(String date, String data){
            this.date = date;
            this.data = data;
        }
    }


    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.d("provider", location.getProvider());
            final double lat = location.getLatitude();
            final double lon = location.getLongitude();
            Log.d("lat",lat+"");
            Log.d("lon",lon+"");

            mLocationManager.removeUpdates(locationListener);

            Get_weather get_weather = new Get_weather(lat , lon);
            get_weather.execute();

//            final double tc, tmax, tmin;

//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//
//                }
//            }).start();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


    class Get_weather extends AsyncTask<String, String,String>{
//        TextView place_name_T, sky_name_T, tmax_T, tmin_T, tc_T;
        String place_name, sky_name ,sky_code , date;
        double tc,tmax,tmin;
        double lat, lon;


        Get_weather(double lat, double lon){
            this.lat = lat;
            this.lon = lon;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("**************","weather async");

//            place_name_T = (TextView)getActivity().findViewById(R.id.place_name);
//            sky_name_T = (TextView)getActivity().findViewById(R.id.sky_name);
//            tmax_T = (TextView)getActivity().findViewById(R.id.tmax);
//            tmin_T = (TextView)getActivity().findViewById(R.id.tmin);
//            tc_T = (TextView)getActivity().findViewById(R.id.tc);
        }

        @Override
        protected String doInBackground(String... params) {

            String buffer_ = "";

            try{
                URL url = new URL("http://apis.skplanetx.com/weather/current/minutely?version=1&lat="+lat+"&lon="+lon+"&appKey=e4e52f4f-2448-3ce2-b660-f9285c089d85");
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoInput(true);

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                buffer_ = bufferedReader.readLine();


            }catch (Exception e){
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject(buffer_);

                JSONObject jsonObject1 = (JSONObject) jsonObject.get("weather");

                JSONArray jsonArray = (JSONArray) jsonObject1.getJSONArray("minutely");

                jsonObject = (JSONObject) jsonArray.get(0);

                JSONObject station_object = (JSONObject) jsonObject.get("station");
                JSONObject sky_object = (JSONObject) jsonObject.get("sky");
                JSONObject temperature_object = (JSONObject) jsonObject.get("temperature");

                place_name = station_object.getString("name");
                sky_name = sky_object.getString("name");
                sky_code = sky_object.getString("code");
                tc = temperature_object.getDouble("tc");
                tmax = temperature_object.getDouble("tmax");
                tmin = temperature_object.getDouble("tmin");

                date = new SimpleDateFormat("업데이트 MM/dd aa hh:mm").format(new Date());
                Log.d("date", date);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("place_name", place_name);
                editor.putString("sky_name", sky_name);
                editor.putString("tc", (int)tc+"°");
                editor.putString("tmax", (int)tmax+"°");
                editor.putString("tmin", (int)tmin+"°");
                editor.putString("date", date);
                editor.putString("code", sky_code);
                editor.commit();

                System.out.println(place_name+":");
                System.out.println(sky_name +":");
                System.out.println(tc+":");
                System.out.println(tmax+":");
                System.out.println(tmin+":");
                System.out.println(date+":");
                System.out.println(sky_code+":");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            imageButton.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            place_name_T.setText(place_name+"");
            sky_name_T.setText(sky_name);
            tc_T.setText((int)tc+"°");
            tmax_T.setText((int)tmax+"°");
            tmin_T.setText((int)tmin+"°");
            date_T.setText(date);

            if(sky_code.equals("SKY_A01")){
                weather_image.setImageResource(R.drawable.weather_1);
            }else if(sky_code.equals("SKY_A02")){
                weather_image.setImageResource(R.drawable.weather_2);
            }else if(sky_code.equals("SKY_A03") || sky_code.equals("SKY_A07")){
                weather_image.setImageResource(R.drawable.weather_3);
            }else if(sky_code.equals("SKY_A04") || sky_code.equals("SKY_A08") || sky_code.equals("SKY_A06") || sky_code.equals("SKY_A10")){
                weather_image.setImageResource(R.drawable.weather_4);
            }else if(sky_code.equals("SKY_A05") || sky_code.equals("SKY_A09")){
                weather_image.setImageResource(R.drawable.weather_5);
            }else if(sky_code.equals("SKY_A11")){
                weather_image.setImageResource(R.drawable.weather_11);
            }else{
                weather_image.setImageResource(R.drawable.weather_11);
            }


        }
    }

    class Calender_adapter extends BaseAdapter{
        Context context;
        ArrayList<Data> calender_list;
        int day = Integer.parseInt(new java.text.SimpleDateFormat("dd").format(new java.util.Date()));

        Calender_adapter(Context context){
            this.context = context;
            calender_list = new ArrayList<>();

        }

        public void AddItem(String date, String data){
            Data data_C = new Data(date, data);
            calender_list.add(data_C);
        }

        public void DeleteItem(){
            calender_list.clear();
        }

        @Override
        public int getCount() {
            return calender_list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.home_calendar_samll_item, parent, false);
            }

            TextView date_T = (TextView)convertView.findViewById(date);
            TextView data_T = (TextView)convertView.findViewById(R.id.data);

            date_T.setText(calender_list.get(position).date + " |");
            data_T.setText(calender_list.get(position).data);

            //46BCE8

            if((calender_list.get(position).date.split("/")[1]).equals(day+"")){
                Log.d("day", calender_list.get(position).date.split("/")[1]+"");
                Log.d("today",day+"");
                ((RelativeLayout)convertView.findViewById(R.id.relative)).setBackgroundColor(Color.parseColor("#46BCE8"));
                ((TextView)convertView.findViewById(R.id.today)).setVisibility(View.VISIBLE);

            }else{
                ((RelativeLayout)convertView.findViewById(R.id.relative)).setBackgroundColor(Color.parseColor("#ffffff"));
                ((TextView)convertView.findViewById(R.id.today)).setVisibility(View.INVISIBLE);
            }


            return convertView;
        }
    }

    class Get_schedule extends AsyncTask<String, String, String>{

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

                calender_adapter.DeleteItem();
                while((buffer = bufferedReader.readLine()) != null){
//                    Log.d("abc", buffer);
                    String[] result = buffer.split("@@!@!@");

                    calender_adapter.AddItem(mon +"/"+result[0],result[1]);

                    data += "\""+mon +"/"+result[0]+"\":\""+result[1]+"\",";
                    i++;
                }
                data = data.substring(0, data.length()-1);
                data += "}";


                SharedPreferences sharedPreferences = getContext().getSharedPreferences("schedule", Context.MODE_PRIVATE);
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
            calender_adapter.notifyDataSetChanged();
            int day = Integer.parseInt(new java.text.SimpleDateFormat("dd").format(new java.util.Date()));
            calender_listview.setSelection(day-1);
        }
    }

    class Get_meal extends AsyncTask<String, String, String>{
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


            ((TextView)getActivity().findViewById(R.id.meal_text)).setText(real_data);


            int year = Integer.parseInt(new java.text.SimpleDateFormat("yyyy").format(new java.util.Date()));
            int month = Integer.parseInt(new java.text.SimpleDateFormat("MM").format(new java.util.Date()));
            int day = Integer.parseInt(new SimpleDateFormat("dd").format(new Date()));

            SharedPreferences sharedPreferences = getContext().getSharedPreferences("meal", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Log.d("date",year+"/"+month+"/"+day);
            editor.putString("date", year+"/"+month+"/"+day);
            editor.putString("data",real_data);
            editor.commit();
        }
    }

    class database extends SQLiteOpenHelper{

        public database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
