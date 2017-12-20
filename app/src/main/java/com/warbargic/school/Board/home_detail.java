package com.warbargic.school.Board;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.warbargic.school.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by kippe_000 on 2017-05-14.
 */

public class home_detail extends Activity {
    String content, title, name, date, id, down, down_name;
    String id_;
    TextView title_T, name_T, date_T, content_T, down_name1_T, down_name2_T;
    Button down_btn1, down_btn2;
    LinearLayout linear1, linear2;
    String[] down_L;
    private DownloadManager downloadManager;
    private DownloadManager.Request request;
    private Uri urlToDownload;
    private long latestId = -1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_detail);
        Intent intent = getIntent();
        id_ = intent.getStringExtra("id");
        String title = intent.getStringExtra("title");
        title_T = (TextView)findViewById(R.id.main_title);
        name_T = (TextView)findViewById(R.id.name);
        date_T = (TextView)findViewById(R.id.date);
        down_btn1 = (Button)findViewById(R.id.down_btn1);
        down_btn2 = (Button)findViewById(R.id.down_btn2);
        down_name1_T = (TextView)findViewById(R.id.down_name1);
        down_name2_T = (TextView)findViewById(R.id.down_name2);
        linear1 = (LinearLayout)findViewById(R.id.linear1);
        linear2 = (LinearLayout)findViewById(R.id.linear2);
        down_L = new String[2];

        content_T = (TextView)findViewById(R.id.content);
        downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);


        ((TextView)findViewById(R.id.title)).setText(title);
        ((ImageButton)findViewById(R.id.back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        down_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urlToDownload = Uri.parse(down_L[0]);
                List<String> pathSegments = urlToDownload.getPathSegments();
                request = new DownloadManager.Request(urlToDownload);
                request.setTitle(down_name1_T.getText().toString()+"");
                request.setDescription("항목 설명");
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, down_name1_T.getText().toString());
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdirs();
                latestId = downloadManager.enqueue(request);
//                startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));


//                Get_file get_file = new Get_file(down_L[0]);
//                get_file.execute();
            }
        });

        down_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                urlToDownload = Uri.parse(down_L[1]);
                List<String> pathSegments = urlToDownload.getPathSegments();
                request = new DownloadManager.Request(urlToDownload);
                request.setTitle(down_name2_T.getText().toString()+"");
                request.setDescription("항목 설명");
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, down_name2_T.getText().toString());
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdirs();
                latestId = downloadManager.enqueue(request);
//                startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));

            }
        });


        Get_home_detail get_home_detail = new Get_home_detail();
        get_home_detail.execute();

    }
    @Override
    public void onResume(){
        super.onResume();
        IntentFilter completeFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(completeReceiver, completeFilter);
    }

    private BroadcastReceiver completeReceiver = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, "다운로드가 완료되었습니다.",Toast.LENGTH_SHORT).show();
        }

    };

    class Get_home_detail extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {


            URL url = null;
            HttpURLConnection httpURLConnection = null;
            String param = "id="+id_;
            Log.d("param", param);

            try{
                url = new URL("http://tlsdndql27.vps.phps.kr/taereung_school/get_home_detail.php");
                httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(param.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String buffer = "";

                while((buffer = bufferedReader.readLine()) != null){
                    Log.d("buffer", buffer+":");

                    String json = buffer;
                    JSONObject jsonObject = new JSONObject(json);

                    id = jsonObject.getString("id");
                    title = jsonObject.getString("title");
                    name = jsonObject.getString("name");
                    content = jsonObject.getString("content");
                    date = jsonObject.getString("date");
                    down = jsonObject.getString("down");
                    down_name = jsonObject.getString("down_name");





                }



            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("id", id + ":");
            Log.d("title", title + ":");
            Log.d("name", name + ":");
            Log.d("content", content + "");
            Log.d("date", date + ":");
            Log.d("down", down + "");
            Log.d("down_name", down_name + "");

            title_T.setText(title);
            name_T.setText(name);
            date_T.setText(date);
            content_T.setText(content + "");

            String[] down_names = null;
            String[] down_link = null;
            if(down_name != null) {
                down_names = down_name.split("!@!@!@!!@");
                down_link = down.split("!@!@!@!!@");

                if (down_names.length == 1) {
                    linear2.setVisibility(View.GONE);
                    down_name1_T.setText(down_names[0]);
                    down_L[0] = down_link[0];
                } else if (down_names.length == 0) {
                    linear1.setVisibility(View.GONE);
                    linear2.setVisibility(View.GONE);
                } else {
                    down_name1_T.setText(down_names[0]);
                    down_name2_T.setText(down_names[1]);
                    down_L[0] = down_link[0];
                    down_L[1] = down_link[1];
                }
            }


        }
    }

    class Get_file extends AsyncTask<String, String, String>{
        String link;

        Get_file(String link){
            this.link = link;
        }

        @Override
        protected String doInBackground(String... params) {

            URL url = null;
            HttpURLConnection httpURLConnection = null;
            Log.d("link",link+"");
            try{
                url = new URL(link);
                httpURLConnection = (HttpURLConnection) url.openConnection();


            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }
    }
}
