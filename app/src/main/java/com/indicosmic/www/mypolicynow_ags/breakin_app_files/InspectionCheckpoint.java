package com.indicosmic.www.mypolicynow_ags.breakin_app_files;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.indicosmic.www.mypolicynow_ags.R;
import com.indicosmic.www.mypolicynow_ags.model.APIDataModel;
import com.indicosmic.www.mypolicynow_ags.model.Agentinfo;
import com.indicosmic.www.mypolicynow_ags.utils.CommonMethods;
import com.indicosmic.www.mypolicynow_ags.utils.GPSTracker;
import com.indicosmic.www.mypolicynow_ags.utils.JSONClass;
import com.indicosmic.www.mypolicynow_ags.utils.UtilitySharedPreferences;
import com.indicosmic.www.mypolicynow_ags.webservices.Common;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.indicosmic.www.mypolicynow_ags.webservices.RestClient.api_password;
import static com.indicosmic.www.mypolicynow_ags.webservices.RestClient.api_user_name;
import static com.indicosmic.www.mypolicynow_ags.webservices.RestClient.x_api_key;


public class InspectionCheckpoint extends AppCompatActivity {

//    private static final String TAG = "";
    private ListView listview;
    private ArrayList<CheckPointListModel> question_modelsarray;
    int position;
    CheckpointListAdapter checkpointListAdapter;
    CheckPointListModel question_model;
    ArrayList<String> imageList = new ArrayList<>();
    ProgressDialog progressDialog;
    boolean doubleBackToExitPressedOnce = false;
    RelativeLayout snackbar;
    GPSTracker gps;
    double latitude;
    double longitude;
    Agentinfo agent;
    String agent_id,QuoteLink;
    File mImageFolder;
    Uri outputFileUri;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    File imageFile = null;
    LayoutInflater layoutinflater;
    private String pictureImagePath = "";
    Button submit;
    String question_id,pos_id,proposal_list_id,ic_id,answer_id,image,break_in_case_id="";
    private int count = 0;

    String text;

    String captionString;

