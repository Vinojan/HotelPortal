package com.vinojan.hotelportal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ViewHotelDetail extends AppCompatActivity {
    private  String name;
    private  String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_hotel_detail);

        TextView nameView=(TextView)findViewById(R.id.textView_hotelName);
        TextView addressView=(TextView)findViewById(R.id.textView_address);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
             name=extras.getString("name");
             address=extras.getString("address");

        }
        else{
            name="Hotel name not available";
            address="Hotel address not available";
        }
        nameView.setText(name);
        addressView.setText(address);
    }
}
