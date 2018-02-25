package com.tuto.superomarion.smartparking;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ParkingRequest extends StringRequest {
    private static final String REQUEST_URL = "http://s724508434.onlinehome.fr/Parking.php";
    private Map<String, String> params;

    public ParkingRequest(String signal, Response.Listener<String> listener) {
        super(Method.POST, REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("signal", signal);
    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
