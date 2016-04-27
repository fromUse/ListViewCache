package com.demo.utlis;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by chen-gui on 16-4-26.
 */
public class ImageLoad extends AsyncTask<String, Void, Bitmap> {

    private static final String TAG = "ImageLoad";
    private ImageView img = null;

    private final static int  MAX_MEMORY_CACHE = (int) (Runtime.getRuntime ().maxMemory () / 4);

    public static LruCache<String, Bitmap> imaCache = new LruCache<String, Bitmap> (MAX_MEMORY_CACHE){
        @Override
        protected int sizeOf(String key, Bitmap value) {

            //默认是返回个数
            //修改返回bitmap的大小
            //每次传入都会调用
            return value.getByteCount ();
        }
    };

    public ImageLoad(ImageView img) {
        this.img = img;
    }

    /**
     * 此方式是在子线程中执行
     * 即可以做一些耗时操作
     *
     * @param params
     *
     * @return
     */
    @Override
    protected Bitmap doInBackground(String... params) {
        return asyncImgDownlod (params[0]);

    }

    /**
     * 此方法是再主线程中过执行
     * 可以写有关更新UI对的操作
     *
     * @param bitmap
     */
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            img.setImageBitmap (bitmap);
        }
    }

    /**
     * 下载图片并将图片解析成bitmap
     * 返回给调用者
     *
     * @param url
     *
     * @return
     */
    private Bitmap asyncImgDownlod(String url) {
        Bitmap bitmap = null;
        String tag = (String) img.getTag ();

        //判断img的tag和当前选项的url是否相同
        //只有相同的时候才是显示正确，然后再加载

        if (tag != null) {

            if (tag.equals (url)) {
                //当从缓存成功获取到bitmap就直接返回
                //当bitmap为空时才网络加载
                bitmap = getBitMapCache (url);
                if (bitmap != null) {

                    Log.i (TAG, "asyncImgDownlod: ------缓存获取"+url+"-----");
                    return bitmap;
                } else {

                    try {
                        URL Url = new URL (url);
                        InputStream inputStream = Url.openStream ();
                        bitmap = BitmapFactory.decodeStream (inputStream);
                        setBitMapCache (url,bitmap);

                        return bitmap;
                    } catch (MalformedURLException e) {
                        e.printStackTrace ();
                    } catch (IOException e) {
                        e.printStackTrace ();
                    }
                }

            }
        }

        return null;
    }

    /**
     * 获取缓存图片
     * @param url
     * @return
     */
    private Bitmap getBitMapCache(String url) {

        return imaCache.get (url);
    }

    /**
     * 将bitmap保存到缓存中
     * @param url
     * @param bitmap
     * @throws AndroidRuntimeException
     */
    private void setBitMapCache(String url, Bitmap bitmap) throws AndroidRuntimeException{

        //当缓存中不存在url对应的图片时，才去网络加载
        if (getBitMapCache (url) == null) {
            //将图片添加到缓存
            imaCache.put (url,bitmap);
            Log.i (TAG, "setBitMapCache: "+url+"缓存");
        }else {
            throw  new ArithmeticException ("----bitmap为空-----");

        }

    }


}
