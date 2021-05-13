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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sebrs3018.SmartSharing.GridCardBooks.BookCardRecyclerViewAdapter;
import com.sebrs3018.SmartSharing.GridCardBooks.BookGridItemDecoration;
import com.sebrs3018.SmartSharing.R;
import com.sebrs3018.SmartSharing.SearchableActivity;
import com.sebrs3018.SmartSharing.databinding.FragmentSearchBinding;
import com.sebrs3018.SmartSharing.network.BookEntry;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class SearchFragment extends Fragment {

    protected static final int RESULT_SPEECH = 1;

    private final String TAG = "SearchFragment";
    private SearchViewModel searchViewModel;
    private FragmentSearchBinding binding;
    private SearchView searchView;
    RecyclerView recyclerView;
    private BookCardRecyclerViewAdapter adapter;


    public SearchFragment(){}

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        //setting up the toolBar
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.mySearchBar);


        // Setting up the RecyclerView
        recyclerView = binding.myRecyclerView;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));

        /* Inizializzo adapter dei dati */
        adapter = new BookCardRecyclerViewAdapter(BookEntry.initProductEntryList(getResources()) );
        recyclerView.setAdapter(adapter);
        int largePadding = getResources().getDimensionPixelSize(R.dimen.book_product_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.book_product_grid_spacing_small);
        recyclerView.addItemDecoration(new BookGridItemDecoration(largePadding, smallPadding));


        return root;
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

/*        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
//                Toast.makeText(SearchableActivity.this, "Search is expanded", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
//                Toast.makeText(SearchableActivity.this, "Search is collapsed", Toast.LENGTH_SHORT).show();
                return true;
            }
        };
        menu.findItem(R.id.search).setOnActionExpandListener(onActionExpandListener);*/


        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint("Cerca dei libri interessanti");
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        //Filtraggio tramite parole appena inserite
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //TODO: inserire qua la chiave per la ricerca in BD !
                Toast.makeText(getContext(), "query submitted: " + query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                Toast.makeText(getContext(), "onQueryTextChange: " + newText, Toast.LENGTH_SHORT).show();
                adapter.getFilter().filter(newText);
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
                return true;
            case R.id.search:
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


}