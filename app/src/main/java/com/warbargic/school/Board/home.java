package com.warbargic.school.Board;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.warbargic.school.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by kippe_000 on 2017-05-13.
 */

public class home extends Activity {
    ArrayList<Data> datalist;
    Adapter adapter;
    ListView listView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_board);

        datalist = new ArrayList<>();
        adapter = new Adapter(getApplicationContext());
        listView = (ListView)findViewById(R.id.listview);

        Get_home get_home = new Get_home();
        get_home.execute();

        ((ImageButton)findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String id_ = datalist.get(position).id;
                String title_ = datalist.get(position).title;

                Intent intent = new Intent(home.this, home_detail.class);
                intent.putExtra("id", id_);
                intent.putExtra("title", title_);
                startActivity(intent);
            }
        });
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

    class Adapter extends BaseAdapter {
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
                convertView = layoutInflater.inflate(R.layout.board_item,parent,false);
            }

            TextView num_T, title_T, name_T, date_T;
            num_T = (TextView) convertView.findViewById(R.id.num);
            title_T = (TextView)convertView.findViewById(R.id.title);
            name_T = (TextView)convertView.findViewById(R.id.name);
            date_T = (TextView)convertView.findViewById(R.id.date);


            num_T.setText(datalist.get(position).num+"");
            title_T.setText(datalist.get(position).title+"");
            name_T.setText(datalist.get(position).name+"");
            date_T.setText(datalist.get(position).date+"");


            return convertView;
        }
    }


    class Get_home extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            datalist.clear();


            URL url = null;
            HttpURLConnection httpURLConnection = null;

            try{
                url = new URL("http://tlsdndql27.vps.phps.kr/taereung_school/get_home_many.php");
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

            Log.d("size", datalist.size()+"");
            System.out.println("----------------");
            listView.setAdapter(adapter);


        }
    }
}
