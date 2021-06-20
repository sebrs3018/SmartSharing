package com.sebrs3018.SmartSharing.GridCardBook;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.sebrs3018.SmartSharing.CustomListeners.OnTouchedItemListener;
import com.sebrs3018.SmartSharing.R;

public class BookCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    private static final String TAG = "BookCardViewHolder";

    OnTouchedItemListener onTouchedItemListener;

    /* Contiene informazioni sul libro */
    private NetworkImageView productImage;
    private TextView bookTitle;
    private TextView bookEditor;
    private String from;

    public BookCardViewHolder(@NonNull View itemView, OnTouchedItemListener _onTouchedItemListener, String _from){
        super(itemView);
        productImage = itemView.findViewById(R.id.product_image);
        bookTitle = itemView.findViewById(R.id.product_title);
        bookEditor = itemView.findViewById(R.id.product_editor);
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

    public TextView getBookTitle() {
        return bookTitle;
    }

    public TextView getBookEditor() {
        return bookEditor;
    }

    public NetworkImageView getProductImage() {
        return productImage;
    }
}
