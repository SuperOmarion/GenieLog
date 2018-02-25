package com.tuto.superomarion.smartparking;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Dashboard extends AppCompatActivity {
    private TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        name = (TextView) findViewById(R.id.user);
        name.setText(getIntent().getExtras().getString("user"));
    }
}
