package com.warbargic.school.Board;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.warbargic.school.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.warbargic.school.R.id.num;


public class Board_main extends Fragment{
    ArrayList<Data> datalist;
    Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.board, container, false);



//        ListView listview = (ListView)view.findViewById(R.id.listView);
        datalist = new ArrayList<>();
        adapter = new Adapter(getContext());


        ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        // wifi 또는 모바일 네트워크 어느 하나라도 연결이 되어있다면,
        if (wifi.isConnected() || mobile.isConnected()) {
            Log.i("연결됨" , "연결이 되었습니다.");
            Get_announcement get_announcement = new Get_announcement();
            get_announcement.execute();
        } else {
            Log.i("연결 안 됨" , "연결이 다시 한번 확인해주세요");
        }



        ((Button)view.findViewById(R.id.btn1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), announcement.class);
                startActivity(intent);
            }
        });


        ((Button)view.findViewById(R.id.btn2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), home.class);
                startActivity(intent);
            }
        });

        ((LinearLayout)view.findViewById(R.id.linear1)).setOnClickListener(listener);
        ((LinearLayout)view.findViewById(R.id.linear2)).setOnClickListener(listener);
        ((LinearLayout)view.findViewById(R.id.linear3)).setOnClickListener(listener);
        ((LinearLayout)view.findViewById(R.id.linear4)).setOnClickListener(listener);
        ((LinearLayout)view.findViewById(R.id.linear5)).setOnClickListener(listener);
        ((LinearLayout)view.findViewById(R.id.linear6)).setOnClickListener(listener);

        return view;
    }

    class Data{
        String num, title, name, date,id;

        Data(String num, String title, String name, String date, String id){
            this.num = num;
            this.title = title;
            this.name = name;
            this.date = date;
            this.id = id;
        }
    }


    View.OnClickListener listener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            int position = -1;
            switch (v.getId()){
                case R.id.linear1:
                    position = 0;
                    break;
                case R.id.linear2:
                    position=1;
                    break;
                case R.id.linear3:
                    position=2;
                    break;
                case R.id.linear4:
                    position=3;
                    break;
                case R.id.linear5:
                    position=4;
                    break;
                case R.id.linear6:
                    position=5;
                    break;
            }

            Intent intent = null;
            if(position < 3){
                intent = new Intent(getActivity(), announcement_detail.class);
            }else{
                intent = new Intent(getActivity(), home_detail.class);
            }

            intent.putExtra("id", datalist.get(position).id);
            intent.putExtra("title", datalist.get(position).title);
            startActivity(intent);
        }
    };


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
            if(convertView == null){
                LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.announcement,parent,false);
            }

            TextView num_T, title_T, name_T, date_T;
            num_T = (TextView) convertView.findViewById(num);
            title_T = (TextView)convertView.findViewById(R.id.title);
            name_T = (TextView)convertView.findViewById(R.id.name);
            date_T = (TextView)convertView.findViewById(R.id.date);


            return null;
        }
    }


    class Get_announcement extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {
            datalist.clear();


            URL url = null;
            HttpURLConnection httpURLConnection = null;

            try{
                url = new URL("http://tlsdndql27.vps.phps.kr/taereung_school/get_announcement.php");
                httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);


                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String buffer = "";
                String num, title, name, date, id;

                while((buffer = bufferedReader.readLine()) != null){
                    String json = buffer;
                    JSONObject jsonObject = new JSONObject(json);

                    num = jsonObject.getString("num");
                    title = jsonObject.getString("title");
                    name = jsonObject.getString("name");
                    date = jsonObject.getString("date");
                    id = jsonObject.getString("id");

                    String[] strings;
                    String[] strings2;



                    // 번호 자르기
//                    strings = num.split(" ");
//                    strings2 = strings[4].split("	");
//                    num = strings2[2];
//                    num = num.substring(0,3);
                    num = "공지";



                    // 이름 자르기
                    strings = name.split("	");
                    name = strings[10];
                    name = name.substring(0,4);


                    // 제목 자르기
                    strings = title.split("	");
                    title = strings[18];
//                    for (int i = 0; i < strings.length; i++){
//                        System.out.println(strings[i]+":");
//                    }

                    Log.d("num", num+":");
                    Log.d("title", title+":");
                    Log.d("name", name+":");
                    Log.d("date", date+":");
                    Log.d("id", id+":");



                    Data data = new Data(num, title, name, date, id);

                    datalist.add(data);
                }



            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            TextView num_E;
            TextView title_E;
            TextView name_E;
            TextView date_E;

            num_E = (TextView)getActivity().findViewById(R.id.num1);
            title_E = (TextView)getActivity().findViewById(R.id.title1);
            name_E = (TextView)getActivity().findViewById(R.id.name1);
            date_E = (TextView)getActivity().findViewById(R.id.date1);

            num_E.setText(datalist.get(0).num);
            title_E.setText(datalist.get(0).title);
            name_E.setText(datalist.get(0).name);
            date_E.setText(datalist.get(0).date);



            num_E = (TextView)getActivity().findViewById(R.id.num2);
            title_E = (TextView)getActivity().findViewById(R.id.title2);
            name_E = (TextView)getActivity().findViewById(R.id.name2);
            date_E = (TextView)getActivity().findViewById(R.id.date2);

            num_E.setText(datalist.get(1).num);
            title_E.setText(datalist.get(1).title);
            name_E.setText(datalist.get(1).name);
            date_E.setText(datalist.get(1).date);


            num_E = (TextView)getActivity().findViewById(R.id.num3);
            title_E = (TextView)getActivity().findViewById(R.id.title3);
            name_E = (TextView)getActivity().findViewById(R.id.name3);
            date_E = (TextView)getActivity().findViewById(R.id.date3);

            num_E.setText(datalist.get(2).num);
            title_E.setText(datalist.get(2).title);
            name_E.setText(datalist.get(2).name);
            date_E.setText(datalist.get(2).date);

            Get_home get_home = new Get_home();
            get_home.execute();

        }
    }



    class Get_home extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {


            URL url = null;
            HttpURLConnection httpURLConnection = null;

            try{
                url = new URL("http://tlsdndql27.vps.phps.kr/taereung_school/get_home.php");
                httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);


                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String buffer = "";
                String num, title, name, date, id;

                while((buffer = bufferedReader.readLine()) != null){
                    String json = buffer;
                    JSONObject jsonObject = new JSONObject(json);

                    num = jsonObject.getString("num");
                    title = jsonObject.getString("title");
                    name = jsonObject.getString("name");
                    date = jsonObject.getString("date");
                    id = jsonObject.getString("id");

                    String[] strings;
                    String[] strings2;



                    // 번호 자르기
                    strings = num.split(" ");
                    strings2 = strings[4].split("	");
                    num = strings2[2];
                    num = num.substring(0,3);



                    // 이름 자르기
                    strings = name.split("	");
                    name = strings[8];
                    name = name.substring(0,4);

                    Log.d("num", num+":");
                    Log.d("title", title+":");
                    Log.d("name", name+":");
                    Log.d("date", date+":");
                    Log.d("id", id+":");

                    Data data = new Data(num, title, name, date, id);

                    datalist.add(data);
                }



            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            TextView num_E;
            TextView title_E;
            TextView name_E;
            TextView date_E;

            num_E = (TextView)getActivity().findViewById(R.id.num4);
            title_E = (TextView)getActivity().findViewById(R.id.title4);
            name_E = (TextView)getActivity().findViewById(R.id.name4);
            date_E = (TextView)getActivity().findViewById(R.id.date4);

            num_E.setText(datalist.get(3).num);
            title_E.setText(datalist.get(3).title);
            name_E.setText(datalist.get(3).name);
            date_E.setText(datalist.get(3).date);



            num_E = (TextView)getActivity().findViewById(R.id.num5);
            title_E = (TextView)getActivity().findViewById(R.id.title5);
            name_E = (TextView)getActivity().findViewById(R.id.name5);
            date_E = (TextView)getActivity().findViewById(R.id.date5);

            num_E.setText(datalist.get(4).num);
            title_E.setText(datalist.get(4).title);
            name_E.setText(datalist.get(4).name);
            date_E.setText(datalist.get(4).date);


            num_E = (TextView)getActivity().findViewById(R.id.num6);
            title_E = (TextView)getActivity().findViewById(R.id.title6);
            name_E = (TextView)getActivity().findViewById(R.id.name6);
            date_E = (TextView)getActivity().findViewById(R.id.date6);

            num_E.setText(datalist.get(5).num);
            title_E.setText(datalist.get(5).title);
            name_E.setText(datalist.get(5).name);
            date_E.setText(datalist.get(5).date);

        }
    }
}
