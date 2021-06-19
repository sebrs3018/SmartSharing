package com.sebrs3018.SmartSharing.barcode;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import com.google.gson.Gson;
import com.sebrs3018.SmartSharing.Entities.Book;
import com.sebrs3018.SmartSharing.Login.SessionManager;
import com.sebrs3018.SmartSharing.TOARRANGE.DataManager;
import com.sebrs3018.SmartSharing.ui.BCScan.BCScanFragmentDirections;
import com.sebrs3018.SmartSharing.ui.home.HomeFragmentDirections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import static com.sebrs3018.SmartSharing.Constants.BOOKS;


/*
 * API docs: https://www.barcodelookup.com/api-documentation
 */


public class BarcodeLookup extends AsyncTask<String, Void, Book> {

    private final String TAG = "BarcodeLookup";
    private NavController navController;
    private Context context;

    private String initial_url = "https://api.barcodelookup.com/v2/products?barcode=";
    private final String key = "&formatted=y&key=r93822v6r228etpb16cxfllvvjvy2n";
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


    /* look for the barcode in the API DB */
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
                /*  public Book( String _ISBN, String _titolo, String _autore, String _editore, String _dataPubblicazione, String _nroPagine, String _descrizione, String _urlImage, String _lender ) */

                productFound = response.products[0];
                Log.i(TAG, "lookUp: " + productFound.author );
                Log.i(TAG, "lookUp: " + productFound.brand );
                Log.i(TAG, "lookUp: " + productFound.release_date );
                /*TODO: verificare se effettivamente description c'è sempre*/
                Log.i(TAG, "lookUp: " + productFound.description );
                Log.i(TAG, "lookUp: " + productFound.images[0] );
                return new Book(_barcode, productFound.title, productFound.author, productFound.brand, productFound.release_date, "", productFound.description, productFound.images[0], getLenderUsername());
            }

        } catch (IOException urlException){
            Log.e(TAG, urlException.getMessage());
        }

        return null;
    }

    private String getLenderUsername(){
        SessionManager sm = new SessionManager(context);
//        DataManager dm = new DataManager(BOOKS, context);
        return sm.getUserSession()[0];
//        dm.addBookLender(ISBN, sm.getUserSession()[0]);

    }



}
