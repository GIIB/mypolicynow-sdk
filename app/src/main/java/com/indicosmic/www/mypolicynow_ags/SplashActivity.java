package com.indicosmic.www.mypolicynow_ags;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.indicosmic.www.mypolicynow_ags.QuotationActivity;
import com.indicosmic.www.mypolicynow_ags.utils.RuntimePermissions;
import com.indicosmic.www.mypolicynow_ags.utils.SharedPrefManager;
import com.indicosmic.www.mypolicynow_ags.utils.SingletonClass;
import com.indicosmic.www.mypolicynow_ags.utils.UtilitySharedPreferences;

import java.util.ArrayList;



public class SplashActivity extends RuntimePermissions {

    private static int SPLASH_TIME_OUT = 2000;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 24;
    Thread splashTread;
    private static final String TAG = "SplashActivity";
    String StrPosToken,StrClientMobile,Force_Update_flag="0";
    int YourApkVersionCode;
    String StrIsActiveAccount,  DeviceId;
    TelephonyManager mTelephonyManager;
    private int    timeoutMillis       = 5000;
    ImageView iv_logo;
    /** The time when this {@link AppCompatActivity} was created. */

    private long  startTimeMillis     = 0;
    private static final int REQUEST_PERMISSIONS = 20;
    SharedPrefManager sharedPrefManager;
    ArrayList<String> questionlist;
    /** The code used when requesting permissions */

    private static final int    PERMISSIONS_REQUEST = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        init();

    }

    private void init() {

        startTimeMillis = System.currentTimeMillis();
        iv_logo = (ImageView)findViewById(R.id.iv_logo);



        SingletonClass.initinstance();
        questionlist = new ArrayList<String>();
        sharedPrefManager = new SharedPrefManager(this);

        if (Build.VERSION.SDK_INT >= 21) {
            SplashActivity.super.requestAppPermissions(new String[]{Manifest.permission.INTERNET, Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION},
                    R.string.runtime_permissions_txt, REQUEST_PERMISSIONS);
        } else {
            redirect();

        }


    }


    @Override
    public void onPermissionsGranted(int requestCode) {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(getResources().getColor(R.color.grey));


        redirect();

    }





    /*@TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST) {
            checkPermissions();
        }
    }



    private void checkPermissions() {
        String[] ungrantedPermissions = requiredPermissionsStillNeeded();
        if (ungrantedPermissions.length == 0) {
            //force_update();

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(ungrantedPermissions, PERMISSIONS_REQUEST);
            }
        }
    }

    @TargetApi(23)
    private String[] requiredPermissionsStillNeeded() {

        Set<String> permissions = new HashSet<String>();
        for (String permission : getRequiredPermissions()) {
            permissions.add(permission);
        }
        for (Iterator<String> i = permissions.iterator(); i.hasNext();) {
            String permission = i.next();
            if (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                Log.d(SplashActivity.class.getSimpleName(),
                        "Permission: " + permission + " already granted.");
                i.remove();
            } else {
                Log.d(SplashActivity.class.getSimpleName(),
                        "Permission: " + permission + " not yet granted.");
            }
        }
        return permissions.toArray(new String[permissions.size()]);
    }

    public String[] getRequiredPermissions() {
        String[] permissions = null;
        try {
            permissions = getPackageManager().getPackageInfo(getPackageName(),
                    PackageManager.GET_PERMISSIONS).requestedPermissions;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (permissions == null) {
            return new String[0];
        } else {
            return permissions.clone();
        }
    }
*/






    private void redirect() {
        StrPosToken = UtilitySharedPreferences.getPrefs(getApplicationContext(),"POS_TOKEN");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

               /* if (StrPosToken!=null && !StrPosToken.equalsIgnoreCase("")){

                    Intent i = new Intent(getApplicationContext(), QuotationActivity.class);
                    i.putExtra("pos_token", StrPosToken);
                    startActivity(i);
                    finish();
                }else {*/
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.animator.move_left,R.animator.move_right);
                    finish();
                //}

            }
        }, SPLASH_TIME_OUT);
    }






    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent a = new Intent(Intent.ACTION_MAIN);
                        a.addCategory(Intent.CATEGORY_HOME);
                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(a);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
