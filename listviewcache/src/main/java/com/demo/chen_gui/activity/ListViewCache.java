package com.demo.chen_gui.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ListViewCache extends AppCompatActivity {

    private static final String TAG = "ListViewCache";
    private ListView listView = null;
    private List<NewsBean> datas = null;

    private String mURL = "http://www.imooc.com/api/teacher?type=4&num=30";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.listview_cache);

        init ();
        iniData ();
    }

    private void init() {
        listView = (ListView) findViewById (R.id.listview);
    }

    private void iniData() {

        datas = new ArrayList<NewsBean> ();

        //下载json数据
        new JsomData ().execute (mURL);
    }

    /**
     * 异步下载json，并解析json，
     * 映射json都NewsBean
     */
    private class JsomData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            return getJson (params[0]);
        }

        @Override
        protected void onPostExecute(String data) {
            StrToJson (data);
        }


        /**
         * 下载json数据
         *
         * @param url
         *
         * @return
         */
        private String getJson(String url) {

            StringBuffer stb = null;
            InputStream input = null;

            try {
                input = new URL (url).openStream ();
                BufferedReader bf = new BufferedReader (new InputStreamReader (input, "utf-8"));
                stb = new StringBuffer ();
                if (bf != null) {
                    String str = null;
                    while ((str = bf.readLine ()) != null) {
                        stb.append (str);
                    }
                }
            } catch (IOException e1) {
                e1.printStackTrace ();
            }
            return stb.toString ();
        }


        /**
         * 将json字符串解析成json数据
         * 并将json对象映射到NewsBean对象
         *
         * @param data
         */
        private void StrToJson(String data) {

            try {
                JSONObject jsonobj = new JSONObject (data);
                JSONArray jsonarr = jsonobj.getJSONArray ("data");

                for (int i = 0; i < jsonarr.length (); i++) {
                    JSONObject json = jsonarr.getJSONObject (i);
                    NewsBean item = new NewsBean ();
                    item.imgurl = json.getString ("picSmall");
                    item.title = json.getString ("name");
                    item.content = json.getString ("description");
                    datas.add (item);
                }
                Log.i (TAG, "StrToJson: -----------------------" + datas.size () + "-------------------------");
            } catch (JSONException e) {
                e.printStackTrace ();
            }

            /**
             *
             * 因为多线程的冰法执行，所以
             * 当子线程把json映射完成后再设置adapter
             *
             */
            if (datas.size () > 0) {
                listView.setAdapter (new ListAdapter (ListViewCache.this, datas,listView));
                Toast.makeText (ListViewCache.this, "json加载完成", Toast.LENGTH_LONG).show ();
            }
        }
    }


}

