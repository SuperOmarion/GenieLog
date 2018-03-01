package com.tuto.superomarion.smartparking;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MesReservations extends AppCompatActivity {

    private ListView listv;
    private ProgressDialog progressDialog;
    private boolean get = false;
    private String userid;
    ArrayList<HashMap<String, String>> reservationList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes_reservations);
        userid = getIntent().getExtras().getString("iduser");
        reservationList = new ArrayList<>();
       // Toast.makeText(MesReservations.this,userid,Toast.LENGTH_LONG).show();
        listv = (ListView) findViewById(R.id.list);
        getPlace();
    }


    public void getJson(JSONObject jsonObj){
        try {

            JSONArray reservations = jsonObj.getJSONArray("reservations");
            if(reservations.length() == 0){
                new AlertDialog.Builder(this)
                        .setTitle("Oups")
                        .setMessage("Aucune réservation")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent dash = new Intent(MesReservations.this,Dashboard.class);
                                startActivity(dash);

                            }
                        }).create().show();

            }else{

                for (int i = 0; i < reservations.length(); i++) {
                    JSONObject c = reservations.getJSONObject(i);

                    String id = c.getString("id");
                    String place= c.getString("place");
                    String parking = c.getString("parking");
                    String heure_debut = c.getString("heure_debut");
                    String heure_fin = c.getString("heure_fin");


                    HashMap<String, String> reservation = new HashMap<>();

                    // adding each child node to HashMap key => value
                    reservation.put("id", id);
                    reservation.put("place", place);
                    reservation.put("parking", parking);
                    reservation.put("heure_debut", heure_debut);
                    reservation.put("heure_fin", heure_fin);


                    // adding contact to contact list
                    reservationList.add(reservation);
                }

                ListAdapter adapter = new SimpleAdapter(
                        MesReservations.this, reservationList,
                        R.layout.list_mesplaces, new String[]{"place",
                        "parking","heure_debut","heure_fin"}, new int[]{R.id.place,
                        R.id.parking,R.id.heureD,R.id.heureF});
               // Toast.makeText(MesReservations.this,adapter.getItem(1).toString(),Toast.LENGTH_LONG).show();

                listv.setAdapter(adapter);
            }

        } catch (final JSONException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MesReservations.this);

            builder.setMessage("Aucune réservation").setTitle("Oups")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent dash = new Intent(MesReservations.this,Dashboard.class);
                            startActivity(dash);

                        }
                    }).create().show();

        }
    }
    public void getPlace() {
        //Toast.makeText(Dashboard.this,"efsfdfdfd",Toast.LENGTH_LONG).show();

        progressDialog = new ProgressDialog(MesReservations.this,R.style.MyTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Recherche...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.show();
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);

                    if (jsonResponse != null) {
                        getJson(jsonResponse);
                        progressDialog.dismiss();
                        get = true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        MesReservationsRequest mesReservationsRequest = new MesReservationsRequest(userid,responseListener);
        final RequestQueue queue = Volley.newRequestQueue(MesReservations.this);
        queue.add(mesReservationsRequest);
        // Toast.makeText(Reservation.this,"queu",Toast.LENGTH_LONG).show();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                        queue.stop();
                        if (!get){
                            new AlertDialog.Builder(MesReservations.this)
                                    .setTitle("Oups")
                                    .setMessage("Probleme de connexion Internet")
                                    .setNegativeButton("Retour",new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface arg0, int arg1) {
                                            Intent dash = new Intent(MesReservations.this,Dashboard.class);
                                            startActivity(dash);

                                        }})
                                    .setPositiveButton("Réessayer", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface arg0, int arg1) {
                                            getPlace();

                                        }
                                    }).create().show();
                        }


                    }
                }, 5000);

    }
}
