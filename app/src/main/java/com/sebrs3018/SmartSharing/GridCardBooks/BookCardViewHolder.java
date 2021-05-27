package com.sebrs3018.SmartSharing.GridCardBooks;

import android.nfc.Tag;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.sebrs3018.SmartSharing.R;
import com.sebrs3018.SmartSharing.TouchCardListener.OnTouchedItemListener;

public class BookCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    private static final String TAG = "BookCardViewHolder";

    OnTouchedItemListener onTouchedItemListener;

    /* Contiene informazioni sul libro */
    public NetworkImageView productImage;
    public TextView bookTitle;
    public TextView bookPrice;
    private String from;

    public BookCardViewHolder(@NonNull View itemView, OnTouchedItemListener _onTouchedItemListener, String _from){
        super(itemView);
        productImage = itemView.findViewById(R.id.product_image);
        bookTitle = itemView.findViewById(R.id.product_title);
        bookPrice = itemView.findViewById(R.id.product_price);
        onTouchedItemListener = _onTouchedItemListener;
        from = _from;

        /* Assegno listener alla mia cardView */
        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        /* passo info sull'elemento appena toccato... */
        onTouchedItemListener.onItemTouched(getBindingAdapterPosition(), from);
    }
}
