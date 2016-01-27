package com.vinojan.hotelportal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by vino-pc on 1/27/16.
 */
public class Utils {

    /*Checking for network connectivity  */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager= (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /*Showing alert to user to change internet settings */
    public static void showInternetConnectivityAlert(Context context){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("Internet Connectivity");

        // Setting Dialog Message
        alertDialog.setMessage("Enable your internet connectivity.");


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
