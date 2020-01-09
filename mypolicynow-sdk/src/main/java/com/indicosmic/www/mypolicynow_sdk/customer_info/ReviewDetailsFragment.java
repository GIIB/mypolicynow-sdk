package com.indicosmic.www.mypolicynow_sdk.customer_info;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.indicosmic.www.mypolicynow_sdk.ProposalPdfActivity;
import com.indicosmic.www.mypolicynow_sdk.R;
import com.indicosmic.www.mypolicynow_sdk.utils.UtilitySharedPreferences;
import com.indicosmic.www.mypolicynow_sdk.webservices.RestClient;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import org.json.JSONObject;

import static com.indicosmic.www.mypolicynow_sdk.utils.CommonMethods.ucFirst;


public class ReviewDetailsFragment extends Fragment implements BlockingStep {


    View rootView;
    Context context;
    ImageView iv_Ic;
    TextView tv_IC_Name,tv_policy_start_date,tv_policy_end_date,tv_IDV_CoverAmt,tv_OwnerName,tv_OwnerEmail,tv_OwnerPhone,tv_NomineeName,
            tv_NomineeAge,tv_NomineeRelation,tv_AppointeeName,tv_AppointeeAge,tv_AppointeeRelation,tv_RegistrationDate,tv_EngineNo,tv_ChassisNo,
            tv_Address1,tv_Address2,tv_City,tv_State,tv_Pincode;
    LinearLayout LayoutAppointeeDetails;
    String StrAgentId="",StrMpnData="",StrUserActionData="",StrImageUrl="";
    String StrRegistrationDate="",Date_of_born = "", StrPolicyType = "";
    String StrSelectedSalutation,StrSelectedGender,StrSelectedMaritalStatus,StrFirstName,StrMiddleName, StrLastName, StrEmailAddress, StrPan, StrAadharCard,StrMobileNo="",StrGstInNumber="";
    String Str_NomineeSalutation,Str_NomineeRelationship,Str_AppointeeSalutation,Str_AppointeeRelationship;
    String Str_NomineeFirstName,Str_NomineeMiddleName,Str_NomineeLastName,Str_NomineeAge;
    String Str_AppointeeFirstName,Str_AppointeeMiddleName,Str_AppointeeLastName,Str_AppointeeAge;
    String Str_Address1,Str_Address2,Str_Pincode,Str_State,Str_City,Str_CityId,Str_StateId;
    String StrPreviousPolicyNo,StrPreviousPolicyIC,StrRtoStateCode,StrRtoCityCode,StrRtoZoneCode,StrVehicleNo,StrEngineNo,StrChassisNo,StrVehicleColor,StrAgreement,
            StrBankName,StrBankId;

    public ReviewDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for context fragment

        rootView = inflater.inflate(R.layout.fragment_review_details_info, container, false);

        context = getContext();

        init();


