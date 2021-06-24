package com.sebrs3018.SmartSharing.barcode;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.navigation.NavController;

import com.google.gson.Gson;
import com.sebrs3018.SmartSharing.FBRealtimeDB.Entities.Book;
import com.sebrs3018.SmartSharing.Login.SessionManager;
import com.sebrs3018.SmartSharing.ui.BCScan.BCScanFragmentDirections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;


/*
 * API docs: https://www.barcodelookup.com/api-documentation
 */


public class BarcodeLookup extends AsyncTask<String, Void, Book> {

    private final String TAG = "BarcodeLookup";
    private NavController navController;
    private Context context;

    private String initial_url = "https://api.barcodelookup.com/v2/products?barcode=";
    private final String key = "&formatted=y&key=idwuike89q98a4lii72m0zvo3vgg9r";
    private Product productFound;

    /**
     * @param _context used to get the username of the current session
     * @param _navController used for navigation between fragments using Navigation UI
     * */
    public BarcodeLookup(NavController _navController, Context _context){
        navController = _navController;
        context = _context;
    }


    /* getter method for productFound */
    public Product getProductFound(){
        return productFound;
    }



    @Override
    protected Book doInBackground(String... strings) {
        return lookUp(strings[0]);
    }


    @Override
    protected void onPostExecute(Book result) {
        BCScanFragmentDirections.ActionNavigationScanToCheckBookFields action;
        action = BCScanFragmentDirections.actionNavigationScanToCheckBookFields(result);
        navController.navigate(action);
    }


    /**
     * @param _barcode to look for in the Barcode API DB  */
    private Book lookUp(String _barcode) {
        try {
            URL url = new URL(initial_url + _barcode + key);
            Log.i(TAG, "lookUp: url ==>  " + url.toString() );
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String in;
            String data = "";
            /* Read data form the url */
            while (null != (in = reader.readLine())) {
                data += in;
            }

            Gson gson = new Gson();
            ProductObject response = gson.fromJson(data, ProductObject.class);

            if (response != null) {
                /* tengo in considerazione solo un prodotto */
                productFound = response.products[0];
                return new Book(_barcode, productFound.title, productFound.author, productFound.brand, productFound.release_date, "", productFound.description, productFound.images[0], getLenderUsername());
            }

        } catch (IOException urlException){
            Log.e(TAG, urlException.getMessage());
        }
        /** Se non trova il libro va comunque nel form per farlo aggiungere a mano all'utente */
        return new Book();
    }

    private String getLenderUsername(){
        SessionManager sm = new SessionManager(context);
        return sm.getUserSession()[0];
    }



}
