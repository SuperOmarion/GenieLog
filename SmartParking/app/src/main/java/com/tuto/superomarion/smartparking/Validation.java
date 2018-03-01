package com.tuto.superomarion.smartparking;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Validation extends AppCompatActivity implements View.OnClickListener{
    private TextView id;
    private EditText timestart;
    private EditText timeend;
    private Button garer;
    private Button reserver;
    private ProgressDialog progressDialog;
    private boolean gare = false;
    private boolean reserve = false;
    private String heureS = "";
    private String heureF = "";
    private String id_user;
    private String id_place;
    private String id_parking;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);
        id = (TextView) findViewById(R.id.idplace);
        id_user = getIntent().getExtras().getString("iduser");
        id_parking = getIntent().getExtras().getString("parkid");
        id_place = getIntent().getExtras().getString("placeid");
      //  Toast.makeText(Validation.this,"user = " + id_user,Toast.LENGTH_LONG).show();
        id.setText("Place : " + id_place + "  Parking : " + id_parking);
        timeend = (EditText) findViewById(R.id.timeend);
        timestart = (EditText) findViewById(R.id.timebegin);
        garer = (Button) findViewById(R.id.garer);
        reserver = (Button) findViewById(R.id.reserver);
        garer.setOnClickListener(this);
        reserver.setOnClickListener(this);
        timestart.setOnClickListener(this);
        timeend.setOnClickListener(this);


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.timebegin:
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(Validation.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                            heureS = selectedHour < 10 ? "0"+Integer.toString(selectedHour)+":" :  Integer.toString(selectedHour)+":";
                            heureS += selectedMinute < 10 ? "0"+Integer.toString(selectedMinute)+":00" :  Integer.toString(selectedMinute)+":00";
                            timestart.setText(heureS);

                        }
                    }, hour, minute, true);
                    mTimePicker.setTitle("Selectionnez l'heure");
                    mTimePicker.show();

                break;

            case R.id.timeend:
                    Calendar mcurrentTime1 = Calendar.getInstance();
                    int hour1 = mcurrentTime1.get(Calendar.HOUR_OF_DAY);
                    int minute1 = mcurrentTime1.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker1;
                    mTimePicker1 = new TimePickerDialog(Validation.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            heureF = selectedHour < 10 ? "0"+Integer.toString(selectedHour)+":" :  Integer.toString(selectedHour)+":";
                            heureF += selectedMinute < 10 ? "0"+Integer.toString(selectedMinute)+":00" :  Integer.toString(selectedMinute)+":00";
                            timeend.setText(heureF);
                        }
                    }, hour1, minute1, true);
                    mTimePicker1.setTitle("Selectionnez l'heure");


                    mTimePicker1.show();
                break;
            case R.id.garer:
                garer();
                break;

            case R.id.reserver:
                reserver();
                break;
        }
    }

    private void garer(){
        progressDialog = new ProgressDialog(Validation.this,
                R.style.MyTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(3);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.setMessage("Envoi de la requette ...");
        progressDialog.show();

        final Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {

                        progressDialog.dismiss();
                        gare = true;
                        if(jsonResponse.getString("operation").equals("garage")){
                            reponseGarage();
                        }

                    } else {

                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        GarerRequest garerRequest = new GarerRequest(id_user,id_parking,id_place,responseListener);
        final RequestQueue queue = Volley.newRequestQueue(Validation.this);
        queue.add(garerRequest);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        progressDialog.dismiss();
                        queue.stop();
                        if (gare == false){
                            Toast.makeText(Validation.this, "Verifiez votre connexion et réessayez", Toast.LENGTH_LONG).show();
                        }
                        gare = false;


                    }
                }, 15000);
    }
    private void reserver(){
        progressDialog = new ProgressDialog(Validation.this,
                R.style.MyTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(3);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.setMessage("Envoi de la requette ...");
        progressDialog.show();

        final Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {

                        progressDialog.dismiss();
                        reserve = true;

                        if(jsonResponse.getString("operation").equals("reservation")){
                            reponseReservation();
                        }

                    } else {

                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        ReservationRequest reservationRequest = new ReservationRequest(id_user,id_parking,id_place,heureS,heureF,responseListener);
        final RequestQueue queue = Volley.newRequestQueue(Validation.this);
        queue.add(reservationRequest);
        // Toast.makeText(getBaseContext(), "Verifiez  " + nom +""+ passw , Toast.LENGTH_LONG).show();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        progressDialog.dismiss();
                        queue.stop();
                        if (reserve == false){
                            Toast.makeText(Validation.this, "Verifiez votre connexion et réessayez", Toast.LENGTH_LONG).show();
                        }
                        reserve = false;


                    }
                }, 15000);
    }

    private void reponseGarage(){
        new AlertDialog.Builder(this)
                .setTitle("Garage")
                .setMessage("Votre operation à été pris en compte")
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent connect = new Intent(Validation.this,Dashboard.class);
                        startActivity(connect);
                    }
                }).create().show();
    }

    private void reponseReservation(){
        new AlertDialog.Builder(this)
                .setTitle("Résérvation")
                .setMessage("La place vous à été résérvée \n" + "Rendez vous à "+ heureS)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent connect = new Intent(Validation.this,Dashboard.class);
                        startActivity(connect);


                    }
                }).create().show();
    }

}
