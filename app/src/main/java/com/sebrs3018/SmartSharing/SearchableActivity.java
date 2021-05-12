package com.sebrs3018.SmartSharing;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;


/*TODO: mettere searchBar apposita in questo Fragment -- NavigationUI*/
public class SearchableActivity extends AppCompatActivity {

    protected static final int RESULT_SPEECH = 1;
    private ListView listView = null;
    private Toolbar toolbar = null;
    private SearchView searchView;
    private Menu menu;
    private MenuItem itemToHide;
    ArrayList<String> books = new ArrayList<>(); //TODO: da cambiare tipo a quello di LIBRI
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);


        Intent intent = getIntent();

        //Setto actionbar
        toolbar = findViewById(R.id.trial_app_bar);
        setSupportActionBar(toolbar);

        listView = findViewById(R.id.listview);

        //TODO: l'aggiunta dei libri verrà fatta qua!
        for (int i = 0; i <= 100; i++) {
            books.add("Item: " + i);
        }

        //Initializing adapter: need to pass books to it!
        //Da notare che l'adapter mi permette di iterare facilmente sulla lista e di mostrarlo in una listView!
        adapter = new ArrayAdapter<>(SearchableActivity.this, android.R.layout.simple_list_item_1, books);

        //setting adapter...
        listView.setAdapter(adapter);

        //setting adapter on list...
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Display click item position in toast
                Toast.makeText(getApplicationContext(), adapter.getItem(position), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.top_nav_menu, menu);

        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                Toast.makeText(SearchableActivity.this, "Search is expanded", Toast.LENGTH_SHORT).show();
                menu.findItem(R.id.microphone).setVisible(true);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Toast.makeText(SearchableActivity.this, "Search is collapsed", Toast.LENGTH_SHORT).show();
                return true;
            }
        };

        menu.findItem(R.id.search).setOnActionExpandListener(onActionExpandListener);
        this.menu = menu;
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint("Cerca dei libri interessanti");


        //Filtraggio tramite parole appena inserite
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //TODO: inserire qua la chiave per la ricerca in BD !
                Toast.makeText(getApplicationContext(), "query submitted: " + query, Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
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
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "it-IT");

        try{
            startActivityForResult(intent, RESULT_SPEECH);
            searchView.setQuery("", false);
        }
        catch (ActivityNotFoundException e){
            Toast.makeText(SearchableActivity.this, "Il tuo dispositivo non supporta la Speech Recognition", Toast.LENGTH_SHORT).show();;
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
