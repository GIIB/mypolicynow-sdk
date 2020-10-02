package com.indicosmic.www.mypolicynow_ags.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.indicosmic.www.mypolicynow_ags.R;
import com.indicosmic.www.mypolicynow_ags.utils.CommonMethods;
import com.indicosmic.www.mypolicynow_ags.utils.UtilitySharedPreferences;
import com.indicosmic.www.mypolicynow_ags.utils.ConnectionDetector;
import com.indicosmic.www.mypolicynow_ags.webservices.RestClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.indicosmic.www.mypolicynow_ags.utils.CommonMethods.md5;
import static com.indicosmic.www.mypolicynow_ags.webservices.RestClient.ROOT_URL2;
import static com.indicosmic.www.mypolicynow_ags.webservices.RestClient.api_password;
import static com.indicosmic.www.mypolicynow_ags.webservices.RestClient.api_user_name;
import static com.indicosmic.www.mypolicynow_ags.webservices.RestClient.x_api_key;

public class MainActivity_1 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    String terminal_id="",merchant_id="",StrUserId,StrEmail,StrUniqueId,POS_TOKEN="";
    TextView TV_WelcomeTxt,TV_TodayDate;
    ProgressDialog myDialog;
    private static final String TAG = "MainActivity_1";
    LinearLayout LayoutCar,LayoutBike,LayoutCommercial;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null){
            if(bundle.getString("terminal_id") != null) {
                terminal_id = bundle.getString("terminal_id");
            }else {
                terminal_id = UtilitySharedPreferences.getPrefs(getApplicationContext(),"TerminalId");
            }

            if(bundle.getString("merchant_id") != null) {
                merchant_id = bundle.getString("merchant_id");
            }else {
                merchant_id = UtilitySharedPreferences.getPrefs(getApplicationContext(),"MerchantId");
            }

        }else {
            terminal_id = UtilitySharedPreferences.getPrefs(getApplicationContext(),"TerminalId");
            merchant_id = UtilitySharedPreferences.getPrefs(getApplicationContext(),"MerchantId");

        }

        init();
        navigationView();

    }

    private void init() {

        myDialog = new ProgressDialog(MainActivity_1.this);
        myDialog.setMessage("Please wait...");
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);
        //UAT MERCHANT / TERMINAL ID
        //terminal_id="PX918512";
        //merchant_id = "PX9400000000012";

        ///LIVE MERCHANT / TERMINAL ID
        terminal_id="RA131914";
        merchant_id = "110000000125922";
        Log.d("terminal_id",""+terminal_id);
        Log.d("merchant_id",""+merchant_id);

        if (terminal_id != null && merchant_id != null && !terminal_id.equalsIgnoreCase("") && !merchant_id.equalsIgnoreCase("")) {
            getPasswordForMPN();
        }else {
            CommonMethods.DisplayToastInfo(this,"Invalid Access. Please contact system administrator");
        }

        LayoutCar = (LinearLayout)findViewById(R.id.LayoutCar);
        LayoutBike = (LinearLayout)findViewById(R.id.LayoutBike);
        LayoutCommercial = (LinearLayout)findViewById(R.id.LayoutCommercial);


        LayoutCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilitySharedPreferences.setPrefs(getApplicationContext(),"QuotationFor","Car");
                if(POS_TOKEN!=null && !POS_TOKEN.equalsIgnoreCase("")){
                    Intent i = new Intent(getApplicationContext(), QuotationActivity_2.class);
                    i.putExtra("pos_token", POS_TOKEN);
                    startActivity(i);
                    overridePendingTransition(R.animator.move_left,R.animator.move_right);
                    finish();
                }else {
                    CommonMethods.DisplayToastInfo(getApplicationContext(),"Invalid Token");
                }
            }
        });

        LayoutBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilitySharedPreferences.setPrefs(getApplicationContext(),"QuotationFor","Bike");
                if(POS_TOKEN!=null && !POS_TOKEN.equalsIgnoreCase("")){
                    Intent i = new Intent(getApplicationContext(), QuotationActivity_2.class);
                    i.putExtra("pos_token", POS_TOKEN);
                    startActivity(i);
                    overridePendingTransition(R.animator.move_left,R.animator.move_right);
                    finish();
                }else {
                    CommonMethods.DisplayToastInfo(getApplicationContext(),"Invalid Token");
                }
            }
        });


        LayoutCommercial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UtilitySharedPreferences.setPrefs(getApplicationContext(),"QuotationFor","Commercial");
                if(POS_TOKEN!=null && !POS_TOKEN.equalsIgnoreCase("")){
                    Intent i = new Intent(getApplicationContext(), QuotationActivity_2.class);
                    i.putExtra("pos_token", POS_TOKEN);
                    startActivity(i);
                    overridePendingTransition(R.animator.move_left,R.animator.move_right);
                    finish();
                }else {
                    CommonMethods.DisplayToastInfo(getApplicationContext(),"Invalid Token");
                }
            }
        });


    }

    private void getPasswordForMPN() {

        String URL = RestClient.ROOT_URL2+"getpassword";
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
                        UtilitySharedPreferences.clearPref(getApplicationContext());
                        POS_TOKEN  = data_obj.getString("token");
                        UtilitySharedPreferences.setPrefs(getApplicationContext(),"MerchantId",merchant_id);
                        UtilitySharedPreferences.setPrefs(getApplicationContext(),"TerminalId",terminal_id);
                        getPreveledgesFromToken();

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

                    map.put("merchant_id", merchant_id);
                    map.put("terminal_id", terminal_id);
                    map.put("access_key", md5(terminal_id));

                    Log.d("GetPasswordToken",""+map);
                    return map;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    //  Authorization: Basic $auth
                    HashMap<String, String> headers = new HashMap<String, String>();

                    headers.put("x-api-key",x_api_key);
                    headers.put("Authorization", "Basic "+CommonMethods.Base64_Encode(api_user_name + ":" + api_password));

                    Log.d("Headers",""+headers);
                    return headers;
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

    private void getPreveledgesFromToken() {

            String URL = ROOT_URL2+"tokenverify";
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
                            JSONObject agent_detailObj = data_obj.getJSONObject("agent_detail");
                            String mer_id = data_obj.getString("merchant_id");
                            String terminal_id = data_obj.getString("terminal_id");
                            String StrAgentId  = agent_detailObj.getString("id");
                            String StrAgentName = agent_detailObj.getString("username");
                            String StrAgentMObile = agent_detailObj.getString("mobile_no");
                            String StrAgentEmail = agent_detailObj.getString("email");

                            UtilitySharedPreferences.setPrefs(getApplicationContext(),"PosId",StrAgentId);
                            UtilitySharedPreferences.setPrefs(getApplicationContext(),"PosName",StrAgentName);
                            UtilitySharedPreferences.setPrefs(getApplicationContext(),"PosMobile",StrAgentMObile);
                            UtilitySharedPreferences.setPrefs(getApplicationContext(),"PosEmail",StrAgentEmail);
                            UtilitySharedPreferences.setPrefs(getApplicationContext(),"MerchantId",mer_id);
                            UtilitySharedPreferences.setPrefs(getApplicationContext(),"TerminalId",terminal_id);


                            navigationView();



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
                        map.put("token_number", POS_TOKEN);
                        Log.d("Token_verify",""+map);
                        return map;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        //  Authorization: Basic $auth
                        HashMap<String, String> headers = new HashMap<String, String>();
                        //headers.put("Content-Type", "application/x-www-form-urlencoded");
                        //headers.put("Content-Type", "application/json; charset=utf-8");
                        headers.put("x-api-key",x_api_key);
                        headers.put("Authorization", "Basic "+CommonMethods.Base64_Encode(api_user_name + ":" + api_password));
                        return headers;
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

    private void navigationView() {

        String StrMerchantId = UtilitySharedPreferences.getPrefs(getApplicationContext(),"MerchantId");
        String StrTerminalId = UtilitySharedPreferences.getPrefs(getApplicationContext(),"TerminalId");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        //TextView nav_header_userId = navigationView.findViewById(R.id.nav_header_userId);
        View hView =  navigationView.getHeaderView(0);
        ImageView iv_facebook = (ImageView)navigationView.findViewById(R.id.iv_facebook);
        ImageView iv_google_plus = (ImageView)navigationView.findViewById(R.id.iv_google_plus);
        ImageView iv_twitter = (ImageView)navigationView.findViewById(R.id.iv_twitter);
        ImageView iv_youtube = (ImageView)navigationView.findViewById(R.id.iv_youtube);
        ImageView iv_linked_in = (ImageView)navigationView.findViewById(R.id.iv_linked_in);


       /* Glide.with(this)
                .load("https://www.mypolicynow.com/assets/images/logo_spinner.gif")
                .placeholder(R.drawable.logo_spinner)
                .into(iv_logo);
*/


        iv_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(getResources().getString(R.string.facebook_page)));
                startActivity(i);
            }
        });

        iv_google_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(getResources().getString(R.string.google_plus_page)));
                startActivity(i);
            }
        });

        iv_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(getResources().getString(R.string.twitter_page)));
                startActivity(i);
            }
        });

        iv_youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(getResources().getString(R.string.youtube_page)));
                startActivity(i);
            }
        });

        iv_linked_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(getResources().getString(R.string.linked_in_page)));
                startActivity(i);
            }
        });



        TextView nav_header_userName = (TextView)hView.findViewById(R.id.nav_header_userName);
        TextView nav_user_email = (TextView)hView.findViewById(R.id.nav_Email);
        if(StrMerchantId!=null && !StrMerchantId.equalsIgnoreCase("")) {
            nav_header_userName.setText("Merchant Id: " + StrMerchantId);
        }

        if(StrTerminalId!=null && !StrTerminalId.equalsIgnoreCase("")) {
            nav_user_email.setText("Terminal Id: "+StrTerminalId);
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_about_us) {
            Intent i = new Intent(getApplicationContext(), AboutUsActivity.class);
            startActivity(i);
            overridePendingTransition(R.animator.move_left, R.animator.move_right);
            finish();
        } else if(id == R.id.nav_contact_us){
            Intent i = new Intent(getApplicationContext(), ContactUsActivity.class);
            startActivity(i);
            overridePendingTransition(R.animator.move_left, R.animator.move_right);
            finish();

        } else if(id == R.id.nav_saved_proposals){

            if(POS_TOKEN!=null && !POS_TOKEN.equalsIgnoreCase("")){
                Intent i = new Intent(getApplicationContext(), SavedProposalActivity.class);
                startActivity(i);
                overridePendingTransition(R.animator.move_left, R.animator.move_right);
                finish();
            }else {
                CommonMethods.DisplayToastInfo(getApplicationContext(),"Invalid Token");
            }

        } else if(id == R.id.nav_sold_policies){
            if(POS_TOKEN!=null && !POS_TOKEN.equalsIgnoreCase("")){
                Intent i = new Intent(getApplicationContext(), SoldPoliciesActivity.class);
                startActivity(i);
                overridePendingTransition(R.animator.move_left, R.animator.move_right);
                finish();
            }else {
                CommonMethods.DisplayToastInfo(getApplicationContext(),"Invalid Token");
            }

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
