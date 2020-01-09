package com.indicosmic.www.mypolicynow_sdk;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.FileProvider;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.indicosmic.www.mypolicynow_sdk.utils.CommonMethods;
import com.indicosmic.www.mypolicynow_sdk.utils.ConnectionDetector;
import com.indicosmic.www.mypolicynow_sdk.utils.DownloadTask;
import com.indicosmic.www.mypolicynow_sdk.utils.UtilitySharedPreferences;
import com.indicosmic.www.mypolicynow_sdk.webservices.RestClient;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ProposalPdfActivity extends AppCompatActivity {

    WebView webViewProposalPdf;
    ProgressBar progressBar;
    ProgressDialog myDialog;
    String StrAgentId="",StrMpnData,StrUserActionData;
    WebViewClient webViewProposalPdfClient;
    boolean isVerifiedCustomerMobile = false;
    String QuoteLink = "",PDF_URL="";
    Button BtnShare,BtnEdit,BtnVerify_Buy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proposal_page_pdf_view);

        init();

    }

    private void init() {

        StrAgentId =   UtilitySharedPreferences.getPrefs(getApplicationContext(),"PosId");
        StrMpnData = UtilitySharedPreferences.getPrefs(getApplicationContext(),"MpnData");
        StrUserActionData = UtilitySharedPreferences.getPrefs(getApplicationContext(),"UserActionData");

        webViewProposalPdf = (WebView) findViewById(R.id.webViewProposalPdf);
        progressBar = (ProgressBar)findViewById(R.id.progressbar);
        webViewProposalPdf.getSettings().setSupportZoom(true);
        webViewProposalPdf.getSettings().setJavaScriptEnabled(true);
        QuoteLink = "A0FD6165B2373435363739";
        PDF_URL = "http://uat.mypolicynow.com/downloadProposalPdf/"+QuoteLink;
        webViewProposalPdf.loadUrl("https://docs.google.com/gview?embedded=true&url="+PDF_URL);

        webViewProposalPdf.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageFinished(WebView view, String url) {
                //super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }
        });


        myDialog = new ProgressDialog(ProposalPdfActivity.this);
        myDialog.setMessage("Please wait...");
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);

        BtnShare= (Button)findViewById(R.id.BtnShare);
        BtnEdit= (Button)findViewById(R.id.BtnEdit);
        BtnVerify_Buy= (Button)findViewById(R.id.BtnVerify_Buy);
        if(!isVerifiedCustomerMobile){
            BtnVerify_Buy.setText("VERIFY");
        }else {
            BtnVerify_Buy.setText("BUY NOW");
        }

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
                                                    //new DownloadFile().execute(PDF_URL, QuoteLink+".pdf");
                                                    new DownloadTask(ProposalPdfActivity.this, PDF_URL);
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


    private class DownloadFile extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "ags/proposal");
            folder.mkdir();


            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName + ".pdf");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            //Log.e("pathOpen", file.getPath());

            Uri contentUri;
            contentUri = Uri.fromFile(file);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            if (Build.VERSION.SDK_INT >= 24) {

                Uri apkURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", file);
                intent.setDataAndType(apkURI, "application/pdf");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            } else {

                intent.setDataAndType(contentUri, "application/pdf");
            }


           // FileDownloader.downloadFile(fileUrl, pdfFile);
            return null;
        }
    }

    private void API_Quote_Forward() {
        myDialog.show();
        String URL = RestClient.ROOT_URL2+"front/Proposal/quoteForward";
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
                    map.put("agent_id", StrAgentId);


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

    public void MessagePopUp(boolean status, String message) {
        final Dialog dialog = new Dialog(ProposalPdfActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.pop_up_info_message);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView title = (TextView)dialog.findViewById(R.id.title) ;
        TextView Message_text = (TextView)dialog.findViewById(R.id.Message_text) ;

        ImageView iv_MessageImg = (ImageView)dialog.findViewById(R.id.iv_MessageImg);
        if(status) {
            iv_MessageImg.setImageDrawable(getDrawable(R.drawable.checked_green));
            iv_MessageImg.setVisibility(View.VISIBLE);
        }else {
            iv_MessageImg.setImageDrawable(getDrawable(R.drawable.alert_circle));
            iv_MessageImg.setVisibility(View.VISIBLE);
        }
        TextView tv_ok = (TextView)dialog.findViewById(R.id.tv_ok);
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
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.animator.left_right,R.animator.right_left);
    }
}
