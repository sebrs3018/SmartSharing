package com.sebrs3018.SmartSharing.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sebrs3018.SmartSharing.GridCardBooks.BookGridItemDecoration;
import com.sebrs3018.SmartSharing.R;
import com.sebrs3018.SmartSharing.GridCardBooks.BookCardRecyclerViewAdapter;
import com.sebrs3018.SmartSharing.databinding.FragmentHomeBinding;
import com.sebrs3018.SmartSharing.network.BookEntry;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;


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



        // Setting up the RecyclerView - Nuovi Arrivi
        binding.rvHorizontal.setHasFixedSize(true);
        binding.rvHorizontal.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        /* Inizializzo adapter dei dati */
        BookCardRecyclerViewAdapter adapter = new BookCardRecyclerViewAdapter(BookEntry.initProductEntryList(getResources()) );
        binding.rvHorizontal.setAdapter(adapter);
        int largePadding = getResources().getDimensionPixelSize(R.dimen.book_product_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.book_product_grid_spacing_small);
        binding.rvHorizontal.addItemDecoration(new BookGridItemDecoration(largePadding, smallPadding));

        // Setting up the RecyclerView - Consigliati
        binding.rvHorizontal2.setHasFixedSize(true);
        binding.rvHorizontal2.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        /* Inizializzo adapter dei dati */
        BookCardRecyclerViewAdapter cAdapter = new BookCardRecyclerViewAdapter(BookEntry.initProductEntryList(getResources()) );
        binding.rvHorizontal2.setAdapter(cAdapter);
        binding.rvHorizontal2.addItemDecoration(new BookGridItemDecoration(largePadding, smallPadding));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}