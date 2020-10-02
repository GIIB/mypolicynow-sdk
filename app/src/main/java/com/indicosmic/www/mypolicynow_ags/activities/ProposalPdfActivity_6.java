package com.indicosmic.www.mypolicynow_ags.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.PermissionRequest;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.FileProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.indicosmic.www.mypolicynow_ags.R;
import com.indicosmic.www.mypolicynow_ags.breakin_app_files.InspectionCheckpoint;
import com.indicosmic.www.mypolicynow_ags.utils.CommonMethods;
import com.indicosmic.www.mypolicynow_ags.utils.ConnectionDetector;
import com.indicosmic.www.mypolicynow_ags.utils.UtilitySharedPreferences;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import com.interfaces.ClsPAXInf;
import com.interfaces.OnPaxPOSTransactionListerner;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import static com.indicosmic.www.mypolicynow_ags.webservices.RestClient.ROOT_URL2;
import static com.indicosmic.www.mypolicynow_ags.webservices.RestClient.api_password;
import static com.indicosmic.www.mypolicynow_ags.webservices.RestClient.api_user_name;
import static com.indicosmic.www.mypolicynow_ags.webservices.RestClient.x_api_key;


public class ProposalPdfActivity_6 extends AppCompatActivity implements OnPaxPOSTransactionListerner{

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static final int PICK_IMAGE_REQUEST = 2;
    private static final int CAPTURE_IMAGE_REQUEST = 231;

    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    ProgressDialog myDialog;
    private int STORAGE_PERMISSION_CODE = 23;
    private static final String TAG = "ProposalPDF";
    WebView webViewProposalPdf;
    SwipeRefreshLayout MySwipeRefreshLayout;
    ProgressBar progressBar;
    String StrAgentId="",StrMpnData,StrUserActionData,StrTotal_Swap,StrIc_premium,StrTotal_Rsa,StrIC_Code;
    WebViewClient webViewProposalPdfClient;
    boolean isVerifiedCustomerMobile = false;
    boolean isUploadedPolicyDocument = false;
    boolean is_policy_purchased = false;
    String IsBreakInCase="",IsBreakInInspectionDone="";
    String StrCustomerMobile = "",QuoteLink = "",PDF_URL="",quote_url="",proposal_no="",proposal_otp="",policy_no="",IsPrePolicyUploaded="",IsPaCoverUploaded="false";
    Button BtnShare,BtnEdit,BtnVerify,BtnUploadPACover,BtnBreakInInspection,BtnUploadPreviousPolicy,BtnBuyPolicy,BtnNewPolicy;
    Dialog DialogVerifyPopup,DialogUploadPolicy,DialogBreakInCase;
    String StrPolicyType="",product_type_id,StrIsPreviousPolicy="",breakin_status_id="";
    ImageView Iv_UploadedImg;
    Button btnPreviousPolicyUpload;
    Uri outputFileUri;
    boolean ServerStatus;
    String Str_Base64Image;
    String download_file_url;
    String dest_file_path = "";
    boolean CallApiOnResume = false;
    int downloadedSize = 0, totalsize;
    float per = 0;
    private static final int SELECT_FILE = 2243;
    private static final int REQUEST_CAMERA = 23424;
    TextView tv_loading;
    String have_motor_policy,have_pa_policy;
    Uri fileUri;
    File myFileToUpload = null;
    String PreviousPolicyFileName,POLICY_GENERATED_URL="";
    byte[] bbytesFile;
    String StrVehicleRegistrationNo="",Proposal_list_id="", merchant_id="",terminal_id="",total_premium_payable="1",StrAdditionalParameters="";
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    String file_base;
    String filePath,picturePath,b64,filename,file_extension;
    String Quote_Link="";
    EditText EdtChooseFile;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap mImageBitmap;
    private String mCurrentPhotoPath;
    File imageFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposal_page_pdf_view);

        init();

    }

    private void init() {

        ImageView back_btn_toolbar = (ImageView)findViewById(R.id.back_btn_toolbar);
        back_btn_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        TextView til_text = (TextView)findViewById(R.id.til_text);
        til_text.setText("GENERATED PROPOSAL");


        StrAgentId =   UtilitySharedPreferences.getPrefs(getApplicationContext(),"PosId");
        StrMpnData = UtilitySharedPreferences.getPrefs(getApplicationContext(),"MpnData");
        StrUserActionData = UtilitySharedPreferences.getPrefs(getApplicationContext(),"UserActionData");
        String ProposalObjStr =  UtilitySharedPreferences.getPrefs(getApplicationContext(),"ProposalAry");
        StrCustomerMobile = UtilitySharedPreferences.getPrefs(getApplicationContext(),"CustomerMobile");
        IsPrePolicyUploaded =  UtilitySharedPreferences.getPrefs(getApplicationContext(),"IsPrePolicyUploaded");
        webViewProposalPdf = findViewById(R.id.webViewProposalPdf);


        MySwipeRefreshLayout = findViewById(R.id.swiperefresh);
        MySwipeRefreshLayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // This method performs the actual data-refresh operation.
                // The method calls setRefreshing(false) when it's finished.
                LoadProposalPage();
            }
        });

        progressBar = findViewById(R.id.progressbar);

        BtnShare= (Button) findViewById(R.id.BtnShare);
        BtnEdit= (Button)  findViewById(R.id.BtnEdit);
        BtnVerify= (Button) findViewById(R.id.BtnVerify);
        BtnUploadPACover = (Button)findViewById(R.id.BtnUploadPACover);
        BtnUploadPreviousPolicy= (Button) findViewById(R.id.BtnUploadPreviousPolicy);
        BtnBreakInInspection= (Button) findViewById(R.id.BtnBreakInInspection);
        BtnBuyPolicy= (Button) findViewById(R.id.BtnBuyPolicy);
        BtnNewPolicy= (Button) findViewById(R.id.BtnNewPolicy);

        myDialog = new ProgressDialog(ProposalPdfActivity_6.this);
        myDialog.setMessage("Please wait...");
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);
        tv_loading = new TextView(ProposalPdfActivity_6.this);
        tv_loading.setGravity(Gravity.CENTER);
        tv_loading.setTypeface(null, Typeface.BOLD);
        try {

            setDataFromMpnData();

            JSONObject proposal_obj = new JSONObject(ProposalObjStr);

            Log.d("proposalobj",""+proposal_obj);
            proposal_no = proposal_obj.getString("proposal_no");
            proposal_otp = proposal_obj.getString("otp");
            policy_no = proposal_obj.getString("policy_no");
            QuoteLink = proposal_obj.getString("quote_link");
            quote_url = proposal_obj.getString("url");

            QuoteLink = QuoteLink.toUpperCase();
            API_GetProposalDetail();

            PDF_URL = ROOT_URL2+quote_url;
            Log.d("PDFURL",""+PDF_URL);
            download_file_url = PDF_URL;
            dest_file_path = QuoteLink.toUpperCase()+".pdf";
            UtilitySharedPreferences.setPrefs(getApplicationContext(),"QuoteLink",QuoteLink);
            UtilitySharedPreferences.setPrefs(getApplicationContext(),"proposal_list_id",proposal_no);
            //Toast.makeText(getApplicationContext(),"Customer Verification OTP :"+proposal_otp,Toast.LENGTH_LONG).show();


        } catch (JSONException e) {
            e.printStackTrace();
        }




        BtnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isVerifiedCustomerMobile){
                    verifyCustomerPopup();
                }
            }
        });

        BtnBreakInInspection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBreakInPopUp(false);
            }
        });

        BtnUploadPreviousPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadDocumentPopup(false,"previous_policy_doc");
            }
        });

        BtnUploadPACover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadDocumentPopup(false,"pa_cover");
            }
        });

        BtnBuyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BUY_POLICY();
            }
        });

        BtnNewPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //UtilitySharedPreferences.clearPref(getApplicationContext());
                Intent intent = new Intent(getApplicationContext(), MainActivity_1.class);
                intent.putExtra("terminal_id", UtilitySharedPreferences.getPrefs(getApplicationContext(),"TerminalId"));
                intent.putExtra("merchant_id",UtilitySharedPreferences.getPrefs(getApplicationContext(),"MerchantId"));
                startActivity(intent);
                overridePendingTransition(R.animator.left_right,R.animator.right_left);
                finish();
            }
        });


        BtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        final PopupMenu menu = new PopupMenu(this, BtnShare);
        menu.getMenu().add("QUOTE FORWARD");
        menu.getMenu().add("DOWNLOAD PDF");
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                            public boolean onMenuItemClick(MenuItem item) {
                                                // insert your code here
                                                Log.d("menu title: ", item.getTitle().toString());
                                                if(item.getTitle().toString().equalsIgnoreCase("QUOTE FORWARD")){
                                                    API_Quote_Forward();
                                                }else if(item.getTitle().toString().equalsIgnoreCase("DOWNLOAD PDF")){

                                                    downloadAndOpenPDF();



                                                }
                                                return true;
                                            }
                                        }
        );


        BtnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menu.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        String StrCallApiONResume = UtilitySharedPreferences.getPrefs(getApplicationContext(),"StrCallApiONResume");
        if(StrCallApiONResume!=null && !StrCallApiONResume.equalsIgnoreCase("")){
            if(StrCallApiONResume.equalsIgnoreCase("true")){
                CallApiOnResume = true;
            }else {
                CallApiOnResume = false;
            }
        }else{
            CallApiOnResume = false;
        }
        if(CallApiOnResume) {
            API_GetProposalDetail();
        }

    }

    private void setDataFromMpnData() {
        try {
            StrMpnData = UtilitySharedPreferences.getPrefs(getApplicationContext(), "MpnData");
            StrUserActionData = UtilitySharedPreferences.getPrefs(getApplicationContext(), "UserActionData");
            JSONObject user_action_data = null;
            if (StrUserActionData != null && !StrUserActionData.equalsIgnoreCase("")) {
                user_action_data = new JSONObject(StrUserActionData);
            }


            if (user_action_data != null) {
                have_motor_policy = user_action_data.getString("have_motor_policy");
                have_pa_policy = user_action_data.getString("have_pa_policy");
                StrPolicyType = user_action_data.getString("policy_type");
                StrIsPreviousPolicy = user_action_data.getString("is_previous_policy");
            }

            if (StrMpnData != null && !StrMpnData.equalsIgnoreCase("")) {

                JSONObject mpn_data = new JSONObject(StrMpnData);
                //product_type_id = mpn_data.getString("product_type_id");
                //IsBreakInCase = mpn_data.getString("is_breakin");
            }






        }catch (Exception e){
            e.printStackTrace();
        }

        }

    @SuppressLint("SetJavaScriptEnabled")
    private void LoadProposalPage() {
        WebSettings ws = webViewProposalPdf.getSettings();
        ws.setSupportZoom(true);
        ws.setJavaScriptEnabled(true);
        ws.setAllowFileAccess(true);

        String google_pdf_url = "https://docs.google.com/gview?embedded=true&url="+PDF_URL;
        webViewProposalPdf.loadUrl(google_pdf_url);

        //webViewProposalPdf.loadUrl("http://uat.mypolicynow.com/proposal/b1f9e248e2373439343430");

        try {
            Log.d(TAG, "Enabling HTML5-Features");
            Method m1 = WebSettings.class.getMethod("setDomStorageEnabled", Boolean.TYPE);
            m1.invoke(ws, Boolean.TRUE);

            Method m2 = WebSettings.class.getMethod("setDatabaseEnabled", Boolean.TYPE);
            m2.invoke(ws, Boolean.TRUE);

            Method m3 = WebSettings.class.getMethod("setDatabasePath", String.class);
            m3.invoke(ws, "/data/data/" + getPackageName() + "/databases/");

            Method m4 = WebSettings.class.getMethod("setAppCacheMaxSize", Long.TYPE);
            m4.invoke(ws, 1024*1024*8);

            Method m5 = WebSettings.class.getMethod("setAppCachePath", String.class);
            m5.invoke(ws, "/data/data/" + getPackageName() + "/cache/");

            Method m6 = WebSettings.class.getMethod("setAppCacheEnabled", Boolean.TYPE);
            m6.invoke(ws, Boolean.TRUE);

            Log.d(TAG, "Enabled HTML5-Features");
        }
        catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            Log.e(TAG, "Reflection fail", e);
        }

        webViewProposalPdf.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(google_pdf_url);
                //view.loadUrl("http://uat.mypolicynow.com/proposal/b1f9e248e2373439343430");

                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
                if(MySwipeRefreshLayout.isRefreshing()){
                    MySwipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                LoadProposalPage();
            }
        });



    }

    private void CheckAlreadyVerified(boolean send_mesg) {
        myDialog.show();
        String URL = ROOT_URL2+"checkalreadyverify";
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

                        boolean status = jsonresponse.getBoolean("status");

                        if(status){
                            CommonMethods.DisplayToastSuccess(getApplicationContext(),"Customer Already Verified");
                            isVerifiedCustomerMobile = true;

                            if(DialogVerifyPopup!=null && DialogVerifyPopup.isShowing()){
                                DialogVerifyPopup.dismiss();
                            }
                        }else {
                            CommonMethods.DisplayToastInfo(getApplicationContext(),"Please verify first using the link sent on registered Mobile no.");
                            if(send_mesg){

                                ResendVerifyLink();
                                //downloadAndOpenPDF();
                            }
                        }
                        LoadProposalPage();
                        DisplayBtnsAsPerFlowConditions();

                    } catch (JSONException e) {
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
                    map.put("quote_link", QuoteLink.toUpperCase());
                    map.put("agent_id", StrAgentId);

                    Log.d("Params",""+map);
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

    private void DisplayBtnsAsPerFlowConditions() {

        if(UtilitySharedPreferences.getPrefs(getApplicationContext(),"IsPrePolicyUploaded")!=null){
            if(!UtilitySharedPreferences.getPrefs(getApplicationContext(),"IsPrePolicyUploaded").equalsIgnoreCase("")){
                IsPrePolicyUploaded = UtilitySharedPreferences.getPrefs(getApplicationContext(),"IsPrePolicyUploaded");
            }
        }else {
            IsPrePolicyUploaded = "false";
        }
        //

        /*if(UtilitySharedPreferences.getPrefs(getApplicationContext(),"isBreakIn")!=null){
            if(!UtilitySharedPreferences.getPrefs(getApplicationContext(),"isBreakIn").equalsIgnoreCase("")){
                 = UtilitySharedPreferences.getPrefs(getApplicationContext(),"isBreakIn");
            }
        }*/

        if(UtilitySharedPreferences.getPrefs(getApplicationContext(),"IsBreakInInspectionDone")!=null){
            if(!UtilitySharedPreferences.getPrefs(getApplicationContext(),"IsBreakInInspectionDone").equalsIgnoreCase("")){
                IsBreakInInspectionDone = UtilitySharedPreferences.getPrefs(getApplicationContext(),"IsBreakInInspectionDone");
            }
        }else {
            IsBreakInInspectionDone ="false";
        }

        if(is_policy_purchased){
            BtnShare.setVisibility(View.GONE);
            BtnEdit.setVisibility(View.GONE);
            BtnVerify.setVisibility(View.GONE);
            BtnUploadPACover.setVisibility(View.GONE);
            BtnUploadPreviousPolicy.setVisibility(View.GONE);
            BtnBuyPolicy.setVisibility(View.GONE);
            BtnNewPolicy.setVisibility(View.VISIBLE);
            BtnBreakInInspection.setVisibility(View.GONE);

        }else {
            BtnNewPolicy.setVisibility(View.GONE);
            if (isVerifiedCustomerMobile) {
                BtnVerify.setVisibility(View.GONE);
                BtnUploadPACover.setVisibility(View.GONE);
                if (StrPolicyType.equalsIgnoreCase("renew")) {
                    Log.d("StrIsPreviousPolicy",""+StrIsPreviousPolicy);
                    Log.d("IsBreakInCase",""+IsBreakInCase);
                    Log.d("breakin_status_id",""+breakin_status_id);
                    Log.d("product_type_id",""+product_type_id);
                    if(IsBreakInInspectionDone!=null && !IsBreakInInspectionDone.equalsIgnoreCase("")){
                        Log.d("IsBreakInInspectionDone",""+IsBreakInInspectionDone);
                    }else {
                        IsBreakInInspectionDone = "false";
                    }


                    if(StrIsPreviousPolicy!=null && StrIsPreviousPolicy.equalsIgnoreCase("no")){

                        BtnUploadPreviousPolicy.setVisibility(View.GONE);
                        //BtnBuyPolicy.setVisibility(View.VISIBLE);
                        //BtnBreakInInspection.setVisibility(View.GONE);
                        if(IsBreakInCase!=null && !IsBreakInCase.equalsIgnoreCase("")){
                            if(IsBreakInCase.equalsIgnoreCase("true") && breakin_status_id.equalsIgnoreCase("7")  && product_type_id.equalsIgnoreCase("1")){
                                BtnBreakInInspection.setVisibility(View.VISIBLE);
                                BtnBuyPolicy.setVisibility(View.GONE);
                            }else {
                                BtnBuyPolicy.setVisibility(View.VISIBLE);
                                BtnBreakInInspection.setVisibility(View.GONE);
                            }
                        }else {
                            BtnBuyPolicy.setVisibility(View.VISIBLE);
                            BtnBreakInInspection.setVisibility(View.GONE);
                        }
                    }else {
                        if (IsPrePolicyUploaded != null && !IsPrePolicyUploaded.equalsIgnoreCase("")) {
                            if (IsPrePolicyUploaded.equalsIgnoreCase("false")) {

                                BtnUploadPreviousPolicy.setVisibility(View.VISIBLE);
                                BtnBuyPolicy.setVisibility(View.GONE);
                            } else if (IsPrePolicyUploaded.equalsIgnoreCase("true")) {

                                BtnUploadPreviousPolicy.setVisibility(View.GONE);
                                //BtnBuyPolicy.setVisibility(View.VISIBLE);
                                //BtnBreakInInspection.setVisibility(View.GONE);
                                if(IsBreakInCase!=null && !IsBreakInCase.equalsIgnoreCase("")){
                                    if(IsBreakInCase.equalsIgnoreCase("true") && breakin_status_id.equalsIgnoreCase("7")   && product_type_id.equalsIgnoreCase("1")){
                                        BtnBreakInInspection.setVisibility(View.VISIBLE);
                                        BtnBuyPolicy.setVisibility(View.GONE);
                                    }else {
                                        BtnBuyPolicy.setVisibility(View.VISIBLE);
                                        BtnBreakInInspection.setVisibility(View.GONE);
                                    }
                                }else {
                                    BtnBuyPolicy.setVisibility(View.VISIBLE);
                                    BtnBreakInInspection.setVisibility(View.GONE);
                                }
                            }
                        }
                    }


                } else {
                    BtnUploadPACover.setVisibility(View.GONE);
                    BtnUploadPreviousPolicy.setVisibility(View.GONE);
                    BtnBuyPolicy.setVisibility(View.VISIBLE);
                }
            } else {
                BtnVerify.setVisibility(View.VISIBLE);
                BtnUploadPACover.setVisibility(View.GONE);
                BtnUploadPreviousPolicy.setVisibility(View.GONE);
                BtnBuyPolicy.setVisibility(View.GONE);
            }

        }
    }


    private void InitiateBreakInLink() {
        myDialog.show();
        String URL = ROOT_URL2+"breakin_initiated";
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

                        //JSONObject msgObj = jsonresponse.getJSONObject("message");

                        boolean result = jsonresponse.getBoolean("status");

                        if(result){
                            Intent i = new Intent(getApplicationContext(), InspectionCheckpoint.class);
                            startActivity(i);
                            overridePendingTransition(R.animator.move_left,R.animator.move_right);
                            CommonMethods.DisplayToast(getApplicationContext(),"BreakIn Initiated Successfully.");
                        }

                    } catch (JSONException e) {
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
                    map.put("quote_link", QuoteLink.toUpperCase());
                    map.put("agent_id", StrAgentId);
                    map.put("proposal_no",proposal_no);
                    map.put("mobile_no",StrCustomerMobile);
                    Log.d("Params",""+map);
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


    private void ResendVerifyLink() {
        myDialog.show();
        String URL = ROOT_URL2+"resendverifylink";
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


                        JSONObject msgObj = jsonresponse.getJSONObject("message");

                        boolean is_error = msgObj.getBoolean("is_error");

                        if(!is_error){
                            CommonMethods.DisplayToast(getApplicationContext(),"Verification Link send on registered Mobile no.");
                        }else {
                            String error_message = msgObj.getString("error_message");
                            CommonMethods.DisplayToastError(getApplicationContext(),error_message);

                        }

                    } catch (JSONException e) {
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
                    map.put("quote_link", QuoteLink.toUpperCase());
                    map.put("agent_id", StrAgentId);
                    map.put("proposal_no",proposal_no);
                    map.put("mobile_no",StrCustomerMobile);
                    Log.d("Params",""+map);
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

    private void verifyCustomerPopup() {

        DialogVerifyPopup = new Dialog(ProposalPdfActivity_6.this);
        DialogVerifyPopup.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogVerifyPopup.setCanceledOnTouchOutside(false);
        DialogVerifyPopup.setCancelable(true);
        DialogVerifyPopup.setContentView(R.layout.popup_verify_mobile_otp);
        DialogVerifyPopup.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));


        ImageView iv_close = DialogVerifyPopup.findViewById(R.id.iv_close);
        Button btnAlreadyVerified = DialogVerifyPopup.findViewById(R.id.btnAlreadyVerified);
        Button btnResendVerifyLink = DialogVerifyPopup.findViewById(R.id.btnResendVerifyLink);

        TextView til_customer_mobile_no  = DialogVerifyPopup.findViewById(R.id.til_customer_mobile_no);


        //EditText edt_Check_Mobile_Otp = (EditText) DialogVerifyPopup.findViewById(R.id.edt_Check_Mobile_Otp);

        String tv_customer_no_text = "Click on 'Send Customer Signature and OTP'. Please verify from link \n(Mobile Number: "+StrCustomerMobile+")";
        til_customer_mobile_no.setText(tv_customer_no_text);

        DialogVerifyPopup.show();

        //starCountdown();

        btnResendVerifyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResendVerifyLink();

            }
        });

        btnAlreadyVerified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckAlreadyVerified(false);
            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DialogVerifyPopup!=null && DialogVerifyPopup.isShowing()) {
                    DialogVerifyPopup.dismiss();
                }
            }
        });
    }

    private void showBreakInPopUp(boolean show_skip_btn) {

        DialogBreakInCase = new Dialog(this);
        DialogBreakInCase.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogBreakInCase.setCanceledOnTouchOutside(false);
        DialogBreakInCase.setCancelable(true);
        DialogBreakInCase.setContentView(R.layout.popup_breakin_case);
        Objects.requireNonNull(DialogBreakInCase.getWindow()).setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));


        ImageView iv_close = (ImageView) DialogBreakInCase.findViewById(R.id.iv_close);

        Button btnInspectNow = (Button) DialogBreakInCase.findViewById(R.id.btnInspectNow);
        Button btnViewProposal = (Button) DialogBreakInCase.findViewById(R.id.btnViewProposal);

        if(show_skip_btn){
            btnViewProposal.setVisibility(View.VISIBLE);
        }else {
            btnViewProposal.setVisibility(View.GONE);
        }



        btnInspectNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DialogBreakInCase!=null && DialogBreakInCase.isShowing()) {
                    DialogBreakInCase.dismiss();
                }
                InitiateBreakInLink();


            }
        });

        DialogBreakInCase.show();



        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DialogBreakInCase!=null && DialogBreakInCase.isShowing()) {
                    DialogBreakInCase.dismiss();
                }
            }
        });
    }
    /*Start Upload Previous Year Policy*/
    private void uploadDocumentPopup(boolean show_skip_btn, String document_type) {

        DialogUploadPolicy = new Dialog(ProposalPdfActivity_6.this);
        DialogUploadPolicy.requestWindowFeature(Window.FEATURE_NO_TITLE);
        DialogUploadPolicy.setCanceledOnTouchOutside(false);
        DialogUploadPolicy.setCancelable(true);
        DialogUploadPolicy.setContentView(R.layout.popup_upload_previous_policy_document);
        Objects.requireNonNull(DialogUploadPolicy.getWindow()).setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView title = (TextView)DialogUploadPolicy.findViewById(R.id.title);

        if(document_type.equalsIgnoreCase("previous_policy_doc")){
            title.setText("UPLOAD PREVIOUS POLICY");
        }else if(document_type.equalsIgnoreCase("pa_cover")){
            title.setText("UPLOAD PA COVER DOCUMENT");
        }

        ImageView iv_close = (ImageView) DialogUploadPolicy.findViewById(R.id.iv_close);
        Button captureImage_button = (Button) DialogUploadPolicy.findViewById(R.id.captureImage_button);

        Iv_UploadedImg = (ImageView)DialogUploadPolicy.findViewById(R.id.Iv_UploadedImg);
        btnPreviousPolicyUpload = (Button) DialogUploadPolicy.findViewById(R.id.btnUpload);
        Button btnViewProposal = (Button) DialogUploadPolicy.findViewById(R.id.btnViewProposal);

        if(show_skip_btn){
            btnViewProposal.setVisibility(View.VISIBLE);
        }else {
            btnViewProposal.setVisibility(View.GONE);
        }

        if(DialogUploadPolicy!=null && !DialogUploadPolicy.isShowing()) {
            DialogUploadPolicy.show();
        }




        captureImage_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //requestMultiplePermissions();
                cameraIntent();

            }
        });

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DialogUploadPolicy!=null && DialogUploadPolicy.isShowing()) {
                    DialogUploadPolicy.dismiss();
                }
            }
        });

        btnPreviousPolicyUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageFile!=null && !imageFile.getAbsolutePath().toString().equalsIgnoreCase("")) {
                    API_UPLOAD_PREVIOUS_YEAR_POLICY(document_type);
                }else {
                    CommonMethods.DisplayToastError(getApplicationContext(),"It seems that you have not captured the image to upload. Please capture the photo and then try to upload.");
                }
            }
        });

    }

    private void requestMultiplePermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.VIBRATE,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {  // check if all permissions are granted
                            Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                            cameraIntent();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) { // check for permanent denial of any permission
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {

                    }




                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void cameraIntent(){
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd").format(new Date());
        PreviousPolicyFileName = QuoteLink+"_"+ timeStamp;
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
            try {

                File direct = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/AGS");
                if (!direct.exists()) {
                    direct.mkdirs();
                }
                imageFile = File.createTempFile(
                        PreviousPolicyFileName,  // prefix
                        ".jpg",         // suffix
                        direct      // directory
                );

                mCurrentPhotoPath = "file:" + imageFile.getAbsolutePath();
                Log.d("ImagePath",""+mCurrentPhotoPath);

                outputFileUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".fileprovider", imageFile);
                if (imageFile != null) {
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                    cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 0);
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                }
            } catch (Exception ex) {
                // Error occurred while creating the File
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                Log.i(TAG, "Exception");
                Log.d("Error", ""+ex.getMessage());
                //CommonMethods.DisplayToast(getApplicationContext(),"It seems to be some technical Issue. Please try again later.");
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {

        try {

            if(imageFile!=null && imageFile.exists()){

                Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                Iv_UploadedImg.setImageBitmap(myBitmap);
                Iv_UploadedImg.setVisibility(View.VISIBLE);
                btnPreviousPolicyUpload.setVisibility(View.VISIBLE);
            }

            /*
             */
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void API_UPLOAD_PREVIOUS_YEAR_POLICY(String document_type){


        String UPLOAD_URL = ROOT_URL2 + "uploaddocumentproposal";
        Log.d("Upload_URL", UPLOAD_URL);
        myDialog.show();

        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(getApplicationContext(), uploadId, UPLOAD_URL)

                    .addFileToUpload(imageFile.getAbsolutePath(), "other_document") //
                    // Adding file
                    .addHeader("x-api-key",x_api_key)
                    .addHeader("Authorization","Basic "+CommonMethods.Base64_Encode(api_user_name + ":" + api_password))
                    .addParameter("document_name", document_type)
                    .addParameter("quote_forward_link", Quote_Link)

                    .setMaxRetries(2)
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(UploadInfo uploadInfo) {

                        }

                        @Override
                        public void onError(UploadInfo uploadInfo, Exception e) {
                            if (myDialog != null && myDialog.isShowing()) {
                                myDialog.dismiss();
                            }
                            CommonMethods.DisplayToastError(getApplicationContext(), "Error in Uploading. Please upload it again.");

                        }

                        @Override
                        public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {
                            if (myDialog != null && myDialog.isShowing()) {
                                myDialog.dismiss();
                            }
                            Log.d("serverRespGetHttpCode",""+serverResponse.getHttpCode());
                            if(serverResponse.getHttpCode()==200) {
                                try {
                                    JSONObject jsonObject = new JSONObject(serverResponse.getBodyAsString());
                                    Log.d("uploadResponse",""+jsonObject);
                                    JSONObject resultObj = jsonObject.getJSONObject("result");
                                    boolean upload_status = resultObj.getBoolean("status");
                                    if(upload_status){
                                        CommonMethods.DisplayToastSuccess(getApplicationContext(), "Document Uploaded Successfully.");


                                        if(document_type.equalsIgnoreCase("previous_policy_doc")){
                                            IsPrePolicyUploaded = "true";
                                            UtilitySharedPreferences.setPrefs(getApplicationContext(), "IsPrePolicyUploaded", "true");

                                        }

                                        if(document_type.equalsIgnoreCase("pa_cover")) {
                                            IsPaCoverUploaded = "true";
                                        }

                                        DisplayBtnsAsPerFlowConditions();

                                        if (DialogUploadPolicy != null && DialogUploadPolicy.isShowing()) {
                                            DialogUploadPolicy.dismiss();
                                        }
                                    }else {
                                        CommonMethods.DisplayToastError(getApplicationContext(), "Error in Uploading. Please upload it again.");
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }

                        }

                        @Override
                        public void onCancelled(UploadInfo uploadInfo) {
                            if (myDialog != null && myDialog.isShowing()) {
                                myDialog.dismiss();
                            }
                            CommonMethods.DisplayToastError(getApplicationContext(), "Error in Uploading. Please upload it again.");
                        }
                    })
                    .startUpload();


        } catch (Exception exc) {
            Log.d("UploadException",""+ exc.getMessage());
            if (myDialog != null && myDialog.isShowing()) {
                myDialog.dismiss();
            }
        }


    }

    /*End Upload Previous Year Policy*/
    private void API_GetProposalDetail() {
        String URL = ROOT_URL2+"getProposalData";
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        boolean isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            Log.d("getProposalData", "" + URL);
            StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    if (myDialog != null && myDialog.isShowing()) {
                        myDialog.dismiss();
                    }
                    try {

                        Log.d("Response", "" + response);
                        JSONObject jsonresponse = new JSONObject(response);

                        //boolean status = jsonresponse.getBoolean("status");
                        StrUserActionData = jsonresponse.getString("user_action_data");
                        String StrQuoteData = jsonresponse.getString("quote_data");
                        JSONObject QuoteDataObj = new JSONObject(StrQuoteData);

                        JSONObject ProposalDataObj = QuoteDataObj.getJSONObject("proposal_data");

                       // boolean is_quote_breakin  = QuoteDataObj.getBoolean("is_quote_breakin");
                        JSONObject userActionObj = new JSONObject(StrUserActionData);
                        StrIsPreviousPolicy = userActionObj.getString("is_previous_policy");
                        Proposal_list_id = ProposalDataObj.getString("proposal_list_id");
                        StrPolicyType = QuoteDataObj.getString("policy_type");
                        IsBreakInCase = QuoteDataObj.getString("is_breakin");
                        product_type_id = QuoteDataObj.getString("product_type_id");
                        String Ic_id = ProposalDataObj.getString("ic_id");

                        if(StrQuoteData.contains("breakin_status_id")) {
                            breakin_status_id = ProposalDataObj.getString("breakin_status_id");
                            Log.d("breakin_status_id", "" + breakin_status_id);
                        }
                        UtilitySharedPreferences.setPrefs(getApplicationContext(),"IC_Id",Ic_id);
                        UtilitySharedPreferences.setPrefs(getApplicationContext(),"proposal_list_id",Proposal_list_id);
                        StrVehicleRegistrationNo = ProposalDataObj.getString("vehicle_reg_no");

                        JSONObject customer_quoteObj = QuoteDataObj.getJSONObject("customer_quote");
                        JSONObject vehicle_ownerObj = customer_quoteObj.getJSONObject("vehicle_owner");


                        JSONObject ic_quoteObj = QuoteDataObj.getJSONObject("ic_quote");

                        StrTotal_Swap = ic_quoteObj.getString("total_swap");
                        StrIc_premium = ic_quoteObj.getString("ic_premium");
                        if(ic_quoteObj.getString("total_rsa")!=null && !ic_quoteObj.getString("total_rsa").equalsIgnoreCase("null")) {
                            StrTotal_Rsa = ic_quoteObj.getString("total_rsa");
                        }else {
                            StrTotal_Rsa = "0.0";
                        }
                        StrIC_Code = ic_quoteObj.getString("ic_code");
                        StrCustomerMobile = vehicle_ownerObj.getString("mobile_no");


                        //UtilitySharedPreferences.setPrefs(getApplicationContext(), "MpnData",StrMpnData);
                        UtilitySharedPreferences.setPrefs(getApplicationContext(), "UserActionData",StrUserActionData);
                        CheckAlreadyVerified(true);
                        setDataFromMpnData();




                    } catch (JSONException e) {
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
                    map.put("quote_link", QuoteLink.toUpperCase());
                    //map.put("agent_id", StrAgentId);

                    Log.d("Params",""+map);
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
    //QUOTE FORWARD
    private void API_Quote_Forward() {
        myDialog.show();
        String URL = ROOT_URL2+"quoteforward";
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

                        boolean status = jsonresponse.getBoolean("status");

                        if(status) {
                            String message = jsonresponse.getString("message");
                            MessagePopUp(status,message);
                        }else {
                            String message = "Failed to send Quote";
                            MessagePopUp(false,message);
                        }

                    } catch (JSONException e) {
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
                    map.put("quote_link", QuoteLink.toUpperCase());
                    //map.put("agent_id", StrAgentId);

                    Log.d("Params",""+map);
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

    public void MessagePopUp(boolean status, String message) {
        final Dialog dialog = new Dialog(ProposalPdfActivity_6.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.pop_up_info_message);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView title = dialog.findViewById(R.id.title);
        TextView Message_text = dialog.findViewById(R.id.Message_text);

        ImageView iv_MessageImg = dialog.findViewById(R.id.iv_MessageImg);
        if(status) {
            iv_MessageImg.setImageDrawable(getDrawable(R.drawable.checked_green));
            iv_MessageImg.setVisibility(View.VISIBLE);
        }else {
            iv_MessageImg.setImageDrawable(getDrawable(R.drawable.alert_circle));
            iv_MessageImg.setVisibility(View.VISIBLE);
        }
        TextView tv_ok = dialog.findViewById(R.id.tv_ok);
        tv_ok.setText("OK");

        title.setText(R.string.app_name);
        Message_text.setText(Html.fromHtml(message));



        dialog.show();
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });



    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Cannot write images to external storage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /* DOWNLOAD PDF */
    private void downloadAndOpenPDF() {
        myDialog.show();
        new Thread(new Runnable() {
            public void run() {
                //Uri path = Uri.fromFile(downloadFile(download_file_url));
                // if(path==null) {
                Uri  path = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".fileprovider", downloadFile(download_file_url));
                //}
                try {
                    if(myDialog!=null && myDialog.isShowing()){
                        myDialog.dismiss();
                    }
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(path, "application/pdf");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(Intent.createChooser(intent, "Open File Using..."));
                } catch (ActivityNotFoundException e) {
                    if(myDialog!=null && myDialog.isShowing()){
                        myDialog.dismiss();
                    }
                    tv_loading.setError("PDF Reader application is not installed in your device");
                }
            }
        }).start();
    }

    File downloadFile(String dwnload_file_path) {
        File file = null;
        try {

            URL url = new URL(dwnload_file_path);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);

            // connect
            urlConnection.connect();

            // set the path where we want to save the file
            File SDCardRoot = Environment.getExternalStorageDirectory();
            // create a new file, to save the downloaded file
            file = new File(SDCardRoot, dest_file_path);

            FileOutputStream fileOutput = new FileOutputStream(file);

            // Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            // this is the total size of the file which we are
            // downloading
            totalsize = urlConnection.getContentLength();
            setText("Starting PDF download...");

            // create a buffer...
            byte[] buffer = new byte[1024 * 1024];
            int bufferLength = 0;

            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                per = ((float) downloadedSize / totalsize) * 100;
                setText("Total PDF File size  : "
                        + (totalsize / 1024)
                        + " KB\n\nDownloading PDF " + (int) per
                        + "% complete");
            }
            // close the output stream when complete //
            fileOutput.close();
            setText("Download Complete. Open PDF Application installed in the device.");

        } catch (final MalformedURLException e) {
            setTextError("Some error occurred. Press back and try again.",
                    Color.RED);
        } catch (final IOException e) {
            setTextError("Some error occurred. Press back and try again.",
                    Color.RED);
        } catch (final Exception e) {
            setTextError(
                    "Failed to download image. Please check your internet connection.",
                    Color.RED);
        }
        return file;
    }

    void setTextError(final String message, final int color) {
        runOnUiThread(new Runnable() {
            public void run() {
                tv_loading.setTextColor(color);
                tv_loading.setText(message);
            }
        });

    }

    void setText(final String txt) {
        runOnUiThread(new Runnable() {
            public void run() {
                tv_loading.setText(txt);
            }
        });
    }

    /*END OF DOWNLOAD PDF*/

    @SuppressLint("DefaultLocale")
    private void BUY_POLICY() {

        merchant_id =  UtilitySharedPreferences.getPrefs(getApplicationContext(),"MerchantId");
        terminal_id =  UtilitySharedPreferences.getPrefs(getApplicationContext(),"TerminalId");
        //total_premium_payable =  UtilitySharedPreferences.getPrefs(getApplicationContext(),"total_premium_payable");

        double total_amt = Double.valueOf(StrTotal_Swap);
        total_premium_payable = String.format("%.2f", total_amt);

        if(StrIc_premium!=null && !StrIc_premium.equalsIgnoreCase("") && !StrIc_premium.equalsIgnoreCase("null")) {
            double total_ic_amt = Double.valueOf(StrIc_premium);
            StrIc_premium = String.format("%.2f", total_ic_amt);
        }else {
            StrIc_premium = total_premium_payable;
        }

        if(StrTotal_Rsa!=null && !StrTotal_Rsa.equalsIgnoreCase("") && !StrTotal_Rsa.equalsIgnoreCase("null")) {
            double total_rsa = Double.valueOf(StrTotal_Rsa);
            StrTotal_Rsa = String.format("%.2f", total_rsa);
        }else {
            StrTotal_Rsa = "0.00";
        }

        if(StrVehicleRegistrationNo!=null && !StrVehicleRegistrationNo.equalsIgnoreCase("")){
            if(StrVehicleRegistrationNo.contains("-")){
                StrVehicleRegistrationNo = StrVehicleRegistrationNo.replace("-","");
                StrVehicleRegistrationNo = StrVehicleRegistrationNo.toUpperCase();
            }else {
                StrVehicleRegistrationNo = StrVehicleRegistrationNo.toUpperCase();
            }

        }


       /* total_premium_payable = "2.00";
        StrTotal_Rsa = "1.00";
        StrIc_premium = "1.00";*/
        StrAdditionalParameters = "INS|"+proposal_no+"|"+StrVehicleRegistrationNo+"|"+product_type_id+"|"+StrIC_Code+"|"+StrTotal_Rsa+"|"+StrIc_premium;

        Log.d("total_premium_payable",""+total_premium_payable);
        Log.d("terminal_id",""+terminal_id);
        Log.d("merchant_id",""+merchant_id);
        Log.d("StrAdditionalParameters",""+StrAdditionalParameters);

        ClsPAXInf objClsPAXInf=new ClsPAXInf(this);
        objClsPAXInf.sale(total_premium_payable,"0.00","", terminal_id,merchant_id, StrAdditionalParameters);

    }

    @Override
    public void OnSuccess(String s) {
        Log.d("success-payment-string",""+s);
        //String StrPaymentResponse_success = "{\"emvTc\":\"1623C60BB7DAD0DC\",\"transaction\":\"SALE\",\"CallFrom\":\"Sale\",\"emvAPPLName\":\"VISA\",\"emvAID\":\"A0000000031010\",\"ResponseCallFrom\":\"TAP\",\"emvTVR\":\"0000000000\",\"emvTSI\":\"0000\",\"date\":\"08-05-2020\",\"time\":\"18:57:51\",\"rrn\":\"000000467847\",\"terminal_id\":\"PX918512\",\"merchant_id\":\"PX9400000000012\",\"batch_no\":\"000003\",\"stan\":\"856005\",\"cardtype\":\"VISA\",\"authcode\":\"989898\",\"cardno\":\"420739******9584\",\"tip\":\"0.00\",\"amount\":\"1.00\",\"total_amount\":\"1.0\",\"PINBLOCK\":\"\",\"card_name\":\"AKSHAY KULSHRESTHA \",\"email\":\"                                                                                                    \",\"phoneno\":\"          \",\"SE_NUMBER\":\"\",\"Internalnal_Card\":\"\"}";
        PostPaymentAPI(s);
    }

    @Override
    public void OnError(String s) {
         CommonMethods.DisplayToastError(getApplicationContext(),s);
         Log.d("failure-payment-string",s);
         //String StrPaymentResponse_success = "{\"emvTc\":\"D7C49B18A088ACA0\",\"transaction\":\"SALE\",\"CallFrom\":\"Sale\",\"emvAPPLName\":\"MASTERCARD\",\"emvAID\":\"A0000000041010\",\"ResponseCallFrom\":\"CHIP\",\"emvTVR\":\"0000048000\",\"emvTSI\":\"E800\",\"date\":\"14-08-2020\",\"time\":\"18:00:58\",\"rrn\":\"002644401741\",\"terminal_id\":\"RA131914\",\"merchant_id\":\"110000000125922\",\"batch_no\":\"000001\",\"stan\":\"284451\",\"cardtype\":\"MASTERCARD\",\"authcode\":\"R55881\",\"cardno\":\"536303******2633\",\"tip\":\"0.00\",\"amount\":\"1733.55\",\"total_amount\":\"1733.55\",\"PINBLOCK\":\"FD089C7227BB46F7\",\"card_name\":\"AKSHAY KULSHRESTHA       \\/\",\"email\":\"                                                                                                    \",\"phoneno\":\"          \",\"Internalnal_Card\":\"MDC\"}, agent_id=13179}";
         //PostPaymentAPI(StrPaymentResponse_success);
    }

    private void PostPaymentAPI(String ResponseResult) {
        myDialog.setCancelable(true);
        myDialog.setCanceledOnTouchOutside(true);
        myDialog.show();
        try {
            JSONObject ResponseResultObj = new JSONObject(ResponseResult);
                String URL = ROOT_URL2+"updatepaymentinfo";
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

                                boolean status = jsonresponse.getBoolean("status");

                                if(status) {
                                    JSONObject issuedPolicyObj = jsonresponse.getJSONObject("issued_policy_id");
                                    String message = issuedPolicyObj.getString("message");
                                    String policy_id = issuedPolicyObj.getString("policy_id");

                                    POLICY_GENERATED_URL = ROOT_URL2+"downloadPolicyPdf/"+policy_id;
                                    download_file_url = POLICY_GENERATED_URL;
                                    PDF_URL = download_file_url;

                                    is_policy_purchased = true;
                                    LoadProposalPage();
                                    DisplayBtnsAsPerFlowConditions();



                                    //PolicyGeneratedPopup(status,message);


                                }else {
                                    String policy_id = jsonresponse.getString("policy_id");
                                    String message = jsonresponse.getString("message");


                                    POLICY_GENERATED_URL = ROOT_URL2+"downloadPolicyPdf/"+policy_id;
                                    download_file_url = POLICY_GENERATED_URL;
                                    PDF_URL = download_file_url;
                                    is_policy_purchased = true;
                                    LoadProposalPage();
                                    DisplayBtnsAsPerFlowConditions();

                                    //PolicyGeneratedPopup(true,message);


                                }

                            } catch (JSONException e) {
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
                            map.put("payment_quote_link", QuoteLink.toUpperCase());
                            map.put("agent_id", StrAgentId);
                            map.put("status", "success");
                            map.put("response",ResponseResultObj.toString());
                            Log.d("Params",""+map);
                            return map;
                        }



                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<String, String>();
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


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void PolicyGeneratedPopup(boolean status,String message) {
        final Dialog dialog = new Dialog(ProposalPdfActivity_6.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.pop_up_policy_generated);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView title = dialog.findViewById(R.id.title);
        TextView Message_text = dialog.findViewById(R.id.Message_text);

        ImageView iv_MessageImg = dialog.findViewById(R.id.iv_MessageImg);
        if(status) {
            iv_MessageImg.setImageDrawable(getDrawable(R.drawable.checked_green));
            iv_MessageImg.setVisibility(View.VISIBLE);
        }else {
            iv_MessageImg.setImageDrawable(getDrawable(R.drawable.alert_circle));
            iv_MessageImg.setVisibility(View.VISIBLE);
        }
       /* TextView tv_ok = dialog.findViewById(R.id.tv_ok);
        tv_ok.setText("OK");
*/
        title.setText("Policy Generated Successfully!!!");
        Message_text.setText(Html.fromHtml(message));

        Button btnViewPolicyPdf = (Button)dialog.findViewById(R.id.btnViewPolicyPdf) ;

        btnViewPolicyPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                downloadAndOpenPDF();
                //finish();
            }
        });


        Button btnClose = (Button)dialog.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                UtilitySharedPreferences.clearPref(ProposalPdfActivity_6.this);
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
                finish();
            }
        });


        dialog.show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.animator.left_right,R.animator.right_left);
        finish();
    }




}


