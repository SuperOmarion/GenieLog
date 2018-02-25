package com.tuto.superomarion.smartparking;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;


public class Connexion extends AppCompatActivity implements View.OnClickListener {

    private String Storage = "data";
    private Button btnConnect;
    private Button btnNew;
    private EditText name;
    private EditText pass;
    private CheckBox show;
    private Boolean logged = false;
    private String nom = "";
    private String passw = "";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        name = (EditText) findViewById(R.id.name);
        pass = (EditText) findViewById(R.id.password);
        show = (CheckBox) findViewById(R.id.show);
        btnConnect = (Button) findViewById(R.id.login);
        btnNew = (Button) findViewById(R.id.signup);

        btnConnect.setOnClickListener(this);
        btnNew.setOnClickListener(this);
        show.setOnClickListener(this);

        if(ConnexionInternet.isConnectedInternet(Connexion.this)){
            readnConnect();
        }
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
                        finish();
                        System.exit(0);
                    }
                }).create().show();
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {

            case R.id.login:

                if(ConnexionInternet.isConnectedInternet(Connexion.this)) {
                    nom = name.getText().toString().trim().toLowerCase();
                    passw = pass.getText().toString().trim();
                    if (validate()) {
                        login();
                    }
                }
                else {
                    Toast.makeText(getBaseContext(), "Verifiez votre connexion internet", Toast.LENGTH_LONG).show();
                }
                break;


            case R.id.signup:
                Intent myIn = new Intent(this,Inscription.class);
                startActivity(myIn);
                break;

            case R.id.show:
                if(show.isChecked()){
                    pass.setInputType(InputType.TYPE_CLASS_TEXT);

                }else{
                    pass.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;

        }

    }
    public void readnConnect(){
        try{
            File file = new File(getCacheDir(),Storage);
            FileInputStream fin = new FileInputStream(file);
            InputStreamReader inputStream = new InputStreamReader(fin);
            BufferedReader bufferReader = new BufferedReader(inputStream);
            StringBuilder sbuilder = new StringBuilder();
            String line = null;
            while((line = bufferReader.readLine()) != null ){
                sbuilder.append(line);
            }
            fin.close();
            inputStream.close();
            if(sbuilder.toString().length()>8){
                String[] Contact = sbuilder.toString().split("/");
                nom = Contact[0];
                passw = Contact[1];

                name.setText(nom);
                pass.setText(passw);
                login();
            }
            //Toast.makeText(Connexion.this,nom+"   " + passw,Toast.LENGTH_LONG).show();
        }catch (java.io.IOException e){
            e.printStackTrace();

        }
    }
    public void saveContact() {
        try {
            File file = new File(getCacheDir(), Storage);
            FileOutputStream fos = new FileOutputStream(file);
            String Contact = nom.trim()+"/"+passw.trim();
            fos.write(Contact.getBytes());
            fos.close();
            //Toast.makeText(Connexion.this,Contact,Toast.LENGTH_LONG).show();
        } catch (java.io.IOException e) {
            e.printStackTrace();
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

    public void login() {

        progressDialog = new ProgressDialog(Connexion.this,
                R.style.MyTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(3);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.setMessage("Authentication...");
        progressDialog.show();

        final Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {

                        progressDialog.dismiss();
                        logged = true;
                        saveContact();
                        //Toast.makeText(getBaseContext(), "Verifiez  " + success , Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Connexion.this, Dashboard.class);
                        intent.putExtra("user", nom);
                        startActivity(intent);
                        finish();

                    } else {
                        deleteContact();
                        pass.setText("");
                        AlertDialog.Builder builder = new AlertDialog.Builder(Connexion.this);
                        builder.setMessage("Le compte n'existe pas\nVotre Nom ou Mot de passe est incorrecte")
                                .setNegativeButton("Retry", null)
                                .create()
                                .show();
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        ConnectionRequest connectionRequest = new ConnectionRequest(nom,passw,responseListener);
        final RequestQueue queue = Volley.newRequestQueue(Connexion.this);
        queue.add(connectionRequest);
       // Toast.makeText(getBaseContext(), "Verifiez  " + nom +""+ passw , Toast.LENGTH_LONG).show();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        progressDialog.dismiss();
                        queue.stop();
                        if (logged == false){
                            Toast.makeText(Connexion.this, "Verifiez votre connexion et réessayez", Toast.LENGTH_LONG).show();
                        }
                        logged = false;


                    }
                }, 15000);


    }

    public boolean validate() {
        boolean valid = true;

        String nom = name.getText().toString();
        String password = pass.getText().toString();

        if (nom.isEmpty()) {
            name.setError("Entrez votre nom s'il vous plait");
            valid = false;
        } else {
            name.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 21) {
            pass.setError("Entrez un mot de passe correct entre 5 et 20 caractères");
            valid = false;
        } else {
            pass.setError(null);
        }

        return valid;
    }

}

