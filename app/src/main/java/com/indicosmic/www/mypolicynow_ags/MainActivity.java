package com.indicosmic.www.mypolicynow_ags;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.indicosmic.www.mypolicynow_sdk.QuotationActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView BtnBuyInsurance = (TextView)findViewById(R.id.BtnBuyInsurance);
        BtnBuyInsurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), QuotationActivity.class);
                startActivity(i);
            }
        });




    }
}
