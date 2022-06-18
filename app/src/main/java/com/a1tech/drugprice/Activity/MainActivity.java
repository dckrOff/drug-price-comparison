package com.a1tech.drugprice.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.a1tech.drugprice.Adapter.DrugsAdapter;
import com.a1tech.drugprice.Model.Drug;
import com.a1tech.drugprice.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    private RecyclerView rvMain;
    private DrugsAdapter drugsAdapter;
    private ArrayList<Drug> drugList = new ArrayList<Drug>();
    private FirebaseDatabase database;
    private DatabaseReference myRef;

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
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("drug_list");
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

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                tanks.clear();
                drugList.clear();
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
//                    Log.e(TAG, "1) " + childDataSnapshot.getKey()); //displays the key for the node
//                    Log.e(TAG, "2) " + childDataSnapshot.child("img").getValue());   //gives the value for given keyname
                    drugList.add(new Drug(childDataSnapshot.child("drug_name").getValue().toString(), childDataSnapshot.child("drug_price").getValue().toString(), childDataSnapshot.child("drug_img").getValue().toString(), childDataSnapshot.child("drug_id").getValue().toString()));
                }
                setAdapter();
                Log.e(TAG, "list size=> " + drugList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void setAdapter() {
        rvMain.setLayoutManager(new GridLayoutManager(this, 2));
        drugsAdapter = new DrugsAdapter(getApplicationContext(), drugList);
        rvMain.setAdapter(drugsAdapter); // set the Adapter to RecyclerView
    }
}