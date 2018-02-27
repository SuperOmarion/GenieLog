package com.tuto.superomarion.smartparking;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Validation extends AppCompatActivity {
    private TextView id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);
        id = (TextView) findViewById(R.id.idplace);
        id.setText("Place : " + getIntent().getExtras().getString("parkid") + "   " + getIntent().getExtras().getString("placeid"));


    }
}
