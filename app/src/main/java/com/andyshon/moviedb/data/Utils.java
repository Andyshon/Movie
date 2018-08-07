package com.andyshon.moviedb.data;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by andyshon on 07.08.18.
 */

public class Utils {

    private static Utils mInstance;
    private Context mContext;

    public Utils(Context context) {
        mContext = context;
    }

    public Context getmContext() {
        return mContext;
    }

    public static Utils getInstance(final Context context) {
        if (mInstance == null) {
            synchronized (Utils.class) {
                if (mInstance == null) {
                    mInstance = new Utils(context);
                }
            }
        }
        return mInstance;
    }

    public boolean isConnectedToInternet() {
        ConnectivityManager cm = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
