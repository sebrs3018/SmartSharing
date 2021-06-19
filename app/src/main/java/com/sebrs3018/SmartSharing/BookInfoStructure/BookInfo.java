package com.sebrs3018.SmartSharing.BookInfoStructure;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sebrs3018.SmartSharing.BookInfoStructure.Tabs.DoveTrovarloTab;
import com.sebrs3018.SmartSharing.BookInfoStructure.Tabs.InfoTab;
import com.sebrs3018.SmartSharing.databinding.FragmentBookInfoBinding;
import com.sebrs3018.SmartSharing.network.BookEntry;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookInfo extends Fragment {

    private static final String TAG = "BookInfo";

    private View myFragment;
    private FragmentBookInfoBinding binding;

    public BookInfo() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentBookInfoBinding.inflate(inflater, container, false);
        myFragment = binding.getRoot();
        return myFragment;
    }


    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tabInit();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    private void tabInit(){
        //Initialize array list with tab names
        ArrayList<String> arrayList = new ArrayList<>();

        //Adding tab names
        arrayList.add("Info");
        arrayList.add("Dove Trovarlo");

        prepareViewPager(arrayList);

        //Setting up with ViewPager
        binding.tabLayout.setupWithViewPager(binding.vpSwappingPages, true);
    }


    private void prepareViewPager(ArrayList<String> tabNames) {
        PageAdapter pageAdapter = new PageAdapter(getChildFragmentManager());
        //initializing main fragment...
        for(int i = 0; i<tabNames.size(); i++){
            //adding fragment...
            if(i == 0){
                BookEntry bookEntry = getMessageFromNavUI();

                //TODO: Get info from book entry and pass the desired data
                InfoTab infoTab = InfoTab.newInstance(new String[] {"isbn", "editore", "dataPubblicazione", "numeroPagine", "Descrizione", bookEntry.getUrlImage()});
                pageAdapter.addFragment(infoTab, tabNames.get(i));
            }
            else if (i == 1){
                pageAdapter.addFragment(new DoveTrovarloTab(), tabNames.get(i));
            }
        }

        binding.vpSwappingPages.setAdapter(pageAdapter);
    }


    private BookEntry getMessageFromNavUI(){
        if(getArguments() == null)
            throw new IllegalArgumentException();

        BookInfoArgs args = BookInfoArgs.fromBundle(getArguments());

        BookEntry bookEntry = args.getBookEntry();
        initLayoutBookInfoArgs(bookEntry.getTitle(), "George Orwell");

        return bookEntry;
    }

    private void initLayoutBookInfoArgs(String titolo, String autore){
        /* Inizializzo il titolo */
        binding.tvTitolo.setText(titolo);
        binding.tvAutori.setText(autore);
    }


}