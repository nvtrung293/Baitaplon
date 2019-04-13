package com.example.nguyentrung.baitaplon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    ArrayList<Thoitiet> arrayList;
    Context context;

    public CustomAdapter(ArrayList<Thoitiet> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get( position );
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        view = inflater.inflate( R.layout.dong_listview,null );

        Thoitiet thoitiet = arrayList.get( i );
        TextView txtDay = view.findViewById( R.id.tvngay );
        TextView txtStatus = view.findViewById( R.id.tvtrangthai );
        TextView txtMaxtemp = view.findViewById( R.id.tvmaxtemp );
        TextView txtMinTemp = view.findViewById( R.id.tvmintemp );

        ImageView imgStatus = (ImageView) view.findViewById( R.id.imgtrangthai );
        txtDay.setText( thoitiet.Day );
        txtStatus.setText( thoitiet.Status );
        txtMaxtemp.setText( thoitiet.MaxTemp+"°C" );
        txtMinTemp.setText( thoitiet.MinTemp+"°C" );

        Picasso.with(context).load("http://openweathermap.org/img/w/" + thoitiet.Image + ".png").into(imgStatus);
        return view;
    }
}
