package com.indicosmic.www.mypolicynow_ags.application;

import android.app.Application;

import com.loyalty.loyaltyapp.loyalty_main.LoyaltyApplication;
import com.uatongo.utils.ConnectivityReceiver;

/**
 * Created by Ravi Tamada on 15/06/16.
 * www.androidhive.info
 */
public class MyApplication extends LoyaltyApplication {

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
