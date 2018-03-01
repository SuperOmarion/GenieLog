package com.tuto.superomarion.smartparking;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MesReservationsRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://s724508434.onlinehome.fr/Reservation.php";
    private Map<String, String> params;

    public MesReservationsRequest(String id, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("id_user", id);


    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
