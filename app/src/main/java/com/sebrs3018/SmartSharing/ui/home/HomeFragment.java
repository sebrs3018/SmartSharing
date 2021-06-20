package com.sebrs3018.SmartSharing.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;

import com.google.firebase.database.ValueEventListener;
import com.sebrs3018.SmartSharing.FBRealtimeDB.Entities.Book;
import com.sebrs3018.SmartSharing.FBRealtimeDB.Entities.User;
import com.sebrs3018.SmartSharing.GridCardBook.BookCardRecyclerViewAdapter;
import com.sebrs3018.SmartSharing.GridCardBook.BookGridItemDecoration;
import com.sebrs3018.SmartSharing.R;
import com.sebrs3018.SmartSharing.CustomListeners.OnTouchedItemListener;
import com.sebrs3018.SmartSharing.FBRealtimeDB.Database.DataManager;
import com.sebrs3018.SmartSharing.databinding.FragmentHomeBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.sebrs3018.SmartSharing.Constants.BOOKS;

public class HomeFragment extends Fragment implements OnTouchedItemListener {


    private static final String TAG = "HomeFragment";
    private static final String NUOVIARRIVI = "NuoviArrivi";
    private static final String CONSIGLIATI = "Consigliati";
    private static final int LIMITELIBRICATEGORIA = 10;

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    private List<Book> consigliati  = new ArrayList<>();
    private Book[] nuoviArrivi = new Book[LIMITELIBRICATEGORIA];


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =  new ViewModelProvider(this).get(HomeViewModel.class);

        //Il binding mi offre la possibilità di esplorare i componenti del layout come se fossero degli attributi!
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.topAppBar);

        int largePadding = getResources().getDimensionPixelSize(R.dimen.book_product_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.book_product_grid_spacing_small);

        // Setting up the RecyclerView - Nuovi Arrivi
        binding.rvHorizontal.setHasFixedSize(true);
        binding.rvHorizontal.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        /* Inizializzo adapter dei dati */
        initRVBooksNArrivi();
        binding.rvHorizontal.addItemDecoration(new BookGridItemDecoration(largePadding, smallPadding));


        // Setting up the RecyclerView - Consigliati
        binding.rvHorizontal2.setHasFixedSize(true);
        binding.rvHorizontal2.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        //Inizializzo libri consigliati - popolazione
        initRVBooksConsigliati();
        binding.rvHorizontal2.addItemDecoration(new BookGridItemDecoration(largePadding, smallPadding));
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onItemTouched(int position, String from) {

        final NavController navController  = Navigation.findNavController(getView());
        /* Passo valore al fragment...  */
        HomeFragmentDirections.ActionNavigationHomeToBookInfo action;

        if(from.equals(CONSIGLIATI)){
            Log.i(TAG, "onUserClick: clicked consigliati");
            action = HomeFragmentDirections.actionNavigationHomeToBookInfo(consigliati.get(position));
            navController.navigate(action);
        } else if (from.equals(NUOVIARRIVI)){
            Log.i(TAG, "onUserClick: clicked nuoviArrivi");
            action = HomeFragmentDirections.actionNavigationHomeToBookInfo(nuoviArrivi[position]);
            navController.navigate(action);
        }

    }


    public void initRVBooksNArrivi(){

        DataManager dm = new DataManager(BOOKS);
        /*query per l'ordinamento*/
        Query bookQuery = dm.getBookRoot().orderByChild("idlogicalClock").limitToLast(10);
        bookQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                int index = LIMITELIBRICATEGORIA - 1;
                for (DataSnapshot appleSnapshot: snapshot.getChildren()) {
                    Book book = appleSnapshot.getValue(Book.class);
                    if(book != null){
                        Log.i(TAG, "initRVBooks: idlogicalClock ==> " + book.getIDlogicalClock());
                        Log.i(TAG, "initRVBooks: ISBN ==> " + book.getISBN());
                        Log.i(TAG, "initRVBooks: Titolo ==> " + book.getTitolo() + "\n\n");
                        nuoviArrivi[index--] = new Book(book.getISBN(), book.getTitolo(), book.getAutore(), book.getEditore(), book.getDataPubblicazione(), book.getNroPagine(), book.getDescrizione(), book.getUrlImage(), book.getLender());
                    }

                    BookCardRecyclerViewAdapter theAdapter = new BookCardRecyclerViewAdapter(Arrays.asList(nuoviArrivi), HomeFragment.this, NUOVIARRIVI);
                    binding.rvHorizontal.setAdapter(theAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.e(TAG, "onCancelled: ", error.toException());
            }
        });



    }

    public void initRVBooksConsigliati(){
        DataManager dm = new DataManager(BOOKS);
        /*query per l'ordinamento*/
        Query bookQuery = dm.getBookRoot().limitToLast(10);
        bookQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Book book = ds.getValue(Book.class);
                    if (book != null) {
                        consigliati.add(new Book(book.getISBN(), book.getTitolo(), book.getAutore(), book.getEditore(), book.getDataPubblicazione(), book.getNroPagine(), book.getDescrizione(), book.getUrlImage(), book.getLender()));
                    }
                }

                BookCardRecyclerViewAdapter cAdapter = new com.sebrs3018.SmartSharing.GridCardBook.BookCardRecyclerViewAdapter(consigliati, HomeFragment.this, CONSIGLIATI);
                binding.rvHorizontal2.setAdapter(cAdapter);

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.e(TAG, "onCancelled: ", error.toException());
            }
        });

    }
}