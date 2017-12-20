package com.warbargic.school.Survey;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.warbargic.school.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by kippe_000 on 2017-04-19.
 */

public class Survey_main extends Fragment {
    ListView listView;
    Adapter adapter;
    ArrayList<Data> datalist;
    ArrayList<Bitmap> bitmaps;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.survey, container, false);

        listView = (ListView)view.findViewById(R.id.listview);
        adapter = new Adapter(getContext());
        datalist = new ArrayList<>();
        bitmaps = new ArrayList<>();


//        Set_data();
//        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent intent = new Intent(getContext(), survey_webview.class);
                intent.putExtra("url",datalist.get(position).url);
                intent.putExtra("title", datalist.get(position).title);
//                Toast.makeText(getContext(), datalist.get(position).url+"이동", Toast.LENGTH_SHORT).show();

                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Get_itme get_itme = new Get_itme();
        get_itme.execute();
    }


//    void Set_data(){
//        Bitmap bitmap;
//        String title;
//        String explain;
//        String url;
//
//        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.umbrella);
//        title = "우산대여소";
//        explain = "우산대여소 어떻게 생각하세요?";
//        url = "https://docs.google.com/forms/d/1b9hgVuehk8i48IizZoa5z3hYqHcGgCoZH0aRb0ypBII/edit?chromeless=1";
//        adapter.Add_Item(bitmap, title, explain, url);
//
//        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.violence);
//        title = "학교폭력 실태조사";
//        explain = "모바일로도 할 수 있어요!";
//        url = "http://eduro.sen.go.kr/stv_svs_sv01_000.do";
//        adapter.Add_Item(bitmap, title, explain, url);
//
//    }


    class Data{
        String picture;
        String title;
        String explain;
        String url;

        Data(String picture, String title, String explain, String url){
            this.picture = picture;
            this.title = title;
            this.explain = explain;
            this.url = url;
        }
    }



    class Get_itme extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;


        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
//            progressDialog = new ProgressDialog(getActivity());
//            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progressDialog.setCanceledOnTouchOutside(false);
//            progressDialog.setMessage("로딩중입니다..");
//            progressDialog.show();
//            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {


            URL url = null;
            HttpURLConnection httpURLConnection = null;

            datalist.clear();
            bitmaps.clear();


            try{

                url = new URL("http://tlsdndql27.vps.phps.kr/taereung_school/get_survey.php");
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");


                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String buffer = "";
                while((buffer = bufferedReader.readLine()) != null){
                    Log.d("buffer", buffer);
                    String[] bufs = buffer.split("@@!@!@");
                    for(int i = 0; i < bufs.length; i++){
                        System.out.println(bufs[i]);
                    }

                    // bufs[0] = 제목  1 = 설명, 2 = 사진, 3 = 주소
                    adapter.Add_Item(bufs[2],bufs[0],bufs[1],bufs[3]);

                }




            }catch (Exception e){
                e.printStackTrace();
            }




            try{

                Log.d("size", datalist.size()+"");
                for(int i = 0; i < datalist.size(); i++) {


                    url = new URL("http://tlsdndql27.vps.phps.kr/taereung_school/survey_item/"+datalist.get(i).picture);
                    httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoInput(true);


                    InputStream inputStream = httpURLConnection.getInputStream();
                    Bitmap bitmap = null;
                    bitmap = BitmapFactory.decodeStream(inputStream);


                    bitmaps.add(bitmap);
                    Log.d("Add bitmap", i+"번");


                }

            }catch (Exception e){
                e.printStackTrace();
            }



            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);

//            progressDialog.dismiss();
        }
    }




    class Adapter extends BaseAdapter{
        Context context;

        Adapter(Context context){
            this.context = context;
        }

        public void Add_Item(String picture, String title, String explain, String url){
            Data data = new Data(picture, title, explain, url);

            datalist.add(data);
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
                convertView = layoutInflater.inflate(R.layout.survey_listview_item, parent, false);
            }

            ImageView imageView = (ImageView)convertView.findViewById(R.id.imageview);
            TextView title = (TextView)convertView.findViewById(R.id.title);
            TextView explain = (TextView)convertView.findViewById(R.id.explain);

            imageView.setImageBitmap(bitmaps.get(position));
            title.setText(datalist.get(position).title);
            explain.setText(datalist.get(position).explain);


            return convertView;
        }
    }
}
