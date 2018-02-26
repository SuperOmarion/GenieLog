package com.tuto.superomarion.smartparking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Reservation extends AppCompatActivity {
    private TextView id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        id = (TextView) findViewById(R.id.id);
        id.setText("ID parking : "+ getIntent().getExtras().getString("id"));

    }
}
