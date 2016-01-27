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
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LocationListener{

    private LocationManager locationManager;
    private Context context;
    private String provider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addButton=(Button)findViewById(R.id.button_add);
        Button searchButton=(Button)findViewById(R.id.button_search);

        /*Get Location Service  */
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        /*Check for GPS provider is enabled */
        Configuration.IS_GPS_ON= locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!Configuration.IS_GPS_ON) {
            showSettingsAlert();
        }

        /*Get Best provider GPS or Network  */
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        /*Get the last know location of user */
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            Configuration.IS_USER_LOCATION_AVAILABLE=true;
            Log.d("Provider ", "Provider" + provider + " has been selected.");
            onLocationChanged(location);
        }


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddHotelActivity();

            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearchHotelActivity();
            }
        });
    }

    private void openSearchHotelActivity() {
        Intent searchIntent=new Intent(this,SearchingHotel.class);
        startActivity(searchIntent);
    }

    public void openAddHotelActivity(){
        /*If user location is  available  open the add hotel activity  else show alert*/
        if(Configuration.IS_USER_LOCATION_AVAILABLE) {
            Intent addIntent = new Intent(this, AddingHotel.class);
            startActivity(addIntent);
        }
        else{
            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                showLocationUpdateAlert();

            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /* Request updates at startup */
    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        locationManager.requestLocationUpdates(provider, 0, 0, this);
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onStop() {
        super.onStop();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        locationManager.removeUpdates(this);
    }


    @Override
    public void onLocationChanged(Location location) {
        Configuration.USER_LATITUDE = location.getLatitude();
        Configuration.USER_LONGITUDE= location.getLongitude();


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,Toast.LENGTH_SHORT).show();
    }

    /*Showing alert to user to enable the location settings */
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    /*Showing alert to user to update the location settings */
    public void showLocationUpdateAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("Update GPS Location");

        // Setting Dialog Message
        alertDialog.setMessage("Please update your location and continue to add your hotel.");



        // on pressing cancel button
        alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

}
