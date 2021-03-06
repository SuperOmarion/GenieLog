package com.tuto.superomarion.smartparking;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class Dashboard extends AppCompatActivity implements View.OnClickListener{
    private TextView name;
    private EditText paking;
    private ImageView logout;
    private String Storage = "data";
    private ListView listv;
    private Button reservation;
    private ProgressDialog progressDialog;
    private boolean get = false;
    private String userid;
    ArrayList<HashMap<String, String>> parkingList;
    //private String jsonStr = "{\"parkings\":[{\"id\":\"1\",\"nom\":\"Parking UPMC\",\"ref\":\"0000001\",\"longitude\":\"48.2343545\",\"latitude\":\"2.64343323\",\"nbr_place\":\"100\",\"place_dispo\":\"56\"},{\"id\":\"2\",\"nom\":\"Parking LECLERC\",\"ref\":\"0000002\",\"longitude\":\"48.3455456\",\"latitude\":\"2.75454543\",\"nbr_place\":\"90\",\"place_dispo\":\"13\"},{\"id\":\"3\",\"nom\":\"Parking CARREFOUR\",\"ref\":\"0000003\",\"longitude\":\"48.4455456\",\"latitude\":\"2.65454543\",\"nbr_place\":\"94\",\"place_dispo\":\"23\"}]}";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        name = (TextView) findViewById(R.id.user);
        paking = (EditText) findViewById(R.id.park);
        logout = (ImageView) findViewById(R.id.disconnect);
        reservation = (Button) findViewById(R.id.reserveration);
        reservation.setOnClickListener(this);
        logout.setOnClickListener(this);
        userid = getIntent().getExtras().getString("iduser");
        name.setText("Bienvenue " + getIntent().getExtras().getString("user"));
       // Toast.makeText(Dashboard.this,"user = " + userid,Toast.LENGTH_LONG).show();

        parkingList = new ArrayList<>();

        listv = (ListView) findViewById(R.id.list);

        listv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] item =  parent.getAdapter().getItem(position).toString().split(",");
                String[] selected = item[2].split("=");
                String parkId = selected[1];
                Intent place = new Intent(Dashboard.this, Reservation.class);
                place.putExtra("iduser", userid);
                place.putExtra("id", parkId);
                startActivity(place);

            }

        });
        getParking();

    }


    @Override
    public void onBackPressed() {
        progressDialog.dismiss();
        new AlertDialog.Builder(this)
                .setTitle("Quiter l'application?")
                .setMessage("Êtes vous sûr de vouloir quitter l'application?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        moveTaskToBack(true);
                        System.exit(0);
                    }
                }).create().show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.disconnect :

                new AlertDialog.Builder(this)
                        .setTitle("Deconnexion?")
                        .setMessage("Vous êtes sur le point de vous déconnecter?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                               // Toast.makeText(Dashboard.this,"deconnexion",Toast.LENGTH_LONG).show();
                                deleteContact();
                                Intent connect = new Intent(Dashboard.this,Connexion.class);
                                startActivity(connect);


                            }
                        }).create().show();

                break;
            case R.id.reserveration:
                Intent I = new Intent(Dashboard.this, MesReservations.class);
                I.putExtra("iduser", userid);
                startActivity(I);
                break;
        }

    }



    public void deleteContact() {
        try {
            File file = new File(getCacheDir(), Storage);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write("".getBytes());
            fos.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }


    public void getJson(JSONObject jsonObj){
        try {
            //JSONObject jsonObj = new JSONObject(jsonStr);

            // Getting JSON Array node
            JSONArray parkings = jsonObj.getJSONArray("parkings");

            // looping through All Contacts
            if(parkings.length()>0) {
                for (int i = 0; i < parkings.length(); i++) {
                    JSONObject c = parkings.getJSONObject(i);

                    String id = c.getString("id");
                    String name = c.getString("nom");
                    String ref = c.getString("ref");
                    String place_dispo = c.getString("place_dispo");
                    String url = c.getString("url");


                    HashMap<String, String> parking = new HashMap<>();

                    // adding each child node to HashMap key => value
                    parking.put("id", id);
                    parking.put("nom", name);
                    parking.put("ref", ref);
                    parking.put("url", url);
                    parking.put("place_dispo", place_dispo);

                    // adding contact to contact list
                    parkingList.add(parking);
                }
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
                builder.setMessage("Vous n'avez aucune réservation")
                        .setNegativeButton("OK", null)
                        .create()
                        .show();

            }

            ListAdapter adapter = new SimpleAdapter(
                    Dashboard.this, parkingList,
                    R.layout.list_item, new String[]{"url","nom",
                    "place_dispo","ref"}, new int[]{R.id.img,R.id.name,
                    R.id.place, R.id.distance});

            listv.setAdapter(adapter);
        } catch (final JSONException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
            builder.setMessage("Vous n'avez aucune réservation")
                    .setNegativeButton("OK", null)
                    .create()
                    .show();


        }
    }
    public void getParking() {
        //Toast.makeText(Dashboard.this,"efsfdfdfd",Toast.LENGTH_LONG).show();

        progressDialog = new ProgressDialog(Dashboard.this,R.style.MyTheme);
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
                            get = true;
                            progressDialog.dismiss();
                            //Toast.makeText(Dashboard.this,"json",Toast.LENGTH_LONG).show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
                        builder.setMessage("Aucun parking dans la base")
                                .setNegativeButton("Oh non!!", null)
                                .create()
                                .show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        ParkingRequest parkingRequest = new ParkingRequest("1",responseListener);
        final RequestQueue queue = Volley.newRequestQueue(Dashboard.this);
        queue.add(parkingRequest);
       // Toast.makeText(Dashboard.this,"queu",Toast.LENGTH_LONG).show();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                        queue.stop();
                        if (!get){
                            new AlertDialog.Builder(Dashboard.this)
                                    .setTitle("Oups")
                                    .setMessage("Probleme de connexion Internet")
                                    .setNegativeButton("Retour",null)
                                    .setPositiveButton("Réessayer", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface arg0, int arg1) {
                                            getParking();

                                        }
                                    }).create().show();
                        }
                    }
                }, 5000);

    }
}
