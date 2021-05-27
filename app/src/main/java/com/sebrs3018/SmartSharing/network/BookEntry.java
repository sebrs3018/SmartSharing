package com.sebrs3018.SmartSharing.network;


import android.content.res.Resources;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sebrs3018.SmartSharing.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A product entry in the list of products.
 */
public class BookEntry implements Parcelable {
    private static final String TAG = BookEntry.class.getSimpleName();

    public final String title;
    public final Uri dynamicUrl;
    public final String url;    //contiene url dell'immagine
    public final String price;
    public final String description;

    public BookEntry(
            String title, String dynamicUrl, String url, String price, String description) {
        this.title = title;
        this.dynamicUrl = Uri.parse(dynamicUrl);
        this.url = url;
        this.price = price;
        this.description = description;
    }

    protected BookEntry(Parcel in) {
        title = in.readString();
        dynamicUrl = in.readParcelable(Uri.class.getClassLoader());
        url = in.readString();
        price = in.readString();
        description = in.readString();
    }

    public static final Creator<BookEntry> CREATOR = new Creator<BookEntry>() {
        @Override
        public BookEntry createFromParcel(Parcel in) {
            return new BookEntry(in);
        }

        @Override
        public BookEntry[] newArray(int size) {
            return new BookEntry[size];
        }
    };

    /**
     * Loads a raw JSON at R.raw.products and converts it into a list of ProductEntry objects
     */
    public static List<BookEntry> initProductEntryList(Resources resources) {
        InputStream inputStream = resources.openRawResource(R.raw.products);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            int pointer;
            while ((pointer = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, pointer);
            }
        } catch (IOException exception) {
            Log.e(TAG, "Error writing/reading from the JSON file.", exception);
        } finally {
            try {
                inputStream.close();
            } catch (IOException exception) {
                Log.e(TAG, "Error closing the input stream.", exception);
            }
        }
        String jsonProductsString = writer.toString();
        Gson gson = new Gson();
        Type productListType = new TypeToken<ArrayList<BookEntry>>() {
        }.getType();
        return gson.fromJson(jsonProductsString, productListType);
    }

    public String getTitle(){
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrlImage() {
        return url;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeParcelable(dynamicUrl, flags);
        dest.writeString(url);
        dest.writeString(price);
        dest.writeString(description);
    }
}