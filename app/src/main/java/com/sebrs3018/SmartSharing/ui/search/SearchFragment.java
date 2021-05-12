package com.sebrs3018.SmartSharing.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.sebrs3018.SmartSharing.R;
import com.sebrs3018.SmartSharing.SearchableActivity;
import com.sebrs3018.SmartSharing.databinding.FragmentSearchBinding;

public class SearchFragment extends Fragment {

    private SearchViewModel searchViewModel;
    private FragmentSearchBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //setting up the toolBar
        //TODO: capire perchè non ti prende il navBar Corretto
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.mySearchBar);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        //TODO: capire perchè non ti prende le icona!
        menuInflater.inflate(R.menu.top_nav_menu, menu);


        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
//                Toast.makeText(SearchableActivity.this, "Search is expanded", Toast.LENGTH_SHORT).show();
                menu.findItem(R.id.microphone).setVisible(true);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
//                Toast.makeText(SearchableActivity.this, "Search is collapsed", Toast.LENGTH_SHORT).show();
                return true;
            }
        };

        menu.findItem(R.id.search).setOnActionExpandListener(onActionExpandListener);
//        this.menu = menu;
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint("Cerca dei libri interessanti");

        super.onCreateOptionsMenu(menu, menuInflater);
    }



}