        return rootView;

    }

    private void init() {

        iv_Ic = (ImageView)rootView.findViewById(R.id.iv_Ic);
        tv_IC_Name  = (TextView)rootView.findViewById(R.id.tv_IC_Name);
        tv_policy_start_date= (TextView)rootView.findViewById(R.id.tv_policy_start_date);
        tv_policy_end_date= (TextView)rootView.findViewById(R.id.tv_policy_end_date);
        tv_IDV_CoverAmt= (TextView)rootView.findViewById(R.id.tv_IDV_CoverAmt);

        tv_OwnerName= (TextView)rootView.findViewById(R.id.tv_OwnerName);
        tv_OwnerEmail= (TextView)rootView.findViewById(R.id.tv_OwnerEmail);
        tv_OwnerPhone= (TextView)rootView.findViewById(R.id.tv_OwnerPhone);

        tv_NomineeName= (TextView)rootView.findViewById(R.id.tv_NomineeName);
        tv_NomineeAge  = (TextView)rootView.findViewById(R.id.tv_NomineeAge);
        tv_NomineeRelation= (TextView)rootView.findViewById(R.id.tv_NomineeRelation);

        tv_AppointeeName= (TextView)rootView.findViewById(R.id.tv_AppointeeName);
        tv_AppointeeAge= (TextView)rootView.findViewById(R.id.tv_AppointeeAge);
        tv_AppointeeRelation= (TextView)rootView.findViewById(R.id.tv_AppointeeRelation);

        tv_RegistrationDate= (TextView)rootView.findViewById(R.id.tv_RegistrationDate);
        tv_EngineNo= (TextView)rootView.findViewById(R.id.tv_EngineNo);
        tv_ChassisNo = (TextView)rootView.findViewById(R.id.tv_ChassisNo);

        tv_Address1  = (TextView)rootView.findViewById(R.id.tv_Address1);
        tv_Address2= (TextView)rootView.findViewById(R.id.tv_Address2);
        tv_City= (TextView)rootView.findViewById(R.id.tv_City);
        tv_State= (TextView)rootView.findViewById(R.id.tv_State);
        tv_Pincode= (TextView)rootView.findViewById(R.id.tv_Pincode);

         LayoutAppointeeDetails = (LinearLayout)rootView.findViewById(R.id.LayoutAppointeeDetails);

         setDataToView();
    }

    private void setDataToView() {

        StrMpnData = UtilitySharedPreferences.getPrefs(context,"MpnData");
        StrUserActionData = UtilitySharedPreferences.getPrefs(context,"UserActionData");

        try{

            JSONObject mpnObj = new JSONObject(StrMpnData);
            JSONObject icQuoteObj = mpnObj.getJSONObject("ic_quote");
            JSONObject icObj = icQuoteObj.getJSONObject("ic");
            String ic_id = icObj.getString("id");
            String ic_name = icObj.getString("code");
            String ic_logo = icObj.getString("logo");
            String total_vehicle_idv = mpnObj.getString("total_vehicle_idv");

            JSONObject policy_start_date_arr = mpnObj.getJSONObject("policy_start_date_arr");
            String str_start_date = policy_start_date_arr.getString("date");

            JSONObject policy_end_date_arr = mpnObj.getJSONObject("policy_end_date_arr");
            String str_end_date = policy_end_date_arr.getString("date");


            StrImageUrl = RestClient.ROOT_IC_IMAGE_URL+ic_logo;

            Glide.with(getActivity()).load(StrImageUrl).into(iv_Ic);

            tv_IC_Name.setText(ic_name.toUpperCase());
            tv_IDV_CoverAmt.setText("\u20B9 "+total_vehicle_idv);
            tv_policy_start_date.setText(str_start_date);
            tv_policy_end_date.setText(str_end_date);

            JSONObject user_action_dataObj = new JSONObject(StrUserActionData);
            StrRegistrationDate = user_action_dataObj.getString("purchase_invoice_date");
            StrPolicyType = user_action_dataObj.getString("policy_type");

            tv_RegistrationDate.setText(StrRegistrationDate);

        }catch (Exception e){
            e.printStackTrace();
        }


        StrEmailAddress = UtilitySharedPreferences.getPrefs(context,"OwnerEmail");
        StrMobileNo = UtilitySharedPreferences.getPrefs(context,"OwnerMobile");
        StrSelectedSalutation = UtilitySharedPreferences.getPrefs(context,"OwnerSalutation");
        StrFirstName = UtilitySharedPreferences.getPrefs(context,"OwnerFirstName");
        StrMiddleName = UtilitySharedPreferences.getPrefs(context,"OwnerMiddleName");
        StrLastName = UtilitySharedPreferences.getPrefs(context,"OwnerLastName");
        Date_of_born = UtilitySharedPreferences.getPrefs(context,"OwnerDob");
        StrSelectedGender = UtilitySharedPreferences.getPrefs(context,"OwnerGender");
        StrSelectedMaritalStatus = UtilitySharedPreferences.getPrefs(context,"OwnerMaritalStatus");

        StrPan = UtilitySharedPreferences.getPrefs(context,"OwnerPan");
        StrAadharCard = UtilitySharedPreferences.getPrefs(context,"OwnerAadhar");
        StrGstInNumber = UtilitySharedPreferences.getPrefs(context,"OwnerGSTInNumber");

        String OwnerFullName = StrSelectedSalutation + " " + StrFirstName + " "+StrMiddleName +" " +StrLastName;
        tv_OwnerName.setText(OwnerFullName.toUpperCase());
        tv_OwnerEmail.setText(StrEmailAddress);
        tv_OwnerPhone.setText(StrMobileNo);

        Str_NomineeSalutation = UtilitySharedPreferences.getPrefs(context,"NomineeSalutation");
        Str_NomineeFirstName = UtilitySharedPreferences.getPrefs(context,"NomineeFirstName");
        Str_NomineeMiddleName = UtilitySharedPreferences.getPrefs(context,"NomineeMiddleName");
        Str_NomineeLastName = UtilitySharedPreferences.getPrefs(context,"NomineeLastName");
        Str_NomineeRelationship = UtilitySharedPreferences.getPrefs(context,"NomineeRelationship");
        Str_NomineeAge = UtilitySharedPreferences.getPrefs(context,"NomineeAge");
        Str_AppointeeSalutation = UtilitySharedPreferences.getPrefs(context,"AppointeeSalutation");
        Str_AppointeeFirstName = UtilitySharedPreferences.getPrefs(context,"AppointeeFirstName");
        Str_AppointeeMiddleName = UtilitySharedPreferences.getPrefs(context,"AppointeeMiddleName");
        Str_AppointeeLastName = UtilitySharedPreferences.getPrefs(context,"AppointeeLastName");
        Str_AppointeeRelationship = UtilitySharedPreferences.getPrefs(context,"AppointeeRelationship");
        Str_AppointeeAge = UtilitySharedPreferences.getPrefs(context,"AppointeeAge");

        String NomineeFullName = Str_NomineeSalutation + " " + Str_NomineeFirstName + " "+Str_NomineeMiddleName +" " +Str_NomineeLastName;
        tv_NomineeName.setText(NomineeFullName.toUpperCase());
        tv_NomineeRelation.setText(ucFirst(Str_NomineeRelationship));

        if(Str_NomineeAge!=null && !Str_NomineeAge.equalsIgnoreCase("")){
            tv_NomineeAge.setText(Str_NomineeAge);
            int nominee_age = Integer.valueOf(Str_NomineeAge);
            if(nominee_age < 18){
                LayoutAppointeeDetails.setVisibility(View.VISIBLE);
                String AppointeeFullName = Str_AppointeeSalutation + " " + Str_AppointeeFirstName + " "+Str_AppointeeMiddleName +" " +Str_AppointeeLastName;
                tv_AppointeeName.setText(AppointeeFullName.toUpperCase());
                tv_AppointeeAge.setText(Str_AppointeeAge);
                tv_AppointeeRelation.setText(ucFirst(Str_AppointeeRelationship));
            }else {
                LayoutAppointeeDetails.setVisibility(View.GONE);
            }
        }


        Str_Address1=  UtilitySharedPreferences.getPrefs(context,"Address1");
        Str_Address2 = UtilitySharedPreferences.getPrefs(context,"Address2");
        Str_Pincode = UtilitySharedPreferences.getPrefs(context,"Pincode");
        Str_State = UtilitySharedPreferences.getPrefs(context,"AddressState");
        Str_City = UtilitySharedPreferences.getPrefs(context,"AddressCity");
        Str_StateId = UtilitySharedPreferences.getPrefs(context,"AddressStateId");
        Str_CityId = UtilitySharedPreferences.getPrefs(context,"AddressCityId");


        tv_Address1.setText(Str_Address1);
        tv_Address2.setText(Str_Address2);
        tv_City.setText(Str_City);
        tv_State.setText(Str_State);
        tv_Pincode.setText(Str_Pincode);

        StrRtoStateCode = UtilitySharedPreferences.getPrefs(context, "RtoStateCode");
        StrRtoCityCode = UtilitySharedPreferences.getPrefs(context, "RtoCityCode");
        StrRtoZoneCode = UtilitySharedPreferences.getPrefs(context, "RtoZoneCode");
        StrVehicleNo = UtilitySharedPreferences.getPrefs(context, "RtoVehicleNo");
        StrEngineNo = UtilitySharedPreferences.getPrefs(context, "EngineNo");
        StrChassisNo = UtilitySharedPreferences.getPrefs(context, "ChassisNo");
        StrVehicleColor = UtilitySharedPreferences.getPrefs(context, "VehicleColor");
        StrAgreement = UtilitySharedPreferences.getPrefs(context, "AgreementType");
        StrBankName = UtilitySharedPreferences.getPrefs(context, "BankName");
        StrBankId =  UtilitySharedPreferences.getPrefs(context, "BankId");

        StrPreviousPolicyNo = UtilitySharedPreferences.getPrefs(context, "PreviousPolicyNo");
        StrPreviousPolicyIC = UtilitySharedPreferences.getPrefs(context, "PreviousPolicyIC");

        if(StrEngineNo!=null && !StrEngineNo.equalsIgnoreCase("")){
            tv_EngineNo.setText(StrEngineNo.toUpperCase());
        }

        if(StrChassisNo!=null && !StrChassisNo.equalsIgnoreCase("")){
            tv_ChassisNo.setText(StrChassisNo.toUpperCase());
        }


    }


    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {

    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {

        JSONObject customerObj = new JSONObject();

        JSONObject vehicle_ownerObj = new JSONObject();
        JSONObject nominee_detailsObj = new JSONObject();
        JSONObject appointee_detailsObj = new JSONObject();
        JSONObject address_detailObj = new JSONObject();
        JSONObject vehicle_detailObj = new JSONObject();

        JSONObject previous_policyObj = new JSONObject();

        String cityObjStr = UtilitySharedPreferences.getPrefs(context,"CustomerCityArry");
        String stateObjStr = UtilitySharedPreferences.getPrefs(context,"CustomerStateArry");

        try {

            vehicle_ownerObj.put("email",StrEmailAddress);
            vehicle_ownerObj.put("mobile_no",StrMobileNo);
            vehicle_ownerObj.put("salutaion",StrSelectedSalutation);
            vehicle_ownerObj.put("first_name",StrFirstName);
            vehicle_ownerObj.put("middle_name",StrMiddleName);
            vehicle_ownerObj.put("last_name",StrLastName);
            vehicle_ownerObj.put("dob",Date_of_born);
            vehicle_ownerObj.put("individual_gst_no",StrGstInNumber);
            vehicle_ownerObj.put("pancard",StrPan.toUpperCase());
            vehicle_ownerObj.put("aadharcard",StrAadharCard);
            vehicle_ownerObj.put("marital_status",StrSelectedMaritalStatus.toLowerCase());
            vehicle_ownerObj.put("gender",StrSelectedGender.toLowerCase());

            nominee_detailsObj.put("nominee_salutaion",Str_NomineeSalutation);
            nominee_detailsObj.put("nominee_first_name",Str_NomineeFirstName);
            nominee_detailsObj.put("nominee_middle_name",Str_NomineeMiddleName);
            nominee_detailsObj.put("nominee_last_name",Str_NomineeLastName);
            nominee_detailsObj.put("nominee_relationship",Str_NomineeRelationship.toLowerCase());
            nominee_detailsObj.put("nominee_age",Str_NomineeAge);

            appointee_detailsObj.put("appointee_salutaion",Str_AppointeeSalutation);
            appointee_detailsObj.put("appointee_first_name",Str_AppointeeFirstName);
            appointee_detailsObj.put("appointee_middle_name",Str_AppointeeMiddleName);
            appointee_detailsObj.put("appointee_last_name",Str_AppointeeLastName);
            appointee_detailsObj.put("appointee_relationship",Str_AppointeeRelationship.toLowerCase());
            appointee_detailsObj.put("appointee_age",Str_AppointeeAge);




            previous_policyObj.put("pre_policy_no",StrPreviousPolicyNo);
            previous_policyObj.put("pre_insurance",StrPreviousPolicyIC);

            JSONObject cityObj = new JSONObject(cityObjStr);
            JSONObject stateObj = new JSONObject(stateObjStr);

            address_detailObj.put("address1",Str_Address1);
            address_detailObj.put("address2",Str_Address2);
            address_detailObj.put("pincode",Str_Address1);
            address_detailObj.put("state_id",Str_StateId);
            address_detailObj.put("state",stateObj);
            address_detailObj.put("city_id",Str_CityId);
            address_detailObj.put("city",cityObj);


            vehicle_detailObj.put("veh1",StrRtoStateCode);
            vehicle_detailObj.put("veh2",StrRtoCityCode);
            vehicle_detailObj.put("veh3",StrRtoZoneCode);
            vehicle_detailObj.put("veh4", StrVehicleNo);
            vehicle_detailObj.put("engine_no", StrEngineNo.toUpperCase());
            vehicle_detailObj.put("chassis_no", StrChassisNo.toUpperCase());
            vehicle_detailObj.put("car_color", StrVehicleColor.toUpperCase());
            vehicle_detailObj.put("reg_date", StrRegistrationDate);
            vehicle_detailObj.put("agreement_type", StrAgreement.toLowerCase());
            vehicle_detailObj.put("agreement_bank", StrBankId);

            customerObj.put("vehicle_owner",vehicle_ownerObj);
            customerObj.put("nominee_details",nominee_detailsObj);

            if(Str_NomineeAge!=null && !Str_NomineeAge.equalsIgnoreCase("")){
                tv_NomineeAge.setText(Str_NomineeAge);
                int nominee_age = Integer.valueOf(Str_NomineeAge);
                if(nominee_age < 18){
                    customerObj.put("appointee_details",appointee_detailsObj);
                }
            }

            customerObj.put("address_detail",address_detailObj);
            customerObj.put("vehicle_detail",vehicle_detailObj);


            if(StrPolicyType!=null && !StrPolicyType.equalsIgnoreCase("null") && !StrPolicyType.equalsIgnoreCase("")){
                if(StrPolicyType.equalsIgnoreCase("renew")) {
                    customerObj.put("previous_policy_details", previous_policyObj);
                }
            }

            //Log.d("customerObj",""+customerObj);



        } catch (Exception e) {
            e.printStackTrace();
        }


        Log.d("customerObj",""+customerObj);

        Intent intent = new Intent(getContext(), ProposalPdfActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.animator.move_left,R.animator.move_right);


    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }
}
