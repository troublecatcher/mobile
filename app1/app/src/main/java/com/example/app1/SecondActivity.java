package com.example.app1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent i = getIntent();

        TextView twName = findViewById(R.id.twName);
        TextView twAge = findViewById(R.id.twAge);
        TextView twSex = findViewById(R.id.twSex);
        twName.setText(i.getStringExtra("iName"));
        twAge.setText(i.getStringExtra("iAge"));
        twSex.setText(i.getStringExtra("iSex"));
    }
    public void changeBg(View view){
        LinearLayout linLay = (LinearLayout) findViewById(R.id.sLay);
        linLay.setBackgroundResource(R.drawable.img);
    }
    public void go(View view){
        Intent i = new Intent(SecondActivity.this, ThirdActivity.class);
        startActivity(i);
    }
}