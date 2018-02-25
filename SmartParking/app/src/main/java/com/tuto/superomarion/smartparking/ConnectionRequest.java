package com.tuto.superomarion.smartparking;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Super.Omarion on 17/01/2018.
 */

public class ConnectionRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "https://aitslimane554.000webhostapp.com/Connect.php";
    private Map<String, String> params;

    public ConnectionRequest(String name, String password, Response.Listener<String> listener) {
        super(Request.Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("name", name);
        params.put("mot_de_passe", password);

    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
