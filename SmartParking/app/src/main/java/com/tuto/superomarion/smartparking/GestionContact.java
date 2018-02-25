package com.tuto.superomarion.smartparking;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

/**
 * Created by Super.Omarion on 25/02/2018.
 */

public class GestionContact {

    public String readnConnect(File file){
        try{
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
            return sbuilder.toString();
        }catch (java.io.IOException e){
            e.printStackTrace();

        }
        return null;
    }
    public void saveContact(File file,String name ,String pass) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            String Contact = name.trim()+"/"+pass.trim();
            fos.write(Contact.getBytes());
            fos.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteContact(File file) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write("".getBytes());
            fos.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }


}
