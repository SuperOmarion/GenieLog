package com.tuto.superomarion.smartparking;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
    private static String old;
    private static String phone;
    private static String passw;
    private static Boolean res;

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


        /*final ProgressDialog progressDialog = new ProgressDialog(Inscription.this,R.style.MyTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Création du compte...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        progressDialog.show();


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
                }, 15000);*/
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

