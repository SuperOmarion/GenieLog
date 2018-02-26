package com.tuto.superomarion.smartparking;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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

public class Gestionnaire extends AppCompatActivity implements View.OnClickListener {

    private TextView name;
    private ImageView disconnect;
    private String Storage = "data";
    private ListView listv;
    ArrayList<HashMap<String, String>> parkingList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestionnaire);
        disconnect = (ImageView) findViewById(R.id.disconnect);
        disconnect.setOnClickListener(this);
        name = (TextView) findViewById(R.id.user);
        name.setText("Bienvenu " + getIntent().getExtras().getString("user"));
        parkingList = new ArrayList<>();

        listv = (ListView) findViewById(R.id.list);

        listv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] item =  parent.getAdapter().getItem(position).toString().split(",");
                String[] selected = item[2].split("=");
                String parkId = selected[1];

                Toast.makeText(Gestionnaire.this,position + " " + id + "  " + parkId,Toast.LENGTH_LONG).show();
                Intent place = new Intent(Gestionnaire.this, Reservation.class);
                place.putExtra("id", parkId);
                startActivity(place);

            }

        });
        getParking();

    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Quiter l'application?")
                .setMessage("Êtes vous sûr de vouloir quitter l'application?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        moveTaskToBack(true);
                        finish();
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
                                Toast.makeText(Gestionnaire.this,"deconnexion",Toast.LENGTH_LONG).show();
                                deleteContact();
                                Intent connect = new Intent(Gestionnaire.this,Connexion.class);
                                startActivity(connect);
                                finish();

                            }
                        }).create().show();

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
            for (int i = 0; i < parkings.length(); i++) {
                JSONObject c = parkings.getJSONObject(i);

                String id = c.getString("id");
                String name = c.getString("nom");



                HashMap<String, String> parking = new HashMap<>();

                // adding each child node to HashMap key => value
                parking.put("id", id);
                parking.put("nom", name);

                // adding contact to contact list
                parkingList.add(parking);
            }

            ListAdapter adapter = new SimpleAdapter(
                    Gestionnaire.this, parkingList,
                    R.layout.list_item_plus, new String[]{"nom",
                    "edit","delete"}, new int[]{R.id.name,
                    R.id.edit, R.id.delete});

            listv.setAdapter(adapter);
        } catch (final JSONException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Json parsing error: " + e.getMessage(),
                            Toast.LENGTH_LONG)
                            .show();

                }
            });

        }
    }
    public void getParking() {
        //Toast.makeText(Dashboard.this,"efsfdfdfd",Toast.LENGTH_LONG).show();

        final ProgressDialog progressDialog = new ProgressDialog(Gestionnaire.this,R.style.MyTheme);
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
                        //Toast.makeText(Dashboard.this,"json",Toast.LENGTH_LONG).show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Gestionnaire.this);
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
        final RequestQueue queue = Volley.newRequestQueue(Gestionnaire.this);
        queue.add(parkingRequest);
        Toast.makeText(Gestionnaire.this,"queu",Toast.LENGTH_LONG).show();

    }
}
