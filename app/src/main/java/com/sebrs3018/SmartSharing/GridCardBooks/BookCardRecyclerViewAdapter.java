package com.sebrs3018.SmartSharing.GridCardBooks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sebrs3018.SmartSharing.R;
import com.sebrs3018.SmartSharing.network.BookEntry;
import com.sebrs3018.SmartSharing.network.ImageRequester;

import org.jetbrains.annotations.NotNull;


import java.util.List;

public class BookCardRecyclerViewAdapter  extends RecyclerView.Adapter<BookCardViewHolder> {

    private List<BookEntry> bookList;
    private ImageRequester imageRequester;


    public BookCardRecyclerViewAdapter(List<BookEntry> bookList) {
        this.bookList = bookList;
        imageRequester = ImageRequester.getInstance();
    }


    @NonNull
    @NotNull
    @Override
    public BookCardViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_card, parent, false);
        return new BookCardViewHolder(layoutView);
    }
    /*  Questo metodo mi dice cosa ogni vista fa col suo contenuto  */
    @Override
    public void onBindViewHolder(@NonNull @NotNull BookCardViewHolder holder, int position) {
        if (bookList != null && position < bookList.size()) {
            BookEntry product = bookList.get(position);
            holder.bookTitle.setText(product.title);
            holder.bookPrice.setText(product.price);
            imageRequester.setImageFromUrl(holder.productImage, product.url);
        }
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }
}
