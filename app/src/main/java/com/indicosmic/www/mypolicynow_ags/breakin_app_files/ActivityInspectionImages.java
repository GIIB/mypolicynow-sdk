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
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.indicosmic.www.mypolicynow_ags.activities.ProposalPdfActivity_6;
import com.indicosmic.www.mypolicynow_ags.model.APIDataModel;
import com.indicosmic.www.mypolicynow_ags.model.Agentinfo;
import com.indicosmic.www.mypolicynow_ags.utils.CommonMethods;
import com.indicosmic.www.mypolicynow_ags.utils.ConnectionDetector;
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


public class ActivityInspectionImages extends AppCompatActivity {

    private static final String TAG = "";
    private ListView listview;
    private ArrayList<InspectionImageModel> nextquestion_modelsarray;
    // Temp save listItem position
    int position;
    InspectionImageAdapter inspectionImageAdapter;
    InspectionImageModel inspectionImageModel;
    ArrayList<String> imageList = new ArrayList<>();
    ProgressDialog progressDialog;
    String Latitude, Longitude, QuoteLink;
    Agentinfo agent;
    String agent_id;
    LayoutInflater layoutinflater;
    boolean doubleBackToExitPressedOnce = false;
    RelativeLayout snackbar;
    Button submit;
    EditText edtOdomenter_reading;
    String str_Odometer_reading;

