package com.warbargic.school;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.warbargic.school.Board.Board_main;
import com.warbargic.school.Home.Home_main;
import com.warbargic.school.Survey.Survey_main;
import com.warbargic.school.left_infor.BaseWebView;
import com.warbargic.school.left_infor.history;
import com.warbargic.school.left_infor.principal;
import com.warbargic.school.left_infor.symbol;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    DrawerLayout drawerLayout;
    ArrayList<Data> datalist;
    ListView listView;
    Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("check","create");


        adapter = new Adapter(getApplicationContext());
        listView = (ListView)findViewById(R.id.left_listview);
        final ArrayList<Fragment> fragment_list = new ArrayList<>();
        datalist = new ArrayList<>();
        drawerLayout = (DrawerLayout)findViewById(R.id.dl_activity_main_drawer);
        fragment_list.add(new Home_main());
        fragment_list.add(new Board_main());
        fragment_list.add(new Survey_main());


        tabLayout = (TabLayout)findViewById(R.id.sliding_tabs);
        viewPager = (ViewPager)findViewById(R.id.viewpager);

        viewPager.setAdapter(new FragmentPagerAdapter(this.getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragment_list.get(position);
            }

            @Override
            public int getCount() {
                return fragment_list.size();
            }
        });


//        verifyStoragePermissions(this);
//        verifyStoragePermissions2(this);
        Log.d("permission", ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)+":");
        if (-1 == ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) || -1 == ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }


        tabLayout.setTabTextColors(ColorStateList.valueOf(Color.BLACK));

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.home_icon);
        tabLayout.getTabAt(1).setIcon(R.drawable.board_icon_off);
        tabLayout.getTabAt(2).setIcon(R.drawable.survey_icon_off);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position){
                    case 0:
                        tabLayout.getTabAt(0).setIcon(R.drawable.home_icon);
                        tabLayout.getTabAt(1).setIcon(R.drawable.board_icon_off);
                        tabLayout.getTabAt(2).setIcon(R.drawable.survey_icon_off);
                        break;
                    case 1:
                        tabLayout.getTabAt(0).setIcon(R.drawable.home_icon_off);
                        tabLayout.getTabAt(1).setIcon(R.drawable.board_icon);
                        tabLayout.getTabAt(2).setIcon(R.drawable.survey_icon_off);
                        break;
                    case 2:
                        tabLayout.getTabAt(0).setIcon(R.drawable.home_icon_off);
                        tabLayout.getTabAt(1).setIcon(R.drawable.board_icon_off);
                        tabLayout.getTabAt(2).setIcon(R.drawable.survey_icon);
                        break;

                }




            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Set_left_data();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (datalist.get(position).text){
                    case "학교장 인사말":
                        intent = new Intent(MainActivity.this, principal.class);
                        startActivity(intent);
                        break;
                    case "학교 상징":
                        intent = new Intent(MainActivity.this, symbol.class);
                        startActivity(intent);
                        break;
                    case "학교 연혁":
                        intent = new Intent(MainActivity.this, history.class);
                        startActivity(intent);
                        break;
                    case "학교 현황":

                        break;
                    case "About...":
                        break;
                    case "태릉고등학교 앱 1:1 문의":
                        intent = new Intent(MainActivity.this, BaseWebView.class);
                        intent.putExtra("title",datalist.get(position).text);
                        intent.putExtra("url", "https://open.kakao.com/o/sSzhvTv");
                        startActivity(intent);
                        break;
                }

            }
        });
    }

    void Set_left_data(){

        Data data;

        data = new Data(R.drawable.principal, "학교장 인사말");
        datalist.add(data);

        data = new Data(R.drawable.symbol, "학교 상징");
        datalist.add(data);

        data = new Data(R.drawable.history, "학교 연혁");
        datalist.add(data);

        data = new Data(R.drawable.now, "학교 현황 (개발중)");
        datalist.add(data);

        data = new Data(R.drawable.teacher, "교직원 소개 (개발중)");
        datalist.add(data);

        data = new Data(0,"");
        datalist.add(data);

        data = new Data(R.drawable.infor1,"About...");
        datalist.add(data);

        data = new Data(R.drawable.inquiry1,"태릉고등학교 앱 1:1 문의");
        datalist.add(data);

        data = new Data(0,"");
        datalist.add(data);


        listView.setAdapter(adapter);
    }

    class Data{
        int image;
        String text;

        Data(int image, String text){
            this.image = image;
            this.text = text;
        }
    }

//    void getweather(){
//        LocationManager mLocationManager = null;
//        String TAG = "loacation";
//        final int LOCATION_INTERVAL = 1000;
//        final float LOCATION_DISTANCE = 1;
//
//        Log.e(TAG, "onCreate");
//        if (mLocationManager == null) {
//            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
//        }
//        try {
//            mLocationManager.requestLocationUpdates(
//                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
//                    locationListener);
//        } catch (java.lang.SecurityException ex) {
//            Log.i(TAG, "fail to request location update, ignore", ex);
//        } catch (IllegalArgumentException ex) {
//            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
//        }
//        try {
//            mLocationManager.requestLocationUpdates(
//                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
//                    locationListener);
//        } catch (java.lang.SecurityException ex) {
//            Log.i(TAG, "fail to request location update, ignore", ex);
//        } catch (IllegalArgumentException ex) {
//            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
//        }
//    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();

        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        actionBar.setDisplayShowHomeEnabled(false);            //홈 아이콘을 숨김처리합니다.


        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.actionbar, null);



        actionBar.setCustomView(actionbar);

        Toolbar parent = (Toolbar)actionbar.getParent();
        parent.setContentInsetsAbsolute(0,0);


        return true;
    }

    boolean drawer = false;
    public void drawerbtn(View view){
        if(drawer){
            drawer = false;
            drawerLayout.closeDrawer(Gravity.LEFT);
        }else {
            drawer = true;
            drawerLayout.openDrawer(Gravity.LEFT);
        }
    }



    class Adapter extends BaseAdapter{
        Context context;

        Adapter(Context context){
            this.context = context;
        }

        @Override
        public int getCount() {
            return datalist.size();
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

            if(datalist.get(position).image != 0) {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.left_listview_item, parent, false);
            }else{
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.left_listview_item2, parent, false);
                return convertView;
            }

            ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView);
            TextView textView = (TextView)convertView.findViewById(R.id.textView);

            imageView.setImageResource(datalist.get(position).image);
            textView.setText(datalist.get(position).text);

            return convertView;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("check","start");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("check","resume");

    }

    //    LocationListener locationListener = new LocationListener() {
//        @Override
//        public void onLocationChanged(Location location) {
//            final double lat = location.getLatitude();
//            final double lon = location.getLongitude();
//            Log.d("lat",lat+"");
//            Log.d("lon",lon+"");
//
//
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//
//                    try{
//                        URL url = new URL("http://apis.skplanetx.com/weather/current/minutely?version=1&lat="+lat+"&lon="+lon+"&appKey=e4e52f4f-2448-3ce2-b660-f9285c089d85");
//                        HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
//                        httpURLConnection.setRequestMethod("GET");
//                        httpURLConnection.setDoInput(true);
//
//                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
//                        String buffer = "";
//                        while ((buffer = bufferedReader.readLine()) != null) {
//                            Log.d("buffer", buffer);
//                        }
//
//
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//
//        }
//    };

}
