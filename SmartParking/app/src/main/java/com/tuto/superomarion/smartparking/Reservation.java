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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Reservation extends AppCompatActivity  {
    private String id = "1";
    private ListView listv;

    ArrayList<HashMap<String, String>> placeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        id = getIntent().getExtras().getString("id");
        placeList = new ArrayList<>();
        listv = (ListView) findViewById(R.id.list);
        getPlace();
    }


    public void getJson(JSONObject jsonObj){
        try {
            //JSONObject jsonObj = new JSONObject(jsonStr);

            // Getting JSON Array node
            JSONArray places = jsonObj.getJSONArray("places");
            if(places.length() == 0){
                new AlertDialog.Builder(this)
                        .setTitle("Oups")
                        .setMessage("Aucune place disponible dans le parking")
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent dash = new Intent(Reservation.this,Dashboard.class);
                                startActivity(dash);
                                finish();

                            }
                        }).create().show();

            }else{
                // looping through All Contacts
                for (int i = 0; i < places.length(); i++) {
                    JSONObject c = places.getJSONObject(i);

                    String id = c.getString("id");
                    String name = c.getString("nom");
                    String tarif = c.getString("tarif");

                    HashMap<String, String> place = new HashMap<>();

                    // adding each child node to HashMap key => value
                    place.put("id", id);
                    place.put("nom", name);
                    place.put("tarif", tarif);

                    // adding contact to contact list
                    placeList.add(place);
                }

                ListAdapter adapter = new SimpleAdapter(
                        Reservation.this, placeList,
                        R.layout.list_item_place, new String[]{"nom",
                        "tarif"}, new int[]{R.id.name,
                        R.id.tarif});

                listv.setAdapter(adapter);
            }

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
    public void getPlace() {
        //Toast.makeText(Dashboard.this,"efsfdfdfd",Toast.LENGTH_LONG).show();

        final ProgressDialog progressDialog = new ProgressDialog(Reservation.this,R.style.MyTheme);
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
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Reservation.this);
                        builder.setMessage("Aucune place dans le parking")
                                .setNegativeButton("OK", null)
                                .create()
                                .show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        PlaceRequest placeRequest = new PlaceRequest(id,responseListener);
        final RequestQueue queue = Volley.newRequestQueue(Reservation.this);
        queue.add(placeRequest);
        Toast.makeText(Reservation.this,"queu",Toast.LENGTH_LONG).show();

    }


}
