package com.indicosmic.www.mypolicynow_ags.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.indicosmic.www.mypolicynow_ags.R;
import com.indicosmic.www.mypolicynow_ags.activities.MainActivity_1;
import com.indicosmic.www.mypolicynow_ags.utils.UtilitySharedPreferences;

public class AboutUsActivity extends AppCompatActivity {
    ImageView back_btn_toolbar;
    TextView til_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        init();
    }

    private void init() {
        ImageView back_btn_toolbar = (ImageView)findViewById(R.id.back_btn_toolbar);
        back_btn_toolbar.setVisibility(View.VISIBLE);
        TextView til_text = (TextView)findViewById(R.id.til_text);

        back_btn_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        til_text.setText("About us");



    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity_1.class);
        intent.putExtra("terminal_id", UtilitySharedPreferences.getPrefs(getApplicationContext(),"TerminalId"));
        intent.putExtra("merchant_id",UtilitySharedPreferences.getPrefs(getApplicationContext(),"MerchantId"));
        startActivity(intent);
        overridePendingTransition(R.animator.left_right,R.animator.right_left);
        finish();
    }
}
