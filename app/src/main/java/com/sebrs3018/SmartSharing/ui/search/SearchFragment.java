package com.sebrs3018.SmartSharing.ui.search;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.sebrs3018.SmartSharing.CustomListeners.OnEmptyQueryResultListener;
import com.sebrs3018.SmartSharing.FBRealtimeDB.Entities.Book;
import com.sebrs3018.SmartSharing.GridCardBook.BookCardRecyclerViewAdapter;
import com.sebrs3018.SmartSharing.GridCardBook.BookGridItemDecoration;
import com.sebrs3018.SmartSharing.Login.LoginActivity;
import com.sebrs3018.SmartSharing.Login.SessionManager;
import com.sebrs3018.SmartSharing.R;
import com.sebrs3018.SmartSharing.CustomListeners.OnTouchedItemListener;
import com.sebrs3018.SmartSharing.FBRealtimeDB.Database.DataManager;
import com.sebrs3018.SmartSharing.databinding.FragmentSearchBinding;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.sebrs3018.SmartSharing.Constants.BOOKS;

public class SearchFragment extends Fragment implements OnTouchedItemListener, OnEmptyQueryResultListener {

    protected static final int RESULT_SPEECH = 1;

    private final String TAG = "SearchFragment";
    private SearchViewModel searchViewModel;
    private FragmentSearchBinding binding;
    private SearchView searchView;
    private BookCardRecyclerViewAdapter adapter;
    private boolean isRegularSearch;
    private final static String FIRSTTIME = "firstTime";
    private List<Book> books;

    public SearchFragment(){}

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.i(TAG, "Search Fragment Created: ");
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //setting up the toolBar
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.mySearchBar);

        // Setting up the RecyclerView
        binding.myRecyclerView.setHasFixedSize(true);
        binding.myRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));

        /* Inizializzo adapter dei dati */
        int largePadding = getResources().getDimensionPixelSize(R.dimen.book_product_grid_spacing);
        initRecyclerView(60, largePadding);
        return root;
    }



    /**
     * @param LRpadding padding added to left and right between cards in a RecyclerView
     * @param TBPadding padding added to top and bottom between cards in a RecyclerView
     * */
    private void initRecyclerView(int LRpadding, int TBPadding){
        /* Inizializzo adapter dei dati */
        DataManager dm = new DataManager(BOOKS);
        books = new ArrayList<>();
        dm.getBookRoot().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Book book = ds.getValue(Book.class);
                    if (book != null) {
                        books.add(new Book(book.getISBN(), book.getTitolo(), book.getAutore(), book.getEditore(), book.getDataPubblicazione(), book.getNroPagine(), book.getDescrizione(), book.getUrlImage(), book.getLender()));
                    }
                }
                adapter = new BookCardRecyclerViewAdapter(books, SearchFragment.this, "", (OnEmptyQueryResultListener)SearchFragment.this);
                binding.myRecyclerView.setAdapter(adapter);
                binding.myRecyclerView.addItemDecoration(new BookGridItemDecoration(TBPadding, LRpadding));

                String ISBNresult = getISBNresult();
                if(!ISBNresult.equals(FIRSTTIME)){
                    Log.i(TAG, "onCreateOptionsMenu: ho appena ricevuto questo ISBN scanerizzato! " + ISBNresult);
                    searchView.setQuery(ISBNresult, true);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: " , error.toException() );
            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i(TAG, "onDestroyView: ");
        binding = null;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menu.clear();
        menuInflater.inflate(R.menu.top_nav_menu, menu);


        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint("Cerca dei libri interessanti");
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        
        
        //Filtraggio tramite parole appena inserite
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(adapter != null){    //nel caso in cui si ottenga la query a partire da BCScanning, l'adapter potrebbe essere nullo... (comportamento deferred)
                    Log.i(TAG, "onQueryTextSubmit:  " + query + "\t" + adapter.getIsEmpty());


                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(adapter != null){    //nel caso in cui si ottenga la query a partire da BCScanning, l'adapter potrebbe essere nullo... (comportamento deferred)
                    Log.i(TAG, "onQueryTextChange:  " + newText);
                    adapter.getFilter().filter(newText);
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, menuInflater);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.microphone:
                getVoiceInput();
                isRegularSearch = false;
                return true;
            case R.id.search:
                isRegularSearch = true;
                return true;
            case R.id.BCSearch:
                isRegularSearch = false;
                performBrScanning();
                return true;
            case R.id.Logout:
                SessionManager sm = new SessionManager(getContext());
                sm.logout();
                startActivity(new Intent(getContext(), LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void getVoiceInput(){
        try{
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "it-IT");
            startActivityForResult(intent, RESULT_SPEECH);
            searchView.setQuery("", false);
        }
        catch (ActivityNotFoundException e){
            Toast.makeText(getContext(), "Il tuo dispositivo non supporta la Speech Recognition", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case RESULT_SPEECH:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    searchView.setQuery(text.get(0), true);    //In questa maniera lasci all'utente scegliere i risultati appena ottenuti
                }
                break;
        }
    }


    private void performBrScanning(){
        final NavController navController  = Navigation.findNavController(getView());
        navController.navigate(SearchFragmentDirections.actionNavigationSearchToNavigationScan().setSearchBrScanning(true) );
    }

    private String getISBNresult(){
        return SearchFragmentArgs.fromBundle(getArguments()).getISBNresult();
    }

    @Override
    public void onItemTouched(int position, String from) {
        Log.i(TAG, "onUserClick: clicked " + books.get(position).getTitolo());

        final NavController navController  = Navigation.findNavController(getView());

        SearchFragmentDirections.ActionNavigationSearchToBookInfo action =
                SearchFragmentDirections.actionNavigationSearchToBookInfo(books.get(position));

        navController.navigate(action);

    }

    @Override
    public void OnEmptyQueryResult(boolean isEmpty) {
        if(isEmpty && !isRegularSearch){
            Log.i(TAG, "OnEmptyQueryResult: is reaaaallly empty");
            Toast.makeText(getContext(), "Non è stato trovato nessun riscontro ", Toast.LENGTH_SHORT).show();
        }
    }
}