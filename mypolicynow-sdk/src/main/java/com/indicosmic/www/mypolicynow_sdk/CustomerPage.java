package com.indicosmic.www.mypolicynow_sdk;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.indicosmic.www.mypolicynow_sdk.adapter.StepperAdapter;
import com.stepstone.stepper.StepperLayout;

public class CustomerPage extends AppCompatActivity {

    StepperLayout mStepperLayout;
    String StrEmployeeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_page);

        mStepperLayout = (StepperLayout) findViewById(R.id.stepperLayout);
        mStepperLayout.setAdapter(new StepperAdapter(getSupportFragmentManager(), this));


    }

}