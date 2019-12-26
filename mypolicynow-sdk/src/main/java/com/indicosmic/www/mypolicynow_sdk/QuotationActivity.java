package com.indicosmic.www.mypolicynow_sdk;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.indicosmic.www.mypolicynow_sdk.utils.CommonMethods;
import com.indicosmic.www.mypolicynow_sdk.utils.Constant;
import com.indicosmic.www.mypolicynow_sdk.utils.UtilitySharedPreferences;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class QuotationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ImageView back_btn;
    ProgressDialog myDialog;
    String QuotationFor="Car",StrPucReminder="0";
    ImageView iv_bike,iv_car,iv_commercial;
    DatePickerDialog registrationDatePickerDialog,prePolicyExpiryDatePickerDialog,dateOfExpiryDatePickerDialog;
    String RegistrationDate="",PusDateOfIssue="",PusDateOfExpiry="";
    private SimpleDateFormat dateFormatter,dateFormatter1,dateFormatter2,dateFormatter3;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private int mYear1, mMonth1, mDay1, mHour1, mMinute1;
    private int mYear2, mMonth2, mDay2, mHour2, mMinute2;

    EditText EdtVehicleName,EdtRegistration,EdtRegistrationDate,EdtEngineNo,EdtChassisNo,EdtMake,EdtModel,EdtVariant,EdtPUCDateOfIssue,EdtPUCDateOfExpiry,
            EdtPUCSerialNo,EdtPUC_CoMonoxidePer,EdtPUC_HydroCarbon,EdtPUC_NM_HCLevel,EdtPUC_ReactiveHC;

    ImageView imageRCImage1,imageRCImage2,imageRCImage3,IV_PucImage;
    TextView captureRCImage1,captureRCImage2,captureRCImage3;

    LinearLayout LayoutBtnPucImage,LayoutImgPucImage;
    CheckBox ChkPUCReminder;
    String ImageToClick="",StrRCImage1="",StrRCImage2="",StrRCImage3="",StrPUCImage="";
    Button BtnAddNewVehicle;
    Dialog DialogPreview;
    String PolicyType="Comprehensive";
    ScrollView Scroll_BelowLayout;
    TextView TV_ComprehensivePolicy,TV_TPPolicy,TV_StandaloneOd;
    RadioGroup RG_PolicyType;
    RadioButton Rb_NewPolicy,Rb_ReNewPolicy;
    ArrayList<String> manufacturingYear = new ArrayList<String>();

    Spinner Spn_ManufacturingYear,Spn_ManufacturingMonth;
    String StrPolicyType = "New",StrManufacturingYear,StrManufacturingMonth;
    Spinner Spn_PolicyHolder;
    int registration_year=0,registration_month=0;
    RadioGroup RG_ChangeInOwnership,RG_PreviousPolicy,RG_PreviousPolicyType,RG_HaveMadeClaim;
    RadioButton Rb_NoChangeInOwnership,Rb_YesChangeInOwnership,Rb_NoPreviousPolicy,Rb_YesPreviousPolicy,Rb_ComprehensivePrePolicy,Rb_ThirdPartyPrePolicy,Rb_NotMadeClaim,Rb_YesMadeClaim;
    LinearLayout SameOwnerLayout,LinearPreviousPolicyType,LinearExpiryDate,LinearHaveMadeClaim,LayoutNilDept,LinearNCB_Per;
    Spinner Spn_NCB_Percent,Spn_ODDiscount;
    EditText EdtPreviousPolicyExpiryDate;
    String StrExpiryDate = "";
    LinearLayout LinearChangeInOwnership,LayoutODDisount,InvoiceLayout,LinearNewPolicyWanted,IndividualPolicyHolderLayout,LinearValidMotorPolicy,LinearAnotherPA_Policy,LinearPA_Cover;
    RadioGroup RG_NewPolicyRequired,RG_ValidLicence,RG_AnotherPolicy,RG_AnotherPA_Policy,RG_PA_Cover;
    RadioButton Rb_1OD3TP,Rb_3OD3TP,Rb_NotValidLicence,Rb_ValidLicence,Rb_NoOtherPolicy,Rb_YesOtherPolicy,Rb_NoOtherPA_Policy,Rb_YesOtherPA_Policy,Rb_1YearPACover;

    String StrPolicyHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation_screen);

        init();
    }

    private void init() {

        back_btn = (ImageView) findViewById(R.id.back_btn_toolbar);
        back_btn.setVisibility(View.VISIBLE);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView til_text = (TextView) findViewById(R.id.til_text);
        til_text.setText("Get Quotation");
        myDialog = new ProgressDialog(QuotationActivity.this);
        myDialog.setMessage("Please wait...");
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);

        QuotationFor = UtilitySharedPreferences.getPrefs(getApplicationContext(), "QuotationFor");



        iv_bike = (ImageView) findViewById(R.id.iv_bike);
        iv_car = (ImageView) findViewById(R.id.iv_car);
        iv_commercial = (ImageView) findViewById(R.id.iv_commercial);

        if(QuotationFor!=null && !QuotationFor.equalsIgnoreCase("")){
            if(QuotationFor.equalsIgnoreCase("Bike")){
                Glide.with(QuotationActivity.this).load(R.drawable.bike_dashboard_bike).into(iv_bike);
                Glide.with(QuotationActivity.this).load(R.drawable.dashboard_car).into(iv_car);
                Glide.with(QuotationActivity.this).load(R.drawable.dashboard_pickup_truck).into(iv_commercial);
            }else if(QuotationFor.equalsIgnoreCase("Commercial")){
                Glide.with(QuotationActivity.this).load(R.drawable.dashboard_bike).into(iv_bike);
                Glide.with(QuotationActivity.this).load(R.drawable.dashboard_car).into(iv_car);
                Glide.with(QuotationActivity.this).load(R.drawable.pickup_dashboard_alert).into(iv_commercial);
            }else if(QuotationFor.equalsIgnoreCase("Car")){
                Glide.with(QuotationActivity.this).load(R.drawable.dashboard_bike).into(iv_bike);
                Glide.with(QuotationActivity.this).load(R.drawable.car_dashboard_alert).into(iv_car);
                Glide.with(QuotationActivity.this).load(R.drawable.dashboard_pickup_truck).into(iv_commercial);
            }


        }


        iv_bike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuotationFor = "Bike";
                Glide.with(QuotationActivity.this).load(R.drawable.bike_dashboard_bike).into(iv_bike);
                Glide.with(QuotationActivity.this).load(R.drawable.dashboard_car).into(iv_car);
                Glide.with(QuotationActivity.this).load(R.drawable.dashboard_pickup_truck).into(iv_commercial);
            }
        });


        iv_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuotationFor = "Car";
                Glide.with(QuotationActivity.this).load(R.drawable.dashboard_bike).into(iv_bike);
                Glide.with(QuotationActivity.this).load(R.drawable.car_dashboard_alert).into(iv_car);
                Glide.with(QuotationActivity.this).load(R.drawable.dashboard_pickup_truck).into(iv_commercial);
            }
        });

        iv_commercial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuotationFor = "Commercial";
                Glide.with(QuotationActivity.this).load(R.drawable.dashboard_bike).into(iv_bike);
                Glide.with(QuotationActivity.this).load(R.drawable.dashboard_car).into(iv_car);
                Glide.with(QuotationActivity.this).load(R.drawable.pickup_dashboard_alert).into(iv_commercial);
            }
        });

        Scroll_BelowLayout = (ScrollView)findViewById(R.id.Scroll_BelowLayout);

        TV_ComprehensivePolicy = (TextView)findViewById(R.id.TV_ComprehensivePolicy);
        TV_TPPolicy = (TextView)findViewById(R.id.TV_TPPolicy);
        TV_StandaloneOd = (TextView)findViewById(R.id.TV_StandaloneOd);

        RG_PolicyType = (RadioGroup)findViewById(R.id.RG_PolicyType);
        Rb_NewPolicy = (RadioButton)findViewById(R.id.Rb_NewPolicy);
        Rb_ReNewPolicy = (RadioButton)findViewById(R.id.Rb_ReNewPolicy);

        TV_ComprehensivePolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackGroundColor("Comprehensive");
                getManufacturingYear();
                setChangeInOwnserhipVisibility();
            }
        });

        TV_TPPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackGroundColor("ThirdParty");
                RG_PolicyType.setVisibility(View.VISIBLE);
                getManufacturingYear();
                setChangeInOwnserhipVisibility();

            }
        });

        TV_StandaloneOd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBackGroundColor("Standalone");
                getManufacturingYear();
                setChangeInOwnserhipVisibility();
            }
        });

        Spn_ManufacturingYear = (Spinner)findViewById(R.id.Spn_ManufacturingYear);
        StrPolicyType = "New";
        getManufacturingYear();
        Spn_ManufacturingYear.setOnItemSelectedListener(this);


        Spn_ManufacturingMonth= (Spinner)findViewById(R.id.Spn_ManufacturingMonth);
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, Constant.Month);
        Spn_ManufacturingMonth.setAdapter(monthAdapter);
        Spn_ManufacturingMonth.setOnItemSelectedListener(this);


        EdtRegistrationDate= (EditText)findViewById(R.id.EdtRegistrationDate);
        EdtRegistrationDate.setInputType(InputType.TYPE_NULL);
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mYear1 = c.get(Calendar.YEAR);
        mMonth1 = c.get(Calendar.MONTH);
        mDay1 = c.get(Calendar.DAY_OF_MONTH);

        EdtRegistrationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setRegistrationDate(mYear,mMonth,mDay);

            }
        });


        EdtPreviousPolicyExpiryDate= (EditText)findViewById(R.id.EdtPreviousPolicyExpiryDate);
        EdtPreviousPolicyExpiryDate.setInputType(InputType.TYPE_NULL);
        EdtPreviousPolicyExpiryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*DialogFragment dFragment = new DatePickerFragment();

                // Show the date picker dialog fragment
                dFragment.show(getFragmentManager(), "Date Picker");*/
               prePolicyExpiryDatePickerDialog = new DatePickerDialog(QuotationActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog,new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        mYear1 = year;
                        mMonth1 = monthOfYear;
                        mDay1 = dayOfMonth;

                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

                        StrExpiryDate =year +"-"+ (monthOfYear + 1) + "-" + dayOfMonth ;
                        Date date = null;
                        try {
                            date = inputFormat.parse(StrExpiryDate);
                            String StayingSince = outputFormat.format(date);

                            EdtPreviousPolicyExpiryDate.setText(StayingSince);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        checkIsBreakInCase();


                    }
                }, mYear1, mMonth1+1, -1);

                prePolicyExpiryDatePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                //prePolicyExpiryDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                //((ViewGroup) datePickerDialog.getDatePicker()).findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);

                prePolicyExpiryDatePickerDialog.show();
            }
        });




        LinearChangeInOwnership = (LinearLayout)findViewById(R.id.LinearChangeInOwnership);
        LayoutODDisount = (LinearLayout)findViewById(R.id.LayoutODDisount);
        InvoiceLayout = (LinearLayout)findViewById(R.id.InvoiceLayout);
        LinearNewPolicyWanted = (LinearLayout)findViewById(R.id.LinearNewPolicyWanted);
        RG_NewPolicyRequired= (RadioGroup)findViewById(R.id.RG_NewPolicyRequired);
        Rb_1OD3TP = (RadioButton)findViewById(R.id.Rb_1OD3TP);
        Rb_3OD3TP = (RadioButton)findViewById(R.id.Rb_3OD3TP);

        RG_ChangeInOwnership= (RadioGroup)findViewById(R.id.RG_ChangeInOwnership);
        Rb_NoChangeInOwnership = (RadioButton)findViewById(R.id.Rb_NoChangeInOwnership);
        Rb_YesChangeInOwnership = (RadioButton)findViewById(R.id.Rb_YesChangeInOwnership);

        RG_PreviousPolicy= (RadioGroup)findViewById(R.id.RG_PreviousPolicy);
        Rb_NoPreviousPolicy = (RadioButton)findViewById(R.id.Rb_NoPreviousPolicy);
        Rb_YesPreviousPolicy = (RadioButton)findViewById(R.id.Rb_YesPreviousPolicy);

        RG_PreviousPolicyType= (RadioGroup)findViewById(R.id.RG_PreviousPolicyType);
        Rb_ComprehensivePrePolicy = (RadioButton)findViewById(R.id.Rb_ComprehensivePrePolicy);
        Rb_ThirdPartyPrePolicy = (RadioButton)findViewById(R.id.Rb_ThirdPartyPrePolicy);

        RG_HaveMadeClaim= (RadioGroup)findViewById(R.id.RG_HaveMadeClaim);
        Rb_NotMadeClaim = (RadioButton)findViewById(R.id.Rb_NotMadeClaim);
        Rb_YesMadeClaim = (RadioButton)findViewById(R.id.Rb_YesMadeClaim);


        SameOwnerLayout= (LinearLayout)findViewById(R.id.SameOwnerLayout);
        LinearPreviousPolicyType= (LinearLayout)findViewById(R.id.LinearPreviousPolicyType);
        LinearExpiryDate = (LinearLayout)findViewById(R.id.LinearExpiryDate);
        LinearHaveMadeClaim= (LinearLayout)findViewById(R.id.LinearHaveMadeClaim);
        LayoutNilDept = (LinearLayout)findViewById(R.id.LayoutNilDept);
        LinearNCB_Per= (LinearLayout)findViewById(R.id.LinearNCB_Per);


        Spn_NCB_Percent= (Spinner)findViewById(R.id.Spn_NCB_Percent);
        Spn_ODDiscount= (Spinner)findViewById(R.id.Spn_ODDiscount);

        EdtPreviousPolicyExpiryDate = (EditText)findViewById(R.id.EdtPreviousPolicyExpiryDate);
        EdtPreviousPolicyExpiryDate = (EditText)findViewById(R.id.EdtPreviousPolicyExpiryDate);


        RG_NewPolicyRequired.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (Rb_1OD3TP.isChecked()) {
                    Rb_1OD3TP.setBackgroundColor(getResources().getColor(R.color.primary_green));
                    Rb_1OD3TP.setTextColor(getResources().getColor(R.color.white));
                    Rb_1OD3TP.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

                    Rb_3OD3TP.setBackground(getResources().getDrawable(R.drawable.form_bg_edittext_bg));
                    Rb_3OD3TP.setTextColor(getResources().getColor(R.color.black));
                    Rb_3OD3TP.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));


                }
                if (Rb_3OD3TP.isChecked()){
                    Rb_3OD3TP.setBackgroundColor(getResources().getColor(R.color.primary_green));
                    Rb_3OD3TP.setTextColor(getResources().getColor(R.color.white));
                    Rb_3OD3TP.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

                    Rb_1OD3TP.setBackground(getResources().getDrawable(R.drawable.form_bg_edittext_bg));
                    Rb_1OD3TP.setTextColor(getResources().getColor(R.color.black));
                    Rb_1OD3TP.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));


                }

            }
        });



        RG_ChangeInOwnership.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (Rb_NoChangeInOwnership.isChecked()) {
                    Rb_NoChangeInOwnership.setBackgroundColor(getResources().getColor(R.color.primary_green));
                    Rb_NoChangeInOwnership.setTextColor(getResources().getColor(R.color.white));
                    Rb_NoChangeInOwnership.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

                    Rb_YesChangeInOwnership.setBackground(getResources().getDrawable(R.drawable.form_bg_edittext_bg));
                    Rb_YesChangeInOwnership.setTextColor(getResources().getColor(R.color.black));
                    Rb_YesChangeInOwnership.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));
                    SameOwnerLayout.setVisibility(View.VISIBLE);
                    LinearExpiryDate.setVisibility(View.GONE);
                    LayoutNilDept.setVisibility(View.GONE);


                }
                if (Rb_YesChangeInOwnership.isChecked()){
                    Rb_YesChangeInOwnership.setBackgroundColor(getResources().getColor(R.color.primary_green));
                    Rb_YesChangeInOwnership.setTextColor(getResources().getColor(R.color.white));
                    Rb_YesChangeInOwnership.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

                    Rb_NoChangeInOwnership.setBackground(getResources().getDrawable(R.drawable.form_bg_edittext_bg));
                    Rb_NoChangeInOwnership.setTextColor(getResources().getColor(R.color.black));
                    Rb_NoChangeInOwnership.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));
                    SameOwnerLayout.setVisibility(View.GONE);
                    LinearExpiryDate.setVisibility(View.GONE);
                    LayoutNilDept.setVisibility(View.GONE);


                }

            }
        });


        RG_PreviousPolicy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (Rb_NoPreviousPolicy.isChecked()) {
                    Rb_NoPreviousPolicy.setBackgroundColor(getResources().getColor(R.color.primary_green));
                    Rb_NoPreviousPolicy.setTextColor(getResources().getColor(R.color.white));
                    Rb_NoPreviousPolicy.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

                    Rb_YesPreviousPolicy.setBackground(getResources().getDrawable(R.drawable.form_bg_edittext_bg));
                    Rb_YesPreviousPolicy.setTextColor(getResources().getColor(R.color.black));
                    Rb_YesPreviousPolicy.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));

                    LinearPreviousPolicyType.setVisibility(View.GONE);
                    LinearExpiryDate.setVisibility(View.GONE);
                    LinearHaveMadeClaim.setVisibility(View.GONE);
                    LayoutNilDept.setVisibility(View.GONE);



                }
                if (Rb_YesPreviousPolicy.isChecked()){
                    Rb_YesPreviousPolicy.setBackgroundColor(getResources().getColor(R.color.primary_green));
                    Rb_YesPreviousPolicy.setTextColor(getResources().getColor(R.color.white));
                    Rb_YesPreviousPolicy.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

                    Rb_NoPreviousPolicy.setBackground(getResources().getDrawable(R.drawable.form_bg_edittext_bg));
                    Rb_NoPreviousPolicy.setTextColor(getResources().getColor(R.color.black));
                    Rb_NoPreviousPolicy.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));

                    LinearPreviousPolicyType.setVisibility(View.VISIBLE);
                    LinearExpiryDate.setVisibility(View.GONE);
                    LinearHaveMadeClaim.setVisibility(View.GONE);
                    LayoutNilDept.setVisibility(View.GONE);



                }

            }
        });


        RG_PreviousPolicyType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (Rb_ComprehensivePrePolicy.isChecked()) {
                    Rb_ComprehensivePrePolicy.setBackgroundColor(getResources().getColor(R.color.primary_green));
                    Rb_ComprehensivePrePolicy.setTextColor(getResources().getColor(R.color.white));
                    Rb_ComprehensivePrePolicy.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

                    Rb_ThirdPartyPrePolicy.setBackground(getResources().getDrawable(R.drawable.form_bg_edittext_bg));
                    Rb_ThirdPartyPrePolicy.setTextColor(getResources().getColor(R.color.black));
                    Rb_ThirdPartyPrePolicy.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));

                    LinearHaveMadeClaim.setVisibility(View.VISIBLE);
                    LinearExpiryDate.setVisibility(View.VISIBLE);
                    LayoutNilDept.setVisibility(View.VISIBLE);
                    LinearNCB_Per.setVisibility(View.GONE);
                    scrollDown();

                }
                if (Rb_ThirdPartyPrePolicy.isChecked()){
                    Rb_ThirdPartyPrePolicy.setBackgroundColor(getResources().getColor(R.color.primary_green));
                    Rb_ThirdPartyPrePolicy.setTextColor(getResources().getColor(R.color.white));
                    Rb_ThirdPartyPrePolicy.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

                    Rb_ComprehensivePrePolicy.setBackground(getResources().getDrawable(R.drawable.form_bg_edittext_bg));
                    Rb_ComprehensivePrePolicy.setTextColor(getResources().getColor(R.color.black));
                    Rb_ComprehensivePrePolicy.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));
                    LayoutNilDept.setVisibility(View.GONE);
                    LinearHaveMadeClaim.setVisibility(View.GONE);
                    LinearExpiryDate.setVisibility(View.VISIBLE);
                    LinearNCB_Per.setVisibility(View.GONE);
                    scrollDown();

                }

            }
        });

        RG_HaveMadeClaim.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (Rb_NotMadeClaim.isChecked()) {
                    Rb_NotMadeClaim.setBackgroundColor(getResources().getColor(R.color.primary_green));
                    Rb_NotMadeClaim.setTextColor(getResources().getColor(R.color.white));
                    Rb_NotMadeClaim.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

                    Rb_YesMadeClaim.setBackground(getResources().getDrawable(R.drawable.form_bg_edittext_bg));
                    Rb_YesMadeClaim.setTextColor(getResources().getColor(R.color.black));
                    Rb_YesMadeClaim.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));

                    LinearNCB_Per.setVisibility(View.VISIBLE);
                    scrollDown();

                }
                if (Rb_YesMadeClaim.isChecked()){
                    Rb_YesMadeClaim.setBackgroundColor(getResources().getColor(R.color.primary_green));
                    Rb_YesMadeClaim.setTextColor(getResources().getColor(R.color.white));
                    Rb_YesMadeClaim.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

                    Rb_NotMadeClaim.setBackground(getResources().getDrawable(R.drawable.form_bg_edittext_bg));
                    Rb_NotMadeClaim.setTextColor(getResources().getColor(R.color.black));
                    Rb_NotMadeClaim.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));

                    LinearNCB_Per.setVisibility(View.GONE);
                    scrollDown();
                }

            }
        });

        Spn_PolicyHolder= (Spinner)findViewById(R.id.Spn_PolicyHolder);
        IndividualPolicyHolderLayout = (LinearLayout)findViewById(R.id.IndividualPolicyHolderLayout);

        Spn_PolicyHolder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                StrPolicyHolder = Spn_PolicyHolder.getSelectedItem().toString().trim();
                if(StrPolicyHolder!=null && StrPolicyHolder.equalsIgnoreCase("INDIVIDUAL")){
                    IndividualPolicyHolderLayout.setVisibility(View.VISIBLE);
                }else if(StrPolicyHolder!=null && StrPolicyHolder.equalsIgnoreCase("CORPORATE")){
                    IndividualPolicyHolderLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        RG_ValidLicence = (RadioGroup)findViewById(R.id.RG_ValidLicence);
        Rb_NotValidLicence = (RadioButton)findViewById(R.id.Rb_NotValidLicence);
        Rb_ValidLicence = (RadioButton)findViewById(R.id.Rb_ValidLicence);
        LinearValidMotorPolicy = (LinearLayout)findViewById(R.id.LinearValidMotorPolicy);

        RG_AnotherPolicy = (RadioGroup)findViewById(R.id.RG_AnotherPolicy);
        Rb_NoOtherPolicy = (RadioButton)findViewById(R.id.Rb_NoOtherPolicy);
        Rb_YesOtherPolicy = (RadioButton)findViewById(R.id.Rb_YesOtherPolicy);
        LinearAnotherPA_Policy= (LinearLayout)findViewById(R.id.LinearAnotherPA_Policy);

        RG_AnotherPA_Policy = (RadioGroup)findViewById(R.id.RG_AnotherPA_Policy);
        Rb_NoOtherPA_Policy = (RadioButton)findViewById(R.id.Rb_NoOtherPA_Policy);
        Rb_YesOtherPA_Policy = (RadioButton)findViewById(R.id.Rb_YesOtherPA_Policy);
        LinearPA_Cover= (LinearLayout)findViewById(R.id.LinearPA_Cover);

        RG_PA_Cover = (RadioGroup)findViewById(R.id.RG_PA_Cover);
        Rb_1YearPACover = (RadioButton)findViewById(R.id.Rb_1YearPACover);

        RG_ValidLicence.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (Rb_NotValidLicence.isChecked()) {
                    Rb_NotValidLicence.setBackgroundColor(getResources().getColor(R.color.primary_green));
                    Rb_NotValidLicence.setTextColor(getResources().getColor(R.color.white));
                    Rb_NotValidLicence.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

                    Rb_ValidLicence.setBackground(getResources().getDrawable(R.drawable.form_bg_edittext_bg));
                    Rb_ValidLicence.setTextColor(getResources().getColor(R.color.black));
                    Rb_ValidLicence.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));

                    LinearValidMotorPolicy.setVisibility(View.GONE);
                    scrollDown();
                }
                if (Rb_ValidLicence.isChecked()){
                    Rb_ValidLicence.setBackgroundColor(getResources().getColor(R.color.primary_green));
                    Rb_ValidLicence.setTextColor(getResources().getColor(R.color.white));
                    Rb_ValidLicence.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

                    Rb_NotValidLicence.setBackground(getResources().getDrawable(R.drawable.form_bg_edittext_bg));
                    Rb_NotValidLicence.setTextColor(getResources().getColor(R.color.black));
                    Rb_NotValidLicence.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));

                    LinearValidMotorPolicy.setVisibility(View.VISIBLE);
                    scrollDown();
                }

            }
        });

        RG_AnotherPolicy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (Rb_NoOtherPolicy.isChecked()) {
                    Rb_NoOtherPolicy.setBackgroundColor(getResources().getColor(R.color.primary_green));
                    Rb_NoOtherPolicy.setTextColor(getResources().getColor(R.color.white));
                    Rb_NoOtherPolicy.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

                    Rb_YesOtherPolicy.setBackground(getResources().getDrawable(R.drawable.form_bg_edittext_bg));
                    Rb_YesOtherPolicy.setTextColor(getResources().getColor(R.color.black));
                    Rb_YesOtherPolicy.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));

                    LinearAnotherPA_Policy.setVisibility(View.VISIBLE);
                    scrollDown();
                }
                if (Rb_YesOtherPolicy.isChecked()){
                    Rb_YesOtherPolicy.setBackgroundColor(getResources().getColor(R.color.primary_green));
                    Rb_YesOtherPolicy.setTextColor(getResources().getColor(R.color.white));
                    Rb_YesOtherPolicy.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

                    Rb_NoOtherPolicy.setBackground(getResources().getDrawable(R.drawable.form_bg_edittext_bg));
                    Rb_NoOtherPolicy.setTextColor(getResources().getColor(R.color.black));
                    Rb_NoOtherPolicy.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));

                    LinearAnotherPA_Policy.setVisibility(View.GONE);
                    scrollDown();
                }

            }
        });


        RG_AnotherPA_Policy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (Rb_NoOtherPA_Policy.isChecked()) {
                    Rb_NoOtherPA_Policy.setBackgroundColor(getResources().getColor(R.color.primary_green));
                    Rb_NoOtherPA_Policy.setTextColor(getResources().getColor(R.color.white));
                    Rb_NoOtherPA_Policy.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

                    Rb_YesOtherPA_Policy.setBackground(getResources().getDrawable(R.drawable.form_bg_edittext_bg));
                    Rb_YesOtherPA_Policy.setTextColor(getResources().getColor(R.color.black));
                    Rb_YesOtherPA_Policy.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));

                    LinearPA_Cover.setVisibility(View.VISIBLE);
                    scrollDown();
                }
                if (Rb_YesOtherPA_Policy.isChecked()){
                    Rb_YesOtherPA_Policy.setBackgroundColor(getResources().getColor(R.color.primary_green));
                    Rb_YesOtherPA_Policy.setTextColor(getResources().getColor(R.color.white));
                    Rb_YesOtherPA_Policy.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.white));

                    Rb_NoOtherPA_Policy.setBackground(getResources().getDrawable(R.drawable.form_bg_edittext_bg));
                    Rb_NoOtherPA_Policy.setTextColor(getResources().getColor(R.color.black));
                    Rb_NoOtherPA_Policy.setButtonTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.black));

                    LinearPA_Cover.setVisibility(View.GONE);
                    scrollDown();
                }

            }
        });


        RG_PolicyType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (Rb_NewPolicy.isChecked()) {
                    Rb_NewPolicy.setBackgroundColor(getResources().getColor(R.color.primary_green));
                    Rb_ReNewPolicy.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    StrPolicyType = "New";
                    getManufacturingYear();
                    setChangeInOwnserhipVisibility();
                }
                if (Rb_ReNewPolicy.isChecked()){
                    Rb_NewPolicy.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    Rb_ReNewPolicy.setBackgroundColor(getResources().getColor(R.color.primary_green));
                    StrPolicyType = "Renew";

                    getManufacturingYear();
                    setChangeInOwnserhipVisibility();
                }

            }
        });

        setChangeInOwnserhipVisibility();

    }

    private void checkIsBreakInCase() {

        if(StrExpiryDate!=null && !StrExpiryDate.equalsIgnoreCase("")){

            boolean dateExpired = CommonMethods.isDateExpired(StrExpiryDate);

            Log.d("Breaking Case Flag", ""+dateExpired);
        }
    }

    private void setRegistrationDate(int Year, int Month, int Day) {

        Log.d("DispRegistrationDate", Year +" - "+ Month +" - "+ Day);
        registrationDatePickerDialog = new DatePickerDialog(QuotationActivity.this,
                android.R.style.Theme_Holo_Light_Dialog,new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;

                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

                RegistrationDate =year +"-"+ (monthOfYear + 1) + "-" + dayOfMonth ;
                Date date = null;
                try {
                    date = inputFormat.parse(RegistrationDate);
                    String StayingSince = outputFormat.format(date);

                    EdtRegistrationDate.setText(StayingSince);
                } catch (ParseException e) {
                    e.printStackTrace();
                }



            }
        }, Year, Month-1
                , Day);

        registrationDatePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        registrationDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        //((ViewGroup) datePickerDialog.getDatePicker()).findViewById(Resources.getSystem().getIdentifier("day", "id", "android")).setVisibility(View.GONE);

        registrationDatePickerDialog.show();
    }

    private void getManufacturingYear() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        Log.d("Current Year", ""+year);
        manufacturingYear = new ArrayList<>();
        manufacturingYear.add("SELECT MANUFACTURING YEAR");

        if(PolicyType!=null && PolicyType.equalsIgnoreCase("Comprehensive")) {
            if (StrPolicyType!=null && StrPolicyType.equalsIgnoreCase("Renew")) {
                Log.d("Im here","--->In Renew");
                for (int k = year; k >= (year - 15); k--) {
                    Log.d("Year", "Renew - " + year);
                    int new_year = k;
                    manufacturingYear.add(String.valueOf(new_year));
                }
                Log.d("ManufacturingYear", "" + manufacturingYear);
                ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, manufacturingYear);
                Spn_ManufacturingYear.setAdapter(yearAdapter);


            } else if (StrPolicyType!=null &&StrPolicyType.equalsIgnoreCase("New")) {
                Log.d("Im here","--->In New");
                for (int m = year; m >= (year - 1); m--) {
                    Log.d("Year", "New - " + year);
                    int new_year = m;
                    manufacturingYear.add(String.valueOf(new_year));
                }
                Log.d("ManufacturingYear", "" + manufacturingYear);
                ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, manufacturingYear);
                Spn_ManufacturingYear.setAdapter(yearAdapter);

            }
        }else  if(PolicyType!=null && PolicyType.equalsIgnoreCase("ThirdParty")) {
            if (StrPolicyType!=null && StrPolicyType.equalsIgnoreCase("Renew")) {
                Log.d("Im here","--->In Renew");
                for (int k = year; k >= (year - 25); k--) {
                    Log.d("Year", "Renew - " + year);
                    int new_year = k;
                    manufacturingYear.add(String.valueOf(new_year));
                }
                Log.d("ManufacturingYear", "" + manufacturingYear);
                ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, manufacturingYear);
                Spn_ManufacturingYear.setAdapter(yearAdapter);


            } else if (StrPolicyType!=null &&StrPolicyType.equalsIgnoreCase("New")) {
                Log.d("Im here","--->In New");
                for (int m = year; m >= (year - 1); m--) {
                    Log.d("Year", "New - " + year);
                    int new_year = m;
                    manufacturingYear.add(String.valueOf(new_year));
                }
                Log.d("ManufacturingYear", "" + manufacturingYear);
                ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, manufacturingYear);
                Spn_ManufacturingYear.setAdapter(yearAdapter);

            }
        }else  if(PolicyType!=null && PolicyType.equalsIgnoreCase("StandaloneOD")) {
            if (StrPolicyType!=null && StrPolicyType.equalsIgnoreCase("Renew")) {
                Log.d("Im here","--->In Renew");
                for (int k = year; k >= (year - 1); k--) {
                    Log.d("Year", "Renew - " + year);
                    int new_year = k;
                    manufacturingYear.add(String.valueOf(new_year));
                }
                Log.d("ManufacturingYear", "" + manufacturingYear);
                ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, manufacturingYear);
                Spn_ManufacturingYear.setAdapter(yearAdapter);


            }
        }




    }

    private void setChangeInOwnserhipVisibility() {

        if(PolicyType!=null && PolicyType.equalsIgnoreCase("Comprehensive")) {
            if (StrPolicyType!=null && StrPolicyType.equalsIgnoreCase("Renew")) {
                LinearChangeInOwnership.setVisibility(View.VISIBLE);
                InvoiceLayout.setVisibility(View.GONE);
                LayoutODDisount.setVisibility(View.VISIBLE);
                LinearNewPolicyWanted.setVisibility(View.GONE);
                SameOwnerLayout.setVisibility(View.GONE);

            } else if (StrPolicyType!=null &&StrPolicyType.equalsIgnoreCase("New")) {
               LinearChangeInOwnership.setVisibility(View.GONE);
                LayoutODDisount.setVisibility(View.VISIBLE);
               InvoiceLayout.setVisibility(View.VISIBLE);
               LinearNewPolicyWanted.setVisibility(View.VISIBLE);
               SameOwnerLayout.setVisibility(View.GONE);

            }
        }else  if(PolicyType!=null && PolicyType.equalsIgnoreCase("ThirdParty")) {
            if (StrPolicyType!=null && StrPolicyType.equalsIgnoreCase("Renew")) {
                LinearChangeInOwnership.setVisibility(View.GONE);
                InvoiceLayout.setVisibility(View.GONE);
                LayoutODDisount.setVisibility(View.GONE);
                LinearNewPolicyWanted.setVisibility(View.GONE);
                SameOwnerLayout.setVisibility(View.GONE);

            } else if (StrPolicyType!=null &&StrPolicyType.equalsIgnoreCase("New")) {
                LinearChangeInOwnership.setVisibility(View.GONE);
                InvoiceLayout.setVisibility(View.GONE);
                LayoutODDisount.setVisibility(View.GONE);
                LinearNewPolicyWanted.setVisibility(View.GONE);
                SameOwnerLayout.setVisibility(View.GONE);
            }
        }else  if(PolicyType!=null && PolicyType.equalsIgnoreCase("StandaloneOD")) {
            if (StrPolicyType!=null && StrPolicyType.equalsIgnoreCase("Renew")) {
                LinearChangeInOwnership.setVisibility(View.VISIBLE);
                InvoiceLayout.setVisibility(View.GONE);
                LayoutODDisount.setVisibility(View.VISIBLE);
                LinearNewPolicyWanted.setVisibility(View.GONE);
                SameOwnerLayout.setVisibility(View.GONE);

            }
        }

    }

    private void setBackGroundColor(String policy_type) {
        if(policy_type!=null && policy_type.equalsIgnoreCase("Comprehensive")){
            TV_ComprehensivePolicy.setBackgroundColor(getResources().getColor(R.color.black));
            TV_ComprehensivePolicy.setTextColor(getResources().getColor(R.color.white));
            TV_TPPolicy.setBackgroundColor(getResources().getColor(R.color.grey_bg_color));
            TV_TPPolicy.setTextColor(getResources().getColor(R.color.black));
            TV_StandaloneOd.setBackgroundColor(getResources().getColor(R.color.grey_bg_color));
            TV_StandaloneOd.setTextColor(getResources().getColor(R.color.black));
            PolicyType = "Comprehensive";
            RG_PolicyType.setVisibility(View.VISIBLE);
            Rb_NewPolicy.setVisibility(View.VISIBLE);
            Rb_NewPolicy.setChecked(true);
            Rb_NewPolicy.setBackgroundColor(getResources().getColor(R.color.primary_green));
            Rb_ReNewPolicy.setVisibility(View.VISIBLE);
            Rb_ReNewPolicy.setChecked(false);
            Rb_ReNewPolicy.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }else if(policy_type!=null && policy_type.equalsIgnoreCase("ThirdParty")){
            TV_ComprehensivePolicy.setBackgroundColor(getResources().getColor(R.color.grey_bg_color));
            TV_ComprehensivePolicy.setTextColor(getResources().getColor(R.color.black));
            TV_TPPolicy.setBackgroundColor(getResources().getColor(R.color.black));
            TV_TPPolicy.setTextColor(getResources().getColor(R.color.white));
            TV_StandaloneOd.setBackgroundColor(getResources().getColor(R.color.grey_bg_color));
            TV_StandaloneOd.setTextColor(getResources().getColor(R.color.black));
            PolicyType = "ThirdParty";
            RG_PolicyType.setVisibility(View.VISIBLE);
            Rb_NewPolicy.setVisibility(View.VISIBLE);
            Rb_NewPolicy.setChecked(true);
            Rb_NewPolicy.setBackgroundColor(getResources().getColor(R.color.primary_green));
            Rb_ReNewPolicy.setVisibility(View.VISIBLE);
            Rb_ReNewPolicy.setChecked(false);
            Rb_ReNewPolicy.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        }else if(policy_type!=null && policy_type.equalsIgnoreCase("Standalone")){

            TV_ComprehensivePolicy.setBackgroundColor(getResources().getColor(R.color.grey_bg_color));
            TV_ComprehensivePolicy.setTextColor(getResources().getColor(R.color.black));
            TV_TPPolicy.setBackgroundColor(getResources().getColor(R.color.grey_bg_color));
            TV_TPPolicy.setTextColor(getResources().getColor(R.color.black));
            TV_StandaloneOd.setBackgroundColor(getResources().getColor(R.color.black));
            TV_StandaloneOd.setTextColor(getResources().getColor(R.color.white));
            PolicyType = "StandaloneOD";
            RG_PolicyType.setVisibility(View.VISIBLE);
            Rb_NewPolicy.setVisibility(View.GONE);
            Rb_NewPolicy.setChecked(false);
            Rb_NewPolicy.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            Rb_ReNewPolicy.setVisibility(View.VISIBLE);
            Rb_ReNewPolicy.setChecked(true);
            Rb_ReNewPolicy.setBackgroundColor(getResources().getColor(R.color.primary_green));
        }
    }

    private void scrollDown() {
        Scroll_BelowLayout.post(new Runnable() {
            @Override
            public void run() {
                Scroll_BelowLayout.smoothScrollTo(Scroll_BelowLayout.getScrollY(), Scroll_BelowLayout.getScrollY()
                        + Scroll_BelowLayout.getHeight());
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int id = adapterView.getId();
        if (id == R.id.Spn_ManufacturingYear) {
            StrManufacturingYear = Spn_ManufacturingYear.getSelectedItem().toString().trim();
            if (Spn_ManufacturingYear.getSelectedItemPosition() > 0) {
                registration_year = Integer.valueOf(StrManufacturingYear);
                  /*  if(registration_year!=0 && registration_month!=0){
                        setRegistrationDate(registration_year,registration_month,1);
                    }*/
            }
        } else if (id == R.id.Spn_ManufacturingMonth) {
            StrManufacturingMonth = Spn_ManufacturingMonth.getSelectedItem().toString().trim();

            if (Spn_ManufacturingMonth.getSelectedItemPosition() > 0) {
                registration_month = Spn_ManufacturingMonth.getSelectedItemPosition();
                if (registration_year != 0 && registration_month != 0) {
                    setRegistrationDate(registration_year, registration_month, 1);
                }

            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

