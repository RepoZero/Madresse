package ir.madresse;

import android.app.Application;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;

import ir.madresse.SQLiteDB.Connection;
import ir.madresse.Services.Events;
import ir.madresse.utils.LruBitmapCache;



public class App extends Application {

    public static final String TAG = App.class.getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static App mInstance;

    public static String SERVER_ADDRESS = "http://d.madresse.ir/";


    private Connection MyDataBase;
    static public SQLiteDatabase DB;

    public static Typeface FONT_iran_sana_light;



    @Override
    public void onCreate() {
        super.onCreate();




        CookieHandler.setDefault(new CookieManager());





        MyDataBase = new Connection(this);
        DB = MyDataBase.getWritableDatabase();


        FONT_iran_sana_light = Typeface.createFromAsset(getAssets(), "fonts/iran_sans_light.ttf");

        startService(new Intent(this,   Events.class));

        mInstance = this;

    }


    public static synchronized App getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue, new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }


    }
}
