package com.unal.reto10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Spinner ips_type_spinner;
    private ArrayList<String> ips_type;
    private ArrayAdapter<String> ips_adapter;
    private ListView list;
    private ArrayList<String> peajes;
    private ArrayAdapter<String> cod_adapter;
    private Context context = this;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ips_type = new ArrayList<>();
        peajes = new ArrayList<>();
        queue = Volley.newRequestQueue(this);
        String url = "https://www.datos.gov.co/resource/8bjd-2rkk.json";
        ips_type_spinner = (Spinner) findViewById(R.id.ips_adapter);
        list = findViewById(R.id.list);
        JsonArrayRequest departamentos = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject tmp = null;
                            try {
                                tmp = response.getJSONObject(i);
                                if (!ips_type.contains(tmp.getString("najunombre"))) {
                                    ips_type.add(tmp.getString("najunombre"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        ips_adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, ips_type);
                        ips_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        ips_type_spinner.setAdapter(ips_adapter);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });


        ips_type_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                peajes.clear();
                final String tmp = (String) parent.getItemAtPosition(pos);
                String url = "https://www.datos.gov.co/resource/8bjd-2rkk.json?najunombre=" + tmp;
                JsonArrayRequest codes = new JsonArrayRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject tmp = null;
                                    try {
                                        tmp = response.getJSONObject(i);
                                        String tmp2 = "Id Departament: " + tmp.getString("coddepartamento") + "\n";
                                        tmp2 += "Departamento: " + tmp.getString("departamento") + "\n";
                                        tmp2 += "Municipio: " + tmp.getString("muninombre") + "\n";
                                        tmp2 += "IPS: " + tmp.getString("nombre") + "\n";
                                        tmp2 += "NIT: " + tmp.getString("nitsnit") + "\n";
                                        tmp2 += "Dirección: " + tmp.getString("direccion") + "\n";
                                        tmp2 += "Mail: " + tmp.getString("email") + "\n";
                                        tmp2 += "Tipo IPS: " + tmp.getString("najunombre") + "\n";
                                        tmp2 += "Fecha de corte: " + tmp.getString("fecha_corte_reps") + "\n";
                                        peajes.add(tmp2);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                cod_adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, peajes);
                                list.setAdapter(cod_adapter);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("REQ", "bad");
                            }
                        });
                queue.add(codes);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        queue.add(departamentos);

    }
}
