package com.sebrs3018.SmartSharing.BookInfoStructure;

import android.icu.text.IDNA;
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

    private View myFragment;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem infoTab, dtTab;

    public PageAdapter pagerAdapter;


    public BookInfo() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myFragment = inflater.inflate(R.layout.fragment_book_info, container, false);

        viewPager = myFragment.findViewById(R.id.vpSwappingPages);
        tabLayout = myFragment.findViewById(R.id.tabLayout);

        Log.i(TAG, "onCreateView: Creating view...");

        // Inflate the layout for this fragment
        return myFragment;
    }


    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tabInit(viewPager, tabLayout);
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
        arrayList.add("Info");
        arrayList.add("Dove Trovarlo");

        prepareViewPager(viewPager, arrayList);

        //Setting up with ViewPager
        tabLayout.setupWithViewPager(viewPager, true);

    }

    private void prepareViewPager(ViewPager viewPager, ArrayList<String> tabNames) {
        PageAdapter pageAdapter = new PageAdapter(getChildFragmentManager());
        //initializing main fragment...
        for(int i = 0; i<tabNames.size(); i++){
            //Initilizing Bundle for communication between tabs...
//            Bundle bundle = new Bundle();
            //Put info
//           bundle.putString("title", tabNames.get(i)); //invio un unico messaggio ai miei tab (può essere customizzato)

            //adding fragment...
            if(i == 0){
                InfoTab infoTab = new InfoTab();
                Log.i(TAG, "prepareViewPager: Ripreparando infoTab");
                pageAdapter.addFragment(infoTab, tabNames.get(i));
            }
            else if (i == 1){
                pageAdapter.addFragment(new DoveTrovarloTab(), tabNames.get(i));
            }

        }

        viewPager.setAdapter(pageAdapter);

    }

}