package com.indicosmic.www.mypolicynow_ags;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.indicosmic.www.mypolicynow_sdk.QuotationActivity;
import com.indicosmic.www.mypolicynow_sdk.utils.CommonMethods;
import com.indicosmic.www.mypolicynow_sdk.utils.ConnectionDetector;
import com.indicosmic.www.mypolicynow_sdk.utils.UtilitySharedPreferences;
import com.indicosmic.www.mypolicynow_sdk.webservices.RestClient;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.indicosmic.www.mypolicynow_ags.RestClient.ROOT_URL2;
import static com.indicosmic.www.mypolicynow_sdk.utils.CommonMethods.md5;

public class MainActivity extends AppCompatActivity {

    String StrMobile,StrEmail;
    String POS_TOKEN="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPasswordForMPN();

    }

    private void getPasswordForMPN() {
        final ProgressDialog myDialog = new ProgressDialog(MainActivity.this);
        myDialog.setMessage("Please wait...");
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

         StrMobile = "8169972611";
         StrEmail = "sush.rokade@gmail.com";

        String URL = ROOT_URL2+"GetPassword";
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        boolean isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            Log.d("URL", "" + URL);
            StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if (myDialog != null && myDialog.isShowing()) {
                        myDialog.dismiss();
                    }
                    try {

                        Log.d("Response", "" + response);
                        JSONObject jsonresponse = new JSONObject(response);

                        JSONObject data_obj = jsonresponse.getJSONObject("data");

                        POS_TOKEN  = data_obj.getString("token");

                        TextView BtnBuyInsurance = (TextView)findViewById(R.id.BtnBuyInsurance);
                        BtnBuyInsurance.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                UtilitySharedPreferences.setPrefs(getApplicationContext(),"QuotationFor","Bike");
                                if(POS_TOKEN!=null && !POS_TOKEN.equalsIgnoreCase("")){
                                    Intent i = new Intent(getApplicationContext(), QuotationActivity.class);
                                    i.putExtra("pos_token", POS_TOKEN);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if (myDialog != null && myDialog.isShowing()) {
                        myDialog.dismiss();
                    }
                    CommonMethods.DisplayToastInfo(getApplicationContext(),"Something went wrong. Please try again later.");

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("mobile_number", StrMobile);
                    map.put("email_id", StrEmail);
                    if(StrMobile!=null && !StrMobile.equalsIgnoreCase("")){
                        map.put("access_key", md5(StrMobile));
                    }else if(StrEmail!=null && !StrEmail.equalsIgnoreCase("")){
                        map.put("access_key", md5(StrEmail));
                    }
                    Log.d("GetPasswordToken",""+map);
                    return map;
                }
            };


            int socketTimeout = 50000; //30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            request.setRetryPolicy(policy);
            // RequestQueue requestQueue = Volley.newRequestQueue(this, new HurlStack(null, getSocketFactory()));
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);
        }else {
            CommonMethods.DisplayToast(getApplicationContext(),"Please check Internet Connection");
        }




    }
}