    @SuppressLint({"MissingPermission", "HardwareIds"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list_view);

        gps = new GPSTracker(InspectionCheckpoint.this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            UtilitySharedPreferences.setPrefs(getApplicationContext(),"Latitude",String.valueOf(latitude));
            UtilitySharedPreferences.setPrefs(getApplicationContext(),"Longitude",String.valueOf(longitude));



        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

        listview = (ListView) findViewById(R.id.listview);
        snackbar = (RelativeLayout)findViewById(R.id.snackbar);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        question_modelsarray = new ArrayList<CheckPointListModel>();
        layoutinflater = getLayoutInflater();

        ImageView back_btn_toolbar = (ImageView)findViewById(R.id.back_btn_toolbar);
        back_btn_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        TextView til_text = (TextView)findViewById(R.id.til_text);
        til_text.setText("Inspection Check Points");

        agent_id = UtilitySharedPreferences.getPrefs(getApplicationContext(),"PosId");
        QuoteLink  = UtilitySharedPreferences.getPrefs(getApplicationContext(),"QuoteLink");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait while data is being uploaded");
        progressDialog.setCancelable(false);

        showdata();

        submit = (Button) findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                   if (!checkpointListAdapter.getAnswer()){
                    Snackbar snack = Snackbar.make(snackbar, "Please answer all the questions", Snackbar.LENGTH_LONG);
                    view = snack.getView();
                    TextView tv = (TextView) view.findViewById(R.id.snackbar_text);
                    tv.setTextColor(Color.RED);
                    snack.show();

                }else {
                       JSONArray jrray = JSONClass.jrray;
                       Log.d("jrray",""+jrray);

                       try {
                           JSONObject jsonObject = jrray.getJSONObject(count);
                           Log.d("jsonObj", "" + jsonObject);

                           question_id = jsonObject.getString("question_id");
                           pos_id = jsonObject.getString("pos_id");
                           proposal_list_id = jsonObject.getString("proposal_list_id");
                           ic_id = jsonObject.getString("ic_id");

                           if (jsonObject.has("answer_id")) {
                               answer_id = jsonObject.getString("answer_id");
                           }

                           if (jsonObject.has("image")) {
                               image = jsonObject.getString("image");
                           } else {
                               image = "";
                           }

                           try {
                               APIDataModel item = new APIDataModel();
                               item.setBreak_in_case_id(break_in_case_id);
                               item.setQuestion_id(question_id);
                               item.setPos_id(pos_id);
                               item.setProposal_list_id(proposal_list_id);
                               item.setIc_id(ic_id);
                               item.setAnswer_id(answer_id);
                               item.setImage(image);


                               if(progressDialog!=null && !progressDialog.isShowing()) {
                                   progressDialog.show();
                               }
                               execute(item);

                           } catch (Exception e) {
                               e.printStackTrace();
                           }


                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                }

           }
        });
    }

    private void showdata() {
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, Common.URL_QUESTION_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(progressDialog!=null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status");

                    if (status.trim().contains("TRUE")){

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject ques_object = jsonArray.getJSONObject(i);

                            String main_question = ques_object.getString("question");
                            JSONObject question_object = ques_object.getJSONObject("answers_obj");

                            String safe = question_object.getString("0");
                            String scratch = question_object.getString("1");
                            String pressed = question_object.getString("2");
                            String broken = question_object.getString("3");

                            question_model = new CheckPointListModel();

                            question_model.setQuestion(main_question.toUpperCase());
                            question_model.setSafe("Safe");
                            question_model.setScrach("Scratch");
                            question_model.setPressed("Pressed");
                            question_model.setBroken("Broken");
                            question_model.setAnswer("Null");
                            question_model.setId(i);

                            question_modelsarray.add(question_model);

                            checkpointListAdapter = new CheckpointListAdapter(question_modelsarray, InspectionCheckpoint.this);

                            listview.setAdapter(checkpointListAdapter);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(progressDialog!=null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Toast.makeText(getApplicationContext(), "Some error occurred. Please try again later. ", Toast.LENGTH_LONG).show();
            }
        }){

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

        Volley.newRequestQueue(getApplicationContext()).add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(
                0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    public void execute(APIDataModel item) {


        Log.d("SubmitReview",Common.URL_SUBMIT_QUESTION_NEW);
        StringRequest request = new StringRequest(Request.Method.POST, Common.URL_SUBMIT_QUESTION_NEW, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.d("response", response);
                    JSONObject jObject = new JSONObject(response);

                    String status = jObject.getString("status");

                    if (status.trim().contains("TRUE")){
                        break_in_case_id = jObject.getString("break_in_case_id");

                        UtilitySharedPreferences.setPrefs(getApplicationContext(), "BreakInCaseId", break_in_case_id);


                        count++;
                        if(count<JSONClass.jrray.length()){
                            //JSONClass.jrray.getJSONObject(count);
                            Log.d("count",""+count);
                            try {
                                JSONObject jsonObject = JSONClass.jrray.getJSONObject(count);
                                Log.d("jsonObj", "" + jsonObject);

                                question_id = jsonObject.getString("question_id");
                                pos_id = jsonObject.getString("pos_id");
                                proposal_list_id = jsonObject.getString("proposal_list_id");
                                ic_id = jsonObject.getString("ic_id");

                                if (jsonObject.has("answer_id")) {
                                    answer_id = jsonObject.getString("answer_id");
                                }

                                if (jsonObject.has("image")) {
                                    image = jsonObject.getString("image");
                                } else {
                                    image = "";
                                }

                                try {
                                    APIDataModel item = new APIDataModel();
                                    item.setBreak_in_case_id(break_in_case_id);
                                    item.setQuestion_id(question_id);
                                    item.setPos_id(pos_id);
                                    item.setProposal_list_id(proposal_list_id);
                                    item.setIc_id(ic_id);
                                    item.setAnswer_id(answer_id);
                                    item.setImage(image);


                                    execute(item);


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else {


                            if(progressDialog!=null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                                Toast.makeText(InspectionCheckpoint.this, "Your data has been successfully saved to Our Server", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), ActivityInspectionImages.class);
                                startActivity(i);
                                overridePendingTransition(R.animator.move_left,R.animator.move_right);
                                //finish();
                            }





                        }


                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                    if(progressDialog!=null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(progressDialog!=null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                volleyError.printStackTrace();
                Toast.makeText(getApplicationContext(), "Some error occurred. Please try again later. ", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> map = new HashMap<String, String>();
                map.put("break_in_case_id", item.getBreak_in_case_id());
                map.put("question_id",item.getQuestion_id());
                map.put("pos_id",item.getPos_id());
                map.put("proposal_list_id",item.getProposal_list_id());
                map.put("ic_id",item.getIc_id());
                map.put("answer_id",item.getAnswer_id());
                map.put("image",item.getImage());

                Log.d("POSTDATA", "question params: " + map.toString());
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //  Authorization: Basic $auth
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/x-www-form-urlencoded");
                //headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("x-api-key",x_api_key);
                headers.put("Authorization", "Basic "+ CommonMethods.Base64_Encode(api_user_name + ":" + api_password));
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue rQueue = Volley.newRequestQueue(InspectionCheckpoint.this);
        rQueue.add(request);


    }

    public void captureImage(int pos, String imageName) {

        position = pos;

        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageTempName = timeStamp + ".jpg";
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
            try {

                File direct = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/ClaimPic");
                if (!direct.exists()) {
                    direct.mkdirs();
                }
                imageFile = File.createTempFile(
                        imageTempName,  // prefix
                        ".jpg",         // suffix
                        direct      // directory
                );

                pictureImagePath = "file:" + imageFile.getAbsolutePath();
                Log.d("ImagePath",""+pictureImagePath);

                outputFileUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".fileprovider", imageFile);
                if (imageFile != null) {
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                    cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 0);

                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                }
            } catch (Exception ex) {

                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);

                Log.i("TAG", "Exception");
                Log.d("Error", ""+ex.getMessage());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode != Activity.RESULT_CANCELED) {
                if (requestCode == REQUEST_IMAGE_CAPTURE) {
                    //onCaptureImageResult(data);

                    if(imageFile!=null && imageFile.exists()){

                        Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
                        captionString = sdf.format(new Date());
                        text = "Date:" +captionString+ "\nLat:" +latitude+ "\nLong:" +longitude; //"\nIMEI:"+agent.getImei();
                        String[] str = text.split("\n");



                        if (myBitmap != null) {
                            Bitmap dest = null;
                            try {
                                dest = myBitmap.copy(myBitmap.getConfig(), true);
                            } catch (OutOfMemoryError e1) {
                                // Log.e("Exception", e1.getMessage());
                                e1.printStackTrace();
                            } catch (Error e) {
                                // Log.e("Exception", e.getMessage());
                                e.printStackTrace();
                            }


                            Canvas cs = new Canvas(dest);
                            Typeface tf = Typeface.create("Verdana", Typeface.BOLD);
                            Paint tPaint = new Paint();
                            tPaint.setAntiAlias(true);
                            tPaint.setTextSize(60);
                            tPaint.setTextAlign(Paint.Align.LEFT);
                            tPaint.setTypeface(tf);
                            tPaint.setColor(Color.RED);
                            tPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                            cs.drawBitmap(myBitmap, 0f, 0f, null);
                            float textHeight = tPaint.measureText("yY");
                            int index = 0;
                            for (String Oneline : str) {
                                cs.drawText(Oneline, 20f, (index + 1) * textHeight + 25f, tPaint);
                                index++;
                            }

                            try {

                                String timeStamp = new SimpleDateFormat("MMdd_HHmmss").format(new Date());
                                String imageTempName = timeStamp + ".png";

                                FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/ClaimPic", imageTempName));
                                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/ClaimPic" + "/" + imageTempName);


                                checkpointListAdapter.setImageInItem(position, dest, file.getAbsolutePath());
                                // Toast.makeText(this, "path"+fos.toString(), Toast.LENGTH_LONG).show();
                                dest.compress(Bitmap.CompressFormat.JPEG, 30, fos);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            }
        }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        overridePendingTransition(R.animator.left_right,R.animator.right_left);
        finish();
    }
}
