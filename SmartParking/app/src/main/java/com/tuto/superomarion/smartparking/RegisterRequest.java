package com.tuto.superomarion.smartparking;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://s724508434.onlinehome.fr/Register.php";
    private Map<String, String> params;

    public RegisterRequest(String name, String tel, String  password , Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("name", name);
        params.put("telephone", tel);
        params.put("mot_de_passe", password);

    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}