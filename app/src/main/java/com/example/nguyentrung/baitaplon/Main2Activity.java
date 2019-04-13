package com.example.nguyentrung.baitaplon;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Main2Activity extends AppCompatActivity {
    String tenthanhpho = "";
    ImageView imgback;
    TextView tvtentp;
    ListView listv;
    CustomAdapter customAdapter;
    ArrayList<Thoitiet> mangthoitiet;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main2 );
        Anhxa();
        Intent intent = getIntent();
        String city = intent.getStringExtra("name");
//        Log.d( "ketqua","Du lieu truyen qua:"+ city );
        if (city.equals( "" )){
            tenthanhpho = "Hanoi";
            get7DayData(tenthanhpho  );
        }else{
            tenthanhpho = city;
            get7DayData( tenthanhpho );
        }
        imgback.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        } );
    }

    @SuppressLint("WrongViewCast")
    private void Anhxa() {
        imgback = findViewById( R.id.imgback );
        tvtentp = findViewById( R.id.tvtentp );
        listv = findViewById( R.id.listv );
        mangthoitiet = new ArrayList<Thoitiet>();
        customAdapter = new CustomAdapter( mangthoitiet, Main2Activity.this );
        listv.setAdapter( customAdapter );
    }

    private void get7DayData( String data) {
        String url ="https://api.openweathermap.org/data/2.5/forecast?q="+data+"&units=metric&appid=1be7bbc3dc42b63e0781dfd02a508ee9";
        RequestQueue requestQueue = Volley.newRequestQueue(Main2Activity.this );
        StringRequest stringRequest = new StringRequest( Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
//                    JSONObject object = new JSONObject(response);
//                    JSONObject jsonObject = object.getJSONArray("list").getJSONObject(0);
                    JSONObject jsonObject = new JSONObject( response );
                    JSONObject jsonObjectCity = jsonObject.getJSONObject( "city" );
                    String name = jsonObjectCity.getString( "name" );
                    tvtentp.setText("Thành phố:"+""+ name );

                    JSONArray jsonArrayList = jsonObject.getJSONArray( "list" );
                    for (int i =0;i<jsonArrayList.length();i++){
                        JSONObject jsonObjectList =  jsonArrayList.getJSONObject(i);
                        String ngay = jsonObjectList.getString( "dt" );


                        long lDay = Long.valueOf(ngay);
                        Date date = new Date(lDay * 1000L);
                        SimpleDateFormat sdf = new SimpleDateFormat("E, dd MMM yyyy HH:mm");
                        String strDay = sdf.format(date);


                        JSONObject jsonObjectTemp =jsonObjectList.getJSONObject( "main" );
                        String max = jsonObjectTemp.getString( "temp_max" );
                        String min = jsonObjectTemp.getString( "temp_min" );

                        Double a = Double.valueOf(max);
                        String nhietDoMax = String.valueOf(a.intValue());
                        Double b = Double.valueOf(min);
                        String nhietDoMin = String.valueOf(b.intValue());

                        JSONArray jsonArrayWeather = jsonObjectList.getJSONArray( "weather" );
                        JSONObject jsonObjectWeather= jsonArrayWeather.getJSONObject( 0 );
                        String Status = jsonObjectWeather.getString( "description" );
                        String icon = jsonObjectWeather.getString( "icon" );

                        mangthoitiet.add( new Thoitiet(strDay,Status,icon,nhietDoMax,nhietDoMin ) );


                    }
                    customAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                } );
        requestQueue.add( stringRequest );
    }



}
