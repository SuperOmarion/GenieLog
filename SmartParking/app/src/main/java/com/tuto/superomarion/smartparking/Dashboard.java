package com.tuto.superomarion.smartparking;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class Dashboard extends AppCompatActivity implements View.OnClickListener{
    private TextView name;
    private EditText paking;
    private ImageView logout;
    private String Storage = "data";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        name = (TextView) findViewById(R.id.user);
        paking = (EditText) findViewById(R.id.park);
        logout = (ImageView) findViewById(R.id.disconnect);
        logout.setOnClickListener(this);
        name.setText("Bienvenu " + getIntent().getExtras().getString("user"));
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
                                Toast.makeText(Dashboard.this,"deconnexion",Toast.LENGTH_LONG).show();
                                deleteContact();
                                Intent connect = new Intent(Dashboard.this,Connexion.class);
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
}
