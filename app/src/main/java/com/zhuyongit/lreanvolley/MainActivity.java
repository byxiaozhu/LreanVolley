package com.zhuyongit.lreanvolley;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Volley 网络请求框架 , 适用于频繁请求网络 ， 异步处理请求 ， 缓存 ， 多级别请求
 *
 *
 */
public class MainActivity extends ActionBarActivity {


    private static final String TAG = "MainActivity" ;

    private TextView tv01 ;
    private ImageView imageView ;
    private NetworkImageView imageView01 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化组件
        initView();


        // 获取Json数据
        getJsonData();
        // 获取网络图片
        getImageView();
        // NetWorkImageView组件获取网络图片
        getNetWorkImageView();
    }

    /**
     * 初始化组件
     */
    private void initView() {

        tv01 = (TextView)findViewById(R.id.tv01) ;
        imageView = (ImageView) findViewById(R.id.imageView) ;
        imageView01 = (NetworkImageView) findViewById(R.id.imageView01) ;
    }


    /**
     * 使用Volley进行网络请求
     */
    public void getJsonData() {
        // 创建一个请求队列
        RequestQueue _requestQueue = Volley.newRequestQueue(this) ;
        // 请求URL
        String _jsonUrl = "http://it-ebooks-api.info/v1/search/php%20mysql" ;
        // 创建一个JsonObject请求
        JsonObjectRequest _jsonObjectQuest = new JsonObjectRequest(Request.Method.GET, _jsonUrl, null, new Response.Listener<JSONObject>() {

            /**
             * 请求成功执行此方法
             * @param response
             */
            @Override
            public void onResponse(JSONObject response) {

                Log.i(TAG, response.toString()) ;

                tv01.setText(response.toString());

            }
        }, new Response.ErrorListener() {

            /**
             * 请求错误执行此方法
              * @param error
             */
            @Override
            public void onErrorResponse(VolleyError error) {
                    Log.e(TAG,error.toString()) ;
            }
        }) ;
        // 将请求对象添加到请求队列中 ， 完成请求
        _requestQueue.add(_jsonObjectQuest) ;
    }

    /**
     * 通过Volley获取网络图片
     */
    public void getImageView()
    {
        RequestQueue _requestQueue = Volley.newRequestQueue(this) ;
        String imageUrl = "http://s.it-ebooks-api.info/3/head_first_php__mysql.jpg" ;
        // 创建一个lru算法缓存对象
        final LruCache<String,Bitmap> _lruCache = new LruCache<String,Bitmap>(20) ;
        // 创建一个Image缓存对象
        ImageLoader.ImageCache _imageCache = new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String key) {

                return  _lruCache.get(key) ;
            }

            @Override
            public void putBitmap(String key, Bitmap bitmap) {
                _lruCache.put(key,bitmap) ;
            }
        } ;
        // 创建ImageLoader对象 ， 加载请求和缓存对象
        ImageLoader _imageLoader = new ImageLoader(_requestQueue,_imageCache) ;
        // 监听ImageView对象
        ImageLoader.ImageListener _listener = _imageLoader.getImageListener(imageView,R.mipmap.ic_launcher,R.mipmap.ic_launcher) ;
        // 加载ImageView
        _imageLoader.get(imageUrl, _listener) ;
    }

    /**
     * 通过Volley NetworkImageView组件获取图像显示
     */
    public void getNetWorkImageView() {
        RequestQueue _requestQueue = Volley.newRequestQueue(this) ;
        String _imageUrl = "http://s.it-ebooks-api.info/3/learning_php_mysql_and_javascript.jpg" ;
        final LruCache<String,Bitmap> _lruCache = new LruCache<>(20) ;
        ImageLoader.ImageCache _imageCache = new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String s) {
                return _lruCache.get(s);
            }

            @Override
            public void putBitmap(String s, Bitmap bitmap) {
                    _lruCache.put(s, bitmap) ;
            }
        } ;

        ImageLoader _imageLoader = new ImageLoader(_requestQueue,_imageCache) ;
        imageView01.setTag("url");
        imageView01.setImageUrl(_imageUrl,_imageLoader);
    }
}
