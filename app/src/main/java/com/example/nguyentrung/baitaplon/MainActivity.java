package com.example.nguyentrung.baitaplon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    EditText editsearch;
    TextView txtthanhpho, txtquocgia, txtnhietdo, txttrangthai, txtdoam,txtmay,txtgio,txtngay;
    Button btnsearch, btn;
    ImageView imageIcon;
    String City ="";
//    static final String DEFAUT_CITY = "Hanoi";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        init();

        getCurrentWeatherData( "Hanoi");
//        clickButton();
        btnsearch.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = editsearch.getText().toString();
                if (city.equals( "" )){
                    City= "Hanoi";
                    getCurrentWeatherData( City );
                }else{
                    City = city;
                    getCurrentWeatherData( City );
                }
                getCurrentWeatherData(city);
            }
        } );
        btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = editsearch.getText().toString();
                Intent intent = new Intent( MainActivity.this,Main2Activity.class );
                intent.putExtra( "name",city );
                startActivity( intent );

            }
        } );
    }


    private void getCurrentWeatherData( String data) {
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
            String url = "http://api.openweathermap.org/data/2.5/find?q="+data+"&units=metric&appid=5e54d540f8e20906502824c46252c4ec";
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                      Log.d("KetQua", response);
//
//                        // Lay du lieu tu json
                        processResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Loi", error.toString());
                    }
                }
        );
        queue.add(request);
    }
    private void init() {
        editsearch = findViewById( R.id.editsearch );
        txtthanhpho = findViewById( R.id.txtthanhpho );
        txtquocgia = findViewById( R.id.txtquocgia );
        txtnhietdo = findViewById( R.id.txtnhietdo );
        txttrangthai = findViewById( R.id.txttrangthai );
        txtdoam = findViewById( R.id.txtdoam );
        txtmay = findViewById( R.id.txtmay );
        txtgio = findViewById( R.id.txtgio );
        txtngay = findViewById( R.id.txtngay );
        btnsearch = findViewById( R.id.btnsearch );
        btn = findViewById( R.id.btn );
        imageIcon = findViewById( R.id.imageIcon );
    }
    private void processResponse(String response) {
        try {
            JSONObject object = new JSONObject(response);
            JSONObject jsonObject = object.getJSONArray("list").getJSONObject(0);
            //Set ten thanh pho, quoc gia
            String name = jsonObject.getString("name");
            txtthanhpho.setText("Thành phố:"+name);
            JSONObject jsonObjectCountry = jsonObject.getJSONObject("sys");
            String quocGia = jsonObjectCountry.getString("country");
            txtquocgia.setText("Quốc gia:"+quocGia);

            // Set text cho ngay (Chua co)
            String day = jsonObject.getString("dt");
            long lDay = Long.valueOf(day);
            Date date = new Date(lDay * 1000L);
            SimpleDateFormat sdf = new SimpleDateFormat("E, dd MMM yyyy HH:mm");
            String strDay = sdf.format(date);
            txtngay.setText(strDay);

            // Set icon cho imgIcon
            JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
            JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
            String status = jsonObjectWeather.getString("main");
            String icon = jsonObjectWeather.getString("icon");

            txttrangthai.setText(status);
            Picasso.with(MainActivity.this).load("http://openweathermap.org/img/w/" + icon + ".png").into(imageIcon);

            // Lay gia tri nhiet do, do am , gio...
            JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
            String strNhietDo = jsonObjectMain.getString("temp");
            String doAm = jsonObjectMain.getString("humidity");
            Double a = Double.valueOf(strNhietDo);
            String nhietDo = String.valueOf(a.intValue());
            txtnhietdo.setText(nhietDo + "°C");
            txtdoam.setText(doAm + "%");

            // Lay gia tri gio, may
            JSONObject jsonObjectWind = jsonObject.getJSONObject("wind");
            String gio = jsonObjectWind.getString("speed");
            txtgio.setText(gio + "m/s");
            JSONObject jsonObjectCloud = jsonObject.getJSONObject("clouds");
            String may = jsonObjectCloud.getString("all");
            txtmay.setText(may + "%");


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
