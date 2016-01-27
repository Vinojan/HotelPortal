package com.vinojan.hotelportal;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vinojan.hotelportal.Model.Hotel;

import java.util.List;

/**
 * Created by vino-pc on 1/27/16.
 * Custom ListView adapter to occupy the ListView
 */
public class ListViewAdapter extends ArrayAdapter<Hotel> {


        public ListViewAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        public ListViewAdapter(Context context, int resource, List<Hotel> items) {
            super(context, resource, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.hotel_list_element, null);
            }

            Hotel p = getItem(position);

            if (p != null) {
                TextView hotelName = (TextView) v.findViewById(R.id.textView_hotel);


                if (hotelName != null) {
                    hotelName.setText(p.getName());

                }

            }

            return v;
        }



}
