package com.vinojan.hotelportal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vino-pc on 1/27/16.
 */
public class HotelCreatingTask extends AsyncTask<String, Void, String>{
    private String name;
    private String address;
    private String city;
    private double longitude,latitude;

    ServiceHandler serviceHandler;
    InputStream is;
    JSONParser parsing;
    Context context;
    Boolean isSuccess=false;
    ProgressDialog proDialog;

    public HotelCreatingTask(Context context,String name,String address, String city,double longitude,double latitude){
        this.context=context;
        this.name=name;
        this.address=address;
        this.city=city;
        this.longitude=longitude;
        this.latitude=latitude;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        proDialog = new ProgressDialog(context);
        proDialog.setMessage("Adding Hotel..");
        proDialog.setIndeterminate(false);
        proDialog.setCancelable(false);
        proDialog.show();
    }

    @Override
    protected String doInBackground(String... arg0) {

        String result= "null";
        List<NameValuePair> value= new ArrayList<NameValuePair>();
        value.add(new BasicNameValuePair("name", name));
        value.add(new BasicNameValuePair("address", address));
        value.add(new BasicNameValuePair("city", city));
        value.add(new BasicNameValuePair("longitude", String.valueOf(longitude)));
        value.add(new BasicNameValuePair("latitude", String.valueOf(latitude)));
        serviceHandler = new ServiceHandler();
        is = serviceHandler.makeServiceCall("http://"+Configuration.HOME_URL+"/hotelportalserv/index.php?r=hotel/createhotel", 2, value);
        parsing = new JSONParser();
        JSONObject json;
        try {
            json = parsing.getJSONFromResponse(is);

            if(json.getString("message").matches("Successfull")){

                isSuccess=true;

                result = "Hotel Creation Success";
            }else{
                result = "Hotel Creation Failed";
                isSuccess=false;
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return result;
    }

    protected void onPostExecute(String result) {

        super.onPostExecute(result);


        if(isSuccess){
            proDialog.hide();
            Toast.makeText(context, "Hotel Creation Success", Toast.LENGTH_SHORT).show();

        }
        else{
            proDialog.hide();
            Toast.makeText(context, "Hotel Creation Failed", Toast.LENGTH_SHORT).show();

        }
    }


}
