package com.sebrs3018.SmartSharing.BookInfoStructure;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.NetworkImageView;
import com.sebrs3018.SmartSharing.R;
import com.sebrs3018.SmartSharing.databinding.FragmentInfoTabBinding;
import com.sebrs3018.SmartSharing.network.ImageRequester;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfoTab#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoTab extends Fragment {


    private static final String TAG = "InfoTab";

    // the fragment initialization parameters, key values of bundle
    private static final String BOOKINFO = "BookInfo";
    private static final String ARG_PARAM2 = "param2";
    private static final int NUMOFVALUES = 5;

    // Argument Values
    private String[] bookValues = new String[NUMOFVALUES];
    private FragmentInfoTabBinding binding;

    public InfoTab() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param _bookValues Values that will be show in the layout page.
     * @return A new instance of fragment InfoTab.
     */
    public static InfoTab newInstance(String[] _bookValues) {
        InfoTab fragment = new InfoTab();
        Bundle args = new Bundle();
        args.putStringArray("BookInfo", _bookValues);
        //Setting argument
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            bookValues = getArguments().getStringArray(BOOKINFO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Initializing dataBinding... no need for findViewById!
        binding = FragmentInfoTabBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initBookInfo();

        for (int i = 0; i < NUMOFVALUES; i++) {
            Log.i(TAG, "onCreateView - messaggio ricevuto: " + bookValues[i]);
        }

        return root;
    }


    // [0] => isbn, [1] => editore, [2] => dataPubblicazione, [3] => numeroPagine, [4] => Descrizione, [5] => UrlImage
    private void initBookInfo(){
        binding.tvISBN.setText(bookValues[0]);
        binding.tvPublisher.setText(bookValues[1]);
        binding.tvPublishDate.setText(bookValues[2]);
        binding.tvPageCount.setText(bookValues[3]);
        binding.tvDescription.setText(getString(R.string.loremIpsum));
        ImageRequester imageRequester = ImageRequester.getInstance();
        imageRequester.setImageFromUrl(binding.nivBook, bookValues[5]);


    }


}