package com.simpus.crmkinarya;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.simpus.crmkinarya.objects.SpinnerItem;
import com.simpus.crmkinarya.adapters.SpinnerAdapter;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    Spinner provinsiView, kabupatenView, kecamatanView, instansiView, karyawanView;
    ArrayList<SpinnerItem> listProvinsi,listKabupaten,listKecamatan,listInstansi,listKaryawan;
    private android.widget.SpinnerAdapter adapterProvinsi, adapterKabupaten,adapterKecamatan, adapterInstansi,adapterKaryawan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        karyawanView = (Spinner) findViewById(R.id.spinnerkaryawan);
        provinsiView = (Spinner) findViewById(R.id.spinnerProvinsi);
        kabupatenView = (Spinner) findViewById(R.id.spinnerKabupaten);
        kecamatanView = (Spinner) findViewById(R.id.spinnerKecamatan);
        instansiView = (Spinner) findViewById(R.id.spinnerInstansi);

        getkaryawan();
        getprovinsi();


    }

    public void getkaryawan() {
            final JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                    "http://apitest.kinaryatama.id/crm/list_karyawan.php", new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    Log.d("DEBUGS", response.toString());
                    try {
                        listKaryawan = new ArrayList<>();
                        for(int i=0;i<response.length();i++){
                            JSONObject object = response.getJSONObject(i);
                            SpinnerItem item = new SpinnerItem(object.getString("id"),object.getString("nama"));
                            listKaryawan.add(item);
                        }
                        adapterKaryawan = new SpinnerAdapter(MainActivity.this,
                                android.R.layout.simple_spinner_item,
                                listKaryawan);

                        karyawanView.setAdapter(adapterKaryawan); // Set the custom adapter to the spinner
                        // You can create an anonymous listener to handle the event when is selected an spinner item
                        karyawanView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view,
                                                       int position, long id) {
                                // Here you get the current item (a User object) that is selected by its position
                                SpinnerItem item = (SpinnerItem) adapterKaryawan.getItem(position);
                                // Here you can do the action you want to...

                                Log.d("DEBUGS ITEM ", item.getId());
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterKaryawan) {  }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("DEBUGS", "Error: " + error.getMessage());
                }
            }) {
                @Override
                protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                    try {
                        Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                        if (cacheEntry == null) {
                            cacheEntry = new Cache.Entry();
                        }
                        final long cacheHitButRefreshed = 0 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                        final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                        long now = System.currentTimeMillis();
                        final long softExpire = now + cacheHitButRefreshed;
                        final long ttl = now + cacheExpired;
                        cacheEntry.data = response.data;
                        cacheEntry.softTtl = softExpire;
                        cacheEntry.ttl = ttl;
                        String headerValue;
                        headerValue = response.headers.get("Date");
                        if (headerValue != null) {
                            cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                        }
                        headerValue = response.headers.get("Last-Modified");
                        if (headerValue != null) {
                            cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                        }
                        cacheEntry.responseHeaders = response.headers;
                        final String jsonString = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers));
                        return Response.success(new JSONArray(jsonString), cacheEntry);
                    } catch (UnsupportedEncodingException | JSONException e) {
                        return Response.error(new ParseError(e));
                    }
                }

                @Override
                protected void deliverResponse(JSONArray response) {
                    super.deliverResponse(response);
                }

                @Override
                public void deliverError(VolleyError error) {
                    super.deliverError(error);
                }

                @Override
                protected VolleyError parseNetworkError(VolleyError volleyError) {
                    return super.parseNetworkError(volleyError);
                }
            };

// Adding request to request queue
            AppController.getInstance().addToRequestQueue(jsonObjReq);
        }
    public void getprovinsi() {
        final JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                "http://apitest.kinaryatama.id/crm/list_provinsi.php", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("DEBUGS", response.toString());
                try {
                    listProvinsi = new ArrayList<>();
                    for(int i=0;i<response.length();i++){
                        JSONObject object = response.getJSONObject(i);
                        SpinnerItem item = new SpinnerItem(object.getString("id"),object.getString("name"));
                        listProvinsi.add(item);
                    }
                    adapterProvinsi = new SpinnerAdapter(MainActivity.this,
                            android.R.layout.simple_spinner_item,
                            listProvinsi);

                    provinsiView.setAdapter(adapterProvinsi); // Set the custom adapter to the spinner
                    // You can create an anonymous listener to handle the event when is selected an spinner item
                    provinsiView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view,
                                                   int position, long id) {
                            // Here you get the current item (a User object) that is selected by its position
                            SpinnerItem item = (SpinnerItem) adapterProvinsi.getItem(position);
                            // Here you can do the action you want to...

                            Log.d("DEBUGS ITEM  ", item.getId());
                            getKabupaten(item.getId());
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> adapterKaryawan) {  }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("DEBUGS", "Error: " + error.getMessage());
            }
        }) {
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 0 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new JSONArray(jsonString), cacheEntry);
                } catch (UnsupportedEncodingException | JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(JSONArray response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }
        };

// Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }
    public void getKabupaten(String kodeprovinsi){
        final String id_provinsi = kodeprovinsi;
        Log.d("DEBUGS kab", id_provinsi);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "http://apitest.kinaryatama.id/crm/list_kabupatenpost.php",
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        Log.d("CREATE", response);
                        try {
                            listKabupaten = new ArrayList<>();
                            JSONArray data = new JSONArray(response);
                            for(int i=0;i<data.length();i++){
                                JSONObject object = data.getJSONObject(i);
                                SpinnerItem item = new SpinnerItem(object.getString("id"),object.getString("name"));
                                listKabupaten.add(item);
                            }
                            adapterKabupaten = new SpinnerAdapter(MainActivity.this,
                                    android.R.layout.simple_spinner_item,
                                    listKabupaten);

                            kabupatenView.setAdapter(adapterKabupaten); // Set the custom adapter to the spinner
                            // You can create an anonymous listener to handle the event when is selected an spinner item
                            kabupatenView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view,
                                                           int position, long id) {
                                    // Here you get the current item (a User object) that is selected by its position
                                    SpinnerItem user = (SpinnerItem) adapterKabupaten.getItem(position);
                                    getKecamatan(user.getId());
                                    getInstansi(user.getId());
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> adapterKabupaten) {  }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.d("CREATE", error.toString());
                        Toast.makeText(MainActivity.this,"Sedang Mengambil Data, Pastikan Perangkat Sudah Tersambung Internet",Toast.LENGTH_LONG).show();
                    }
                }){


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //menambahkan parameter post, nama put sama dengan nama variable pada webservice PHP
                params.put("id_provinsi", id_provinsi);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    public void getKecamatan(String kodekabupaten){
        final String id_kabupaten = kodekabupaten;
        Log.d("DEBUGS kec", id_kabupaten);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "http://apitest.kinaryatama.id/crm/list_kecamatan.php",
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        Log.d("CREATE", response);
                        try {
                            listKecamatan = new ArrayList<>();
                            JSONArray data = new JSONArray(response);
                            for(int i=0;i<data.length();i++){
                                JSONObject object = data.getJSONObject(i);
                                SpinnerItem item = new SpinnerItem(object.getString("id"),object.getString("name"));
                                listKecamatan.add(item);
                            }
                            adapterKecamatan = new SpinnerAdapter(MainActivity.this,
                                    android.R.layout.simple_spinner_item,
                                    listKecamatan);

                            kecamatanView.setAdapter(adapterKecamatan); // Set the custom adapter to the spinner
                            // You can create an anonymous listener to handle the event when is selected an spinner item
                            kecamatanView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view,
                                                           int position, long id) {
                                    // Here you get the current item (a User object) that is selected by its position
                                    SpinnerItem user = (SpinnerItem) adapterKecamatan.getItem(position);

                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> adapterKecamatan) {  }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.d("CREATE", error.toString());
                        Toast.makeText(MainActivity.this,"Sedang Mengambil Data, Pastikan Perangkat Sudah Tersambung Internet",Toast.LENGTH_LONG).show();
                    }
                }){


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //menambahkan parameter post, nama put sama dengan nama variable pada webservice PHP
                params.put("id_kabupaten", id_kabupaten);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    public void getInstansi(String kodekabupaten){
        final String id_kabupaten = kodekabupaten;
        Log.d("DEBUGS instansi", id_kabupaten);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "http://apitest.kinaryatama.id/crm/list_instansi.php",
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        Log.d("CREATE", response);
                        try {
                            listInstansi = new ArrayList<>();
                            JSONArray data = new JSONArray(response);
                            for(int i=0;i<data.length();i++){
                                JSONObject object = data.getJSONObject(i);
                                SpinnerItem item = new SpinnerItem(object.getString("id"),object.getString("nama"));
                                listInstansi.add(item);
                            }
                            adapterInstansi = new SpinnerAdapter(MainActivity.this,
                                    android.R.layout.simple_spinner_item,
                                    listInstansi);

                            instansiView.setAdapter(adapterInstansi); // Set the custom adapter to the spinner
                            // You can create an anonymous listener to handle the event when is selected an spinner item
                            instansiView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view,
                                                           int position, long id) {
                                    // Here you get the current item (a User object) that is selected by its position
                                    SpinnerItem user = (SpinnerItem) adapterInstansi.getItem(position);
                                    //getKecamatan(user.getId());
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> adapterInstansi) {  }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.d("CREATE", error.toString());
                        Toast.makeText(MainActivity.this,"Sedang Mengambil Data, Pastikan Perangkat Sudah Tersambung Internet",Toast.LENGTH_LONG).show();
                    }
                }){


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //menambahkan parameter post, nama put sama dengan nama variable pada webservice PHP
                params.put("id_kabupaten", id_kabupaten);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }
    }

