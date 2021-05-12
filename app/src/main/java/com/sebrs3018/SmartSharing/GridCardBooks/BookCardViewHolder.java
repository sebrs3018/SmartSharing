package com.sebrs3018.SmartSharing.GridCardBooks;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.sebrs3018.SmartSharing.R;

public class BookCardViewHolder extends RecyclerView.ViewHolder {

    public NetworkImageView productImage;
    public TextView bookTitle;
    /* TODO: questo campo del prezzo nel nostro dominio applicativo non ci servirà */
    public TextView bookPrice;

    public BookCardViewHolder(@NonNull View itemView){
        super(itemView);
        productImage = itemView.findViewById(R.id.product_image);
        bookTitle = itemView.findViewById(R.id.product_title);
        bookPrice = itemView.findViewById(R.id.product_price);
    }

}
