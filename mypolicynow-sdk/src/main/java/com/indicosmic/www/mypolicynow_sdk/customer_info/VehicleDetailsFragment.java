package com.indicosmic.www.mypolicynow_sdk.customer_info;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.indicosmic.www.mypolicynow_sdk.R;
import com.indicosmic.www.mypolicynow_sdk.utils.CommonMethods;
import com.indicosmic.www.mypolicynow_sdk.utils.ConnectionDetector;
import com.indicosmic.www.mypolicynow_sdk.utils.MyValidator;
import com.indicosmic.www.mypolicynow_sdk.utils.UtilitySharedPreferences;
import com.indicosmic.www.mypolicynow_sdk.webservices.RestClient;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VehicleDetailsFragment extends Fragment implements BlockingStep, AdapterView.OnItemSelectedListener {


    View rootView;
    Context context;

    //    //Arraylist for occupation
    ArrayList<String> occupationNameList = new ArrayList<String>();
    ArrayList<String> occupationCodeList = new ArrayList<String>();
    String StrAgentId="";
    EditText Edt_RtoStateCode,Edt_RtoCityCode,Edt_RtoZoneCode,Edt_RtoVehicleNo,Edt_EngineNo,Edt_ChassisNo;
    Spinner Spn_VehicleColor,Spn_Agreement;
    TextView tv_registration_date;
    SearchableSpinner Spn_Bank;
    String StrMpnData,StrUserActionData,StrPolicyType;
    String StrRtoStateCode,StrRtoCityCode,StrRtoZoneCode,StrVehicleNo,StrEngineNo,StrChassisNo,StrRegistrationDate,StrVehicleColor,StrAgreement,
            StrBankName,StrBankId,strRtoVehicleNoFull="";
    String SelectedIc_Id;
    LinearLayout LayoutPreviousPolicyRenewalData;
    EditText Edt_PreviousPolicyNo;
    Spinner Spn_Ic;
    Integer IsExistRTOCode_ChassisNo=0;

    String StrPreviousPolicyNo,StrPreviousPolicyIC;

    ArrayList<String> vehicleColorValue = new ArrayList<String>();
    ArrayList<String> vehicleColorDisplayValue = new ArrayList<String>();

    ArrayList<String> bankValue = new ArrayList<String>();
    ArrayList<String> bankDisplayValue = new ArrayList<String>();



    ArrayList<String> icValue = new ArrayList<String>();
    ArrayList<String> icDisplayValue = new ArrayList<String>();

    public VehicleDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for context fragment

        rootView = inflater.inflate(R.layout.fragment_vehicle_details_info, container, false);

        context = getContext();


        init();


        return rootView;

    }

    private void init() {
        StrAgentId =   UtilitySharedPreferences.getPrefs(context,"PosId");
        SelectedIc_Id =   UtilitySharedPreferences.getPrefs(context,"SelectedIcId");
        StrUserActionData = UtilitySharedPreferences.getPrefs(context,"UserActionData");
        StrMpnData = UtilitySharedPreferences.getPrefs(context,"MpnData");

        Edt_RtoStateCode = (EditText)rootView.findViewById(R.id.Edt_RtoStateCode);
        Edt_RtoCityCode = (EditText)rootView.findViewById(R.id.Edt_RtoCityCode);
        Edt_RtoZoneCode = (EditText)rootView.findViewById(R.id.Edt_RtoZoneCode);
        Edt_RtoVehicleNo = (EditText)rootView.findViewById(R.id.Edt_RtoVehicleNo);
        Edt_EngineNo = (EditText)rootView.findViewById(R.id.Edt_EngineNo);
        Edt_ChassisNo = (EditText)rootView.findViewById(R.id.Edt_ChassisNo);
        Spn_VehicleColor = (Spinner)rootView.findViewById(R.id.Spn_VehicleColor);
        Spn_Agreement = (Spinner)rootView.findViewById(R.id.Spn_Agreement);
        tv_registration_date = (TextView)rootView.findViewById(R.id.tv_registration_date);
        Spn_Bank = (SearchableSpinner)rootView.findViewById(R.id.Spn_Bank);

        LayoutPreviousPolicyRenewalData = (LinearLayout) rootView.findViewById(R.id.LayoutPreviousPolicyRenewalData);
        Edt_PreviousPolicyNo = (EditText)rootView.findViewById(R.id.Edt_PreviousPolicyNo);
        Spn_Ic = (Spinner)rootView.findViewById(R.id.Spn_Ic);


        Edt_RtoVehicleNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString()!=null && editable.toString().length()>0){
                    StrRtoZoneCode = Edt_RtoZoneCode.getText().toString();
                    strRtoVehicleNoFull  = StrRtoStateCode + "-"+StrRtoCityCode+"-"+StrRtoZoneCode+"-"+editable.toString();
                    //API_CHECK_CHASSIS_NO("",strRtoVehicleNoFull.toLowerCase());
                }
            }
        });


        Edt_ChassisNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString()!=null && editable.toString().length()>0){
                    //API_CHECK_CHASSIS_NO(editable.toString(),strRtoVehicleNoFull.toLowerCase());
                }
            }
        });


        Spn_VehicleColor.setOnItemSelectedListener(this);
        Spn_Agreement.setOnItemSelectedListener(this);
        Spn_Bank.setOnItemSelectedListener(this);
        Spn_Ic.setOnItemSelectedListener(this);

        API_GET_CUSTOMER_MASTER();

        setValuesToView();

    }

    private  void API_CHECK_CHASSIS_NO(String strChassisNo,String strRegNo){

        String URL_IsChasisSelected = RestClient.ROOT_URL2 +"IsChasisSelected";

        try {
            Log.d("URL",URL_IsChasisSelected);

            ConnectionDetector cd = new ConnectionDetector(context);
            boolean isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_IsChasisSelected,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("Response",response);
                                try {
                                    JSONObject jobj = new JSONObject(response);

                                    boolean status = jobj.getBoolean("status");
                                    if(status){
                                        JSONObject dataObj = jobj.getJSONObject("data");
                                        IsExistRTOCode_ChassisNo = dataObj.getInt("is_exist");
                                        if(IsExistRTOCode_ChassisNo==1) {
                                            if (strChassisNo != null && !strChassisNo.equalsIgnoreCase("")) {
                                                Edt_ChassisNo.setError("Chassis No. Exist");
                                            }

                                            if (strRegNo != null && !strRegNo.equalsIgnoreCase("")) {
                                                Edt_RtoVehicleNo.setError("Vehicle No. Exist");
                                            }
                                        }

                                    }


                                } catch (Exception e) {

                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("volley", "Error: " + error.getMessage());
                        error.printStackTrace();

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("agent_id", StrAgentId);
                        map.put("mpn_data",StrMpnData);
                        map.put("chassis_no",strChassisNo.toLowerCase());
                        map.put("reg_no",strRegNo.toLowerCase());
                        Log.d("strChassisNoParam",""+map);
                        return map;
                    }
                };

                int socketTimeout = 50000;//30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);



            } else {
                CommonMethods.DisplayToast(context, "Please check your internet connection");
            }
        } catch (Exception e) {

            e.printStackTrace();

        }

    }



    private void API_GET_CUSTOMER_MASTER() {

        vehicleColorValue = new ArrayList<>();
        vehicleColorDisplayValue = new ArrayList<>();
        vehicleColorValue.add("0");
        vehicleColorDisplayValue.add("Select Color");
        bankValue = new ArrayList<>();
        bankDisplayValue = new ArrayList<>();
        bankValue.add("0");
        bankDisplayValue.add("Select Bank");
        icValue = new ArrayList<>();
        icDisplayValue = new ArrayList<>();
        icValue.add("0");
        icDisplayValue.add("Select Insurance Company");


        String URL_CUSTOMER_MASTER = RestClient.ROOT_URL2 +"customerMaster";

        try {
            Log.d("URL",URL_CUSTOMER_MASTER);

            ConnectionDetector cd = new ConnectionDetector(context);
            boolean isInternetPresent = cd.isConnectingToInternet();
            if (isInternetPresent) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CUSTOMER_MASTER,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("Response",response);
                                try {
                                    JSONObject jobj = new JSONObject(response);

                                    boolean status = jobj.getBoolean("status");
                                    if(status){
                                        JSONObject dataObj = jobj.getJSONObject("data");

                                        JSONArray vehicle_colorArry = dataObj.getJSONArray("vehicle_color");

                                        for(int k=0;k<vehicle_colorArry.length();k++) {

                                            JSONObject jsonObject = vehicle_colorArry.getJSONObject(k);
                                            String color_name = jsonObject.getString("name");
                                            String color_id = jsonObject.getString("id");



                                            vehicleColorDisplayValue.add(color_name);
                                            vehicleColorValue.add(color_id);
                                        }

                                        ArrayAdapter<String> colorAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, vehicleColorDisplayValue);
                                        Spn_VehicleColor.setAdapter(colorAdapter);
                                        if(StrVehicleColor!=null && !StrVehicleColor.equalsIgnoreCase("") && !StrVehicleColor.equalsIgnoreCase("null")){
                                            int i = getIndex(Spn_VehicleColor,StrVehicleColor);
                                            Spn_VehicleColor.setSelection(i);
                                        }

                                        JSONArray agreement_banksArry = dataObj.getJSONArray("agreement_banks");

                                        for(int n=0;n<agreement_banksArry.length();n++) {

                                            JSONObject jsonObject = agreement_banksArry.getJSONObject(n);
                                            String BankName = jsonObject.getString("BankName");
                                            String BankID = jsonObject.getString("BankID");



                                            bankDisplayValue.add(BankName);
                                            bankValue.add(BankID);
                                        }

                                        ArrayAdapter<String> bankAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, bankDisplayValue);
                                        Spn_Bank.setAdapter(bankAdapter);
                                        if(StrBankName!=null && !StrBankName.equalsIgnoreCase("") && !StrBankName.equalsIgnoreCase("null")){
                                            int i = getIndex1(Spn_Bank,StrBankName);
                                            Spn_Bank.setSelection(i);

                                        }

                                        JSONArray insurence_companiesArry = dataObj.getJSONArray("insurence_companies");

                                        for(int m=0;m<insurence_companiesArry.length();m++) {

                                            JSONObject jsonObject = insurence_companiesArry.getJSONObject(m);
                                            String ic_name = jsonObject.getString("code");
                                            String ic_id = jsonObject.getString("id");


                                            icDisplayValue.add(ic_name);
                                            icValue.add(ic_id);
                                        }

                                        ArrayAdapter<String> icAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, icDisplayValue);
                                        Spn_Ic.setAdapter(icAdapter);
                                        if (StrPreviousPolicyIC != null && !StrPreviousPolicyIC.equalsIgnoreCase("") && !StrPreviousPolicyIC.equalsIgnoreCase("null")) {
                                            int i = getIndex(Spn_Ic, StrPreviousPolicyIC);
                                            Spn_Ic.setSelection(i);
                                        }else {
                                            Spn_Ic.setSelection(0);
                                        }


                                    }



                                } catch (Exception e) {

                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("volley", "Error: " + error.getMessage());
                        error.printStackTrace();

                    }
                }){
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("agent_id", StrAgentId);
                        Log.d("MapLoadIcList",""+map);
                        return map;
                    }
                };

                int socketTimeout = 50000;//30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(stringRequest);



            } else {
                CommonMethods.DisplayToast(context, "Please check your internet connection");
            }
        } catch (Exception e) {

            e.printStackTrace();

        }

    }



    private void setValuesToView() {

        try {
            JSONObject user_action_dataObj = new JSONObject(StrUserActionData);
            StrRegistrationDate = user_action_dataObj.getString("purchase_invoice_date");
            StrPolicyType = user_action_dataObj.getString("policy_type");
            String RTO_Label = user_action_dataObj.getString("rto_label");
            if(RTO_Label!=null && !RTO_Label.equalsIgnoreCase("") && !RTO_Label.equalsIgnoreCase("null")){

                String[] rtoSeparated = RTO_Label.split("-");
                if(rtoSeparated.length==2) {
                    StrRtoStateCode = rtoSeparated[0].toUpperCase();
                    StrRtoCityCode = rtoSeparated[1];

                    Edt_RtoStateCode.setText(StrRtoStateCode);
                    Edt_RtoCityCode.setText(StrRtoCityCode);


                }

            }

            if(StrRegistrationDate!=null && !StrRegistrationDate.equalsIgnoreCase("") && !StrRegistrationDate.equalsIgnoreCase("null")){
                tv_registration_date.setText(StrRegistrationDate);
            }

            if(StrPolicyType!=null && !StrPolicyType.equalsIgnoreCase("null") && !StrPolicyType.equalsIgnoreCase("")){
                if(StrPolicyType.equalsIgnoreCase("new")){
                    LayoutPreviousPolicyRenewalData.setVisibility(View.GONE);
                }else if(StrPolicyType.equalsIgnoreCase("renew")){
                    LayoutPreviousPolicyRenewalData.setVisibility(View.VISIBLE);
                    Edt_PreviousPolicyNo.setText("");
                    Spn_Ic.setSelection(0);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        StrRtoZoneCode = UtilitySharedPreferences.getPrefs(context, "RtoZoneCode");
        StrVehicleNo = UtilitySharedPreferences.getPrefs(context, "RtoVehicleNo");
        StrEngineNo = UtilitySharedPreferences.getPrefs(context, "EngineNo");
        StrChassisNo = UtilitySharedPreferences.getPrefs(context, "ChassisNo");
        StrVehicleColor = UtilitySharedPreferences.getPrefs(context, "VehicleColor");
        StrAgreement = UtilitySharedPreferences.getPrefs(context, "AgreementType");
        StrBankName = UtilitySharedPreferences.getPrefs(context, "BankName");
        StrPreviousPolicyNo = UtilitySharedPreferences.getPrefs(context, "PreviousPolicyNo");
        StrPreviousPolicyIC = UtilitySharedPreferences.getPrefs(context, "PreviousPolicyIC");


        if(StrRtoZoneCode!=null && !StrRtoZoneCode.equalsIgnoreCase("") && !StrRtoZoneCode.equalsIgnoreCase("null")){
            Edt_RtoZoneCode.setText(StrRtoZoneCode);
        }

        if(StrVehicleNo!=null && !StrVehicleNo.equalsIgnoreCase("") && !StrVehicleNo.equalsIgnoreCase("null")){
            Edt_RtoVehicleNo.setText(StrVehicleNo);
        }

        if(StrEngineNo!=null && !StrEngineNo.equalsIgnoreCase("") && !StrEngineNo.equalsIgnoreCase("null")){
            Edt_EngineNo.setText(StrEngineNo);
        }

        if(StrChassisNo!=null && !StrChassisNo.equalsIgnoreCase("") && !StrChassisNo.equalsIgnoreCase("null")){
            Edt_ChassisNo.setText(StrChassisNo);
        }

        if(StrVehicleColor!=null && !StrVehicleColor.equalsIgnoreCase("") && !StrVehicleColor.equalsIgnoreCase("null")){
            int i = getIndex(Spn_VehicleColor,StrVehicleColor);
            Spn_VehicleColor.setSelection(i);
        }

        if(StrAgreement!=null && !StrAgreement.equalsIgnoreCase("") && !StrAgreement.equalsIgnoreCase("null")){
            if(StrAgreement.contains("_")){
                StrAgreement = StrAgreement.replace("_"," ");
            }
            int i = getIndex(Spn_Agreement,StrAgreement);
            Spn_Agreement.setSelection(i);
        }

        if(StrBankName!=null && !StrBankName.equalsIgnoreCase("") && !StrBankName.equalsIgnoreCase("null")){
            int i = getIndex1(Spn_Bank,StrBankName);
            Spn_Bank.setSelection(i);
        }

        if(StrPolicyType!=null && !StrPolicyType.equalsIgnoreCase("null") && !StrPolicyType.equalsIgnoreCase("")){
            if(StrPolicyType.equalsIgnoreCase("new")){
                LayoutPreviousPolicyRenewalData.setVisibility(View.GONE);
            }else if(StrPolicyType.equalsIgnoreCase("renew")){
                LayoutPreviousPolicyRenewalData.setVisibility(View.VISIBLE);

                if (StrPreviousPolicyNo != null && !StrPreviousPolicyNo.equalsIgnoreCase("") && !StrPreviousPolicyNo.equalsIgnoreCase("null")) {
                    Edt_PreviousPolicyNo.setText(StrPreviousPolicyNo);
                }else {
                    Edt_PreviousPolicyNo.setText("");
                }

                if (StrPreviousPolicyIC != null && !StrPreviousPolicyIC.equalsIgnoreCase("") && !StrPreviousPolicyIC.equalsIgnoreCase("null")) {
                    int i = getIndex(Spn_Ic, StrPreviousPolicyIC);
                    Spn_Ic.setSelection(i);
                }else {
                    Spn_Ic.setSelection(0);
                }
            }
        }


    }


    private int getIndex(Spinner spinner, String searchString) {

        if (searchString == null || spinner.getCount() == 0) {

            return -1; // Not found

        } else {

            for (int i = 0; i < spinner.getCount(); i++) {
                if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(searchString)) {
                    return i; // Found!
                }
            }

            return -1; // Not found
        }
    }

    private int getIndex1(SearchableSpinner spinner, String searchString) {

        if (searchString == null || spinner.getCount() == 0) {

            return -1; // Not found

        } else {

            for (int i = 0; i < spinner.getCount(); i++) {
                if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(searchString)) {
                    return i; // Found!
                }
            }

            return -1; // Not found
        }
    }


    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {

        if(IsExistRTOCode_ChassisNo==0) {
            if (IsValidFields()) {

                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(Edt_ChassisNo.getWindowToken(), 0);

                StrRtoStateCode = Edt_RtoStateCode.getText().toString().toLowerCase();
                StrRtoCityCode = Edt_RtoCityCode.getText().toString().toLowerCase();
                StrRtoZoneCode = Edt_RtoZoneCode.getText().toString();
                StrVehicleNo = Edt_RtoVehicleNo.getText().toString();
                StrEngineNo = Edt_EngineNo.getText().toString();
                StrChassisNo = Edt_ChassisNo.getText().toString();
                StrVehicleColor = Spn_VehicleColor.getSelectedItem().toString();
                //StrAgreement = Spn_Agreement.getSelectedItem().toString().toLowerCase();
                StrBankName = Spn_Bank.getSelectedItem().toString();
                int pos_bank = Spn_Bank.getSelectedItemPosition();
                StrBankId = bankValue.get(pos_bank);

                if (StrPolicyType.equalsIgnoreCase("renew")) {
                    StrPreviousPolicyNo = Edt_PreviousPolicyNo.getText().toString();
                    String IcName = Spn_Ic.getSelectedItem().toString();
                    int pos_ic = Spn_Ic.getSelectedItemPosition();
                    String Ic_Id = icValue.get(pos_ic);
                    StrPreviousPolicyIC = Ic_Id + "," + IcName;


                } else {
                    StrPreviousPolicyNo = "";
                    StrPreviousPolicyIC = "";
                }

                UtilitySharedPreferences.setPrefs(context, "RtoStateCode", StrRtoStateCode);
                UtilitySharedPreferences.setPrefs(context, "RtoCityCode", StrRtoCityCode);
                UtilitySharedPreferences.setPrefs(context, "RtoZoneCode", StrRtoZoneCode);
                UtilitySharedPreferences.setPrefs(context, "RtoVehicleNo", StrVehicleNo);
                UtilitySharedPreferences.setPrefs(context, "EngineNo", StrEngineNo);
                UtilitySharedPreferences.setPrefs(context, "ChassisNo", StrChassisNo);
                UtilitySharedPreferences.setPrefs(context, "VehicleColor", StrVehicleColor);
                UtilitySharedPreferences.setPrefs(context, "AgreementType", StrAgreement);
                UtilitySharedPreferences.setPrefs(context, "BankName", StrBankName);
                UtilitySharedPreferences.setPrefs(context, "BankId", StrBankId);

                UtilitySharedPreferences.setPrefs(context, "PreviousPolicyNo", StrPreviousPolicyNo);
                UtilitySharedPreferences.setPrefs(context, "PreviousPolicyIC", StrPreviousPolicyIC);

                callback.goToNextStep();
            }
        }else {
            CommonMethods.DisplayToastError(context,"Vehicle No. or Chassis No. Exist");
        }
    }


    private boolean IsValidFields() {
        boolean result = true;

        if (!MyValidator.isValidField(Edt_RtoZoneCode)) {
            Edt_RtoZoneCode.requestFocus();
            CommonMethods.DisplayToastWarning(context, "Please Enter Valid Vehicle No");
            result = false;
        }

        if (!MyValidator.isValidField(Edt_RtoVehicleNo)) {
            Edt_RtoVehicleNo.requestFocus();
            CommonMethods.DisplayToastWarning(context, "Please Enter Valid Vehicle No");
            result = false;
        }

        if (!MyValidator.isValidEngine_ChassisNumber(Edt_EngineNo)) {
            Edt_EngineNo.requestFocus();
            CommonMethods.DisplayToastWarning(context, "Please Enter Engine No");
            result = false;
        }

        if (!MyValidator.isValidEngine_ChassisNumber(Edt_ChassisNo)) {
            Edt_ChassisNo.requestFocus();
            CommonMethods.DisplayToastWarning(context, "Please Enter Chassis No");
            result = false;
        }


        if (!MyValidator.isValidSpinner(Spn_VehicleColor)) {
            CommonMethods.DisplayToastWarning(context, "Please Select Vehicle Color");
            result = false;
        }

        if (!MyValidator.isValidSpinner(Spn_Agreement)) {
            CommonMethods.DisplayToastWarning(context, "Please Select Agreement Type");
            result = false;
        }

        if(Spn_Agreement.getSelectedItemPosition()!=0) {
            if (!MyValidator.isValidSearchableSpinner(Spn_Bank)) {
                CommonMethods.DisplayToastWarning(context, "Please Select Bank");
                result = false;
            }
        }

        if(StrPolicyType!=null && !StrPolicyType.equalsIgnoreCase("null") && !StrPolicyType.equalsIgnoreCase("")){
             if(StrPolicyType.equalsIgnoreCase("renew")){
                 if (!MyValidator.isValidField(Edt_PreviousPolicyNo)) {
                     Edt_PreviousPolicyNo.requestFocus();
                     CommonMethods.DisplayToastWarning(context, "Please Enter Previous Policy number");
                     result = false;
                 }


                 if (!MyValidator.isValidSpinner(Spn_Ic)) {
                     CommonMethods.DisplayToastWarning(context, "Please Select Previous Policy Insurance Company");
                     result = false;
                 }
            }
        }




        return result;
    }


    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
            callback.goToPrevStep();
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        if (!IsValidFields()) {
            return new VerificationError("");

        }
        return null;

    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int id = adapterView.getId();
        if (id == R.id.Spn_VehicleColor) {
            StrVehicleColor = Spn_VehicleColor.getSelectedItem().toString().trim();
        } else if (id == R.id.Spn_Agreement) {
            StrAgreement = Spn_Agreement.getSelectedItem().toString().trim();
            if (StrAgreement.equalsIgnoreCase("hire purchase")) {
                StrAgreement = "hire_purchase";
            } else if (StrAgreement.equalsIgnoreCase("Lease Agreement")) {
                StrAgreement = "lease_agreement";
            }
        } else if (id == R.id.Spn_Bank) {
            StrBankName = Spn_Bank.getSelectedItem().toString().trim();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}