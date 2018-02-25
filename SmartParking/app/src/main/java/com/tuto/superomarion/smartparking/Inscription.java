package com.tuto.superomarion.smartparking;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;

public class Inscription extends AppCompatActivity implements View.OnClickListener {
    private String Storage = "data";
    private Button btnSign;
    private EditText nom;
    private EditText pass;
    private EditText repass;
    private EditText tel;
    private Button connect;

    private Boolean logged = false;

    private static String iden;
    private static String phone;
    private static String passw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        btnSign = (Button) findViewById(R.id.signup);
        nom = (EditText) findViewById(R.id.name);
        pass = (EditText) findViewById(R.id.password);
        repass = (EditText) findViewById(R.id.passwordMatch);
        tel = (EditText) findViewById(R.id.tel);
        connect = (Button) findViewById(R.id.login);
        btnSign.setOnClickListener(this);
        connect.setOnClickListener(this);
    }


    public void saveContact() {
        try {
            File file = new File(getCacheDir(), Storage);
            FileOutputStream fos = new FileOutputStream(file);
            String Contact = nom.getText().toString().trim().toLowerCase()+"/"+pass.getText().toString().trim();
            fos.write(Contact.getBytes());
            fos.close();
            Toast.makeText(Inscription.this,Contact,Toast.LENGTH_LONG).show();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
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
        switch (v.getId()) {

            case R.id.login:

                Intent myIntent = new Intent(Inscription.this, Connexion.class);
                startActivityForResult(myIntent, 0);

                break;
            case R.id.signup:
                if (ConnexionInternet.isConnectedInternet(Inscription.this)) {
                    signup();
                } else {
                    Toast.makeText(this, "Verifiez votre connexion internet", Toast.LENGTH_LONG).show();
                }

                break;

        }
    }
    public void signup() {

        if (!validate()) {
            onSignupFailed();
            return;
        }


        onSignupSuccess();
        saveContact();
        iden = nom.getText().toString();
        phone = tel.getText().toString();
        passw = pass.getText().toString();

        final ProgressDialog progressDialog = new ProgressDialog(Inscription.this,R.style.MyTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Création du compte...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.show();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        onSignupSuccess();
                        progressDialog.dismiss();
                        logged = true;
                        /*Intent intent = new Intent(Inscription.this, Dashboard.class);
                        intent.putExtra("user", iden);
                        startActivity(intent);
                        finish();*/

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Inscription.this);
                        builder.setMessage("Un compte sur ce nom existe déja")
                                .setNegativeButton("Oh non!!", null)
                                .create()
                                .show();
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        RegisterRequest registerRequest = new RegisterRequest(iden, phone, passw , responseListener);
        final RequestQueue queue = Volley.newRequestQueue(Inscription.this);
        queue.add(registerRequest);


        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        progressDialog.dismiss();
                        //queue.stop();
                        if (logged == false){
                            Toast.makeText(Inscription.this, "Verifiez votre connexion et réessayez", Toast.LENGTH_LONG).show();
                        }
                        logged = false;

                    }
                }, 5000);
    }


    public void onSignupSuccess() {
        btnSign.setEnabled(false);
    }

    public void onSignupFailed() {


        btnSign.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = nom.getText().toString();
        String phone = tel.getText().toString();
        String passw = pass.getText().toString();
        String repassw = repass.getText().toString();


        if (name.isEmpty() || name.length() < 3) {
            nom.setError("Entrez votre nom s'il vous plait");
            valid = false;
        } else {
            nom.setError(null);
        }

        if (phone.isEmpty()) {
            tel.setError("Entrez votre numéro de télephone");
            valid = false;
        } else {

            tel.setError(null);
        }

        if (passw.equals("") || passw.length() < 5 || passw.length() > 21) {
            pass.setError("Entrez un mot de passe correct entre 5 et 20 caractères");
            valid = false;
        } else {
            pass.setError(null);
        }

        if (repassw.equals("") || !repassw.toString().equals(passw.toString())) {
            repass.setError("Vous mots de passe ne sont pas identiques");
            valid = false;
        } else {
            repass.setError(null);
        }

        return valid;
    }
}

