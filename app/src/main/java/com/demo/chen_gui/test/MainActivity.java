package com.demo.chen_gui.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.demo.chen_gui.activity.ListViewCache;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";
    private ImageView img = null;
    private boolean flag = true;
    private int count = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.test);
        init ();
        data ();
        listner ();
    }

    private void init() {
        img = (ImageView) findViewById (R.id.img);
    }

    private void data() {

    }

    private void listner() {
        img.setOnTouchListener (new View.OnTouchListener () {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction ()) {
                    case MotionEvent.ACTION_DOWN:

                        break;
                    case MotionEvent.ACTION_MOVE:
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) img.getLayoutParams ();
                        Log.i (TAG, "onTouch: X" + event.getX ());
                        Log.i (TAG, "onTouch: Y" + event.getY ());
                        img.setLayoutParams (layoutParams);
                        break;
                    case MotionEvent.ACTION_UP:
                        break;

                }


                return true;

            }
        });
    }

    public void open_listViewCache(View view) {

       Intent it = new Intent (MainActivity.this, ListViewCache.class);

        startActivity (it);

    }

    public void click(View view) {

        Log.i (TAG, "click: -----------------------------");

    }
}