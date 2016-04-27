package com.demo.chen_gui.test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by chen-gui on 16-4-26.
 */
public class WebThread extends  Thread {

    private WebView webView;
    private ImageView imageView;
    private String url;
    private Handler handler;
    private Bitmap bitmap;
    private StringBuffer sb = new StringBuffer ();
    private int flag = 0;
    private static final String TAG = "WebThread";
    public WebThread(WebView webView,String url,Handler handler){
        this.webView =webView;
        this.url =url;
        this.handler = handler;
        flag = 0;
    }

      public WebThread(ImageView imageView,String url,Handler handler){
        this.imageView =imageView;
        this.url =url;
        this.handler = handler;
          flag =1;
    }

    @Override
    public void run() {


        if (flag ==0){
                 Log.i (TAG, "run: -------------web_start------------------------");
        if (webView!=null){
            try {
                URL Url = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) Url.openConnection ();
                connection.setRequestMethod ("GET");
                connection.setReadTimeout (5000);

                String str ;

                if (connection != null) {
                    BufferedReader bf = new BufferedReader (new InputStreamReader (connection.getInputStream ()));
                    if (bf != null) {


                        while((str = bf.readLine ())!=null){
                            sb.append (str);
                        }

                        bf.close ();
                    }
                }

                //将数据加载到WebView上
                handler.post (new Runnable () {
                    @Override
                    public void run() {
                        Log.i (TAG, "run: "+sb.toString ());
                        webView.loadData (sb.toString (),"text/html;charset=utf-8",null);
                    }
                });


            } catch (MalformedURLException e) {
                e.printStackTrace ();
            } catch (IOException e) {
                e.printStackTrace ();
            }
        }
        Log.i (TAG, "run: ----------------web_end--------------------");
        }else if(flag ==1){


            if (imageView != null) {
                Log.i (TAG, "run: ----------------img_start--------------------");

                URL Url = null;
                try {
                    Url = new URL (url);
                    HttpURLConnection connection = (HttpURLConnection) Url.openConnection ();
                    connection.setReadTimeout (5000);
                    connection.setRequestMethod ("GET");
                    InputStream inputStream = connection.getInputStream ();

                    FileOutputStream fileout = null;
                    String file = url.substring (url.lastIndexOf ("/")+1,url.length ());

                    if (isMounted (Environment.MEDIA_MOUNTED)) {

                        //打开文件输出流
                        fileout = new FileOutputStream (new File (Environment.getExternalStorageDirectory (),file));
                      //  Log.i (TAG, "----------------mounted -------------"+Environment.getExternalStorageDirectory ().getPath ());
                    }


                    //将输流读取到自己数据
                    //再将字节数组写到文件中
                    byte []  data = new byte[1024];
                    int len;
                    if (fileout != null && inputStream!=null) {

                        while ((len = inputStream.read (data))!=-1){
                            fileout.write (data,0,len);
                            Log.i (TAG, "Write --------------->>> :  "+len+"byte");
                        }

                        fileout.flush ();
                        fileout.close ();
                        Log.i (TAG, "Write --------------->>> done");
                    }

                    //判断手机是否挂载内存卡
                    if (isMounted (Environment.MEDIA_MOUNTED)) {


                        //从手机内存卡中获取文件输入流
                        FileInputStream imagInput = new FileInputStream (new File (Environment.getExternalStorageDirectory (),file));
                        if (imagInput != null) {
                            //将输入流解析成bitmap
                            bitmap = BitmapFactory.decodeStream (imagInput);
                        }
                        imagInput.close ();
                    }

                    //将bitmap设置到imageView上
                    handler.post (new Runnable () {
                        @Override
                        public void run() {
                            if (bitmap != null) {
                               // Log.i (TAG, "bitmap --------------------------------------------------------------"+Environment.getExternalStorageState ());
                                imageView.setImageBitmap (bitmap);
                            }
                        }
                    });
                    inputStream.close ();
                } catch (MalformedURLException e) {
                    e.printStackTrace ();
                }catch (IOException e) {
                    e.printStackTrace ();
                }finally {

                }
            }
            Log.i (TAG, "run: ----------------img_end--------------------");

        }




    }

    private boolean isMounted(String type){

      if ( Environment.getExternalStorageState ().equals (Environment.MEDIA_MOUNTED))
          return true;

        return false;
    }
}
