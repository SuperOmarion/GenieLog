package com.tuto.superomarion.smartparking;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PlaceRequest extends StringRequest {
    private static final String REQUEST_URL = "http://s724508434.onlinehome.fr/Place.php";
    private Map<String, String> params;

    public PlaceRequest(String id, Response.Listener<String> listener) {
        super(Method.POST, REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("id_parking", id);
    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
