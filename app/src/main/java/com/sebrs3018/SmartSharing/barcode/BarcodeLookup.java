package com.sebrs3018.SmartSharing.barcode;

import android.util.Log;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;


/*
 * API docs: https://www.barcodelookup.com/api-documentation
 */


/* TODO: Refactoring */
public class BarcodeLookup {

    private final String TAG = "BarcodeLookup";

    private String initial_url = "https://api.barcodelookup.com/v2/products?key=a01b8vm4b0clxr7zsjgdztpm69glbb&barcode=";
    private Product productFound;

    /* getter method for productFound */
    public Product getProductFound(){
        return productFound;
    }


    /* look for the barcode in the API DB */
    public boolean lookUp(String _barcode) {
        try {
            URL url = new URL(initial_url + _barcode);
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
                productFound = response.products[0];
                return true;
            }
            return false;
        }catch (MalformedURLException urlException){
            Log.e(TAG, urlException.getMessage());
        }catch (IOException ioException){
            Log.e(TAG, ioException.getMessage());
        }

        return false;
    }
}
