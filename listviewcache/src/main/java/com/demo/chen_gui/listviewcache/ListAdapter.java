package com.demo.chen_gui.listviewcache;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by chen-gui on 16-4-26.
 */
public class ListAdapter extends BaseAdapter implements AbsListView.OnScrollListener{

    private static final String TAG = "ListAdapter";
    private List<NewsBean> datas = null;
    private LayoutInflater inflater = null;
    private Context context  = null;
    //可见选项的起始位置、可见最后位置
    private int mStart,mEnd;
    //数据源的所以图片url
    public static String [] URLS = null;
    private ListView mListView = null;
    private Set<ImageLoad>  imageLoadManager  = null;
    private boolean loadTag = true;

    public ListAdapter(Context context,List<NewsBean> datas,ListView listView){
        this.context = context;
        this.datas = datas;
        this.mListView = listView;
        mListView.setOnScrollListener (this);
        imageLoadManager = new HashSet<ImageLoad> ();

        URLS = new String[datas.size ()];
        for (int i = 0; i <datas.size () ; i++) {
            URLS[i] = datas.get (i).imgurl;
        }


    }

    @Override
    public int getCount() {
        return datas.size ();
    }

    @Override
    public Object getItem(int position) {
        return datas.get (position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHold hold = null;
        if (convertView==null){
            hold = new ViewHold ();
            convertView  =  inflater.from (context).inflate (R.layout.list_item,null);
            hold.img = (ImageView) convertView.findViewById (R.id.img);
            hold.title = (TextView) convertView.findViewById (R.id.title);
            hold.content = (TextView) convertView.findViewById (R.id.content);

            convertView.setTag (hold);
        }else{
            hold = (ViewHold) convertView.getTag ();
        }


        hold.title.setText (datas.get (position).title);
        hold.content.setText (datas.get (position).content);
        //设置默认图标
        hold.img.setImageResource (R.mipmap.ic_launcher);
        //设置url标志
        hold.img.setTag (datas.get (position).imgurl);
        return convertView;
    }

    //滑动状态改时调用
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        Log.i (TAG, "onScrollStateChanged: -----------滑动状态发生改变----------");
        if (scrollState == SCROLL_STATE_IDLE) {
            //加载可见项
            loadImage2 (mStart,mEnd);

        }else {
            //停止加载
            unLoad();
        }

    }



    //一直被调用
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //可见的第一项
        mStart = firstVisibleItem;
        //可见项的地第一项到可见最后一项
        mEnd = firstVisibleItem + visibleItemCount;

        if(loadTag && visibleItemCount >0){
            loadImage2 (mStart,mEnd);
            loadTag = false;
        }
    }


    /**
     *
     * 当滑动停止的时候才记载图片
     *
     * @param start
     * @param end
     */
    private void loadImage2(int start,int end){

        for (int i = start; i <end ; i++) {
            String url = ListAdapter.URLS[i];
            //通过url标记获取指定的可见项
            ImageView imgView = (ImageView) mListView.findViewWithTag (url);
            ImageLoad load= new ImageLoad (imgView);
            load.execute (url);
            Log.i (TAG, "loadImage2: ------加载可见项--------");
            imageLoadManager.add (load);
        }

    }

    /**
     *
     * 当正在滑时，就终止所有异步任务
     */
    private void unLoad() {
        if (imageLoadManager != null) {
        for (ImageLoad load : imageLoadManager){
            load.cancel (true);
        }
    }
    }

    private class ViewHold{
        public ImageView img;
        public TextView title;
        public TextView content;
    }

}
