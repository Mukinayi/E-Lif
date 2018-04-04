package com.wolfsoft.one.cg.splashscreen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.wolfsoft.one.cg.R;

public class NoImei extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_imei);

        TextView tv = (TextView)findViewById(R.id.tvMess);
        Intent intent = getIntent();
        tv.setText(intent.getStringExtra("message"));
    }
}
