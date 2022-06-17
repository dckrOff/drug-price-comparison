package com.a1tech.drugprice.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.a1tech.drugprice.Adapter.DrugsAdapter;
import com.a1tech.drugprice.Model.Drug;
import com.a1tech.drugprice.R;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {


    private RecyclerView rvMain;
    private DrugsAdapter drugsAdapter;
    private ArrayList<Drug> drugList = new ArrayList<Drug>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setDrugList();
        setAdapter();
    }

    private void init() {
        Objects.requireNonNull(getSupportActionBar()).setTitle("Лекарства");  // provide compatibility to all the versions

        rvMain = findViewById(R.id.rv_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // below line is to get our inflater
        MenuInflater inflater = getMenuInflater();

        // inside inflater we are inflating our menu file.
        inflater.inflate(R.menu.search_menu, menu);

        // below line is to get our menu item.
        MenuItem searchItem = menu.findItem(R.id.actionSearch);

        // getting search view of our item.
        SearchView searchView = (SearchView) searchItem.getActionView();

        // below line is to call set on query text listener method.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                filter(newText);
                return false;
            }
        });
        return true;
    }

    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<Drug> filteredlist = new ArrayList<>();

        // running a for loop to compare elements.
        for (Drug item : drugList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getDrugName().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            drugsAdapter.filterList(filteredlist);
        }
    }

    private void setDrugList() {
        drugList.add(new Drug("Ингавирин", "79000", "https://firebasestorage.googleapis.com/v0/b/testproject-7ed45.appspot.com/o/Drugs%2FIngavirin-e1565687015128.jpg?alt=media&token=854fa891-08a5-4536-8ca7-9d996f9dbc62", 1));
        drugList.add(new Drug("Климадинон", "88000", "https://firebasestorage.googleapis.com/v0/b/testproject-7ed45.appspot.com/o/Drugs%2F2382a661d5cc2229c514c21f33c1bade.jpg?alt=media&token=205eb0e5-927a-4529-ba7b-d02d23c2e002", 2));
        drugList.add(new Drug("Детрифорс", "67900", "https://firebasestorage.googleapis.com/v0/b/testproject-7ed45.appspot.com/o/Drugs%2F26132.jpg?alt=media&token=78b90239-d8cd-4bc5-a1a9-ab92cae7ba84", 3));
        drugList.add(new Drug("Эргоферон", "47000", "https://firebasestorage.googleapis.com/v0/b/testproject-7ed45.appspot.com/o/Drugs%2F%D0%91%D0%B5%D0%B7%20%D0%BD%D0%B0%D0%B7%D0%B2%D0%B0%D0%BD%D0%B8%D1%8F.png?alt=media&token=d14bcd41-465e-48f6-a972-b68bd271e5f9", 4));
        drugList.add(new Drug("Канефрон", "87000", "https://firebasestorage.googleapis.com/v0/b/testproject-7ed45.appspot.com/o/Drugs%2Fcanephron-n-dragees-120-st-768x768.jpg?alt=media&token=38f36b34-3f49-4f00-beb9-a2948923acc4", 5));
        drugList.add(new Drug("Беневрон", "57900", "https://firebasestorage.googleapis.com/v0/b/testproject-7ed45.appspot.com/o/Drugs%2F3.jpg?alt=media&token=ae2905aa-7ec9-4500-be48-eef8d0b13cd1", 6));
        drugList.add(new Drug("ФАВИПИРАВИР", "60000", "https://firebasestorage.googleapis.com/v0/b/testproject-7ed45.appspot.com/o/Drugs%2FFavipiravir-1.jpg?alt=media&token=a40ee953-4471-4bba-8754-029cefaa8381", 7));
    }

    private void setAdapter() {
        rvMain.setLayoutManager(new GridLayoutManager(this, 2));
        drugsAdapter = new DrugsAdapter(getApplicationContext(), drugList);
        rvMain.setAdapter(drugsAdapter); // set the Adapter to RecyclerView
    }
}