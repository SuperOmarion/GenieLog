package com.tuto.superomarion.smartparking;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
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
    private String id_park = "1";
    private ListView listv;
    private ProgressDialog progressDialog;
    private boolean get = false;
    private String userid;
    ArrayList<HashMap<String, String>> placeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        userid = getIntent().getExtras().getString("iduser");
        Toast.makeText(Reservation.this,"user = " + userid,Toast.LENGTH_LONG).show();
        id_park = getIntent().getExtras().getString("id");
        placeList = new ArrayList<>();
        listv = (ListView) findViewById(R.id.list);
        getPlace();
        listv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] item =  parent.getAdapter().getItem(position).toString().split(",");
                String[] selected = item[item.length - 1].split("=");
                String placeId = selected[1].replace("}","");

                Toast.makeText(Reservation.this,position + " " + id + "  " + placeId,Toast.LENGTH_LONG).show();
                Intent place = new Intent(Reservation.this, Validation.class);
                place.putExtra("userid", userid);
                place.putExtra("parkid", id_park);
                place.putExtra("placeid", placeId);
                startActivity(place);

            }

        });
    }

    @Override
    public void onBackPressed() {
        Intent I = new Intent(Reservation.this,Dashboard.class);
        startActivity(I);
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

                            }
                        }).create().show();

            }else{

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

        progressDialog = new ProgressDialog(Reservation.this,R.style.MyTheme);
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

        PlaceRequest placeRequest = new PlaceRequest(id_park,responseListener);
        final RequestQueue queue = Volley.newRequestQueue(Reservation.this);
        queue.add(placeRequest);
        Toast.makeText(Reservation.this,"queu",Toast.LENGTH_LONG).show();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        progressDialog.dismiss();
                        queue.stop();
                        if (!get){
                            new AlertDialog.Builder(Reservation.this)
                                    .setTitle("Oups")
                                    .setMessage("Probleme de connexion Internet")
                                    .setNegativeButton("Retour",new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface arg0, int arg1) {
                                            Intent dash = new Intent(Reservation.this,Dashboard.class);
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
