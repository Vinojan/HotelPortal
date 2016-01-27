package com.vinojan.hotelportal;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vinojan.hotelportal.Model.Hotel;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SearchingHotel extends AppCompatActivity {
    ListView hotelListView;
    TextView cityNameView;
    Context context;
    String cityName;
    Button searchButton;
    ListViewAdapter customListAdapter;

    List<Hotel> hotelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context =  this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searching_hotel);

        cityNameView = (TextView) findViewById(R.id.editText_city);
        searchButton = (Button) findViewById(R.id.button_search);
        hotelListView = (ListView) findViewById(R.id.listView_hotel);

        /*Set the custom adapter to the List view */
        customListAdapter = new ListViewAdapter(this, R.id.listView_hotel, hotelList);
        hotelListView.setAdapter(customListAdapter);

        /*Set search button click event */
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Check for the network connectivity*/
                if(Utils.isNetworkAvailable(context)) {
                    cityName = cityNameView.getText().toString().trim();
                    /*Search for hotels if user given a city name  */
                    if (cityName != null && !cityName.equals("")) {
                    /*Fetch the hotels for city name in backgroun  */
                        new HotelSearchingTask(SearchingHotel.this).execute();
                    } else {
                        Toast.makeText(context, "Please type the city name to search for hotels.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Utils.showInternetConnectivityAlert(context);
                }
            }
        });

            /*Set Item click listner to to list view element to show hotel details when user click a hotel name in the list */
            hotelListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Hotel hotel = hotelList.get(position);
                    String name = hotel.getName();
                    String address = hotel.getAddress();
                    Intent viewHotelIntent = new Intent(context, ViewHotelDetail.class);
                    viewHotelIntent.putExtra("name", name);
                    viewHotelIntent.putExtra("address", address);
                    startActivity(viewHotelIntent);
                }
            });


    }

    /*Set the hotel list with new values  */
    public void setHotelList(List<Hotel> list){
        this.hotelList=list;
    }

    /*HotelSearchingTask for fetch hotels data from database
    *Run in Background
     */
    public class HotelSearchingTask extends AsyncTask<String, Void, ArrayList<Hotel>> {

        ProgressDialog proDialog;
        ServiceHandler serviceHandler;
        InputStream is;
        JSONParser parsing;
        Boolean isSuccess = false;
        String result = "null";
        ArrayList<Hotel> hotelListTask = new ArrayList<>();
        SearchingHotel searchingHotel;

        public HotelSearchingTask(SearchingHotel searchingHotel){
            this.searchingHotel=searchingHotel;
        }


        protected void onPreExecute() {
            super.onPreExecute();
            proDialog = new ProgressDialog(context);
            proDialog.setMessage("Searching for hotels ");
            proDialog.setIndeterminate(false);
            proDialog.setCancelable(false);
            proDialog.show();
        }


        @Override
        protected ArrayList<Hotel> doInBackground(String... params) {

            List<NameValuePair> value = new ArrayList<NameValuePair>();
            value.add(new BasicNameValuePair("city", cityName));

            serviceHandler = new ServiceHandler();
            is = serviceHandler.makeServiceCall("http://" + Configuration.HOME_URL + "/hotelportalserv/index.php?r=hotel/gethotelbycity", 2, value);
            parsing = new JSONParser();
            JSONObject json;
            try {
                json = parsing.getJSONFromResponse(is);

                if (json.getString("message").matches("Successfull")) {

                    isSuccess = true;
                    result = "Successfully got hotels details in " + cityName;
                    JSONArray hotelsArray = json.getJSONArray("hotels");
                    for (int i = 0; i < hotelsArray.length(); i++) {
                        JSONObject object = hotelsArray.getJSONObject(i);
                        String name = object.getString("name");
                        String city = object.getString("city");
                        String address = object.getString("address");
                        double longitude = object.getDouble("longitude");
                        double latitude = object.getDouble("latitude");

                        Hotel hotel = new Hotel(name, city, address, longitude, latitude);
                        hotelListTask.add(hotel);

                    }

                } else {
                    result = "Failed to get hotels details in " + cityName;
                    isSuccess = false;
                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            return hotelListTask;
        }

        protected void onPostExecute(ArrayList<Hotel> hotelListTask) {

            super.onPostExecute(hotelListTask);

            /*If user location detail is available, then sort hotel list according to the distance or sort hotel according to alphabetical order*/
            hotelListTask=sortingHotel(hotelListTask,Configuration.USER_LONGITUDE,Configuration.USER_LATITUDE);


            searchingHotel.setHotelList(hotelListTask);
             /*Notify the List view adapter that its data is changed. */
            customListAdapter.clear();
            customListAdapter.addAll(hotelListTask);
            customListAdapter.notifyDataSetChanged();


            proDialog.hide();
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();

        }

        /*If user location available, Sort hotels in increasing order of distance from user current location.
        *user current location taken from his mobile location.
        * or
        * Sort hotels according to their names' alphabetical order
         */
        public ArrayList<Hotel> sortingHotel(ArrayList<Hotel> hotels, double userLongitude, double userLatitude){
            int i;
            for(i=0;i<hotels.size();i++){
                Hotel h=hotels.get(i);
                double distance=calculateDistance(userLongitude,userLatitude,h.getLongitude(),h.getLatitude());
                h.setDistanceFromUser(distance);
            }
            if(Configuration.IS_USER_LOCATION_AVAILABLE){
                Collections.sort(hotels,new HotelDistanceComparator());
            }
            else{
                Collections.sort(hotels,new HotelNameComparator());
            }


            return hotels;
        }

        public double calculateDistance(double userLongitude,double userLatitude, double hotelLongitude, double hotelLatitude){
            double longSquare=Math.pow((userLongitude-hotelLongitude),2);
            double latSquare=Math.pow((userLatitude - hotelLatitude), 2);
            return Math.sqrt(longSquare+latSquare);
        }

        class HotelDistanceComparator implements Comparator<Hotel>{

            @Override
            public int compare(Hotel lhs, Hotel rhs) {
                return (int)(lhs.getDistanceFromUser()-rhs.getDistanceFromUser());
            }
        }

        class HotelNameComparator implements Comparator<Hotel>{

            @Override
            public int compare(Hotel lhs, Hotel rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        }



    }


}