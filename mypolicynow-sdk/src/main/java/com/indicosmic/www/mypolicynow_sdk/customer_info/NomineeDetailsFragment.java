package com.indicosmic.www.mypolicynow_sdk.customer_info;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.indicosmic.www.mypolicynow_sdk.R;
import com.indicosmic.www.mypolicynow_sdk.utils.CommonMethods;
import com.indicosmic.www.mypolicynow_sdk.utils.MyValidator;
import com.indicosmic.www.mypolicynow_sdk.utils.UtilitySharedPreferences;
import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class NomineeDetailsFragment extends Fragment implements BlockingStep,AdapterView.OnItemSelectedListener {

    Spinner Spn_Education;

    SearchableSpinner Spn_EducationField;

    String Str_Education,Str_EducationField,Str_EducationFieldCode;
    String UserObj,ClientId,StrFirstName,StrLastName,StrEmail,StrMobile,DeviceId,StrCountryId,StrReligionId;
    ProgressDialog myDialog;
    Context context;
    StepperLayout.OnNextClickedCallback mCallback;


    String StrCollegeName;

    private ArrayList<String> stateArrayList;


    //    //Arraylist for occupation
    ArrayList<String> educationFieldNameList = new ArrayList<String>();
    ArrayList<String> educationFieldCodeList = new ArrayList<String>();



    Spinner Spn_NomineeSalutation,Spn_NomineeRelationship,Spn_AppointeeSalutation,Spn_AppointeeRelationship;
    EditText edt_NomineeFirstName,edt_NomineeMiddleName,edt_NomineeLastName,edt_NomineeAge,edt_AppointeeFirstName,edt_AppointeeMiddleName,
            edt_AppointeeLastName,edt_AppointeeAge;
    LinearLayout LayoutAppointeeDetails;
    String Str_NomineeSalutation,Str_NomineeRelationship,Str_AppointeeSalutation,Str_AppointeeRelationship;
    String Str_NomineeFirstName,Str_NomineeMiddleName,Str_NomineeLastName,Str_NomineeAge;
    String Str_AppointeeFirstName,Str_AppointeeMiddleName,Str_AppointeeLastName,Str_AppointeeAge;

    View rootView;



    public NomineeDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_nominee_details_info, container, false);
        context = getContext();
        init();

        return rootView;
    }

    private void init() {

        Spn_NomineeSalutation = (Spinner)rootView.findViewById(R.id.Spn_NomineeSalutation);
        Spn_NomineeRelationship = (Spinner)rootView.findViewById(R.id.Spn_NomineeRelationship);
        Spn_AppointeeSalutation = (Spinner)rootView.findViewById(R.id.Spn_AppointeeSalutation);
        Spn_AppointeeRelationship = (Spinner)rootView.findViewById(R.id.Spn_AppointeeRelationship);

        edt_NomineeFirstName = (EditText)rootView.findViewById(R.id.edt_NomineeFirstName);
        edt_NomineeMiddleName = (EditText)rootView.findViewById(R.id.edt_NomineeMiddleName);
        edt_NomineeLastName = (EditText)rootView.findViewById(R.id.edt_NomineeLastName);
        edt_NomineeAge = (EditText)rootView.findViewById(R.id.edt_NomineeAge);


        LayoutAppointeeDetails = (LinearLayout)rootView.findViewById(R.id.LayoutAppointeeDetails);
        LayoutAppointeeDetails.setVisibility(View.GONE);

        edt_AppointeeFirstName = (EditText)rootView.findViewById(R.id.edt_AppointeeFirstName);
        edt_AppointeeMiddleName = (EditText)rootView.findViewById(R.id.edt_AppointeeMiddleName);
        edt_AppointeeLastName = (EditText)rootView.findViewById(R.id.edt_AppointeeLastName);
        edt_AppointeeAge = (EditText)rootView.findViewById(R.id.edt_AppointeeAge);


        edt_NomineeAge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().length()==2){
                    String nomineeAge = charSequence.toString();
                    Integer nominee_age = Integer.valueOf(nomineeAge);
                    if(nominee_age < 18){
                        LayoutAppointeeDetails.setVisibility(View.VISIBLE);
                    }else {
                        LayoutAppointeeDetails.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Spn_NomineeSalutation.setOnItemSelectedListener(this);
        Spn_NomineeRelationship.setOnItemSelectedListener(this);
        Spn_AppointeeSalutation.setOnItemSelectedListener(this);
        Spn_AppointeeRelationship.setOnItemSelectedListener(this);

        myDialog = new ProgressDialog(getActivity());
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.setMessage("Please Wait...");




        setValuesToView();
    }

    private void setValuesToView() {

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


        if(Str_NomineeSalutation!=null && !Str_NomineeSalutation.equalsIgnoreCase("") && !Str_NomineeSalutation.equalsIgnoreCase("null")){
            int i = getIndex(Spn_NomineeSalutation,Str_NomineeSalutation);
            Spn_NomineeSalutation.setSelection(i);
        }

        if(Str_NomineeFirstName!=null && !Str_NomineeFirstName.equalsIgnoreCase("") && !Str_NomineeFirstName.equalsIgnoreCase("null")){
            edt_NomineeFirstName.setText(Str_NomineeFirstName);
        }

        if(Str_NomineeMiddleName!=null && !Str_NomineeMiddleName.equalsIgnoreCase("") && !Str_NomineeMiddleName.equalsIgnoreCase("null")){
            edt_NomineeMiddleName.setText(Str_NomineeMiddleName);
        }

        if(Str_NomineeLastName!=null && !Str_NomineeLastName.equalsIgnoreCase("") && !Str_NomineeLastName.equalsIgnoreCase("null")){
            edt_NomineeLastName.setText(Str_NomineeLastName);
        }

        if(Str_NomineeRelationship!=null && !Str_NomineeRelationship.equalsIgnoreCase("") && !Str_NomineeRelationship.equalsIgnoreCase("null")){
            int i = getIndex(Spn_NomineeRelationship,Str_NomineeRelationship);
            Spn_NomineeRelationship.setSelection(i);
        }
        if(Str_NomineeAge!=null && !Str_NomineeAge.equalsIgnoreCase("") && !Str_NomineeAge.equalsIgnoreCase("null")){
            edt_NomineeAge.setText(Str_NomineeAge);
            int nominee_age = Integer.valueOf(Str_NomineeAge);
            if(nominee_age<18){
                LayoutAppointeeDetails.setVisibility(View.VISIBLE);
            }else {
                LayoutAppointeeDetails.setVisibility(View.GONE);
            }
        }

        if(LayoutAppointeeDetails.getVisibility()==View.VISIBLE){

            if(Str_AppointeeSalutation!=null && !Str_AppointeeSalutation.equalsIgnoreCase("") && !Str_AppointeeSalutation.equalsIgnoreCase("null")){
                int i = getIndex(Spn_AppointeeSalutation,Str_AppointeeSalutation);
                Spn_AppointeeSalutation.setSelection(i);
            }

            if(Str_AppointeeFirstName!=null && !Str_AppointeeFirstName.equalsIgnoreCase("") && !Str_AppointeeFirstName.equalsIgnoreCase("null")){
                edt_AppointeeFirstName.setText(Str_AppointeeFirstName);
            }

            if(Str_AppointeeMiddleName!=null && !Str_AppointeeMiddleName.equalsIgnoreCase("") && !Str_AppointeeMiddleName.equalsIgnoreCase("null")){
                edt_AppointeeMiddleName.setText(Str_AppointeeMiddleName);
            }

            if(Str_AppointeeLastName!=null && !Str_AppointeeLastName.equalsIgnoreCase("") && !Str_AppointeeLastName.equalsIgnoreCase("null")){
                edt_AppointeeLastName.setText(Str_AppointeeLastName);
            }

            if(Str_AppointeeRelationship!=null && !Str_AppointeeRelationship.equalsIgnoreCase("") && !Str_AppointeeRelationship.equalsIgnoreCase("null")){
                int i = getIndex(Spn_AppointeeRelationship,Str_AppointeeRelationship);
                Spn_AppointeeRelationship.setSelection(i);
            }
            if(Str_AppointeeAge!=null && !Str_AppointeeAge.equalsIgnoreCase("") && !Str_AppointeeAge.equalsIgnoreCase("null")){
                edt_AppointeeAge.setText(Str_AppointeeAge);

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


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {


        int id = adapterView.getId();
        if (id == R.id.Spn_NomineeSalutation) {
            Str_NomineeSalutation = Spn_NomineeSalutation.getSelectedItem().toString().trim();
        } else if (id == R.id.Spn_NomineeRelationship) {
            Str_NomineeRelationship = Spn_NomineeRelationship.getSelectedItem().toString().toLowerCase().trim();
        } else if (id == R.id.Spn_AppointeeSalutation) {
            Str_AppointeeSalutation = Spn_AppointeeSalutation.getSelectedItem().toString().trim();
        } else if (id == R.id.Spn_AppointeeRelationship) {
            Str_AppointeeRelationship = Spn_AppointeeRelationship.getSelectedItem().toString().toLowerCase().trim();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @Nullable
    @Override
    public VerificationError verifyStep() {
        if(!validateFields()) {
            return new VerificationError("");
        }

        return null;
    }

    private boolean validateFields() {
        boolean result = true;

        if (!MyValidator.isValidSpinner(Spn_NomineeSalutation)) {
            CommonMethods.DisplayToastWarning(getContext(),"Please Select Nominee Salutation");
            result = false;
        }

        if (!MyValidator.isValidName(edt_NomineeFirstName)) {
            edt_NomineeFirstName.requestFocus();
            CommonMethods.DisplayToastWarning(getContext(),"Please Enter First Name");
            result = false;
        }

        if (!MyValidator.isValidName(edt_NomineeLastName)) {
            edt_NomineeLastName.requestFocus();
            CommonMethods.DisplayToastWarning(getContext(),"Please Enter Last Name");
            result = false;
        }

        if (!MyValidator.isValidSpinner(Spn_NomineeRelationship)) {
            CommonMethods.DisplayToastWarning(getContext(),"Please Select Nominee Relationship");
            result = false;
        }

        if (!MyValidator.isValidField(edt_NomineeAge)) {
            edt_NomineeAge.requestFocus();
            CommonMethods.DisplayToastWarning(getContext(),"Please Enter Nominee Age");
            result = false;
        }


        if(LayoutAppointeeDetails!=null && LayoutAppointeeDetails.getVisibility()==View.VISIBLE){

            if (!MyValidator.isValidSpinner(Spn_AppointeeSalutation)) {
                CommonMethods.DisplayToastWarning(getContext(),"Please Select Appointee Salutation");
                result = false;
            }

            if (!MyValidator.isValidName(edt_AppointeeFirstName)) {
                edt_AppointeeFirstName.requestFocus();
                CommonMethods.DisplayToastWarning(getContext(),"Please Enter First Name");
                result = false;
            }

            if (!MyValidator.isValidName(edt_AppointeeLastName)) {
                edt_AppointeeLastName.requestFocus();
                CommonMethods.DisplayToastWarning(getContext(),"Please Enter Last Name");
                result = false;
            }

            if (!MyValidator.isValidSpinner(Spn_AppointeeRelationship)) {
                CommonMethods.DisplayToastWarning(getContext(),"Please Select Appointee Relationship");
                result = false;
            }

            if (!MyValidator.isValidField(edt_AppointeeAge)) {
                edt_AppointeeAge.requestFocus();
                CommonMethods.DisplayToastWarning(getContext(),"Please Enter Appointee Age");
                result = false;
            }


        }






        return result;
    }
        //Block Steps Until Operation gets Finished

    @Override
    public void onNextClicked(final StepperLayout.OnNextClickedCallback onNextClickedCallback) {

        mCallback = onNextClickedCallback;

        if(validateFields()) {

            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edt_NomineeAge.getWindowToken(), 0);

            Str_NomineeSalutation = Spn_NomineeSalutation.getSelectedItem().toString();
            Str_NomineeFirstName = edt_NomineeFirstName.getText().toString();
            Str_NomineeMiddleName = edt_NomineeMiddleName.getText().toString();
            Str_NomineeLastName = edt_NomineeLastName.getText().toString();
            Str_NomineeRelationship = Spn_NomineeRelationship.getSelectedItem().toString();
            Str_NomineeAge = edt_NomineeAge.getText().toString();

            if (LayoutAppointeeDetails != null && LayoutAppointeeDetails.getVisibility() == View.VISIBLE) {
                Str_AppointeeSalutation = Spn_AppointeeSalutation.getSelectedItem().toString();
                Str_AppointeeFirstName = edt_AppointeeFirstName.getText().toString();
                Str_AppointeeMiddleName = edt_AppointeeMiddleName.getText().toString();
                Str_AppointeeLastName = edt_AppointeeLastName.getText().toString();
                Str_AppointeeRelationship = Spn_AppointeeRelationship.getSelectedItem().toString();
                Str_AppointeeAge = edt_AppointeeAge.getText().toString();
            } else {
                Str_AppointeeSalutation = "";
                Str_AppointeeFirstName = "";
                Str_AppointeeMiddleName = "";
                Str_AppointeeLastName = "";
                Str_AppointeeRelationship = "";
                Str_AppointeeAge = "";
            }

            UtilitySharedPreferences.setPrefs(context, "NomineeSalutation", Str_NomineeSalutation);
            UtilitySharedPreferences.setPrefs(context, "NomineeFirstName", Str_NomineeFirstName);
            UtilitySharedPreferences.setPrefs(context, "NomineeMiddleName", Str_NomineeMiddleName);
            UtilitySharedPreferences.setPrefs(context, "NomineeLastName", Str_NomineeLastName);
            UtilitySharedPreferences.setPrefs(context, "NomineeRelationship", Str_NomineeRelationship);
            UtilitySharedPreferences.setPrefs(context, "NomineeAge", Str_NomineeAge);
            UtilitySharedPreferences.setPrefs(context, "AppointeeSalutation", Str_AppointeeSalutation);
            UtilitySharedPreferences.setPrefs(context, "AppointeeFirstName", Str_AppointeeFirstName);
            UtilitySharedPreferences.setPrefs(context, "AppointeeMiddleName", Str_AppointeeMiddleName);
            UtilitySharedPreferences.setPrefs(context, "AppointeeLastName", Str_AppointeeLastName);
            UtilitySharedPreferences.setPrefs(context, "AppointeeRelationship", Str_AppointeeRelationship);
            UtilitySharedPreferences.setPrefs(context, "AppointeeAge", Str_AppointeeAge);

            mCallback.goToNextStep();
        }

    }




    @Override
    public void onCompleteClicked(final StepperLayout.OnCompleteClickedCallback onCompleteClickedCallback) {



    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback onBackClickedCallback) {

        onBackClickedCallback.goToPrevStep();
    }



    @Override
    public void onSelected() {

    }

    @Override
    public void onError(@NonNull VerificationError verificationError) {

    }






    private void fetchEducationalFields() {

        //Get state json value from assets folder
        try {
            JSONObject obj = new JSONObject(loadJSONFromAssetEducationalFields());
            JSONArray m_jArry = obj.getJSONArray("educational_area");

            for (int i = 0; i < m_jArry.length(); i++) {

                JSONObject jo_inside = m_jArry.getJSONObject(i);

                String code = jo_inside.getString("id");
                String educationField = jo_inside.getString("display_value");

                educationFieldNameList.add(educationField);
                educationFieldCodeList.add(code);
            }

            ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, educationFieldNameList);
            Spn_EducationField.setAdapter(countryAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //Loading Income JSON
    public String loadJSONFromAssetEducationalFields() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("educational_area.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


}