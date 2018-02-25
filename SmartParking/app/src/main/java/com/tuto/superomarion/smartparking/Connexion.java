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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
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
        readnConnect();
        if(nom != null && pass != null && ConnexionInternet.isConnectedInternet(Connexion.this)){
            login();
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
                    if (!validate()) {
                        onLoginFailed();

                    }else {
                        onLoginSuccess();
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
            String[] Contact = sbuilder.toString().split("/");
            nom = Contact[0];
            passw = Contact[1];
            //Toast.makeText(Connexion.this,nom+"   " + passw,Toast.LENGTH_LONG).show();
        }catch (java.io.IOException e){
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

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        progressDialog.dismiss();
                        //queue.stop();
                        if (logged == false){
                            Toast.makeText(Connexion.this, "Verifiez votre connexion et réessayez", Toast.LENGTH_LONG).show();
                        }
                        logged = false;


                    }
                }, 5000);


    }

    public void onLoginSuccess() {
        btnConnect.setEnabled(false);
    }

    public void onLoginFailed() {
        btnConnect.setEnabled(true);
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

