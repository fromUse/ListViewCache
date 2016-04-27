package com.demo.chen_gui.test;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ImageView;

public class WebActivity extends AppCompatActivity {

    private WebView webView = null;
    private ImageView img = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_web);
        webView = (WebView) findViewById (R.id.web);
        img = (ImageView) findViewById (R.id.img);
        new WebThread (img,"http://www.0739i.com.cn/data/attachment/portal/201603/09/120158ksjocrjsoohrmhtg.jpg",new Handler ()).start ();
    }

}
