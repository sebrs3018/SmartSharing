package com.sebrs3018.SmartSharing.GridCardBook;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sebrs3018.SmartSharing.CustomListeners.OnTouchedItemListener;
import com.sebrs3018.SmartSharing.FBRealtimeDB.Entities.Book;
import com.sebrs3018.SmartSharing.R;
import com.sebrs3018.SmartSharing.network.ImageRequester;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BookCardRecyclerViewAdapter  extends RecyclerView.Adapter<BookCardViewHolder> implements Filterable {

    private List<Book> bookListAll;    //Questa lista terrà traccia della lista originale
    private List<Book> bookList;       //Questa lista sarà modificata dal filtraggio
    private ImageRequester imageRequester;
    private OnTouchedItemListener onTouchedItemListener;
    private final String TAG = "BCRViewAdapter";
    private String from;
    private Context context;


    public BookCardRecyclerViewAdapter(List<Book> bookList, OnTouchedItemListener _onTouchedItemListener, String _from) {
        this.bookList = bookList;
        bookListAll = new ArrayList<>(bookList);

        /* Nel caso iniziale, se non c'è nessun filtraggio, allora la lista di filtraggio risulta uguale a quella di dati*/
        onTouchedItemListener = _onTouchedItemListener;
        from = _from;
    }


    public BookCardRecyclerViewAdapter(List<Book> bookList, OnTouchedItemListener _onTouchedItemListener, String _from, Context _context) {
        this(bookList, _onTouchedItemListener, _from);
        context = _context;
    }



    @NonNull
    @NotNull
    @Override
    public BookCardViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_card, parent, false);

        imageRequester = ImageRequester.getInstance();

        return new BookCardViewHolder(layoutView, onTouchedItemListener, from);
    }

    /*  Questo metodo mi dice cosa ogni vista fa col suo contenuto  */
    @Override
    public void onBindViewHolder(@NonNull @NotNull BookCardViewHolder holder, int position) {
        if (bookList != null && position < bookList.size()) {
            Book book = bookList.get(position);

            holder.getBookTitle().setText(book.getTitolo());
            holder.getBookEditor().setText(book.getEditore());
            imageRequester.setImageFromUrl(holder.getProductImage(), book.getUrlImage());

        }
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }


    //Filtro per la searchbar
    public Filter getFilter(){
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        /* Questo processo di filtering viene eseguito su un altro thread */
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Book> filteredList = new ArrayList<>();

            if(charSequence == null || charSequence.length() == 0){
                //Se la chiave è vuota restituisco la lista di valori già presente in DB
                filteredList.addAll(bookListAll);
            }
            else{
                String key = charSequence.toString().toLowerCase().trim();
                for (Book row : bookListAll){
                    if(row.getTitolo().toLowerCase().contains(key)){
                        filteredList.add(row);
                    }
                    else if(row.getISBN().contains(key)){
                        filteredList.add(row);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        /* Una volta eseguito il filtraggio, il risultato viene preso da questo metodo  - UI thread */
        protected void publishResults(CharSequence constraint, FilterResults results) {

            Collection<? extends Book> resultList = (Collection<? extends Book>) results.values;

            if(resultList.size() <= 0)
                Toast.makeText(context, "Non è stato trovato nessun risultato", Toast.LENGTH_SHORT).show();
                /* elimino gli item che non mi interessano dalla mia lista filtrata */
            bookList.clear();
            bookList.addAll(resultList);
            /* Comunico all'adapter di aggionrare la propria lista */
            notifyDataSetChanged();
        }
    };


}
