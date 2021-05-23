package com.sebrs3018.SmartSharing.BookInfoStructure;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.sebrs3018.SmartSharing.R;
import com.sebrs3018.SmartSharing.network.BookEntry;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookInfo extends Fragment {

    private static final String TAG = "BookInfo";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem infoTab, dtTab;

    public PageAdapter pagerAdapter;


    public BookInfo() {
        // Required empty public constructor
    }

    /*TODO: da controllare se funge o meno! */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View layoutInflater = inflater.inflate(R.layout.fragment_book_info, container, false);

        tabLayout = layoutInflater.findViewById(R.id.tabLayout);
        viewPager = layoutInflater.findViewById(R.id.vpSwappingPages);

        dtTab     = layoutInflater.findViewById(R.id.dtTab);
        infoTab   = layoutInflater.findViewById(R.id.infoTab);


        tabInit(viewPager, tabLayout);

//        ((AppCompatActivity) getActivity()).getSupportFragmentManager();

//        pagerAdapter =  new PageAdapter(getParentFragmentManager(), tabLayout.getTabCount());





/*        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition() == 0){
                    pagerAdapter.notifyDataSetChanged();
                    Log.i(TAG, "onTabSelected: touched");
                }
                else if(tab.getPosition() == 1){
                    pagerAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        }); //da ragionare su dove inserire il listener di riomozione
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));*/

        // Inflate the layout for this fragment
        return layoutInflater;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getArguments() != null){
            BookInfoArgs args = BookInfoArgs.fromBundle(getArguments());
            String message = args.getMessage();
            Log.i(TAG, "onViewCreated: " + message);

            BookEntry bookEntry = args.getBookEntry();
            Log.i(TAG, "onViewCreated: " + bookEntry.getTitle());
        }
    }



    private void tabInit(ViewPager viewPager, TabLayout tabLayout){
        //Initialize array list with tab names
        ArrayList<String> arrayList = new ArrayList<>();

        //Adding tab names
        arrayList.add("Dove Trovarlo");
        arrayList.add("Info");

        prepareViewPager(viewPager, arrayList);

        //Setting up with ViewPager
        tabLayout.setupWithViewPager(viewPager);

    }

    private void prepareViewPager(ViewPager viewPager, ArrayList<String> tabNames) {
        PageAdapter pageAdapter = new PageAdapter(getParentFragmentManager());

        //initializing main fragment...

        for(int i = 0; i<tabNames.size(); i++){
            //Initilizing Bundle for communication between tabs...
//            Bundle bundle = new Bundle();
            //Put info

//           bundle.putString("title", tabNames.get(i)); //invio un unico messaggio ai miei tab (può essere customizzato)

            //adding fragment...
            if(i == 0){
                pageAdapter.addFragment(new InfoTab(), tabNames.get(i));
            }
            else if (i == 1){
                pageAdapter.addFragment(new DoveTrovarloTab(), tabNames.get(i));
            }

        }

        viewPager.setAdapter(pageAdapter);

    }

}