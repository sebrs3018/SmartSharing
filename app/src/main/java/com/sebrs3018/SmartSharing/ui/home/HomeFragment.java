package com.sebrs3018.SmartSharing.ui.home;

import android.os.Bundle;
import android.util.Log;
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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sebrs3018.SmartSharing.GridCardBooks.BookGridItemDecoration;
import com.sebrs3018.SmartSharing.R;
import com.sebrs3018.SmartSharing.GridCardBooks.BookCardRecyclerViewAdapter;
import com.sebrs3018.SmartSharing.TouchCardListener.OnTouchedItemListener;
import com.sebrs3018.SmartSharing.databinding.FragmentHomeBinding;
import com.sebrs3018.SmartSharing.network.BookEntry;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HomeFragment extends Fragment implements OnTouchedItemListener {


    private static final String TAG = "HomeFragment";
    private static final String NUOVIARRIVI = "NuoviArrivi";
    private static final String CONSIGLIATI = "Consigliati";

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private List<BookEntry> nuoviArrivi;
    private List<BookEntry> consigliati;


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
        nuoviArrivi = BookEntry.initProductEntryList(getResources());
        BookCardRecyclerViewAdapter adapter = new BookCardRecyclerViewAdapter(nuoviArrivi, this, NUOVIARRIVI);
        binding.rvHorizontal.setAdapter(adapter);
        int largePadding = getResources().getDimensionPixelSize(R.dimen.book_product_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.book_product_grid_spacing_small);
        binding.rvHorizontal.addItemDecoration(new BookGridItemDecoration(largePadding, smallPadding));

        // Setting up the RecyclerView - Consigliati
        binding.rvHorizontal2.setHasFixedSize(true);
        binding.rvHorizontal2.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        /* Inizializzo adapter dei dati */
        consigliati = BookEntry.initProductEntryList(getResources());
        BookCardRecyclerViewAdapter cAdapter = new BookCardRecyclerViewAdapter(consigliati, this, CONSIGLIATI);
        binding.rvHorizontal2.setAdapter(cAdapter);
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
        Log.i(TAG, "onUserClick: clicked " + consigliati.get(position).getTitle());

        final NavController navController  = Navigation.findNavController(getView());
        /* Passo valore al fragment...  */
        HomeFragmentDirections.ActionNavigationHomeToBookInfo action;

        if(from.equals(CONSIGLIATI)){
            action = HomeFragmentDirections.actionNavigationHomeToBookInfo(consigliati.get(position));
            action.setMessage("This is just another string field for Consigliati ...");
            navController.navigate(action);
        } else if (from.equals(NUOVIARRIVI)){
            action = HomeFragmentDirections.actionNavigationHomeToBookInfo(nuoviArrivi.get(position));
            action.setMessage("This is just another string field for NuoviArrivi ....");
            navController.navigate(action);
        }

    }
}