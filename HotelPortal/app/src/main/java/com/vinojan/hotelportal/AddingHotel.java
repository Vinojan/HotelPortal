package com.vinojan.hotelportal;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddingHotel extends AppCompatActivity  {
    Context context;

    TextView longitudeView;
    TextView latitudeView;
    Button addButton;

    HotelCreatingTask creatingTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_hotel);

        longitudeView = (TextView) findViewById(R.id.textView_longitude);
        latitudeView = (TextView) findViewById(R.id.textView_latitude);

        /*Checking for user Location */
        if(Configuration.IS_USER_LOCATION_AVAILABLE) {
            longitudeView.setText(String.valueOf(Configuration.USER_LONGITUDE));
            latitudeView.setText(String.valueOf(Configuration.USER_LATITUDE));
        }
        else{
            longitudeView.setText("Location not available");
            latitudeView.setText("");
        }

        addButton=(Button)findViewById(R.id.button_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Checking for the network connectivity*/
                if(Utils.isNetworkAvailable(context)) {
                    createNewHotel();

                }
                else{
                    Utils.showInternetConnectivityAlert(context);
                }
            }
        });

    }

    /*Validating the required data to create new hotel entry
    *check for hotel name, city, address, and user location.
    * If  data is available then add the hotel to database using a background task.
     */
    public void createNewHotel(){
        EditText nameEditView=(EditText)findViewById(R.id.editText_name);
        String hotelName=nameEditView.getText().toString().trim();
        if(hotelName!=null && !hotelName.equals("")){
            EditText cityEditView=(EditText)findViewById(R.id.editText_city);
            String city=cityEditView.getText().toString().trim();
            if(city!=null && !city.equals("")){
                EditText addressEditView=(EditText)findViewById(R.id.editText_address);
                String hotelAddress=addressEditView.getText().toString().trim();
                if(hotelAddress !=null && !hotelAddress.equals("")){
                    if(Configuration.IS_USER_LOCATION_AVAILABLE){
                        /*Create the hotel entry to the database*/
                        creatingTask=new HotelCreatingTask(this,hotelName,hotelAddress,city,Configuration.USER_LONGITUDE,Configuration.USER_LATITUDE);
                        creatingTask.execute();
                        finish();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Please move around and update your device gps location",Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    Toast.makeText(getApplicationContext(),"Please add hotel address",Toast.LENGTH_SHORT).show();
                    addressEditView.setFocusable(true);
                }
            }
            else{

                Toast.makeText(getApplicationContext(),"Please add city name",Toast.LENGTH_SHORT).show();
                cityEditView.setFocusable(true);
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "Please add hotel name", Toast.LENGTH_SHORT).show();
            nameEditView.setFocusable(true);
        }

    }

}
