package com.tuto.superomarion.smartparking;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ReservationRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://s724508434.onlinehome.fr/Reservation.php";
    private Map<String, String> params;

    public ReservationRequest(String id_user,String id_parking,String id_place,String heureD,String heureF, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("id_user", id_user);
        params.put("id_place", id_place);
        params.put("id_parking", id_parking);
        params.put("heure_debut", heureD);
        params.put("heure_fin", heureF);

    }


    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
