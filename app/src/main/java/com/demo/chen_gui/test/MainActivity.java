package com.demo.chen_gui.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.roger.catloadinglibrary.CatLoadingView;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";
    private ImageView img = null;
    private boolean flag = true;
    private int count = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.test);
        init();
        data();
        listner();
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

                switch (event.getAction ()){
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

    public void click(View view){

        final CatLoadingView catLoadingView = new CatLoadingView ();

        catLoadingView.show (getSupportFragmentManager (),"");

        Thread thread = new Thread (){
            @Override
            public void run() {

                if (!siDismss()){
                    catLoadingView.dismiss ();
                }

               }
            };
        thread.start ();
    }

    private boolean siDismss() {

        for (int i = 0; i < count; i++) {
            Log.i (TAG, "siDismss: "+i);
        }

        return  true;
    }


}