    String question_status, odo_status;
    String IC_NAME = "";
    File mImageFolder;
    JSONObject jsonodo;
    private String pictureImagePath = "";
    Uri outputFileUri;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    File imageFile = null;
    String captionString, text, ic_id = "", break_in_case_id = "";
    private int count = 0;
    String question_id, pos_id, proposal_list_id, answer_id, image;
    boolean isLastObject = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_questions);

        listview = (ListView) findViewById(R.id.listview);
        submit = (Button) findViewById(R.id.submit);
        nextquestion_modelsarray = new ArrayList<InspectionImageModel>();
        layoutinflater = getLayoutInflater();
        ImageView back_btn_toolbar = (ImageView) findViewById(R.id.back_btn_toolbar);
        back_btn_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        TextView til_text = (TextView) findViewById(R.id.til_text);
        til_text.setText("Next Inspection Check Points");
        //getSupportActionBar().setTitle("Next Inspection Check Points");
        snackbar = (RelativeLayout) findViewById(R.id.snackbar);

        Latitude = UtilitySharedPreferences.getPrefs(getApplicationContext(), "Latitude");
        Longitude = UtilitySharedPreferences.getPrefs(getApplicationContext(), "Longitude");
        ic_id = UtilitySharedPreferences.getPrefs(getApplicationContext(), "IC_Id");
        break_in_case_id = UtilitySharedPreferences.getPrefs(getApplicationContext(), "BreakInCaseId");
        QuoteLink = UtilitySharedPreferences.getPrefs(getApplicationContext(), "QuoteLink");
        agent_id = UtilitySharedPreferences.getPrefs(getApplicationContext(), "PosId");
        IC_NAME = UtilitySharedPreferences.getPrefs(getApplicationContext(), "IC_NAME");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait while images are being uploaded");
        progressDialog.setCancelable(false);

        // Add a footer to the ListView
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.listview_footer, listview, false);
        listview.addHeaderView(header);

        edtOdomenter_reading = (EditText) header.findViewById(R.id.edtOdomenter_reading);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                str_Odometer_reading = edtOdomenter_reading.getText().toString().trim();

                // Log.d(TAG, "imageclick "+SingletonClass.getinstance().nextimage_counter);
                if (str_Odometer_reading.isEmpty() && str_Odometer_reading.equals("")) {
                    Snackbar snack = Snackbar.make(snackbar, "Please Enter odometer reading", Snackbar.LENGTH_LONG);
                    view = snack.getView();
                    TextView tv = (TextView) view.findViewById(R.id.snackbar_text);
                    tv.setTextColor(Color.RED);
                    snack.show();
                    return;
                } else {
                    if (!inspectionImageAdapter.CheckImageExist()) {
                        Snackbar snack = Snackbar.make(snackbar, "You have not uploaded all required images", Snackbar.LENGTH_LONG);
                        view = snack.getView();
                        TextView tv = (TextView) view.findViewById(R.id.snackbar_text);
                        tv.setTextColor(Color.RED);
                        snack.show();
                        return;

                    } else {

                        sendOdometerReading();
                    }
                }
            }
        });

        showdata();
    }

    private void showdata() {

        StringRequest request = new StringRequest(Request.Method.POST, Common.URL_NEXT_QUESTION_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("next question list", "" + response);
                    String status = jsonObject.getString("status");

                    if (status.trim().contains("TRUE")) {

                        JSONArray jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject ques_object = jsonArray.getJSONObject(i);

                            String main_question = ques_object.getString("name");
                            String str_is_mand = ques_object.getString("is_mand");

                            inspectionImageModel = new InspectionImageModel();

                            inspectionImageModel.setQuestion(main_question);
                            inspectionImageModel.setIs_mand(str_is_mand);

                            inspectionImageModel.setImagePath("Null");

                            nextquestion_modelsarray.add(inspectionImageModel);

                            inspectionImageAdapter = new InspectionImageAdapter(nextquestion_modelsarray, ActivityInspectionImages.this);

                            listview.setAdapter(inspectionImageAdapter);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                //Toast.makeText(getApplicationContext(), volleyError.toString(), Toast.LENGTH_LONG).show();
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

//        RequestQueue rQueue = Volley.newRequestQueue(NextQuestions.this);
//        rQueue.add(request);

        Volley.newRequestQueue(getApplicationContext()).add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

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
                // Error occurred while creating the File
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                Log.i("TAG", "Exception");
                Log.d("Error", ""+ex.getMessage());
                //CommonMethods.DisplayToast(getApplicationContext(),"It seems to be some technical Issue. Please try again later.");
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_CANCELED) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {

                File imgFile = new File(pictureImagePath);
                if(imageFile!=null && imageFile.exists()){
                    Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());

                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
                    captionString = sdf.format(new Date());
                    text = "Date:" +captionString+ "\nLat:" +Latitude+ "\nLong:" +Longitude;//+ "\nIMEI:"+agent.getImei();
                    String[] str = text.split("\n");

                    if (myBitmap != null) {
                        Bitmap dest = null;
                        try {
                            dest = myBitmap.copy(myBitmap.getConfig(), true);
                        } catch (OutOfMemoryError e1) {
                            Log.e("Exception", e1.getMessage());
                            e1.printStackTrace();
                        } catch (Error e) {
                            Log.e("Exception", e.getMessage());
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
                            cs.drawText(Oneline, 20f, (index + 1) * textHeight + 25f,
                                    tPaint);
                            index++;
                        }

                        try {
                            @SuppressLint("SimpleDateFormat")
                            String timeStamp = new SimpleDateFormat("MMdd_HHmmss").format(new Date());
                            String imageTempName = timeStamp + ".png";

                            FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/ClaimPic", imageTempName));
                            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/ClaimPic" + "/" + imageTempName);

                            inspectionImageAdapter.setImageInItem(position, dest, file.getAbsolutePath());
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
        overridePendingTransition(R.animator.left_right, R.animator.right_left);
        finish();
    }

    public void sendOdometerReading() {

        StringRequest request = new StringRequest(Request.Method.POST, Common.URL_SUBMIT_ODOMETER_READING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //progressDialog.dismiss();
                try {

                    JSONObject jObject = new JSONObject(response);

                    odo_status = jObject.getString("status");
                    if (odo_status.trim().contains("TRUE")) {


                        JSONArray jrray = InspectionImageAdapter.jrray2;
                        Log.d("jrray", "" + jrray);


                        try {
                            JSONObject jsonObject = jrray.getJSONObject(count);
                            Log.d("jsonObj", "" + jsonObject);


                            break_in_case_id = jsonObject.getString("break_in_case_id");
                            question_id = jsonObject.getString("question_id");
                            pos_id = jsonObject.getString("pos_id");
                            proposal_list_id = jsonObject.getString("proposal_list_id");
                            ic_id = jsonObject.getString("ic_id");


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
                                item.setImage(image);

                                if (count == jrray.length() - 1) {
                                    isLastObject = true;

                                }
                                if (progressDialog != null && !progressDialog.isShowing()) {
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

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Some error occurred. Please try again later. ", Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Some error occurred. Please try again later. ", Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                try {
                    jsonodo = new JSONObject();
                    JSONObject odojson = new JSONObject();
                    odojson.put("odometer", str_Odometer_reading);
                    odojson.put("pos_id", UtilitySharedPreferences.getPrefs(getApplicationContext(), "agent_id"));
                    odojson.put("break_in_case_id", UtilitySharedPreferences.getPrefs(getApplicationContext(), "BreakInCaseId"));
                    jsonodo.put("odometer_reading", odojson);

                    Log.d("odoadterjson", jsonodo.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Map<String, String> map = new HashMap<String, String>();
                map.put("odometer_reading", jsonodo.toString());
                //map.put("images",encodedString);
                Log.d("POSTDATA", "odoparams params: " + map.toString());
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
        Volley.newRequestQueue(getApplicationContext()).add(request);
        request.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    public void execute(APIDataModel item) {


        Log.d("URL_NEXT_QUESTION", Common.URL_NEXT_QUESTION_SUBMIT_NEW);
        StringRequest request = new StringRequest(Request.Method.POST, Common.URL_NEXT_QUESTION_SUBMIT_NEW, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    Log.d("response", response);
                    JSONObject jObject = new JSONObject(response);

                    String status = jObject.getString("status");

                    if (status.trim().contains("TRUE")) {

                        count++;
                        if (count < InspectionImageAdapter.jrray2.length()) {
                            Log.d("count", "" + count);
                            if (count == InspectionImageAdapter.jrray2.length() - 1) {
                                isLastObject = true;
                            }
                            try {
                                JSONObject jsonObject = InspectionImageAdapter.jrray2.getJSONObject(count);
                                Log.d("jsonObj", "" + jsonObject);

                                question_id = jsonObject.getString("question_id");
                                pos_id = jsonObject.getString("pos_id");
                                proposal_list_id = jsonObject.getString("proposal_list_id");
                                ic_id = jsonObject.getString("ic_id");

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
                        } else {
                            if (progressDialog != null && progressDialog.isShowing()) {
                                progressDialog.dismiss();
                                deleteFolder();
                                UtilitySharedPreferences.setPrefs(getApplicationContext(),"IsBreakInInspectionDone","true");
                                UtilitySharedPreferences.setPrefs(getApplicationContext(),"StrCallApiONResume","true");

                                Intent intent = new Intent(getApplicationContext(), ProposalPdfActivity_6.class);
                                startActivity(intent);
                                overridePendingTransition(R.animator.move_left,R.animator.move_right);

                            }
                        }
                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                    if (progressDialog != null && progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (progressDialog != null && progressDialog.isShowing()) {
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
                map.put("question_id", item.getQuestion_id());
                map.put("pos_id", item.getPos_id());
                map.put("proposal_list_id", item.getProposal_list_id());
                map.put("ic_id", item.getIc_id());
                map.put("image", item.getImage());
                map.put("isLastObject", String.valueOf(isLastObject));

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
                headers.put("Authorization", "Basic "+CommonMethods.Base64_Encode(api_user_name + ":" + api_password));
                return headers;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue rQueue = Volley.newRequestQueue(ActivityInspectionImages.this);
        rQueue.add(request);


    }




    public void deleteFolder() {
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/ClaimPic");

        //   Toast.makeText(this, "dir="+dir, Toast.LENGTH_LONG).show();
        Log.d("deleteFolder: ", dir.toString());

        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                new File(dir, children[i]).delete();
            }
        }
    }

}