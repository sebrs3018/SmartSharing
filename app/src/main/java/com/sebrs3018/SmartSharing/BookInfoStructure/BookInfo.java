package com.sebrs3018.SmartSharing.BookInfoStructure;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.sebrs3018.SmartSharing.BookInfoStructure.Tabs.DoveTrovarloTab;
import com.sebrs3018.SmartSharing.BookInfoStructure.Tabs.InfoTab;
import com.sebrs3018.SmartSharing.FBRealtimeDB.Entities.Book;
import com.sebrs3018.SmartSharing.FBRealtimeDB.Entities.User;
import com.sebrs3018.SmartSharing.FBRealtimeDB.Database.DataManager;
import com.sebrs3018.SmartSharing.databinding.FragmentBookInfoBinding;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

import static com.sebrs3018.SmartSharing.Constants.USERS;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookInfo extends Fragment {

    private static final String TAG = "BookInfo";

    private View myFragment;
    private FragmentBookInfoBinding binding;
    private PageAdapter pageAdapter;

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
        pageAdapter = new PageAdapter(getChildFragmentManager());
        Book bookEntry = getMessageFromNavUI();

        //initializing main fragment...
        for(int i = 0; i<tabNames.size(); i++){
            //adding fragment...
            if(i == 0){
                InfoTab infoTab = InfoTab.newInstance(new String[] {bookEntry.getISBN(), bookEntry.getEditore(), bookEntry.getDataPubblicazione(), bookEntry.getNroPagine(), bookEntry.getDescrizione(), bookEntry.getUrlImage()});
                pageAdapter.addFragment(infoTab, tabNames.get(i));
            }
            else if (i == 1){
                DoveTrovarloTab doveTrovarloTab = new DoveTrovarloTab();
                pageAdapter.addFragment(doveTrovarloTab, tabNames.get(i));
                getLenderInfo(bookEntry.getLender(), doveTrovarloTab);
            }
            binding.vpSwappingPages.setAdapter(pageAdapter);
        }

    }


    private User getLenderInfo(String lenderUsername, DoveTrovarloTab doveTrovarloTab){
        DataManager dm = new DataManager(USERS);
        dm.getUserRoot().child(lenderUsername).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {

                User u = task.getResult().getValue(User.class);
                if(u!= null){
                    doveTrovarloTab.setUserInfo(u);
                }
            }
        });
        return null;
    }


    private Book getMessageFromNavUI(){
        if(getArguments() == null)
            throw new IllegalArgumentException();

        BookInfoArgs args = BookInfoArgs.fromBundle(getArguments());

        Book book = args.getBookEntry();
        initLayoutBookInfoArgs(book.getTitolo(), book.getAutore());

        return book;
    }

    private void initLayoutBookInfoArgs(String titolo, String autore){
        /* Inizializzo il titolo */
        binding.tvTitolo.setText(titolo);
        binding.tvAutori.setText(autore);
    }


}