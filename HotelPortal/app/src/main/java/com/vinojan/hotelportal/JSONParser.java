package com.vinojan.hotelportal;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

/**
 * Created by vino-pc on 1/26/16.
 * Create JSONObject from InputStream
 */
public class JSONParser {

    InputStream is = null;

    public JSONParser() {

    }

    public JSONObject getJSONFromResponse(InputStream is) throws IOException {
        String theString = null;
        JSONObject jObj = null;
        String json = null;
        StringWriter writer = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            Log.d("memoryTest", "bc");

            Log.d("Response", sb.toString());
            jObj = new JSONObject(sb.toString());

            Log.d("memoryTest", "gc");

        } catch (IOException e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }


        return jObj;

    }
}
