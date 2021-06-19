package com.sebrs3018.SmartSharing.barcode;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sebrs3018.SmartSharing.BookInfoStructure.BookInfoArgs;
import com.sebrs3018.SmartSharing.Entities.Book;
import com.sebrs3018.SmartSharing.R;
import com.sebrs3018.SmartSharing.TOARRANGE.DataManager;
import com.sebrs3018.SmartSharing.databinding.FragmentBCScanBinding;
import com.sebrs3018.SmartSharing.databinding.FragmentBookInfoBinding;
import com.sebrs3018.SmartSharing.databinding.FragmentCheckBookFieldsBinding;
import com.sebrs3018.SmartSharing.network.BookEntry;
import com.sebrs3018.SmartSharing.network.ImageRequester;
import com.sebrs3018.SmartSharing.ui.BCScan.BCScanFragmentDirections;

import static com.sebrs3018.SmartSharing.Constants.*;

public class CheckBookFields extends Fragment {

    private static final String TAG = "CheckBookFields";
    private View myFragment;
    private FragmentCheckBookFieldsBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    public CheckBookFields() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentCheckBookFieldsBinding.inflate(inflater, container, false);
        myFragment = binding.getRoot();

        /* Precompilazione del form con i dati attuali */
        Book bookToCheck = getBookToCheckFromNavUI();
        initForm(bookToCheck);

        binding.tvConfermaDati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* Updating the bookToCheck info... */
                Log.i(TAG, "onClick: inserting the following ISBN ==> " + bookToCheck.getISBN());
                bookToCheck.setTitolo(binding.etTitolo.getText().toString());
                bookToCheck.setAutore(binding.etAutore.getText().toString());
                bookToCheck.setEditore(binding.etEditore.getText().toString());
                bookToCheck.setDataPubblicazione(binding.etdataPubblicazione.getText().toString());
                bookToCheck.setNroPagine(binding.etNroPagine.getText().toString());

                /* inserting bookToCheck to DB*/
                DataManager dm = new DataManager(BOOKS);
                dm.addBookLender(bookToCheck);

                NavController navController = Navigation.findNavController(getView());
                navController.navigate(CheckBookFieldsDirections.actionCheckBookFieldsToNavigationHome());
            }
        });
        return myFragment;
    }



    private Book getBookToCheckFromNavUI(){
        if(getArguments() == null)
            throw new IllegalArgumentException();

        CheckBookFieldsArgs args = CheckBookFieldsArgs.fromBundle(getArguments());
        return args.getBookToCheck();
    }


    private void initForm(Book bookToCheck){

        ImageRequester imageRequester = ImageRequester.getInstance();
        imageRequester.setImageFromUrl(binding.bookImage, bookToCheck.getUrlImage());

        binding.etTitolo.setText(bookToCheck.getTitolo());
        binding.etAutore.setText(bookToCheck.getAutore());
        binding.etEditore.setText(bookToCheck.getEditore());
        binding.etdataPubblicazione.setText(bookToCheck.getDataPubblicazione());
        binding.etNroPagine.setText(bookToCheck.getNroPagine());

    }

}